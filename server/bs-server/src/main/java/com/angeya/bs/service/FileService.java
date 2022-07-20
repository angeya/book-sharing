package com.angeya.bs.service;

import com.angeya.bs.model.*;
import com.angeya.bs.result.PagingBookResult;
import com.angeya.bs.result.SimpleResult;
import com.angeya.bs.config.FileConfig;
import com.angeya.bs.consts.Const;
import com.angeya.bs.enums.OperateTypeEnum;
import com.angeya.bs.enums.SimpleResultEnum;
import com.angeya.bs.mapper.BookMapper;
import com.angeya.bs.model.*;
import com.angeya.bs.result.ContentResult;
import com.angeya.bs.util.FileUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

/**
 * @Author: Angeya
 * @date: 2021/8/5 9:51
 */
@Service
public class FileService {
    private static final Logger logger = LoggerFactory.getLogger(FileService.class);

    @Autowired
    FileConfig fileConfig;

    @Autowired
    BookMapper bookMapper;

    @Autowired
    LogManager logManager;

    public List<FileInfo> getBaseFiles() {
        String basePath = fileConfig.getBasePath();
        return FileUtil.getFilesByPath(basePath);
    }

    public ContentResult<List<Book>> getAllFile() {
        List<Book> bookList = bookMapper.selectAll();
        FileUtil.removeFileDirectory(bookList);
        return new ContentResult<>(SimpleResultEnum.SUCCESS, bookList);
    }

    public ContentResult<List<Book>> getUserFiles(HttpSession session) {
        User user = (User) session.getAttribute(Const.SESSION_KEY);
        List<Book> bookList = bookMapper.selectByUserId(user.getId());
        FileUtil.removeFileDirectory(bookList);
        return new ContentResult<>(SimpleResultEnum.SUCCESS, bookList);
    }

    public ContentResult<Book> getFileById(Integer id) {
        Book file = bookMapper.selectById(id);
        return new ContentResult<>(SimpleResultEnum.SUCCESS, file);
    }

    public List<FileInfo> getFilesByRelativePath(String path) {
        String fullPath = FileUtil.addBasePath(path);
        return FileUtil.getFilesByPath(fullPath);
    }

    /**
     * 获取电子书分页数据
     * @param param 分页参数
     * @return 分页查询结果
     */
    public PagingBookResult getPagingBook(PagingBookParam param) {
        int totalNUm = bookMapper.getTotalNum(param);
        List<Book> bookList = bookMapper.getPagingList(param);
        FileUtil.removeFileDirectory(bookList);
        return new PagingBookResult(SimpleResultEnum.SUCCESS, bookList, totalNUm);
    }

    /**
     * 文件上传
     *
     * @param session 会话信息
     * @param file    文件
     * @return 上传结果
     */
    @Transactional(rollbackFor = Exception.class)
    public SimpleResult uploadFile(HttpSession session, MultipartFile file,
                                   String classify, String labels, String desc) {
        String fileName = file.getOriginalFilename(); // 获取源文件名称
//        if (this.isFileNameExist(fileName)){
//            return new SimpleResult(SimpleResultEnum.FILE_NAME_EXIST);
//        }
        String fullPath;
        fullPath = fileConfig.getBasePath() + File.separator + classify + File.separator + fileName;
        File destFile = new File(fullPath);
        if (FileUtil.isPathAvailable(destFile.getParent())) {
            try {
                file.transferTo(destFile);// 保存文件
            } catch (IOException e) {
                logger.warn("Upload file {} failed", fullPath, e);
                return new SimpleResult(SimpleResultEnum.FAILED);
            }
        } else {
            return new SimpleResult(SimpleResultEnum.FAILED);
        }
        User user = (User) session.getAttribute(Const.SESSION_KEY);
        Book book = new Book(user.getId(), fullPath, destFile.length(), classify, labels, desc);
        if (recordFileInfo(book)) {
            OperateLog operateLog = new OperateLog(user.getId(), OperateTypeEnum.UPLOAD_FILE.getIndex(), book.getId());
            logManager.log(operateLog);
            return new SimpleResult(SimpleResultEnum.SUCCESS);
        }
        return new SimpleResult(SimpleResultEnum.FAILED);
    }


    /**
     * 记录文件信息
     *
     * @param file 文件
     * @return 记录是否成功
     */
    private boolean recordFileInfo(Book file) {
        int result = bookMapper.insertSelective(file);
        return result == 1;
    }

    /**
     * 文件下载
     * @param session 会话对象
     * @param response 请求响应对象
     * @param id 文件id
     * @return 下载结果
     */
    public void downloadFile(HttpSession session, HttpServletResponse response, Integer id) {
        Book book = bookMapper.selectById(id);
        File file = new File(book.getPath());
        if (file.isFile()) {
            String fileName = file.getName(); // 文件名
            response.setCharacterEncoding("utf-8");
            response.setContentType("application/force-download");// 设置强制下载不打开
            response.addHeader("Content-Disposition", // 设置下载文件名及编码
                    "attachment;fileName=" + new String(fileName.getBytes(), StandardCharsets.ISO_8859_1));

            byte[] buffer = new byte[1024];

            // jdk 1.7 try-with-source 在try中可以定义多个资源，try执行后会自动关闭
            try (FileInputStream fis = new FileInputStream(file);
                 BufferedInputStream bis = new BufferedInputStream(fis)){
                OutputStream os = response.getOutputStream();
                int i;
                while ((i = bis.read(buffer))!= -1) {
                    os.write(buffer, 0, i);
                }
                User user = (User)session.getAttribute(Const.SESSION_KEY);
                logManager.log(new OperateLog(user.getId(), OperateTypeEnum.DOWNLOAD_FILE.getIndex(), book.getId()));
                // 多次返回会报错，因为http server 返回一次response之后socket就关闭了
                // return new SimpleResult(SimpleResultEnum.SUCCESS);
            } catch (Exception e) {
                logger.error("Read file failed", e);
            }
        } else {
            logger.warn("File {} does not exists", id);
        }
    }

    /**
     * 删除文件
     * @param session 会话对象
     * @param id 文件 id
     * @return 删除结果
     */
    public SimpleResult deleteFile(HttpSession session, Integer id) {
        String filePath = bookMapper.selectPathById(id);
        if (filePath == null) {
            return new SimpleResult(SimpleResultEnum.FILE_DOES_NOT_EXIST);
        }
        try {
            Files.deleteIfExists(Paths.get(filePath));
        } catch (IOException e) {
            logger.warn("Delete file {} from disk failed", filePath,e );
            return new SimpleResult(SimpleResultEnum.FAILED);
        }
        User user = (User)session.getAttribute(Const.SESSION_KEY);
        int result = bookMapper.deleteById(id, user.getId());
        if (result == 1) {
            return new SimpleResult(SimpleResultEnum.SUCCESS);
        }
        logger.warn("Delete file {} from db failed", id);
        return new SimpleResult(SimpleResultEnum.FAILED);
    }

    /**
     * 判断文件名是否已经存在
     *
     * @param name 文件名
     * @return 是否存在
     */
    public boolean isFileNameExist(String name) {
        // 在文件名前面加上分隔符再进行查找
        name = File.separator + name;
        int fileNumber = bookMapper.selectBookNumByName(name);
        if (fileNumber == 0) {
            return false;
        }
        if (fileNumber > 1) {
            logger.warn("There are more than one file has the same name");
        }
        return true;
    }

    /**
     * 获取分类列表
     * @return 文件分类列表
     */
    public ContentResult<List<String>> getClassifyList() {
        List<String> classifyList = bookMapper.getClassifyList();
        return new ContentResult<>(SimpleResultEnum.SUCCESS, classifyList);
    }
}

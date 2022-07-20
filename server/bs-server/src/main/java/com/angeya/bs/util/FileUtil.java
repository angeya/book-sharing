package com.angeya.bs.util;

import com.angeya.bs.config.FileConfig;
import com.angeya.bs.enums.FileTypeEnum;
import com.angeya.bs.model.Book;
import com.angeya.bs.model.FileInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 文件处理工具类
 *
 * @Author: Angeya
 * @date: 2021/8/5 9:47
 */

public class FileUtil {

    private static final Logger logger = LoggerFactory.getLogger(FileUtil.class);
    private static final FileConfig fileConfig = BeanUtil.getBean(FileConfig.class);

    /**
     * 获取路径下的所有文件
     *
     * @param path 目录路径
     * @return 文件列表
     */
    public static List<FileInfo> getFilesByPath(String path) {
        File file = new File(path);
        List<FileInfo> fileInfoList = new ArrayList<>();
        if (file.isFile()) {
            addFileToInfoList(fileInfoList, file);
        } else if (file.isDirectory()) {
            File[] files = file.listFiles();
            if (files == null) {
                return fileInfoList;
            }
            for (File f : files) {
                addFileToInfoList(fileInfoList, f);
            }
        } else {
            logger.info("Path {} does not exist", path);
        }
        return fileInfoList;
    }

    /**
     * 将 file 转化为 fileInfo 并添加到列表
     *
     * @param fileInfoList 文件信息列表
     * @param file         待添加文件
     */
    private static void addFileToInfoList(List<FileInfo> fileInfoList, File file) {
        if (file.isFile()) {
            fileInfoList.add(new FileInfo(removeBasePath(file.getPath()), FileTypeEnum.FILE.getIndex()));
        } else {
            fileInfoList.add(new FileInfo(removeBasePath(file.getPath()), FileTypeEnum.DIR.getIndex()));
        }
    }

    /**
     * 移除基本路径，获取相对路径
     *
     * @param path 完整路径
     * @return 相对路径
     */
    public static String removeBasePath(String path) {
        return path.replace(fileConfig.getBasePath(), "");
    }

    /**
     * 相对路径加上基础路径
     *
     * @param path 相对路径
     * @return 完整路径
     */
    public static String addBasePath(String path) {
        return fileConfig.getBasePath() + path;
    }

    /**
     * 检查目录是否可用
     *
     * @param path 路径
     * @return 是否可用
     */
    public static boolean isPathAvailable(String path) {
        File dir = new File(path);
        if (!dir.exists() && !dir.mkdirs()) {
            logger.warn("create file path [{}] failed", path);
            return false;
        }
        return true;
    }

    public static File getFileByRelativePath(String path) {
        String fullPath = addBasePath(path);
        return new File(fullPath);
    }


    /**
     * 取出路径保留文件名
     * @param booKList 书籍信息列表
     * @return 只保留文件名的列表
     */
    public static void removeFileDirectory(List<Book> booKList) {
        for (Book book : booKList) {
            book.setPath(book.getPath().substring(book.getPath().lastIndexOf(File.separator) + 1));
        }
    }
}

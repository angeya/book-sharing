package com.angeya.bs.controller;

import com.angeya.bs.consts.Const;
import com.angeya.bs.model.FileInfo;
import com.angeya.bs.model.Book;
import com.angeya.bs.model.PagingBookParam;
import com.angeya.bs.model.User;
import com.angeya.bs.result.PagingBookResult;
import com.angeya.bs.result.ContentResult;
import com.angeya.bs.result.SimpleResult;
import com.angeya.bs.service.FileService;
import io.swagger.annotations.Api;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * 文件控制器
 * @Author: Angeya
 * @date: 2021/8/5 10:20
 */

@RestController
@RequestMapping("file")
@CrossOrigin
@Api(value = "BookManager", description = "电子书管理")
public class FileController {
    private static final Logger logger = LoggerFactory.getLogger(FileController.class);

    @Autowired
    private FileService fileService;

    @GetMapping("getBaseFiles")
    public List<FileInfo> getBaseFiles() {
        return fileService.getBaseFiles();
    }

    @GetMapping("getAllFile")
    public ContentResult<List<Book>> getAllFile() {
        return fileService.getAllFile();
    }

    @GetMapping("getUserFiles")
    public ContentResult<List<Book>> getUserFiles(HttpServletRequest request) {
        return fileService.getUserFiles(request.getSession());
    }

    @GetMapping("getFilesByPath/{path}")
    public List<FileInfo> getFilesByRelativePath(@PathVariable String path) {
        return fileService.getFilesByRelativePath(path);
    }

    @GetMapping("getPagingBook")
    public PagingBookResult getPagingBook(HttpServletRequest request, String classify, Integer pageSize, Integer currentPage) {
        PagingBookParam param;
        if(classify == null) {
            HttpSession session = request.getSession();
            User user = (User)session.getAttribute(Const.SESSION_KEY);
            // 通过用户id获取
            param = new PagingBookParam(user.getId(), (currentPage - 1) * pageSize, pageSize, currentPage);
        } else {
            // 通过分类获取
            param = new PagingBookParam(classify, (currentPage - 1) * pageSize, pageSize, currentPage);
        }
        return fileService.getPagingBook(param);
    }

    @PostMapping("uploadFile")
    public SimpleResult uploadFile (HttpServletRequest request, MultipartFile file,
                                    String classify, String labels, String desc) {
        return fileService.uploadFile(request.getSession(), file, classify, labels, desc);
    }

    @GetMapping("downloadFile")
    public void downloadFile(HttpServletRequest request, HttpServletResponse response, Integer id) {
        fileService.downloadFile(request.getSession(), response, id);
    }

    @GetMapping("deleteFile")
    public SimpleResult deleteFile(HttpServletRequest request, Integer id) {
        return fileService.deleteFile(request.getSession(), id);
    }

    @GetMapping("isFileNameExist")
    public boolean isFileNameExist(String name) {
        return fileService.isFileNameExist(name);
    }

    @GetMapping("getClassifyList")
    public ContentResult<List<String>> getClassifyList() {
        return fileService.getClassifyList();
    }
}

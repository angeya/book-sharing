package com.angeya.bs.model;

/**
 * @Author: Angeya
 * @date: 2021/8/5 9:59
 */

public class FileInfo {
    /**
     * 文件相对路径
     */
    private String filePath;
    /**
     * 文件类型
     */
    private int fileType;

    public FileInfo(String filePath, int fileType) {
        this.filePath = filePath;
        this.fileType = fileType;
    }

    public String getfilePath() {
        return filePath;
    }

    public void setfilePath(String filePath) {
        this.filePath = filePath;
    }

    public int getFileType() {
        return fileType;
    }

    public void setFileType(int fileType) {
        this.fileType = fileType;
    }
}

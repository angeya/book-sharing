package com.angeya.bs.model;

import java.util.Date;

public class OperateLog {
    public OperateLog() {
    }

    public OperateLog(Integer userId, Byte operate, Integer fileId) {
        this.userId = userId;
        this.operate = operate;
        this.fileId = fileId;
    }

    public OperateLog(Integer userId, Byte operate) {
        this.userId = userId;
        this.operate = operate;
    }

    private Long id;

    private Integer userId;

    private Byte operate;

    private Integer fileId;

    private Date createDate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Byte getOperate() {
        return operate;
    }

    public void setOperate(Byte operate) {
        this.operate = operate;
    }

    public Integer getFileId() {
        return fileId;
    }

    public void setFileId(Integer fileId) {
        this.fileId = fileId;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
}
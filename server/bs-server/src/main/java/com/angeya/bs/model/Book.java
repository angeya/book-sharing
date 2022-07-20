package com.angeya.bs.model;

import java.util.Date;

public class Book {

    /**
     * 无参构造方法给 mybatis 使用
     */
    public Book() {
    }

    public Book(Integer userId, String path, long size, String classify, String label, String desc) {
        this.userId = userId;
        this.path = path;
        this.size = size;
        this.classify = classify;
        this.label = label;
        this.desc = desc;
    }

    private Integer id;

    private Integer userId;

    private String path;

    private long size;

    private String classify;

    private String label;

    private String desc;

    private Byte operability;

    private Date createDate;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path == null ? null : path.trim();
    }


    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public String getClassify() {
        return classify;
    }

    public void setClassify(String classify) {
        this.classify = classify;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public Byte getOperability() {
        return operability;
    }

    public void setOperability(Byte operability) {
        this.operability = operability;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
}
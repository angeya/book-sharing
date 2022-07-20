package com.angeya.bs.model;

/**
 * 书籍分页查询参数
 * @Author: Angeya
 * @date: 2021/8/18 10:16
 */

public class PagingBookParam {
    private Integer userId;

    private String classify;

    private Integer pageSize;

    private Integer startIndex;

    private Integer currentPage;

    public PagingBookParam(String classify, Integer startIndex,Integer pageSize, Integer currentPage) {
        this.classify = classify;
        this.pageSize = pageSize;
        this.startIndex = startIndex;
        this.currentPage = currentPage;
    }

    public PagingBookParam(Integer userId, Integer startIndex, Integer pageSize, Integer currentPage) {
        this.userId = userId;
        this.pageSize = pageSize;
        this.startIndex = startIndex;
        this.currentPage = currentPage;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getClassify() {
        return classify;
    }

    public void setClassify(String classify) {
        this.classify = classify;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Integer getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(Integer currentPage) {
        this.currentPage = currentPage;
    }
}

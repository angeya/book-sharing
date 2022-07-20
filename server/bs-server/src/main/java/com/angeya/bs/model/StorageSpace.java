package com.angeya.bs.model;

/**
 * 用户存储空间 单位字节
 * @Author: Angeya
 * @date: 2021/8/9 14:08
 */

public class StorageSpace {
    /**
     * 已用存储空间
     */
    private long usedSpace;

    /**
     * 总存储空间
     */
    private long totalSpace;

    public long getUsedSpace() {
        return usedSpace;
    }

    public void setUsedSpace(long usedSpace) {
        this.usedSpace = usedSpace;
    }

    public long getTotalSpace() {
        return totalSpace;
    }

    public void setTotalSpace(long totalSpace) {
        this.totalSpace = totalSpace;
    }
}

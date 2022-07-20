package com.angeya.bs.enums;

/**
 * @Author: Angeya
 * @date: 2021/8/5 15:07
 */

public enum SimpleResultEnum {
    SUCCESS(0, "success"),
    FAILED(-1, "failed"),

    USER_NAME_EXIST(100, "user name has existed"),

    FILE_NAME_EXIST(200, "file name has existed"),
    FILE_DOES_NOT_EXIST(201, "file does not exist"),
    DELETE_FILE_FROM_DISK_FAILED(202, "delete file from disk failed"),
    DELETE_FILE_FROM_DB_FAILED(202, "delete file from db failed");

    private int code;
    private String msg;

    SimpleResultEnum(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}

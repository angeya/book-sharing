package com.angeya.bs.result;

import com.angeya.bs.enums.SimpleResultEnum;

/**
 * @Author: Angeya
 * @date: 2021/8/5 14:53
 */

public class SimpleResult {
    private int code;
    private String msg;

    public SimpleResult(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public SimpleResult(SimpleResultEnum sre) {
        this.code = sre.getCode();
        this.msg = sre.getMsg();
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}

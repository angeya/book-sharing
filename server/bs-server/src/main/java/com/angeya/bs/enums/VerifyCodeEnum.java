package com.angeya.bs.enums;

/**
 * Desc: 验证码状态
 *
 * @Author: Angeya
 * DateTime: 2021-09-23 21:22
 */
public enum VerifyCodeEnum {
    /**
     * 不存在
     */
    INVALID(300, "验证码不存在或已经失效"),
    /**
     * 错误
     */
    ERROR(301, "验证码错误"),
    /**
     * 正确
     */
    CORRECT(302, "验证码正确");

    private int code;

    private String msg;

    VerifyCodeEnum(int code, String msg) {
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

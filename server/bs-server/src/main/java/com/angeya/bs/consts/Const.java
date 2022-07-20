package com.angeya.bs.consts;

/**
 * @Author: Angeya
 * @date: 2021/8/6 14:35
 */

public class Const {
    private Const() {
    }

    public static final String LOGIN_URI = "/user/login";
    public static final String CREATE_USER_URI = "/user/createUser";
    public static final String GET_VERIF_CODE_URI = "/user/getVerifyCode";

    public static final String SESSION_KEY = "user";
    /**
     * 验证码长度
     */
    public static final int VERIFICATION_CODE_LENGTH = 4;
    /**
     * 注册验证码邮件模板
     */
    public static final String REGISTER_MAIL_TEMPLATE = "【邮箱注册】验证码：%s (有效期为1分钟)，请勿泄露给他人，" +
            "如非本人操作，请忽略此信息。 【安杰文件分享平台】";
    /**
     * 邮件主题
     */
    public static final String MAIL_SUBJECT = "安杰文件分享平台验证码";
    /**
     * 邮箱编码标志（@为特殊符号）
     */
    public static final String MAIL_ENCODE_SYMBOL = ".._..";
    /**
     * at，邮箱使用
     */
    public static final String AT = "@";
    public static final String CLASSIFY_COMMON_COLUMN_NAME = "classify";

}

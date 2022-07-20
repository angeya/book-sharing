package com.angeya.bs.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.security.MessageDigest;

/**
 * @Author: Angeya
 * @date: 2021/8/9 11:13
 */

public class MD5 {
    private MD5() {
    }

    private static final Logger logger = LoggerFactory.getLogger(MD5.class);

    public static String getMD5String(String s) {
        char[] hexDigits = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
        try {
            byte[] strBytes = s.getBytes();
            MessageDigest mdTemp = MessageDigest.getInstance("MD5");
            mdTemp.update(strBytes);
            byte[] md = mdTemp.digest();
            int j = md.length;
            char[] str = new char[j * 2];
            int k = 0;
            // 将十进制的字节转为16进制
            for (byte byte0 : md) {
                // >>> 无符号右移
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(str);
        } catch (Exception e) {
            logger.warn("Get MD5 failed", e );
            return null;
        }
    }
}

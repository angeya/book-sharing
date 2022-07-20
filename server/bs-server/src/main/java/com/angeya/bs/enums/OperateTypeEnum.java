package com.angeya.bs.enums;

/**
 * @Author: Angeya
 * @date: 2021/8/6 15:52
 */

public enum OperateTypeEnum {
    LOGIN((byte)0),
    LOGOUT((byte)1),
    MODIFY_USER_NAME((byte)2),
    MODIFY_PASSWORD((byte)3),
    UPLOAD_FILE((byte)4),
    DOWNLOAD_FILE((byte)5),
    DELETE_FILE((byte)6);

    private byte index;

    OperateTypeEnum(byte index) {
        this.index = index;
    }

    public byte getIndex() {
        return index;
    }

}

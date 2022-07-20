package com.angeya.bs.enums;

/**
 * @Author: Angeya
 * @date: 2021/8/5 9:57
 */

public enum FileTypeEnum {
    DIR(0),
    FILE(1);
    private int index;

    FileTypeEnum(int index) {
        this.index = index;
    }

    public int getIndex() {
        return index;
    }
}

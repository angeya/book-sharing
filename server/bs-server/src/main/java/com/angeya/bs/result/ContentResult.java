package com.angeya.bs.result;

import com.angeya.bs.enums.SimpleResultEnum;

/**
 * @Author: Angeya
 * @date: 2021/8/5 14:53
 */

public class ContentResult<T> extends SimpleResult {
    private T data;

    public ContentResult(int code, String msg, T data) {
        super(code, msg);
        this.data = data;
    }

    public ContentResult(SimpleResultEnum sre, T data) {
        super(sre);
        this.data = data;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}

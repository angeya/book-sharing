package com.angeya.bs.result;

import com.angeya.bs.enums.SimpleResultEnum;
import com.angeya.bs.model.Book;

import java.util.List;

/**
 * @Author: Angeya
 * @date: 2021/8/18 10:11
 */

public class PagingBookResult extends ContentResult<List<Book>> {
    private int totalNum;

    public PagingBookResult(SimpleResultEnum sre, List<Book> data, int totalNum) {
        super(sre, data);
        this.totalNum = totalNum;
    }

    public int getTotalNum() {
        return totalNum;
    }

    public void setTotalNum(int totalNum) {
        this.totalNum = totalNum;
    }
}

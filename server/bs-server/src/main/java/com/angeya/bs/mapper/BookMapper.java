package com.angeya.bs.mapper;

import com.angeya.bs.model.Book;
import com.angeya.bs.model.PagingBookParam;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookMapper {
    /**
     * 通过文件 id 和 用户 id 删除
     */
    int deleteById(Integer id, Integer userId);

    /**
     * 新增文件
     */
    int insertSelective(Book record);

    /**
     * 通过文件 id 获取文件信息
     */
    Book selectById(Integer id);

    /**
     * 通过文件 id 获取文件路径
     */
    String selectPathById(Integer id);

    /**
     * 获取所有文件信息
     */
    List<Book> selectAll();

    /**
     * 获取用户所有文件信息
     */
    List<Book> selectByUserId(Integer userId);

    int updateById(Book file);

    /**
     * 通过文件名查找对应文件数量
     */
    int selectBookNumByName(String name);

    /**
     * 获取分类信息
     */
    List<String> getClassifyList();

    /**
     * 获取分页列表
     */
    List<Book> getPagingList(PagingBookParam param);

    /**
     * 获取总条数（如果有分类，用户Id，则优先根据限制筛选）
     */
    Integer getTotalNum(PagingBookParam param);
}
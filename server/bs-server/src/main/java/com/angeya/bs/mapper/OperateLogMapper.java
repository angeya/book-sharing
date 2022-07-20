package com.angeya.bs.mapper;

import com.angeya.bs.model.OperateLog;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OperateLogMapper {
    int deleteByPrimaryKey(Long id);

    int insertSelective(OperateLog record);

    OperateLog selectByPrimaryKey(Long id);

    List<OperateLog> selectAll();

    int updateByPrimaryKey(OperateLog record);
}
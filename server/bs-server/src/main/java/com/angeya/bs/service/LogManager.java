package com.angeya.bs.service;

import com.angeya.bs.mapper.OperateLogMapper;
import com.angeya.bs.model.OperateLog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 日志管理
 * @Author: Angeya
 * @date: 2021/8/6 10:14
 */

@Component
public class LogManager {

    private static final Logger logger = LoggerFactory.getLogger(LogManager.class);

    @Autowired
    OperateLogMapper operateLogMapper;

    public void log(OperateLog log){
        int result = operateLogMapper.insertSelective(log);
        if (result != 1) {
            logger.warn("Record operate log failed ...");
        }
    }
}

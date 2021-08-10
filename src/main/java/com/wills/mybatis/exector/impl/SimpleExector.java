package com.wills.mybatis.exector.impl;

import com.wills.mybatis.config.Configuration;
import com.wills.mybatis.config.MappedStatement;
import com.wills.mybatis.exector.Exector;

import java.sql.Connection;
import java.util.List;

/**
 * @ClassName SimpleExector
 * @Date 2021/8/10 12:00
 * @Author 王帅
 * @Version 1.0
 * @Description
 */
public class SimpleExector implements Exector {

    private Connection connection = null;

    @Override
    public <E> List<E> query(Configuration configuration, MappedStatement statement, Object[] param) throws Exception {
        connection = configuration.getDataSource().getConnection();
        // 对sql进行处理：
        // Select * from user where id= #{id} and name = #{name}

        return null;
    }

    @Override
    public void close() throws Exception{
        connection.close();
    }
}

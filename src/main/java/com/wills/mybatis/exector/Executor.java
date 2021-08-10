package com.wills.mybatis.exector;

import com.wills.mybatis.config.Configuration;
import com.wills.mybatis.config.MappedStatement;

import java.util.List;

/**
 * @ClassName Exector
 * @Date 2021/8/10 11:59
 * @Author 王帅
 * @Version 1.0
 * @Description
 * sql 执行器
 */
public interface Executor {

    public <E> List<E> query(Configuration configuration, MappedStatement statement,Object[] param) throws Exception;

    public <T> void insert(Configuration configuration, MappedStatement statement, T obj) throws Exception;

    public <T> void updateById(Configuration configuration, MappedStatement statement, T obj) throws Exception;

    public void delete(Configuration configuration, MappedStatement statement, Object id) throws Exception;

    public void close() throws Exception;
}

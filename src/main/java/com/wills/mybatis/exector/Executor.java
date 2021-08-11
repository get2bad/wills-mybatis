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

    public <T> void insertAdapter(Configuration configuration, String sql, T obj) throws Exception;

    public <T> void updateById(Configuration configuration, MappedStatement statement, T obj) throws Exception;

    public <T> void updateByIdAdapter(Configuration configuration, String sql, T obj) throws Exception;

    public void deleteById(Configuration configuration, MappedStatement statement, Object id) throws Exception;

    public void deleteByIdAdapter(Configuration configuration, String sql, Object id) throws Exception;

    public void close() throws Exception;

    <X> List<X> execute(Configuration configuration, String sql, X obj,Class<?> resultTypeClass) throws Exception;
}

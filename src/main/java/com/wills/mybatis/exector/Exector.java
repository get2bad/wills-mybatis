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
public interface Exector {

    public <E> List<E> query(Configuration configuration, MappedStatement statement,Object[] param) throws Exception;

    public void close() throws Exception;
}

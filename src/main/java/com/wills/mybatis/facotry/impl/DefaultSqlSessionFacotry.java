package com.wills.mybatis.facotry.impl;

import com.wills.mybatis.config.Configuration;
import com.wills.mybatis.facotry.SqlSessionFactory;
import com.wills.mybatis.session.SqlSession;
import com.wills.mybatis.session.impl.DefaultSqlSession;

/**
 * @ClassName DefaultSqlSessionFacotry
 * @Date 2021/8/10 10:33
 * @Author 王帅
 * @Version 1.0
 * @Description
 * 默认的 SqlSessionFactory
 */
public class DefaultSqlSessionFacotry implements SqlSessionFactory {

    private Configuration configuration;

    public DefaultSqlSessionFacotry(Configuration configuration) {
        this.configuration = configuration;
    }


    @Override
    public SqlSession openSession() {
        return new DefaultSqlSession(configuration);
    }
}

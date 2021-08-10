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
 * 默认的 SqlSessionFactory，作为默认实现，如果有其他的需求，可以实现 SqlSessionFactory 接口，自己重写 openSession方法
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

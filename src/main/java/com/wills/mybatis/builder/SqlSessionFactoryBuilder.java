package com.wills.mybatis.builder;

import com.wills.mybatis.config.Configuration;
import com.wills.mybatis.facotry.impl.DefaultSqlSessionFacotry;
import com.wills.mybatis.facotry.SqlSessionFactory;

import java.io.InputStream;

/**
 * @ClassName SqlSessionFactoryBuilder
 * @Date 2021/8/10 10:24
 * @Author 王帅
 * @Version 1.0
 * @Description
 */
public class SqlSessionFactoryBuilder {

    private Configuration configuration;

    public SqlSessionFactoryBuilder() {
        this.configuration = new Configuration();
    }

    public SqlSessionFactory build(InputStream inputStream) throws Exception{
        // 解析配置文件
        XMLConfigerBuilder xmlConfigurator = new XMLConfigerBuilder(configuration);

        Configuration configuration = xmlConfigurator.parseConfiguator(inputStream);

        SqlSessionFactory sqlSessionFactory = new DefaultSqlSessionFacotry(configuration);


        return sqlSessionFactory;
    }
}

package com.wills.mybatis.builder;

import com.wills.mybatis.config.Configuration;
import com.wills.mybatis.facotry.impl.DefaultSqlSessionFacotry;
import com.wills.mybatis.facotry.SqlSessionFactory;
import com.wills.mybatis.util.Resources;

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

    /**
     * 绑定了 inputStream 后，读取目标文件下的一些基础链接信息 以及 卸载配置文件中的 *Mapper.xml 文件读取
     * @param inputStream
     * @return
     * @throws Exception
     */
    public SqlSessionFactory build(InputStream inputStream) throws Exception{
        // 解析配置文件
        XMLConfigerBuilder xmlConfigurator = new XMLConfigerBuilder(configuration);

        Configuration configuration = xmlConfigurator.parseConfiguator(inputStream);

        SqlSessionFactory sqlSessionFactory = new DefaultSqlSessionFacotry(configuration);


        return sqlSessionFactory;
    }

    public SqlSessionFactory build() throws Exception{
        // 解析配置文件
        XMLConfigerBuilder xmlConfigurator = new XMLConfigerBuilder(configuration);

        // 读取默认配置文件的输入流
        InputStream inputStream = Resources.getResourceAsStream("WillsMyBatisConfig.xml");

        Configuration configuration = xmlConfigurator.parseConfiguator(inputStream);

        SqlSessionFactory sqlSessionFactory = new DefaultSqlSessionFacotry(configuration);


        return sqlSessionFactory;
    }
}

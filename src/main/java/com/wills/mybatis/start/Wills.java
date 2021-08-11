package com.wills.mybatis.start;

import com.wills.mybatis.builder.SqlSessionFactoryBuilder;
import com.wills.mybatis.example.mapper.UserMapper;
import com.wills.mybatis.facotry.SqlSessionFactory;
import com.wills.mybatis.session.SqlSession;
import com.wills.mybatis.template.BaseMapper;
import com.wills.mybatis.util.Resources;

import java.io.InputStream;

/**
 * @ClassName Wills
 * @Date 2021/8/11 14:49
 * @Author 王帅
 * @Version 1.0
 * @Description
 */
public class Wills {

    public static SqlSession getSqlSession(String configFileName) throws Exception{
        InputStream resourceAsSteam = Resources.getResourceAsStream("WillsMyBatisConfig.xml");
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(resourceAsSteam);
        return sqlSessionFactory.openSession();
    }

    public static SqlSession getSqlSession() throws Exception{
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build();
        return sqlSessionFactory.openSession();
    }

    public static<T> T getMapper(Class<T> mapperClass) throws Exception{
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build();
        SqlSession sqlSession = sqlSessionFactory.openSession();
        return (T)sqlSession.getMapper(mapperClass);
    }
}

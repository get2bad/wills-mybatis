package com.wills.mybatis.example;

import com.wills.mybatis.builder.SqlSessionFactoryBuilder;
import com.wills.mybatis.example.entity.User;
import com.wills.mybatis.facotry.SqlSessionFactory;
import com.wills.mybatis.session.SqlSession;
import com.wills.mybatis.util.Resources;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * @ClassName Example
 * @Date 2021/8/10 10:01
 * @Author 王帅
 * @Version 1.0
 * @Description
 */
public class Example {

    public static void main(String[] args) throws Exception {
        InputStream resourceAsSteam = Resources.getResourceAsStream("WillsMyBatisConfig.xml");
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(resourceAsSteam);
        SqlSession sqlSession = sqlSessionFactory.openSession();

        //调用
        User user = new User();
        user.setId(1);
        user.setName("wills");
        System.out.println("======================条件查询=======================");
        User user2 = sqlSession.selectOne("com.wills.mybatis.example.mapper.UserMapper.selectOne", user);
        System.out.println(user2);
        System.out.println("======================查询全部=======================");
        List<User> users = sqlSession.selectList("com.wills.mybatis.example.mapper.UserMapper.selectList");
        for (User user1 : users) {
            System.out.println(user1);
        }

    }
}

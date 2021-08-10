package com.wills.mybatis.example;

import com.wills.mybatis.builder.SqlSessionFactoryBuilder;
import com.wills.mybatis.example.entity.User;
import com.wills.mybatis.example.mapper.UserMapper;
import com.wills.mybatis.facotry.SqlSessionFactory;
import com.wills.mybatis.session.SqlSession;
import com.wills.mybatis.util.Resources;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * @ClassName Example
 * @Date 2021/8/10 10:01
 * @Author 王帅
 * @Version 1.0
 * @Description
 */
public class Example {

    /**
     * 自己指定 配置文件
     * @throws Exception
     */
    @Test
    public void test1() throws Exception {
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

    /**
     * 简略模板
     * @throws Exception
     */
    @Test
    public void test2() throws Exception {
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build();
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

    /**
     * 测试删除
     * @throws Exception
     */
    @Test
    public void test3() throws Exception {
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build();
        SqlSession sqlSession = sqlSessionFactory.openSession();

        //调用
        sqlSession.deleteById("com.wills.mybatis.example.mapper.UserMapper.deleteById", 1);
        System.out.println("======================查询全部=======================");
        List<User> users = sqlSession.selectList("com.wills.mybatis.example.mapper.UserMapper.selectList");
        for (User user1 : users) {
            System.out.println(user1);
        }
    }

    /**
     * 测试添加
     * @throws Exception
     */
    @Test
    public void test4() throws Exception {
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build();
        SqlSession sqlSession = sqlSessionFactory.openSession();

        //调用
        User user = new User();
        user.setName("wills");
        user.setAge(24);
        user.setRemark(new String("一个酷酷的boy".getBytes(StandardCharsets.UTF_8)));
        sqlSession.insert("com.wills.mybatis.example.mapper.UserMapper.insert", user);
        System.out.println("======================查询全部=======================");
        List<User> users = sqlSession.selectList("com.wills.mybatis.example.mapper.UserMapper.selectList");
        for (User user1 : users) {
            System.out.println(user1);
        }
    }

    /**
     * 更新
     * @throws Exception
     */
    @Test
    public void test5() throws Exception {
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build();
        SqlSession sqlSession = sqlSessionFactory.openSession();

        //调用
        User user = new User();
        user.setId(5);
        user.setName("wills");
        user.setAge(24);
        user.setRemark(new String("一个酷酷的boy".getBytes(StandardCharsets.UTF_8)));
        sqlSession.updateById("com.wills.mybatis.example.mapper.UserMapper.updateById", user);
        System.out.println("======================查询全部=======================");
        List<User> users = sqlSession.selectList("com.wills.mybatis.example.mapper.UserMapper.selectList");
        for (User user1 : users) {
            System.out.println(user1);
        }
    }

    @Test
    public void test6() throws Exception {
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build();
        SqlSession sqlSession = sqlSessionFactory.openSession();
        UserMapper mapper = (UserMapper) sqlSession.getMapper(UserMapper.class);

        //调用
        User user = new User();
        user.setId(5);
        user.setName("wills");
        user.setAge(24);
        user.setRemark(new String("一个酷酷的boy".getBytes(StandardCharsets.UTF_8)));
        System.out.println("======================条件查询=======================");
        User one = mapper.selectOne(5, "wills");
        System.out.println(one);
        System.out.println("======================查询全部=======================");
        List<User> users = mapper.selectList();
        for (User user1 : users) {
            System.out.println(user1);
        }
    }
}

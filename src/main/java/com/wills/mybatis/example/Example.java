package com.wills.mybatis.example;

import com.wills.mybatis.builder.SqlSessionFactoryBuilder;
import com.wills.mybatis.example.entity.User;
import com.wills.mybatis.example.mapper.UserMapper;
import com.wills.mybatis.facotry.SqlSessionFactory;
import com.wills.mybatis.session.SqlSession;
import com.wills.mybatis.start.Wills;
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
     * 自己指定 配置文件,然后执行目标statementId(在UserMapper.xml文件下)的sql语句
     */
    @Test
    public void test1() throws Exception {
        SqlSession sqlSession = Wills.getSqlSession("WillsMyBatisConfig.xml");

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
     * 使用 默认配置文件名(WillsMyBatisConfig.xml),然后执行目标statementId(在UserMapper.xml文件下)的sql语句
     */
    @Test
    public void test2() throws Exception {
        SqlSession sqlSession = Wills.getSqlSession();

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
     * 使用 默认配置文件名(WillsMyBatisConfig.xml),然后执行目标statementId(在UserMapper.xml文件下)的sql语句
     */
    @Test
    public void test3() throws Exception {
        SqlSession sqlSession = Wills.getSqlSession();

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
     * 使用 默认配置文件名(WillsMyBatisConfig.xml),然后执行目标statementId(在UserMapper.xml文件下)的sql语句
     */
    @Test
    public void test4() throws Exception {
        SqlSession sqlSession = Wills.getSqlSession();

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
     * 测试更新
     * 使用 默认配置文件名(WillsMyBatisConfig.xml),然后执行目标statementId(在UserMapper.xml文件下)的sql语句
     */
    @Test
    public void test5() throws Exception {
        SqlSession sqlSession = Wills.getSqlSession();

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

    /**
     * 测试直接通过代理模式，获取目标的Mapper接口类，直接调用增删改查方法
     * 使用 默认配置文件名(WillsMyBatisConfig.xml),然后执行目标statementId(在UserMapper.xml文件下)的sql语句
     */
    @Test
    public void test6() throws Exception {
        UserMapper mapper = Wills.getMapper(UserMapper.class);

        //调用
        User user = new User();
        user.setId(5);
        user.setName("wills");
        user.setAge(24);
        user.setRemark(new String("一个酷酷的boy".getBytes(StandardCharsets.UTF_8)));
        mapper.insert(user);
        System.out.println("======================条件查询=======================");
        User one = mapper.selectOne(user);
        System.out.println(one);
        System.out.println("======================修改用户=======================");
        user.setAge(23);
        mapper.updateById(user);
        System.out.println("======================查询全部=======================");
        List<User> users = mapper.selectList();
        for (User user1 : users) {
            System.out.println(user1);
        }
        System.out.println("======================删除用户=======================");
        mapper.deleteById(users.get(users.size() - 1).getId());
    }

    /**
     * 测试注解的使用 @Sql @Select
     * 使用 默认配置文件名(WillsMyBatisConfig.xml),然后执行目标statementId(在UserMapper.xml文件下)的sql语句
     */
    @Test
    public void test7() throws Exception {
        UserMapper mapper = Wills.getMapper(UserMapper.class);

        List<User> users = mapper.select();
//        users = mapper.selectAll();
        users.forEach(System.out::println);
    }

    /**
     * 测试注解的使用 @Insert
     * 使用 默认配置文件名(WillsMyBatisConfig.xml),然后执行目标statementId(在UserMapper.xml文件下)的sql语句
     */
    @Test
    public void test8() throws Exception {
        UserMapper mapper = Wills.getMapper(UserMapper.class);

        User user = new User();
        user.setName("wills");
        user.setAge(24);
        user.setRemark(new String("一个酷酷的boy - 测试注解插入".getBytes(StandardCharsets.UTF_8)));

        mapper.insert(user);
    }

    /**
     * 测试注解的使用 @Update
     * 使用 默认配置文件名(WillsMyBatisConfig.xml),然后执行目标statementId(在UserMapper.xml文件下)的sql语句
     */
    @Test
    public void test9() throws Exception {
        UserMapper mapper = Wills.getMapper(UserMapper.class);

        User user = new User();
        user.setId(17);
        user.setName("wills");
        user.setAge(24);
        user.setRemark(new String("一个酷酷的boy - 测试注解更新".getBytes(StandardCharsets.UTF_8)));

        mapper.update(user);
    }

    /**
     * 测试注解的使用 @Delete
     * 使用 默认配置文件名(WillsMyBatisConfig.xml),然后执行目标statementId(在UserMapper.xml文件下)的sql语句
     */
    @Test
    public void test10() throws Exception{
        UserMapper mapper = Wills.getMapper(UserMapper.class);

        mapper.delete(14);
    }
}

package com.wills.mybatis.facotry;

import com.wills.mybatis.session.SqlSession;

/**
 * @ClassName SqlSessionFactory
 * @Date 2021/8/10 10:25
 * @Author 王帅
 * @Version 1.0
 * @Description
 * SqlSessionFactory 工厂类，里面的方法有：
 *  openSession 用作开启数据库的Session
 */
public interface SqlSessionFactory {

    public SqlSession openSession();
}

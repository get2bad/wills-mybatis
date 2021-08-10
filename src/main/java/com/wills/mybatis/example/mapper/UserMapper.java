package com.wills.mybatis.example.mapper;

import com.wills.mybatis.entity.User;

import java.util.List;

/**
 * @ClassName UserMapper
 * @Date 2021/8/10 11:44
 * @Author 王帅
 * @Version 1.0
 * @Description
 */
public interface UserMapper {

    public List<User> selectList();

    public User selectOne(Integer id,String name);
}
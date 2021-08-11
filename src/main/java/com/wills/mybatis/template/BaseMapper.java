package com.wills.mybatis.template;

import com.wills.mybatis.example.entity.User;

import java.util.List;

/**
 * @ClassName BaseMapper
 * @Date 2021/8/11 11:07
 * @Author 王帅
 * @Version 1.0
 * @Description
 */
public interface BaseMapper<T> {

    public List<User> selectList();

    public User selectOne(T obj);

    public void insert(T obj);

    public void updateById(T obj);

    public void deleteById(Integer obj);
}

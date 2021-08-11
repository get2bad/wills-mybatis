package com.wills.mybatis.example.mapper;

import com.wills.mybatis.annotation.*;
import com.wills.mybatis.example.entity.User;
import com.wills.mybatis.template.BaseMapper;

import java.util.List;

/**
 * @ClassName UserMapper
 * @Date 2021/8/10 11:44
 * @Author 王帅
 * @Version 1.0
 * @Description
 */
public interface UserMapper extends BaseMapper<User> {

    @Select(value = "select * from user")
    public List<User> select();

    @Update("update user set name = #{name},age=#{age},remark=#{remark} where id = #{id}")
    public void update(User user);

    @Insert("insert into user(name,age,remark) values(#{name},#{age},#{remark})")
    public void insert(User user);

    @Delete("delete from user where id = #{id}")
    public void delete(Integer id);

    @Sql("select * from user")
    public List<User> selectAll();
}

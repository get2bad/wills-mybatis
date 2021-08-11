package com.wills.mybatis.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @ClassName Sql
 * @Date 2021/8/11 12:09
 * @Author 王帅
 * @Version 1.0
 * @Description
 */
// 作用范围是方法上
@Target(ElementType.METHOD)
// 作用生效是在运行中有效
@Retention(RetentionPolicy.RUNTIME)
public @interface Sql {

    public String value();
}

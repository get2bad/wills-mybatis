package com.wills.mybatis.util;

import java.io.InputStream;

/**
 * @ClassName Resources
 * @Date 2021/8/10 10:18
 * @Author 王帅
 * @Version 1.0
 * @Description
 *
 * 从配置文件中读取输入流的工具类
 */
public class Resources {

    // 获取配置文件的文件输入流
    public static InputStream getResourceAsStream(String path){
        InputStream stream = Resources.class.getClassLoader().getResourceAsStream(path);
        return stream;
    }
}

package com.wills.mybatis.example;

import com.wills.mybatis.util.Resources;

import java.io.IOException;
import java.io.InputStream;

/**
 * @ClassName Example
 * @Date 2021/8/10 10:01
 * @Author 王帅
 * @Version 1.0
 * @Description
 */
public class Example {

    public static void main(String[] args) throws IOException {
        InputStream stream = Resources.getResourceAsStream("UserMapper.xml");
        byte[] data = new byte[1024];
        int read = stream.read(data);
        System.out.println(new String(data).trim());
    }
}

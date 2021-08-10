package com.wills.mybatis.builder;

import com.wills.mybatis.config.Configuration;
import com.wills.mybatis.config.MappedStatement;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.InputStream;
import java.util.List;

/**
 * @ClassName XMLMapperBuilder
 * @Date 2021/8/10 11:16
 * @Author 王帅
 * @Version 1.0
 * @Description
 */
public class XMLMapperBuilder {

    private Configuration configuration;

    public XMLMapperBuilder(Configuration configuration) {
        this.configuration = configuration;
    }

    public void parse(InputStream inputStream) throws Exception{
        Document document = new SAXReader().read(inputStream);
        Element root = document.getRootElement();

        // 拿到 namespace
        String namespace = root.attributeValue("namespace");
        // 拿到 select
        List<Element> nodes = root.selectNodes("//select");
        // 读取select的内容
        for (Element node : nodes) {
            String id = node.attributeValue("id");
            String parameterType = node.attributeValue("parameterType");
            String resultType = node.attributeValue("resultType");
            String sql = node.getTextTrim();
            // 转换 参数类 和 返回值类，方便后期 Class.newInstance 使用
            Class<?> parameterTypeClass = getClassNameAsClassObj(parameterType);
            Class<?> resultTypeClass = getClassNameAsClassObj(resultType);

            MappedStatement statement = new MappedStatement();
            statement.setId(id);
            statement.setParameterType(parameterTypeClass);
            statement.setResultType(resultTypeClass);
            statement.setSql(sql);
            // 组装key 例如： com.wills.mybatis.example.mapper.UserMapper.selectList
            String key = namespace + "." + id;
            configuration.getMappedStatementMap().put(key,statement);
        }
    }

    public Class<?> getClassNameAsClassObj(String className) throws Exception{
        Class<?> clazz = Class.forName(className);
        return clazz;
    }
}

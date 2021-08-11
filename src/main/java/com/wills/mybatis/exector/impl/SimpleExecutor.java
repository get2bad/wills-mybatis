package com.wills.mybatis.exector.impl;

import com.wills.mybatis.config.Configuration;
import com.wills.mybatis.config.MappedStatement;
import com.wills.mybatis.exector.Executor;
import com.wills.mybatis.util.GenericTokenParser;
import com.wills.mybatis.util.ParameterMappingTokenHandler;
import com.wills.mybatis.util.entity.BoundSql;
import com.wills.mybatis.util.entity.ParameterMapping;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName SimpleExector
 * @Date 2021/8/10 12:00
 * @Author 王帅
 * @Version 1.0
 * @Description
 * Sql执行器 的 默认实现 ，如果有其他的实现方法，可以实现这个 Executor 接口，重写里面的方法即可~
 */
public class SimpleExecutor implements Executor {

    private Connection connection = null;


    @Override
    public <E> List<E> query(Configuration configuration, MappedStatement mappedStatement, Object... params) throws Exception {
        // 1. 注册驱动，获取连接
        connection = configuration.getDataSource().getConnection();

        // 2. 获取sql语句 : select * from user where id = #{id} and username = #{username}
        //转换sql语句： select * from user where id = ? and username = ? ，转换的过程中，还需要对#{}里面的值进行解析存储
        String sql = mappedStatement.getSql();
        BoundSql boundSql = getBoundSql(sql);

        // 3.获取预处理对象：preparedStatement
        PreparedStatement preparedStatement = connection.prepareStatement(boundSql.getSql());

        // 4. 设置参数
        //获取到了参数的全路径
        Class<?> paramtertypeClass = mappedStatement.getParameterType();

        List<ParameterMapping> parameterMappingList = boundSql.getParameterMappingList();
        for (int i = 0; i < parameterMappingList.size(); i++) {
            ParameterMapping parameterMapping = parameterMappingList.get(i);
            String content = parameterMapping.getContent();
            //反射
            Field declaredField = paramtertypeClass.getDeclaredField(content);
            // 设置可以随意访问
            declaredField.setAccessible(true);
            Object o = declaredField.get(params[0]);
            // 空出间隔符
            preparedStatement.setObject(i + 1, o);

        }

        // 5. 执行sql
        ResultSet resultSet = preparedStatement.executeQuery();
        Class<?> resultTypeClass = mappedStatement.getResultType();

        ArrayList<Object> objects = new ArrayList<>();

        // 6. 封装返回结果集
        while (resultSet.next()) {
            Object o = resultTypeClass.newInstance();
            //元数据
            ResultSetMetaData metaData = resultSet.getMetaData();
            for (int i = 1; i <= metaData.getColumnCount(); i++) {

                // 字段名
                String columnName = metaData.getColumnName(i);
                // 字段的值
                Object value = resultSet.getObject(columnName);

                //使用反射，根据数据库表和实体的对应关系，完成封装
                PropertyDescriptor propertyDescriptor = new PropertyDescriptor(columnName, resultTypeClass);
                Method writeMethod = propertyDescriptor.getWriteMethod();
                writeMethod.invoke(o, value);


            }
            objects.add(o);

        }
        return (List<E>) objects;

    }

    @Override
    public <X> List<X> execute(Configuration configuration, String sql, X obj,Class<?> resultTypeClass) throws Exception {
        connection = configuration.getDataSource().getConnection();
        BoundSql boundSql = getBoundSql(sql);
        sql = boundSql.getSql();
        PreparedStatement preparedStatement = connection.prepareStatement(sql);

        if(obj != null){
            Class<?> paramtertypeClass = obj.getClass();

            List<ParameterMapping> parameterMappingList = boundSql.getParameterMappingList();
            for (int i = 0; i < parameterMappingList.size(); i++) {
                ParameterMapping parameterMapping = parameterMappingList.get(i);
                String content = parameterMapping.getContent();
                //反射
                Field declaredField = paramtertypeClass.getDeclaredField(content);
                // 设置可以随意访问
                declaredField.setAccessible(true);
                Object o = declaredField.get(obj);
                // 空出间隔符
                preparedStatement.setObject(i + 1, o);

            }
        }

        ResultSet resultSet = preparedStatement.executeQuery();
        if(resultSet == null){
            return null;
        }
        ArrayList<Object> objects = new ArrayList<>();

        // 6. 封装返回结果集
        while (resultSet.next()) {
            Object o = resultTypeClass.newInstance();
            //元数据
            ResultSetMetaData metaData = resultSet.getMetaData();
            for (int i = 1; i <= metaData.getColumnCount(); i++) {

                // 字段名
                String columnName = metaData.getColumnName(i);
                // 字段的值
                Object value = resultSet.getObject(columnName);

                //使用反射，根据数据库表和实体的对应关系，完成封装
                PropertyDescriptor propertyDescriptor = new PropertyDescriptor(columnName, resultTypeClass);
                Method writeMethod = propertyDescriptor.getWriteMethod();
                writeMethod.invoke(o, value);


            }
            objects.add(o);

        }
        return (List<X>) objects;
    }

    /**
     * 自适应方法，自动适应下面的insert方法
     * @param configuration
     * @param sql
     * @param obj
     * @param <T>
     * @throws Exception
     */
    @Override
    public <T> void insertAdapter(Configuration configuration, String sql, T obj) throws Exception {
        MappedStatement mappedStatement = new MappedStatement();
        mappedStatement.setSql(sql);
        mappedStatement.setParameterType(obj.getClass());
        insert(configuration, mappedStatement, obj);
    }

    @Override
    public <T> void insert(Configuration configuration, MappedStatement statement, T obj) throws Exception {
        connection = configuration.getDataSource().getConnection();
        String sql = statement.getSql();
        BoundSql boundSql = getBoundSql(sql);
        sql = boundSql.getSql();
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        // 进行条件赋值
        Class<?> parameterType = statement.getParameterType();
        Field[] fields = parameterType.getDeclaredFields();
        //
        for (int i = 1; i < fields.length; i++) {
            PropertyDescriptor propertyDescriptor = new PropertyDescriptor(fields[i].getName(), parameterType);
            Method readMethod = propertyDescriptor.getReadMethod();
            Object value = readMethod.invoke(obj);
            preparedStatement.setObject(i, value);
        }
        preparedStatement.execute();
    }

    @Override
    public <T> void updateById(Configuration configuration, MappedStatement statement, T obj) throws Exception {
        connection = configuration.getDataSource().getConnection();
        String sql = statement.getSql();
        BoundSql boundSql = getBoundSql(sql);
        sql = boundSql.getSql();
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        Class<?> parameterType = statement.getParameterType();
        Field[] fields = parameterType.getDeclaredFields();
        for (int i = 1; i < fields.length; i++) {
            PropertyDescriptor propertyDescriptor = new PropertyDescriptor(fields[i].getName(), parameterType);
            Method readMethod = propertyDescriptor.getReadMethod();
            Object value = readMethod.invoke(obj);
            preparedStatement.setObject(i, value);
        }
        // 赋值 id
        PropertyDescriptor propertyDescriptor = new PropertyDescriptor(fields[0].getName(), parameterType);
        Method readMethod = propertyDescriptor.getReadMethod();
        Object value = readMethod.invoke(obj);
        preparedStatement.setObject(fields.length, value);

        preparedStatement.execute();
    }

    @Override
    public <T> void updateByIdAdapter(Configuration configuration, String sql, T obj) throws Exception {
        MappedStatement mappedStatement = new MappedStatement();
        mappedStatement.setSql(sql);
        mappedStatement.setParameterType(obj.getClass());
        updateById(configuration, mappedStatement, obj);
    }

    @Override
    public void deleteByIdAdapter(Configuration configuration, String sql, Object id) throws Exception {
        MappedStatement mappedStatement = new MappedStatement();
        mappedStatement.setSql(sql);
        mappedStatement.setParameterType(id.getClass());
        deleteById(configuration, mappedStatement, id);
    }

    @Override
    public void deleteById(Configuration configuration, MappedStatement statement, Object id) throws Exception {
        connection = configuration.getDataSource().getConnection();

        String sql = statement.getSql();

        BoundSql boundSql = getBoundSql(sql);
        sql = boundSql.getSql();
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        // 这里要注意，preparedStatement 起始的 index 是 1  ！！！ 神坑！
        preparedStatement.setInt(1, (Integer) id);

        preparedStatement.execute();

    }

    @Override
    public void close() throws Exception {
        if(connection != null){
            connection.close();
        }
    }


    /**
     * 完成对#{}的解析工作：1.将#{}使用？进行代替，2.解析出#{}里面的值进行存储
     *
     * @param sql
     * @return
     */
    private BoundSql getBoundSql(String sql) {
        //标记处理类：配置标记解析器来完成对占位符的解析处理工作
        ParameterMappingTokenHandler parameterMappingTokenHandler = new ParameterMappingTokenHandler();
        GenericTokenParser genericTokenParser = new GenericTokenParser("#{", "}", parameterMappingTokenHandler);
        //解析出来的sql
        String parseSql = genericTokenParser.parse(sql);
        //#{}里面解析出来的参数名称
        List<ParameterMapping> parameterMappings = parameterMappingTokenHandler.getParameterMappings();

        BoundSql boundSql = new BoundSql(parseSql, parameterMappings);
        return boundSql;

    }


}

package com.wills.mybatis.session.impl;

import com.mysql.jdbc.StringUtils;
import com.wills.mybatis.annotation.*;
import com.wills.mybatis.config.Configuration;
import com.wills.mybatis.config.MappedStatement;
import com.wills.mybatis.example.entity.User;
import com.wills.mybatis.exector.Executor;
import com.wills.mybatis.exector.impl.SimpleExecutor;
import com.wills.mybatis.session.SqlSession;
import org.apache.log4j.Logger;

import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @ClassName DefaultSqlSession
 * @Date 2021/8/10 11:53
 * @Author 王帅
 * @Version 1.0
 * @Description
 * SqlSession 的默认实现， 如果有其他的实现，就可以创建其他的类，实现 SqlSession接口，重写里面的方法即可
 */
public class DefaultSqlSession implements SqlSession {

    final Logger log = Logger.getLogger(this.getClass());

    private String mapperClassName;
    private Configuration configuration;
    private Executor executor;

    public DefaultSqlSession(Configuration configuration) {
        this.configuration = configuration;
        executor = new SimpleExecutor();
    }

    public MappedStatement doubleCheckStatement(){
        MappedStatement mappedStatement = configuration.getMappedStatementMap().get(mapperClassName);
        if(mappedStatement == null || StringUtils.isNullOrEmpty(mappedStatement.getSql())){
            throw new RuntimeException("提取不到对应mapper文件下的相关sql信息，请您重试！");
        }
        return mappedStatement;
    }

    @Override
    public <E> List<E> selectList(String statementId, Object... args) throws Exception {
        MappedStatement mappedStatement = configuration.getMappedStatementMap().get(statementId);
        if(mappedStatement == null || StringUtils.isNullOrEmpty(mappedStatement.getSql())){
            mappedStatement = doubleCheckStatement();
        }
        List<E> query = executor.query(configuration, mappedStatement, args);
        return query;
    }


    @Override
    public <X> List<X> execute(String sql, X obj,Class<?> resultTypeClass) throws Exception {
        return executor.execute(configuration, sql, obj, resultTypeClass);
    }

    @Override
    public <T> T selectOne(String statementId, Object... args) throws Exception {
//        List<T> list = selectList(statementId, args);
        MappedStatement mappedStatement = configuration.getMappedStatementMap().get(statementId);
        if(mappedStatement == null || StringUtils.isNullOrEmpty(mappedStatement.getSql())){
            mappedStatement = doubleCheckStatement();
        }
        List<T> list = executor.query(configuration, mappedStatement, args);
        if(list == null || list.size() == 0) {
            return null;
        }
        if(list.size() > 1){
            throw new RuntimeException("您的查询条件返回的记录包含多个！不允许调用本方法！请您检查查询条件！");
        }
        return list.get(0);
    }

    @Override
    public <T> void insert(String statementId, T obj) throws Exception {
        MappedStatement mappedStatement = configuration.getMappedStatementMap().get(statementId);
        if(mappedStatement == null){
            mappedStatement = doubleCheckStatement();
        }
        executor.insert(configuration,mappedStatement,obj);
    }

    @Override
    public <T> void insertAdapter(String sql, T obj) throws Exception {
        if(sql == null || sql.trim().length() == 0){
            throw new RuntimeException("您输入的sql为空！");
        }
        executor.insertAdapter(configuration, sql, obj);
    }

    @Override
    public <T> void updateById(String statementId, T obj) throws Exception {
        MappedStatement mappedStatement = configuration.getMappedStatementMap().get(statementId);
        if(mappedStatement == null){
            mappedStatement = doubleCheckStatement();
        }
        executor.updateById(configuration,mappedStatement,obj);
    }

    @Override
    public <T> void updateByIdAdapter(String sql, T obj) throws Exception {
        if(sql == null || sql.trim().length() == 0){
            throw new RuntimeException("您输入的sql为空！");
        }
        executor.updateByIdAdapter(configuration, sql, obj);
    }

    @Override
    public void deleteByIdAdapter(String sql, Object id) throws Exception {
        if(sql == null || sql.trim().length() == 0){
            throw new RuntimeException("您输入的sql为空！");
        }
        executor.deleteByIdAdapter(configuration, sql, id);
    }

    @Override
    public void deleteById(String statementId, Object id)  throws Exception{
        // 找到这个 statementId
        MappedStatement mappedStatement = configuration.getMappedStatementMap().get(statementId);
        if(mappedStatement == null){
            mappedStatement = doubleCheckStatement();
        }
        executor.deleteById(configuration, mappedStatement, id);
    }

    @Override
    public <T> T getMapper(Class<?> mapperClass) {
        T res = (T) Proxy.newProxyInstance(mapperClass.getClassLoader(), new Class[]{mapperClass}, new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                log.warn("反射代理调用了： " + method.getName());

                // 获取基本信息
                // selectOne
                String methodName = method.getName();
                mapperClassName = mapperClass.getName() + "." + methodName;
                // className:namespace
                String className = method.getDeclaringClass().getName();
                //statementid
                String key = className + "." + methodName;
                //判断是否实现泛型类型参数化
                Annotation[] annotations = method.getAnnotations();

                if(annotations.length != 0){
                    for (Annotation annotation : annotations) {
                        String clazzName = method.getReturnType().getName();
                        // 如果是泛型
                        if(method.getGenericReturnType() instanceof ParameterizedType){
                            Type type = method.getGenericReturnType();
                            clazzName = ((ParameterizedType)type).getActualTypeArguments()[0].getTypeName();
                        }

                        if(method.getAnnotation(Sql.class) != null){
                            // 执行方法
                            return execute(method.getAnnotation(Sql.class).value() , args, Class.forName(clazzName));
                        }
                        if(method.getAnnotation(Select.class) != null){
                            // 执行查询方法
                            return execute(method.getAnnotation(Select.class).value() , args, Class.forName(clazzName));
                        }
                        if(method.getAnnotation(Insert.class) != null){
                            // 执行插入方法
                            insertAdapter(method.getAnnotation(Insert.class).value(), args==null?null:args[0]);
                        }
                        if(method.getAnnotation(Update.class) != null){
                            // 执行更新方法
                            updateByIdAdapter(method.getAnnotation(Update.class).value(), args==null?null:args[0]);
                        }
                        if(method.getAnnotation(Delete.class) != null){
                            // 执行删除方法
                            deleteByIdAdapter(method.getAnnotation(Delete.class).value(), args==null?null:args[0]);
                        }
                    }
                }else {
                    if("selectOne".equals(methodName)){
                        Object one = selectOne(key, args[0]);
                        return one;
                    }

                    if("selectList".equals(methodName)){
                        return selectList(key, args);
                    }

                    if("insert".equals(methodName)){
                        insert(key, args[0]);
                    }

                    if("deleteById".equals(methodName)){
                        deleteById(key, args[0]);
                    }

                    if("updateById".equals(methodName)){
                        updateById(key, args[0]);
                    }
                }
                return null;
            }
        });
        return res;
    }

    @Override
    public void close() throws Exception {
        executor.close();
    }
}

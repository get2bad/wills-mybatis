package com.wills.mybatis.session.impl;

import com.mysql.jdbc.StringUtils;
import com.wills.mybatis.config.Configuration;
import com.wills.mybatis.config.MappedStatement;
import com.wills.mybatis.example.entity.User;
import com.wills.mybatis.exector.Executor;
import com.wills.mybatis.exector.impl.SimpleExecutor;
import com.wills.mybatis.session.SqlSession;
import org.apache.log4j.Logger;

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
    public <T> void updateById(String statementId, T obj) throws Exception {
        MappedStatement mappedStatement = configuration.getMappedStatementMap().get(statementId);
        if(mappedStatement == null){
            mappedStatement = doubleCheckStatement();
        }
        executor.updateById(configuration,mappedStatement,obj);
    }

    @Override
    public void deleteById(String statementId, Object id)  throws Exception{
        // 找到这个 statementId
        MappedStatement mappedStatement = configuration.getMappedStatementMap().get(statementId);
        if(mappedStatement == null){
            mappedStatement = doubleCheckStatement();
        }
        executor.delete(configuration, mappedStatement, id);
    }

    @Override
    public <T> T getMapper(Class<?> mapperClass) {
        T res = (T) Proxy.newProxyInstance(mapperClass.getClassLoader(), new Class[]{mapperClass}, new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                // selectOne
                String methodName = method.getName();
                mapperClassName = mapperClass.getName() + "." + methodName;
                // className:namespace
                String className = method.getDeclaringClass().getName();
                //statementid
                String key = className + "." + methodName;
                Type genericReturnType = method.getGenericReturnType();
                //判断是否实现泛型类型参数化
//                if (genericReturnType instanceof ParameterizedType) {
//
//                }
                log.warn("反射代理调用了： " + methodName);
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

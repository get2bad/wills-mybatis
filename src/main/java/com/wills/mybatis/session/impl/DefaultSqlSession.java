package com.wills.mybatis.session.impl;

import com.mysql.jdbc.StringUtils;
import com.wills.mybatis.config.Configuration;
import com.wills.mybatis.config.MappedStatement;
import com.wills.mybatis.exector.Executor;
import com.wills.mybatis.exector.impl.SimpleExecutor;
import com.wills.mybatis.session.SqlSession;

import java.lang.reflect.*;
import java.util.ArrayList;
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

    private Configuration configuration;
    private Executor executor;

    public DefaultSqlSession(Configuration configuration) {
        this.configuration = configuration;
        executor = new SimpleExecutor();
    }

    @Override
    public <E> List<E> selectList(String statementId, Object... args) throws Exception {
        MappedStatement mappedStatement = configuration.getMappedStatementMap().get(statementId);
        if(mappedStatement == null || StringUtils.isNullOrEmpty(mappedStatement.getSql())){
            throw new RuntimeException("提取不到对应mapper文件下的相关sql信息，请您重试！");
        }
        List<E> query = executor.query(configuration, mappedStatement, args);
        return query;
    }

    @Override
    public <T> T selectOne(String statementId, Object... args) throws Exception {
//        List<T> list = selectList(statementId, args);
        MappedStatement mappedStatement = configuration.getMappedStatementMap().get(statementId);
        if(mappedStatement == null || StringUtils.isNullOrEmpty(mappedStatement.getSql())){
            throw new RuntimeException("提取不到对应mapper文件下的相关sql信息，请您重试！");
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
        MappedStatement statement = configuration.getMappedStatementMap().get(statementId);
        if(statement == null){
            throw new RuntimeException("未找到对应的statement");
        }
        executor.insert(configuration,statement,obj);
    }

    @Override
    public <T> void updateById(String statementId, T obj) throws Exception {
        MappedStatement statement = configuration.getMappedStatementMap().get(statementId);
        if(statement == null){
            throw new RuntimeException("未找到对应的statement");
        }
        executor.updateById(configuration,statement,obj);
    }

    @Override
    public void deleteById(String statementId, Object id)  throws Exception{
        // 找到这个 statementId
        MappedStatement mappedStatement = configuration.getMappedStatementMap().get(statementId);
        if(mappedStatement == null){
            throw new RuntimeException("未找到对应的statement");
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
                // className:namespace
                String className = method.getDeclaringClass().getName();
                //statementid
                String key = className + "." + methodName;
                MappedStatement mappedStatement =
                        configuration.getMappedStatementMap().get(key);
                Type genericReturnType = method.getGenericReturnType();
                ArrayList arrayList = new ArrayList<>();
                //判断是否实现泛型类型参数化
                if (genericReturnType instanceof ParameterizedType) {

                    if(methodName.equals("selectOne")){
                        return selectOne(key, args);
                    }

                    if(methodName.equals("selectList")){
                        return selectList(key, args);
                    }

                    if(methodName.equals("deleteById")){
                        deleteById(key, args);
                    }

                    if(methodName.equals("updateById")){
                        updateById(key, args);
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

package com.wills.mybatis.session;

import java.util.List;

/**
 * @ClassName SqlSession
 * @Date 2021/8/10 11:52
 * @Author 王帅
 * @Version 1.0
 * @Description
 * 主要声明两个方法，就是我们定义在 *Mapper.xml 中的 SelectOne / SelectList
 */
public interface SqlSession {

    /**
     * @param statementId 我们在 com.wills.mybatis.builder.XMLMapperBuilder 下读取的statementid
     * @param args 传入的参数
     * @param <E> 返回的泛型
     * @return
     */
    public <E> List<E> selectList(String statementId,Object... args) throws Exception;

    public <X> List<X> execute(String sql,X obj,Class<?> resultTypeClass) throws Exception;

    /**
     * @param statementId 我们在 com.wills.mybatis.builder.XMLMapperBuilder 下读取的statementid
     * @param args 传入的参数
     * @param <T> 返回的泛型
     * @return
     */
    public <T> T selectOne(String statementId,Object... args) throws Exception;

    public <T> void insert(String statementId,T obj) throws Exception;

    public <T> void insertAdapter(String sql,T obj) throws Exception;

    public <T> void updateById(String statementId,T obj) throws Exception;

    public <T> void updateByIdAdapter(String sql,T obj) throws Exception;

    public void deleteById(String statementId,Object id) throws Exception;

    public void deleteByIdAdapter(String sql,Object id) throws Exception;

    public <T> T getMapper(Class<?> mapperClass);

    public void close() throws Exception;
}

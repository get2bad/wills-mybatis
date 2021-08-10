package com.wills.mybatis.config;

/**
 * @ClassName MappedStatement
 * @Date 2021/8/10 10:13
 * @Author 王帅
 * @Version 1.0
 * @Description
 */
public class MappedStatement {

    private String id;

    // sql语句
    private String sql;

    // 参数类型
    private Class<?> parameterType;

    // 结果类型
    private Class<?> resultType;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }

    public Class<?> getParameterType() {
        return parameterType;
    }

    public void setParameterType(Class<?> parameterType) {
        this.parameterType = parameterType;
    }

    public Class<?> getResultType() {
        return resultType;
    }

    public void setResultType(Class<?> resultType) {
        this.resultType = resultType;
    }
}

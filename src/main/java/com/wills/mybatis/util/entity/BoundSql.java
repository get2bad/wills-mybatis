package com.wills.mybatis.util.entity;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName BoundSql
 * @Date 2021/8/10 13:58
 * @Author 王帅
 * @Version 1.0
 * @Description
 */
public class BoundSql {

    private String sql;

    private List<ParameterMapping> parameterMappingList = new ArrayList<>();

    public BoundSql(String sql, List<ParameterMapping> parameterMappingList) {
        this.sql = sql;
        this.parameterMappingList = parameterMappingList;
    }

    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }

    public List<ParameterMapping> getParameterMappingList() {
        return parameterMappingList;
    }

    public void setParameterMappingList(List<ParameterMapping> parameterMappingList) {
        this.parameterMappingList = parameterMappingList;
    }
}

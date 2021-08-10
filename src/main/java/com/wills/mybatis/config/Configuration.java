package com.wills.mybatis.config;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName Configuration
 * @Date 2021/8/10 10:12
 * @Author 王帅
 * @Version 1.0
 * @Description
 * 配置类实体类 包含 数据源 DataSource ，以及各种 Mapper.xml的解析缓存记录
 */
public class Configuration {

    private DataSource dataSource;

    private Map<String,MappedStatement> mappedStatementMap = new HashMap();

    public DataSource getDataSource() {
        return dataSource;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public Map<String, MappedStatement> getMappedStatementMap() {
        return mappedStatementMap;
    }

    public void setMappedStatementMap(Map<String, MappedStatement> mappedStatementMap) {
        this.mappedStatementMap = mappedStatementMap;
    }
}

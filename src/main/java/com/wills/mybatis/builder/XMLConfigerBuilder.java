package com.wills.mybatis.builder;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.wills.mybatis.config.Configuration;
import com.wills.mybatis.util.Resources;
import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.InputStream;
import java.util.List;
import java.util.Properties;

/**
 * @ClassName XMLConfigerBuilder
 * @Date 2021/8/10 10:30
 * @Author 王帅
 * @Version 1.0
 * @Description
 *
 * XML 解析类， parseConfiguator 会生成一个基本的配置类 Configuation
 */
public class XMLConfigerBuilder {

    private Logger log = Logger.getLogger(XMLConfigerBuilder.class);

    private Configuration configuration;

    public XMLConfigerBuilder(Configuration configuration) {
        this.configuration = configuration;
    }

    // 进行configuration转化
    public Configuration parseConfiguator(InputStream inputStream) throws Exception{
        // 读取 输入流，生成一个 Document 文档
        Document document = new SAXReader().read(inputStream);
        // 拿到根element
        Element element = document.getRootElement();
        List<Element> list = element.selectNodes("//properties");
        // 生成一个 Properties(本质还是一个map结构)
        Properties properties = new Properties();
        for (Element e : list) {
            String name = e.attributeValue("name");
            String value = e.attributeValue("value");
            properties.setProperty(name, value);
        }

        // 配置 c3p0 连接池
        ComboPooledDataSource dataSource = new ComboPooledDataSource();
        dataSource.setDriverClass(properties.getProperty("driverClass"));
        dataSource.setUser(properties.getProperty("username"));
        dataSource.setPassword(properties.getProperty("password"));
        dataSource.setJdbcUrl(jdbcUrlBuilder(properties));

        configuration.setDataSource(dataSource);

        // 读取配置文件配置的 *Mapper.xml
        List<Element> nodes = element.selectNodes("//mapper");
        XMLMapperBuilder xmlMapperBuilder = new XMLMapperBuilder(configuration);
        for (Element node : nodes) {
            String resource = node.attributeValue("resource");
            InputStream stream = Resources.getResourceAsStream(resource);
            xmlMapperBuilder.parse(stream);
        }

        return configuration;
    }

    // 组装 连接的 jdbc url,类似于工厂类，用于生成一个连接字符串url(残次品版 -_- )
    public String jdbcUrlBuilder(Properties properties){
        // 校验传入的properties是否包含目标字符串
        String host = properties.getProperty("host");
        String port = properties.getProperty("port");
        String database = properties.getProperty("database");
        if(host == null || port == null || database == null){
            throw new RuntimeException("关键信息没有传入！请您检查 WillsMybatisConfig.xml中的数据库配置的Properties中的host、port、database是否正确配置！");
        }

        StringBuilder sb = new StringBuilder();
        String driverClass = properties.getProperty("driverClass");
        switch (driverClass){
            case "com.jdbc.mysql.Driver": {
                // 如果是mysql系列：
                // 实例： jdbc:mysql://47.93.126.142:3306/uav
                sb.append("jdbc:mysql://");
                sb.append(host);
                sb.append(":");
                sb.append(port);
                sb.append("/");
                sb.append(database);
            };break;
            default: sb.append("none");break;
        }

        log.info("jdbc 连接url：" + sb.toString());
        return sb.toString();
    }
}

# wills-Mybatis

> 本仓库用作属于wills的自制mybatis框架的源码，后续会不断完善，敬请期待

## 要实现的功能

+ 配置文件完善(详情请见[resource](./src/main/resources))<font color=red>已完成</font>
+ 基本解析 XXMapper.xml 功能(包含Select、Update、Delete、Insert)(暂不支持条件if条件判断)<font color=green>半完成</font>
+ 基本的[BaseMapper<T>](./src/main/java/com/wills/mybatis/template/BaseMapper.java)基础功能(CRUD，类似于MyBatis-Plus)<font color=red>已完成</font>
+ 注解支持(暂不支持 要实现的注解@Sql @Select @Insert @Update @Delete)<font color=orange>未完成</font>

## 实现步骤

本框架是一个简易的MyBatis框架，在框架端我们要做到一下的操作：

1. 读取配置⽂件

读取完成以后以流的形式存在，我们不能将读取到的配置信息以流的形式存放在内存中，不好操作，可

以创建javaBean来存储

+ Configuration : 存放数据库基本信息、Map<唯⼀标识，Mapper> 唯⼀标识：namespace + "."+ id

+ MappedStatement：sql语句、statement类型、输⼊参数java类型、输出参数java类型

2. 解析配置⽂件

创建sqlSessionFactoryBuilder类：

⽅法：sqlSessionFactory build()：

第⼀：使⽤dom4j解析配置⽂件，将解析出来的内容封装到Configuration和MappedStatement中

第⼆：创建SqlSessionFactory的实现类DefaultSqlSession

3. 创建SqlSessionFactory：

⽅法：openSession() : 获取sqlSession接⼝的实现类实例对象

4. 创建sqlSession接⼝及实现类：主要封装crud⽅法

selectList(String statementId,Object param)：查询所有

selectOne(String statementId,Object param)：查询单个

具体实现：封装JDBC完成对数据库表的查询操作

5. 创建Executor 执行器，主要承接SqlSession实现类的调用，用来执行具体的sql任务



涉及到的设计模式：

Builder构建者设计模式、⼯⼚模式、代理模式



## 流程解析

1. 首先创建配置文件 - [WillsMyBatisConfig.xml](./src/main/resources/WillsMyBatisConfig.xml)

主要就是和mybatis一样(非mybatis-starter)，配置一个配置文件，包含基础的连接信息还有本地的*Mapper.xml所在地址

```xml
<configuration>
    <property name="driverClass" value="com.mysql.jdbc.Driver"></property>
    <property name="username" value="root"></property>
    <property name="password" value="123456"></property>
    <property name="host" value="localhost"></property>
    <property name="port" value="3306"></property>
    <property name="database" value="test"></property>
    <property name="option" value="useUnicode=true&amp;characterEncoding=utf8"></property>

    <mapper resource="UserMapper.xml"></mapper>
</configuration>
```

2. 创建和配置文件关联的 - [UserMapper.xml](./src/main/resources/UserMapper.xml)

```xml
<mapper namespace="com.wills.mybatis.example.mapper.UserMapper">
    <select id="selectList" parameterType="com.wills.mybatis.example.entity.User" resultType="com.wills.mybatis.example.entity.User">
        Select * from user
    </select>

    <select id="selectOne" parameterType="com.wills.mybatis.example.entity.User" resultType="com.wills.mybatis.example.entity.User">
        Select * from user where id= #{id} and name = #{name}
    </select>
    <delete id="deleteById" parameterType="java.lang.Integer">
        delete from user where id = #{id}
    </delete>
    <insert id="insert" parameterType="com.wills.mybatis.example.entity.User">
        insert into user(name,age,remark) values(#{name},#{age},#{remark});
    </insert>
    <update id="updateById" parameterType="com.wills.mybatis.example.entity.User">
        update user set name = #{name},age=#{age},remark=#{remark} where id = #{id}
    </update>
</mapper>
```

3. 创建一个工具类，用于读取文件，返回一个文件输入流，用于dom4j的读取 - [Resources](./src/main/java/com/wills/mybatis/util/Resources.java)

```java
public class Resources {

    // 获取配置文件的文件输入流
    public static InputStream getResourceAsStream(String path){
        InputStream stream = Resources.class.getClassLoader().getResourceAsStream(path);
        return stream;
    }
}
```

4. 创建[SqlSessionFactoryBuilder](./src/main/java/com/wills/mybatis/builder/SqlSessionFactoryBuilder.java)，用途是build出来一个SqlSessionFactory(用于生产一个SqlSession实现类)

```java
public class SqlSessionFactoryBuilder {

    private Configuration configuration;

    public SqlSessionFactoryBuilder() {
        this.configuration = new Configuration();
    }

    /**
     * 指定要解析的xml配置文件
     * 绑定了 inputStream 后，读取目标文件下的一些基础链接信息 以及 卸载配置文件中的 *Mapper.xml 文件读取
     * @param inputStream
     * @return
     * @throws Exception
     */
    public SqlSessionFactory build(InputStream inputStream) throws Exception{
        // 解析配置文件
        XMLConfigerBuilder xmlConfigurator = new XMLConfigerBuilder(configuration);

        Configuration configuration = xmlConfigurator.parseConfiguator(inputStream);

        SqlSessionFactory sqlSessionFactory = new DefaultSqlSessionFacotry(configuration);


        return sqlSessionFactory;
    }
	
  	// 读取默认的
    public SqlSessionFactory build() throws Exception{
        // 解析配置文件
        XMLConfigerBuilder xmlConfigurator = new XMLConfigerBuilder(configuration);

        // 读取默认配置文件的输入流
        InputStream inputStream = Resources.getResourceAsStream("WillsMyBatisConfig.xml");

        Configuration configuration = xmlConfigurator.parseConfiguator(inputStream);

        SqlSessionFactory sqlSessionFactory = new DefaultSqlSessionFacotry(configuration);


        return sqlSessionFactory;
    }
}
```

相关实体类：

[Configuration](./src/main/java/com/wills/mybatis/config/Configuration.java)

```java
public class Configuration {

    private DataSource dataSource;

    private Map<String,MappedStatement> mappedStatementMap = new HashMap();

    // getter / setter
}
```

[MappedStatement](./src/main/java/com/wills/mybatis/config/MappedStatement.java)

```java
public class MappedStatement {

    private String id;

    // sql语句
    private String sql;

    // 参数类型
    private Class<?> parameterType;

    // 结果类型
    private Class<?> resultType;

		// getter / setter
}
```

5. 创建 *Mapper.xml解析类，用于缓存statementId,参数和返回值类的反射 Class<?> clazz

[XMLMapperBuilder](./src/main/java/com/wills/mybatis/builder/XMLMapperBuilder.java)

```java
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
        List<Element> deletes = root.selectNodes("//delete");
        List<Element> updates = root.selectNodes("//update");
        List<Element> inserts = root.selectNodes("//insert");
        nodes.addAll(deletes);
        nodes.addAll(updates);
        nodes.addAll(inserts);
        // 读取select的内容
        for (Element node : nodes) {
            String id = node.attributeValue("id");
            String parameterType = node.attributeValue("parameterType");
            String resultType = node.attributeValue("resultType");
            String sql = node.getTextTrim();
            // 转换 参数类 和 返回值类，方便后期 Class.newInstance 使用
            Class<?> parameterTypeClass = null;
            if(parameterType != null){
                parameterTypeClass = getClassNameAsClassObj(parameterType);
            }
            Class<?> resultTypeClass = null;
            if(resultType != null) {
                resultTypeClass = getClassNameAsClassObj(resultType);
            }

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
```

6. 创建 SqlSessionFactoryBuilder 中build方法中需要的[ XMLConfigerBuilder](./src/main/java/com/wills/mybatis/builder/XMLConfigerBuilder.java) 类，此类用于读取配置文件的内容，进行解析

```java
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
        List<Element> list = element.selectNodes("//property");
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
        String option = properties.getProperty("option");
        if(host == null || port == null || database == null){
            throw new RuntimeException("关键信息没有传入！请您检查 WillsMybatisConfig.xml中的数据库配置的Properties中的host、port、database是否正确配置！");
        }

        StringBuilder sb = new StringBuilder();
        String driverClass = properties.getProperty("driverClass");
        switch (driverClass){
            case "com.mysql.jdbc.Driver": {
                // 如果是mysql系列：
                // 实例： jdbc:mysql://localhost:3306/uav
                sb.append("jdbc:mysql://");
                sb.append(host);
                sb.append(":");
                sb.append(port);
                sb.append("/");
                sb.append(database);
                if(option != null){
                    sb.append("?");
                    sb.append(option);
                }
            };break;
            default: sb.append("none");break;
        }

        log.info("jdbc 连接url：" + sb.toString());
        return sb.toString();
    }
}
```

7. 创建 SqlSessionFactoryBuilder 中build方法中需要的 [SqlSessionFactory](./src/main/java/com/wills/mybatis/facotry/SqlSessionFactory.java) 类,以及他的实现类

```java
public interface SqlSessionFactory {

    public SqlSession openSession();
}
```

[DefaultSqlSessionFacotry](./src/main/java/com/wills/mybatis/facotry/impl/DefaultSqlSessionFacotry.java)

```java
public class DefaultSqlSessionFacotry implements SqlSessionFactory {

    private Configuration configuration;

    public DefaultSqlSessionFacotry(Configuration configuration) {
        this.configuration = configuration;
    }


    @Override
    public SqlSession openSession() {
        return new DefaultSqlSession(configuration);
    }
}
```

8. 创建SqlSessionFactory 中 openSession方法中需要的 [SqlSession](./src/main/java/com/wills/mybatis/session/SqlSession.java)以及实现类

本类就是自制Mybatis的重中之重！！！用来和 *Mapper.xml做绑定的方法，解析sql，并且进行赋值，最终执行

```java
public interface SqlSession {

    /**
     * @param statementId 我们在 com.wills.mybatis.builder.XMLMapperBuilder 下读取的statementid
     * @param args 传入的参数
     * @param <E> 返回的泛型
     * @return
     */
    public <E> List<E> selectList(String statementId,Object... args) throws Exception;

    /**
     * @param statementId 我们在 com.wills.mybatis.builder.XMLMapperBuilder 下读取的statementid
     * @param args 传入的参数
     * @param <T> 返回的泛型
     * @return
     */
    public <T> T selectOne(String statementId,Object... args) throws Exception;

    public <T> void insert(String statementId,T obj) throws Exception;

    public <T> void updateById(String statementId,T obj) throws Exception;

    public void deleteById(String statementId,Object id) throws Exception;

    public void close() throws Exception;
}
```

[DefaultSqlSession](./src/main/java/com/wills/mybatis/session/impl/DefaultSqlSession.java)

```java
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
        List<T> list = selectList(statementId, args);
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
    public void close() throws Exception {
        executor.close();
    }
}
```

9. 相关测试类

[Example](./src/main/java/com/wills/mybatis/example/Example.java)

```java
public class Example {

    /**
     * 自己指定 配置文件
     * @throws Exception
     */
    @Test
    public void test1() throws Exception {
        InputStream resourceAsSteam = Resources.getResourceAsStream("WillsMyBatisConfig.xml");
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(resourceAsSteam);
        SqlSession sqlSession = sqlSessionFactory.openSession();

        //调用
        User user = new User();
        user.setId(1);
        user.setName("wills");
        System.out.println("======================条件查询=======================");
        User user2 = sqlSession.selectOne("com.wills.mybatis.example.mapper.UserMapper.selectOne", user);
        System.out.println(user2);
        System.out.println("======================查询全部=======================");
        List<User> users = sqlSession.selectList("com.wills.mybatis.example.mapper.UserMapper.selectList");
        for (User user1 : users) {
            System.out.println(user1);
        }
    }

    /**
     * 简略模板
     * @throws Exception
     */
    @Test
    public void test2() throws Exception {
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build();
        SqlSession sqlSession = sqlSessionFactory.openSession();

        //调用
        User user = new User();
        user.setId(1);
        user.setName("wills");
        System.out.println("======================条件查询=======================");
        User user2 = sqlSession.selectOne("com.wills.mybatis.example.mapper.UserMapper.selectOne", user);
        System.out.println(user2);
        System.out.println("======================查询全部=======================");
        List<User> users = sqlSession.selectList("com.wills.mybatis.example.mapper.UserMapper.selectList");
        for (User user1 : users) {
            System.out.println(user1);
        }
    }

    /**
     * 测试删除
     * @throws Exception
     */
    @Test
    public void test3() throws Exception {
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build();
        SqlSession sqlSession = sqlSessionFactory.openSession();

        //调用
        sqlSession.deleteById("com.wills.mybatis.example.mapper.UserMapper.deleteById", 1);
        System.out.println("======================查询全部=======================");
        List<User> users = sqlSession.selectList("com.wills.mybatis.example.mapper.UserMapper.selectList");
        for (User user1 : users) {
            System.out.println(user1);
        }
    }

    /**
     * 测试添加
     * @throws Exception
     */
    @Test
    public void test4() throws Exception {
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build();
        SqlSession sqlSession = sqlSessionFactory.openSession();

        //调用
        User user = new User();
        user.setName("wills");
        user.setAge(24);
        user.setRemark(new String("一个酷酷的boy".getBytes(StandardCharsets.UTF_8)));
        sqlSession.insert("com.wills.mybatis.example.mapper.UserMapper.insert", user);
        System.out.println("======================查询全部=======================");
        List<User> users = sqlSession.selectList("com.wills.mybatis.example.mapper.UserMapper.selectList");
        for (User user1 : users) {
            System.out.println(user1);
        }
    }

    /**
     * 更新
     * @throws Exception
     */
    @Test
    public void test5() throws Exception {
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build();
        SqlSession sqlSession = sqlSessionFactory.openSession();

        //调用
        User user = new User();
        user.setId(5);
        user.setName("wills");
        user.setAge(24);
        user.setRemark(new String("一个酷酷的boy".getBytes(StandardCharsets.UTF_8)));
        sqlSession.updateById("com.wills.mybatis.example.mapper.UserMapper.updateById", user);
        System.out.println("======================查询全部=======================");
        List<User> users = sqlSession.selectList("com.wills.mybatis.example.mapper.UserMapper.selectList");
        for (User user1 : users) {
            System.out.println(user1);
        }
    }
}
```

10. 实现效果

![](http://image.tinx.top/20210810180738.png)





至此，本简易MyBatis大部分结束了，后面可能会追加相关注解支持以及sql语句的判断支持~~~

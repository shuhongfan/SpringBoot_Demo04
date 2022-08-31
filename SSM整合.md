

# SSM整合

## 1.SSM整合

### 1.0 步骤分析

​	我们先来分析下如何把Spring,SpringMVC,Mybatis整合到一起。



#### 1.0.1 步骤

①Spring整合上Mybatis

​	通过Service层Dao层都注入Spring容器中

②引入配置SpringMVC

​	把Controller层注入SpringMVC容器中

③让web项目启动时自动读取Spring配置文件来创建Spring容器

​	可以使用ContextLoaderListener来实现Spring容器的创建。





#### 1.0.2 常见疑惑

- 为什么要用两个容器？

  因为Controller如果不放在MVC容器中会没有效果，无法处理请求。而Service如果不放在Spring容器中，声明式事务也无法使用。

  

- SpringMVC容器中的Controller需要依赖Service，能从Spring容器中获取到所依赖的Service对象嘛？

  Spring容器相当于是父容器，MVC容器相当于是子容器。子容器除了可以使用自己容器中的对象外还可以使用父容器中的对象。

  

- 是如何实现这样父子容器的关系的？

  具体可以看源码解析阶段的视频。但是我们目前可以用代码模拟下。

  

- 是什么时候让两个容器产生这种父子容器的关系的？

  在ContextLoaderListener中，会在创建好容器后把容器存入servletContext域。这样在DispatcherServlet启动时，创建完SpringMVC容器后会从servletContext域中获取到Spring容器对象，设置为其父容器,这样子容器就能获取到父容器中的bean了。详情请见源码解析视频。

  

SpringMVC容器中的Controller需要依赖Service，能从Spring容器中获取到所依赖的Service对象嘛？

是如何实现这样父子容器的关系的？

### 1.1 准备工作

引入所有依赖

~~~~xml
 <!--Spring-context-->
        <dependency>
            <groupId>org.springframework</groupId>
            <artifactId>spring-context</artifactId>
            <version>5.1.9.RELEASE</version>
        </dependency>
        <!--AOP相关依赖-->
        <dependency>
            <groupId>org.aspectj</groupId>
            <artifactId>aspectjweaver</artifactId>
            <version>1.8.13</version>
        </dependency>
        <!-- spring-jdbc -->
        <dependency>
            <groupId>org.springframework</groupId>
            <artifactId>spring-jdbc</artifactId>
            <version>5.1.9.RELEASE</version>
        </dependency>
        <!-- mybatis整合到Spring的整合包 -->
        <dependency>
            <groupId>org.mybatis</groupId>
            <artifactId>mybatis-spring</artifactId>
            <version>2.0.4</version>
        </dependency>
        <!--mybatis依赖-->
        <dependency>
            <groupId>org.mybatis</groupId>
            <artifactId>mybatis</artifactId>
            <version>3.5.4</version>
        </dependency>
        <!--log4j依赖，打印mybatis日志-->
        <dependency>
            <groupId>log4j</groupId>
            <artifactId>log4j</artifactId>
            <version>1.2.17</version>
        </dependency>
        <!--分页查询，pagehelper-->
        <dependency>
            <groupId>com.github.pagehelper</groupId>
            <artifactId>pagehelper</artifactId>
            <version>4.0.0</version>
        </dependency>

        <!--mysql驱动-->
        <dependency>
            <groupId>mysql</groupId>
            <artifactId>mysql-connector-java</artifactId>
            <version>5.1.47</version>
        </dependency>
        <!-- druid数据源 -->
        <dependency>
            <groupId>com.alibaba</groupId>
            <artifactId>druid</artifactId>
            <version>1.1.16</version>
        </dependency>

        <!-- junit -->
        <dependency>
            <groupId>junit</groupId>
            <artifactId>junit</artifactId>
            <version>4.12</version>
        </dependency>
        <!-- spring整合junit的依赖 -->
        <dependency>
            <groupId>org.springframework</groupId>
            <artifactId>spring-test</artifactId>
            <version>5.1.9.RELEASE</version>
        </dependency>

        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
            <version>1.18.12</version>
            <scope>provided</scope>
        </dependency>


        <!-- servlet依赖 -->
        <dependency>
            <groupId>javax.servlet</groupId>
            <artifactId>javax.servlet-api</artifactId>
            <version>3.1.0</version>
            <scope>provided</scope>
        </dependency>
        <!--jsp依赖 -->
        <dependency>
            <groupId>javax.servlet.jsp</groupId>
            <artifactId>jsp-api</artifactId>
            <version>2.1</version>
            <scope>provided</scope>
        </dependency>
        <!--springmvc的依赖-->
        <dependency>
            <groupId>org.springframework</groupId>
            <artifactId>spring-webmvc</artifactId>
            <version>5.1.9.RELEASE</version>
        </dependency>

        <!-- jackson，帮助进行json转换-->
        <dependency>
            <groupId>com.fasterxml.jackson.core</groupId>
            <artifactId>jackson-databind</artifactId>
            <version>2.9.0</version>
        </dependency>

        <!--commons文件上传，如果需要文件上传功能，需要添加本依赖-->
        <dependency>
            <groupId>commons-fileupload</groupId>
            <artifactId>commons-fileupload</artifactId>
            <version>1.4</version>
        </dependency>

~~~~

数据库初始化语句

~~~~mysql
CREATE DATABASE /*!32312 IF NOT EXISTS*/`mybatis_db` /*!40100 DEFAULT CHARACTER SET utf8 */;
USE `mybatis_db`;
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(50) DEFAULT NULL,
  `age` int(11) DEFAULT NULL,
  `address` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;
insert  into `user`(`id`,`username`,`age`,`address`) values (1,'UZI',19,'上海'),(2,'PDD',25,'上海');
~~~~





### 1.2 相关配置

#### ①整合Spring和Mybatis

在resources目录下创建Spring核心配置文件： **applicationContext.xml** 内容如下

~~~~xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:context="http://www.springframework.org/schema/context" xmlns:tx="http://www.springframework.org/schema/tx"
       xmlns:aop="http://www.springframework.org/schema/aop"
       xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd http://www.springframework.org/schema/context https://www.springframework.org/schema/context/spring-context.xsd http://www.springframework.org/schema/tx http://www.springframework.org/schema/tx/spring-tx.xsd http://www.springframework.org/schema/aop https://www.springframework.org/schema/aop/spring-aop.xsd">

    <!--组件扫描，排除controller-->
    <context:component-scan base-package="com.sangeng">
        <context:exclude-filter type="annotation" expression="org.springframework.stereotype.Controller"></context:exclude-filter>
    </context:component-scan>

    <!--读取properties文件-->
    <context:property-placeholder location="classpath:jdbc.properties"></context:property-placeholder>
    <!--创建连接池注入容器-->
    <bean class="com.alibaba.druid.pool.DruidDataSource" id="dataSource">
        <property name="url" value="${jdbc.url}"></property>
        <property name="username" value="${jdbc.username}"></property>
        <property name="password" value="${jdbc.password}"></property>
        <property name="driverClassName" value="${jdbc.driver}"></property>
    </bean>
    <!--spring整合mybatis后控制的创建获取SqlSessionFactory的对象-->
    <bean class="org.mybatis.spring.SqlSessionFactoryBean" id="sessionFactory">
        <!--配置连接池-->
        <property name="dataSource" ref="dataSource"></property>
        <!--配置mybatis配置文件的路径-->
        <property name="configLocation" value="classpath:mybatis-config.xml"></property>
    </bean>

    <!--mapper扫描配置，扫描到的mapper对象会被注入Spring容器中-->
    <bean class="org.mybatis.spring.mapper.MapperScannerConfigurer" id="mapperScannerConfigurer">
        <property name="basePackage" value="com.sangeng.dao"></property>
    </bean>

    <!--开启aop注解支持-->
    <aop:aspectj-autoproxy></aop:aspectj-autoproxy>

    <!--声明式事务相关配置-->
    <bean class="org.springframework.jdbc.datasource.DataSourceTransactionManager" id="transactionManager">
        <property name="dataSource" ref="dataSource"></property>
    </bean>
    <tx:annotation-driven transaction-manager="transactionManager"></tx:annotation-driven>

</beans>
~~~~



在resources目录下创建**jdbc.properties** 文件，内容如下：

~~~~properties
jdbc.url=jdbc:mysql://localhost:3306/mybatis_db?useUnicode=true&characterEncoding=UTF-8&serverTimezone=UTC
jdbc.driver=com.mysql.jdbc.Driver
jdbc.username=root
jdbc.password=root
~~~~



在resources目录下创建**mybatis-config.xml** ，内容如下：

~~~~xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE configuration
        PUBLIC "-//mybatis.org//DTD Config 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-config.dtd">
<configuration>
    <settings>
        <!--指定使用log4j打印Mybatis日志-->
        <setting name="logImpl" value="LOG4J"/>
    </settings>
    <!--配置别名包-->
    <typeAliases>
        <package name="com.sangeng.domain"></package>
    </typeAliases>
    <plugins>
        <!-- 注意：分页助手的插件，配置在通用mapper之前 -->
        <plugin interceptor="com.github.pagehelper.PageHelper">
            <!-- 指定方言 -->
            <property name="dialect" value="mysql"/>
        </plugin>
    </plugins>
</configuration>
~~~~



在resources目录下创建**log4j.properties** ，内容如下：

~~~~properties
### direct log messages to stdout ###
log4j.appender.stdout=org.apache.log4j.ConsoleAppender
log4j.appender.stdout.Target=System.out
log4j.appender.stdout.layout=org.apache.log4j.PatternLayout
log4j.appender.stdout.layout.ConversionPattern=%d{ABSOLUTE} %5p %c{1}:%L - %m%n

### direct messages to file mylog.log ###
log4j.appender.file=org.apache.log4j.FileAppender
log4j.appender.file.File=c:/mylog.log
log4j.appender.file.layout=org.apache.log4j.PatternLayout
log4j.appender.file.layout.ConversionPattern=%d{ABSOLUTE} %5p %c{1}:%L - %m%n

### set log levels - for more verbose logging change 'info' to 'debug' ###

log4j.rootLogger=debug, stdout

~~~~

#### ②SpringMVC引入

在resources目录下创建**spring-mvc.xml** ，内容如下：

~~~~xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:context="http://www.springframework.org/schema/context"
       xmlns:mvc="http://www.springframework.org/schema/mvc"
       xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd http://www.springframework.org/schema/context https://www.springframework.org/schema/context/spring-context.xsd http://www.springframework.org/schema/mvc https://www.springframework.org/schema/mvc/spring-mvc.xsd">
    <!--
         SpringMVC只扫描controller包即可
     -->
    <context:component-scan base-package="com.sangeng.controller"/>
    <!-- 解决静态资源访问问题，如果不加mvc:annotation-driven会导致无法访问handler-->
    <mvc:default-servlet-handler/>
    <!--解决响应乱码-->
    <mvc:annotation-driven>
        <mvc:message-converters>
            <bean class="org.springframework.http.converter.StringHttpMessageConverter">
                <constructor-arg value="utf-8"/>
            </bean>
        </mvc:message-converters>
    </mvc:annotation-driven>


    <!--配置视图解析器  前后端不分离项目使用-->
<!--    <bean class="org.springframework.web.servlet.view.InternalResourceViewResolver" id="viewResolver">
        &lt;!&ndash;要求拼接的前缀&ndash;&gt;
        <property name="prefix" value="/WEB-INF/page/"></property>
        &lt;!&ndash;要拼接的后缀&ndash;&gt;
        <property name="suffix" value=".jsp"></property>
    </bean>-->

    <!--配置拦截器-->
<!--    <mvc:interceptors>

        <mvc:interceptor>
            &lt;!&ndash;
            &ndash;&gt;
            <mvc:mapping path="/**"/>
            &lt;!&ndash;配置排除拦截的路径&ndash;&gt;
            <mvc:exclude-mapping path="/"/>
            &lt;!&ndash;配置拦截器对象注入容器&ndash;&gt;
            <bean class=""></bean>
        </mvc:interceptor>
    </mvc:interceptors>-->

    <!--
          文件上传解析器
          注意：id 必须为 multipartResolver
          如果需要上传文件时可以放开相应配置
      -->
    <!--<bean id="multipartResolver" class="org.springframework.web.multipart.commons.CommonsMultipartResolver">-->
        <!--&lt;!&ndash; 设置默认字符编码 &ndash;&gt;-->
        <!--<property name="defaultEncoding" value="utf-8"/>-->
        <!--&lt;!&ndash; 一次请求上传的文件的总大小的最大值，单位是字节&ndash;&gt;-->
        <!--<property name="maxUploadSize" value="#{1024*1024*100}"/>-->
        <!--&lt;!&ndash; 每个上传文件大小的最大值，单位是字节&ndash;&gt;-->
        <!--<property name="maxUploadSizePerFile" value="#{1024*1024*50}"/>-->
    <!--</bean>-->
</beans>
~~~~



修改web.xml文件

~~~~xml
 <servlet>
        <servlet-name>DispatcherServlet</servlet-name>
        <servlet-class>org.springframework.web.servlet.DispatcherServlet</servlet-class>
        <!--
            为DispatcherServlet提供初始化参数的
            设置springmvc配置文件的路径
                name是固定的，必须是contextConfigLocation
                value指的是SpringMVC配置文件的位置
         -->
        <init-param>
            <param-name>contextConfigLocation</param-name>
            <param-value>classpath:spring-mvc.xml</param-value>
        </init-param>
        <!--
            指定项目启动就初始化DispatcherServlet
         -->
        <load-on-startup>1</load-on-startup>
    </servlet>
    <servlet-mapping>
        <servlet-name>DispatcherServlet</servlet-name>
        <!--
             /           表示当前servlet映射除jsp之外的所有请求（包含静态资源）
             *.do        表示.do结尾的请求路径才能被SpringMVC处理(老项目会出现)
             /*          表示当前servlet映射所有请求（包含静态资源,jsp），不应该使用其配置DispatcherServlet
         -->
        <url-pattern>/</url-pattern>
    </servlet-mapping>


    <!--乱码处理过滤器，由SpringMVC提供-->
    <!-- 处理post请求乱码 -->
    <filter>
        <filter-name>CharacterEncodingFilter</filter-name>
        <filter-class>org.springframework.web.filter.CharacterEncodingFilter</filter-class>
        <init-param>
            <!-- name固定不变，value值根据需要设置 -->
            <param-name>encoding</param-name>
            <param-value>UTF-8</param-value>
        </init-param>
    </filter>
    <filter-mapping>
        <filter-name>CharacterEncodingFilter</filter-name>
        <!-- 所有请求都设置utf-8的编码 -->
        <url-pattern>/*</url-pattern>
    </filter-mapping>
~~~~



#### ③Spring整合入web项目

​	让web项目启动的时候就能够创建Spring容器。可以使用Spring提供的监听器ContextLoaderListener，所以我们需要再web.xml中配置这个监听器,并且配置上Spring配置文件的路径。

~~~~xml
    <!--配置spring的配置文件路径-->
    <context-param>
        <param-name>contextConfigLocation</param-name>
        <param-value>classpath:applicationContext.xml</param-value>
    </context-param>
    <!--配置监听器，可以再应用被部署的时候创建spring容器-->
    <listener>
        <listener-class>org.springframework.web.context.ContextLoaderListener</listener-class>
    </listener>
~~~~



### 1.3 编写Controller,Service，Dao

​	我们来编写根据id查询用户的接口来进行测试



~~~~java
@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/user/{id}")
    public User findById(@PathVariable("id") Integer id){
        User user = userService.findById(id);
        return user;
    }
}

~~~~

~~~~java
public interface UserService {
    User findById(Integer id);
}
~~~~

~~~~java
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserDao userDao;

    public User findById(Integer id) {
        return userDao.findById(id);
    }
}

~~~~

~~~~java
public interface UserDao {
    /**
     * 根据id查询用户
     * @param id
     * @return
     */
    User findById(Integer id);
}

~~~~



~~~~xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd" >
<mapper namespace="com.sangeng.dao.UserDao">


    <select id="findById" resultType="com.sangeng.domain.User">
        select * from user where id = #{id}
    </select>
</mapper>
~~~~





## 2.案例

### 2.0 响应格式统一

​	我们要保证一个项目中所有接口返回的数据格式的统一。这样无论是前端还是移动端开发获取到我们的数据后都能更方便的进行统一处理。

​	所以我们定义以下结果封装类



~~~~java
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseResult<T> {
    /**
     * 状态码
     */
    private Integer code;
    /**
     * 提示信息，如果有错误时，前端可以获取该字段进行提示
     */
    private String msg;
    /**
     * 查询到的结果数据，
     */
    private T data;

    public ResponseResult(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public ResponseResult(Integer code, T data) {
        this.code = code;
        this.data = data;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public ResponseResult(Integer code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }
}
~~~~



之前的接口修改为 ：

~~~~java
@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/user/{id}")
    public ResponseResult findById(@PathVariable("id") Integer id){
        User user = userService.findById(id);
        if(user==null){
            //说明没有对应的用户
            return new ResponseResult(500,"没有该用户");
        }
        return new ResponseResult(200,"操作成功",user);
    }
}

~~~~



### 2.1 查询所有用户

~~~~java
@RestController
public class UserController {

    @Autowired
    private UserService userService;

	//省略其他无关代码
    @GetMapping("/user")
    public ResponseResult findAll(){
        List<User> list = userService.findAll();
        return new ResponseResult(200,"操作成功",list);
    }

}

~~~~

~~~~java
public interface UserService {
    User findById(Integer id);

    List<User> findAll();
}

~~~~

~~~~java
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserDao userDao;
	//省略其他无关代码

    public List<User> findAll() {
        return userDao.findAll();
    }
}
~~~~

~~~~java

public interface UserDao {
	//省略其他无关代码
    List<User> findAll();
}
~~~~

~~~~xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd" >
<mapper namespace="com.sangeng.dao.UserDao">
    <select id="findAll" resultType="com.sangeng.domain.User">
        select * from user
    </select>
</mapper>
~~~~





### 2.2 分页查询用户

​	分页查询的结果除了要包含查到的用户数据外还要有当前页数，每页条数，总记录数这些分页数据。

分页数据封装类

~~~~java

public class PageResult<T> {

    private Integer currentPage;

    private Integer pageSize;

    private Integer total;
    
    private List<T> data;

    public PageResult(Integer currentPage, Integer pageSize, Integer total, List<T> data) {
        this.currentPage = currentPage;
        this.pageSize = pageSize;
        this.total = total;
        this.data = data;
    }

    public Integer getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(Integer currentPage) {
        this.currentPage = currentPage;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }
}

~~~~

~~~~java

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/user/{pageSize}/{pageNum}")
    public ResponseResult findByPage(@PathVariable("pageSize") Integer pageSize,@PathVariable("pageNum") Integer pageNum){
        PageResult pageResult =  userService.findByPage(pageSize,pageNum);
        return new ResponseResult(200,"操作成功",pageResult);
    }

}

~~~~

~~~~java
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserDao userDao;


    public PageResult findByPage(Integer pageSize, Integer pageNum) {
        PageHelper.startPage(pageNum,pageSize);
        List<User> list = userDao.findAll();
        PageInfo pageInfo = new PageInfo(list);
        PageResult pageResult = new PageResult(pageInfo.getPageNum(),pageInfo.getPageSize(), (int) pageInfo.getTotal(),list);
        return pageResult;
    }
}

~~~~



### 2.3 插入用户

Controller层

~~~~java
    @PostMapping("/user")
    public ResponseResult insertUser(@RequestBody User user){
        userService.insertUser(user);
        return new ResponseResult(200,"操作成功");
    }
~~~~

Service层

在Service接口中增加方法定义

~~~~java
    void insertUser(User user);
~~~~

实现类中实现该方法:

~~~~java
  public void insertUser(User user) {
        userDao.insertUser(user);
    }
~~~~

Dao层

在接口中定义方法

~~~~java
    void insertUser(User user);
~~~~

在mapper映射文件中

~~~~xml
    <insert id="insertUser">
        insert into user values(null,#{username},#{age},#{address})
    </insert>
~~~~

测试

~~~~json
{"username":"三更草堂","age":15,"address":"请问"}
~~~~





### 2.4 删除用户

Controller层

~~~~java
    @DeleteMapping("/user/{id}")
    public ResponseResult deleteUser(@PathVariable("id") Integer id){
        userService.deleteUser(id);
        return new ResponseResult(200,"操作成功");
    }
~~~~

Service层

在Service接口中增加方法定义

~~~~java
    void deleteUser(Integer id);
~~~~

实现类中实现该方法:

~~~~java
    public void deleteUser(Integer id) {
        userDao.deleteUser(id);
    }
~~~~

Dao层

在接口中定义方法

~~~~java
    void deleteUser(Integer id);
~~~~

在mapper映射文件中

~~~~xml
    <delete id="deleteUser">
        delete from user where id = #{id}
    </delete>
~~~~







### 2.5 更新用户

Controller层

~~~~java
    @PutMapping("/user")
    public ResponseResult updateUser(@RequestBody User user){
        userService.updateUser(user);
        return new ResponseResult(200,"操作成功");
    }
~~~~

Service层

在Service接口中增加方法定义

~~~~java
    void updateUser(User user);
~~~~

实现类中实现该方法:

~~~~java
    public void updateUser(User user) {
        userDao.updateUser(user);
    }
~~~~

Dao层

在接口中定义方法

~~~~java
    void updateUser(User user);
~~~~

在mapper映射文件中

~~~~xml
    <update id="updateUser">
        update user set username = #{username},age = #{age},address = #{address} where id = #{id}
    </update>
~~~~





## 3.异常统一处理

​	我们可以使用@ControllerAdvice实现对异常的统一处理。让异常出现时也能返回响应一个JSON。

​	代码如下：

~~~~java
@ControllerAdvice
public class SGControllerAdvice {

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public ResponseResult handleException(Exception e){
        return new ResponseResult(500,e.getMessage());
    }
}
~~~~



## 4.拦截器

~~~~java
public class SGHandlerInterceptor implements HandlerInterceptor {

    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        System.out.println("preHandle");
        return true;
    }

    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        System.out.println("postHandle");
    }

    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        System.out.println("afterCompletion");
    }
}

~~~~

~~~~xml
    <!--配置拦截器-->
        <mvc:interceptors>

            <mvc:interceptor>
                <!--
                -->
                <mvc:mapping path="/**"/>
                <!--配置排除拦截的路径-->
                <!--<mvc:exclude-mapping path="/"/>-->
                <!--配置拦截器对象注入容器-->
                <bean class="com.sangeng.interceptor.SGHandlerInterceptor"></bean>
            </mvc:interceptor>
        </mvc:interceptors>
~~~~

## 5.声明式事务

​	已经做好了相应的配置，只要在service方法上加上注解即可

~~~~java
    @Transactional
    public void test() {
        userDao.insertUser(new User(null,"test1",11,"cc"));
//        System.out.println(1/0);
        userDao.insertUser(new User(null,"test2",12,"cc2"));
    }
~~~~





## 6.AOP

​	注意，自己去使用AOP进行增强时，应该是对Service进行增强。不能对Controller进行增强，因为切面类会被放入父容器，而我们的Controller是在子容器中的。父容器不能访问子容器。

​	并且我们如果需要对Controller进行增强，使用拦截器也会更加的好用。

~~~~java
@Aspect
@Component
public class SGAspect {

    //定义切点
    @Pointcut("execution(* com.sangeng.service..*.*(..))")
    public void pt(){

    }

    //进行增强
    @Before("pt()")
    public void before(){
        System.out.println("before");
    }
}

~~~~








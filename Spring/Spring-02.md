# Spring-02

## 1.注解开发

​	为了简化配置，Spring支持使用注解代替xml配置。

​	

## 2.Spring常用注解

### 2.0 注解开发准备工作

​	如果要使用注解开发必须要开启组件扫描，这样加了注解的类才会被识别出来。Spring才能去解析其中的注解。

~~~~xml
<!--启动组件扫描，指定对应扫描的包路径，该包及其子包下所有的类都会被扫描，加载包含指定注解的类-->
<context:component-scan base-package="com.sangeng"/>
~~~~



### 2.1 IOC相关注解

#### 2.1.1 @Component,@Controller,@Service ,@Repository	

​	上述4个注解都是加到类上的。

​	他们都可以起到类似bean标签的作用。可以把加了该注解类的对象放入Spring容器中。

​	实际再使用时选择任意一个都可以。但是后3个注解是语义化注解。

​	如果是Service类要求使用@Service。

​	如果是Dao类要求使用@Repository

​	如果是Controllerl类(SpringMVC中会学习到)要求使用@Controller

​	如果是其他类可以使用@Component



例如：

配置文件如下：

~~~~xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:context="http://www.springframework.org/schema/context"
       xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd http://www.springframework.org/schema/context https://www.springframework.org/schema/context/spring-context.xsd">
<!--启动组件扫描，指定对应扫描的包路径，该包及其子包下所有的类都会被扫描，加载包含指定注解的类-->
    <context:component-scan base-package="com.sangeng"></context:component-scan>

</beans>
~~~~

类如下：

~~~~java
@Repository("userDao")
public class UserDaoImpl implements UserDao {

    public void show() {
        System.out.println("查询数据库，展示查询到的数据");
    }
}

~~~~

~~~~java
@Data
@NoArgsConstructor
@AllArgsConstructor
@Component("phone")
public class Phone {
    private double price;
    private String name;
    private String password;
    private String path;

}

~~~~

~~~~java
@Service("userService")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserServiceImpl implements UserService {


    private UserDao userDao;

    private int num;

    private String str;


    public void show() {
        userDao.show();
    }
}

~~~~



测试类如下：

~~~~java
public class Demo {
    public static void main(String[] args) {
        //创建容器
        ClassPathXmlApplicationContext app = new ClassPathXmlApplicationContext("applicationContext.xml");
        //获取对象
        UserDao userDao = (UserDao) app.getBean("userDao");
        Phone phone = (Phone) app.getBean("phone");
        UserService userService = (UserService) app.getBean("userService");
        System.out.println(phone);
        System.out.println(userService);
        System.out.println(userDao);
    }
}
~~~~



### 2.2 DI相关注解

​	如果一个bean已经放入Spring容器中了。那么我们可以使用下列注解实现属性注入，让Spring容器帮我们完成属性的赋值。



#### 2.2.1 @Value

​	主要用于String,Integer等可以直接赋值的属性注入。不依赖setter方法，支持SpEL表达式。

例如：

~~~~java
@Service("userService")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserServiceImpl implements UserService {
    private UserDao userDao;
    @Value("199")
    private int num;
    @Value("三更草堂")
    private String str;
    @Value("#{19+3}")
    private Integer age;


    public void show() {
        userDao.show();
    }
}
~~~~



#### 2.2.2 @AutoWired

​	Spring会给加了该注解的属性自动注入数据类型相同的对象。

例如：

~~~~java
@Service("userService")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;

    @Value("199")
    private int num;
    @Value("三更草堂")
    private String str;

    @Value("#{19+3}")
    private Integer age;


    public void show() {
        userDao.show();
    }
}

~~~~



​	**required属性代表这个属性是否是必须的，默认值为true。如果是true的话Spring容器中如果找不到相同类型的对象完成属性注入就会出现异常。**





#### 2.2.3 @Qualifier

​	如果相同类型的bean在容器中有多个时，单独使用@AutoWired就不能满足要求，这时候可以再加上@Qualifier来指定bean的名字从容器中获取bean注入。

例如：

~~~~java
    @Autowired
    @Qualifier("userDao2")
    private UserDao userDao;
~~~~



**注意：该直接不能单独使用。单独使用没有作用**



### 2.3 xml配置文件相关注解

#### @Configuration

​	标注在类上，表示当前类是一个配置类。我们可以用注解类来完全替换掉xml配置文件。

​	注意：如果使用配置类替换了xml配置，spring容器要使用：AnnotationConfigApplicationContext

例如：

~~~~java
@Configuration
public class ApplicationConfig {
}

~~~~



#### @ComponentScan

​	可以用来代替context:component-scan标签来配置组件扫描。

​	basePackages属性来指定要扫描的包。

​	注意要加在配置类上。

例如：

~~~~java
@Configuration
@ComponentScan(basePackages = "com.sangeng")//指定要扫描的包
public class ApplicationConfig {
}

~~~~





#### @Bean

​	可以用来代替bean标签，主要用于第三方类的注入。

​	使用：定义一个方法，在方法中创建对应的对象并且作为返回值返回。然后在方法上加上@Bean注解，注解的value属性来设置bean的名称。

例如：

~~~~java
@Configuration
@ComponentScan(basePackages = "com.sangeng")
public class ApplicationConfig {

    @Bean("dataSource")
    public DruidDataSource getDataSource(){
        DruidDataSource druidDataSource = new DruidDataSource();
        druidDataSource.setDriverClassName("com.mysql.jdbc.Driver");
        druidDataSource.setUsername("root");
        druidDataSource.setUrl("jdbc:mysql://localhost:3306/mybatis_db");
        druidDataSource.setPassword("root");
        return druidDataSource;
    }

}
~~~~



**注意事项：如果同一种类型的对象在容器中只有一个，我们可以不设置bean的名称。**

具体写法如下：

~~~~java
@Configuration
@ComponentScan(basePackages = "com.sangeng")
public class ApplicationConfig {

    @Bean
    public DruidDataSource getDataSource(){
        DruidDataSource druidDataSource = new DruidDataSource();
        druidDataSource.setDriverClassName("com.mysql.jdbc.Driver");
        druidDataSource.setUsername("root");
        druidDataSource.setUrl("jdbc:mysql://localhost:3306/mybatis_db");
        druidDataSource.setPassword("root");
        return druidDataSource;
    }

}
~~~~

获取方式如下：

~~~~java
    public static void main(String[] args) {
        //创建注解容器
        AnnotationConfigApplicationContext app = new AnnotationConfigApplicationContext(ApplicationConfig.class);
		//根据对应类的字节码对象获取
        DataSource bean = app.getBean(DataSource.class);
        System.out.println(userService);
    }
~~~~





#### @PropertySource

​	可以用来代替context:property-placeholder，让Spring读取指定的properties文件。然后可以使用@Value来获取读取到的值。



​	**使用：在配置类上加@PropertySource注解，注解的value属性来设置properties文件的路径。**

​	**然后在配置类中定义成员变量。在成员变量上使用@Value注解来获取读到的值并给对应的成员变量赋值。**



例如：

~~~~properties
jdbc.driver=com.mysql.jdbc.Driver
jdbc.url=jdbc:mysql://localhost:3306/mybatis_db
jdbc.username=root
jdbc.password=root
~~~~

读取文件并且获取值

~~~~java
@Configuration
@ComponentScan(basePackages = "com.sangeng")
@PropertySource("jdbc.properties")
public class ApplicationConfig {

    @Value("${jdbc.driver}")
    private String driverClassName;
    @Value("${jdbc.url}")
    private String url;
    @Value("${jdbc.username}")
    private String username;
    @Value("${jdbc.password}")
    private String password;


    @Bean
    public DruidDataSource getDataSource(){
        DruidDataSource druidDataSource = new DruidDataSource();
        druidDataSource.setDriverClassName(driverClassName);
        druidDataSource.setUsername(username);
        druidDataSource.setUrl(url);
        druidDataSource.setPassword(password);
        return druidDataSource;
    }

}

~~~~



**注意事项：使用@Value获取读到的properties文件中的值时使用的是${key},而不是#{key}。**



## 3.如何选择

①SSM  

​		自己项目中的类的IOC和DI都使用注解，对第三方jar包中的类，配置组件扫描时使用xml进行配置。

②SpringBoot

​		纯注解开发
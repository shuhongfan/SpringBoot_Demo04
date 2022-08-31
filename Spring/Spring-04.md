# Spring-04

## 1.Spring整合Junit

### ①导入依赖

~~~~xml
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
~~~~



### ② 编写测试类

在测试类上加上

**@RunWith(SpringJUnit4ClassRunner.class)**注解，指定让测试运行于Spring环境

**@ContextConfiguration注解**，指定Spring容器创建需要的配置文件或者配置类

~~~~java
@RunWith(SpringJUnit4ClassRunner.class)//让测试运行与Spring测试环境
@ContextConfiguration(locations = "classpath:配置文件1.xml")//设置Spring配置文件或者配置类
//@ContextConfiguration(classes = SpringConfig.class)
public class SpringTest {}
~~~~



### ③注入对象进行测试

在测试类中注入要测试的对象，定义测试方法，在其中使用要测试的对象。

~~~~java
@RunWith(SpringJUnit4ClassRunner.class)//让测试运行与Spring测试环境
@ContextConfiguration(locations = "classpath:配置文件1.xml")//设置Spring配置文件或者配置类
//@ContextConfiguration(classes = SpringConfig.class)
public class SpringTest {
    
    // 想测哪个对象，就注入哪个对象
    @Autowired
    private UserService userService;
    
    //定义测试方法
    @Test
    public void testUserService() {
        userService.findById(10);
    }
    
}
~~~~





## 2.Spring整合Mybatis

​	我们如果想把Mybatis整合到Spring中需要使用一个整合包**mybatis-spring**

​	官方文档：http://mybatis.org/spring/zh/index.html



### ①导入依赖

~~~~xml
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

~~~~



### ②往容器中注入整合相关对象

~~~~xml
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
~~~~

mybatis配置文件**mybatis-config.xml**如下:

~~~~xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE configuration
        PUBLIC "-//mybatis.org//DTD Config 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-config.dtd">
<configuration>
    <typeAliases>
        <package name="com.sangeng.domain"></package>
    </typeAliases>
</configuration>
~~~~



### ③从容器中获取Mapper对象进行使用

~~~~java
    @Autowired
    private UserDao userDao;
~~~~



## 3.Spring声明式事务

### 3.1 事务回顾

### 	

#### **3.1.1 事务的概念**

​		保证一组数据库的操作，要么同时成功，要么同时失败



#### 3.1.2 四大特性

- 隔离性

  多个事务之间要相互隔离，不能互相干扰

- 原子性

  指事务是一个不可分割的整体，类似一个不可分割的原子

- 一致性

  保障事务前后这组数据的状态是一致的。要么都是成功的，要么都是失败的。

- 持久性

  指事务一旦被提交，这组操作修改的数据就真的的发生变化了。即使接下来数据库故障也不应该对其有影响。

  

### 3.2 实现声明式事务

​	如果我们自己去对事务进行控制的话我们就需要值原来核心代码的基础上加上事务控制相关的代码。而在我们的实际开发中这种事务控制的操作也是非常常见的。所以Spring提供了声明式事务的方式让我们去控制事务。

​	只要简单的加个注解(或者是xml配置)就可以实现事务控制，不需要事务控制的时候只需要去掉相应的注解即可。



#### 3.2.0 案例环境准备

①数据初始化

~~~~mysql
CREATE DATABASE /*!32312 IF NOT EXISTS*/`spring_db` /*!40100 DEFAULT CHARACTER SET utf8 */;
USE `spring_db`;
DROP TABLE IF EXISTS `account`;
CREATE TABLE `account` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(50) DEFAULT NULL,
  `money` DOUBLE DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=INNODB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;
INSERT  INTO `account`(`id`,`name`,`money`) VALUES (1,'三更',100),(2,'草堂',100);
~~~~

②Spring整合Mybatis

③创建Service和Dao

~~~~java
public interface AccountService {
    /**
     * 转账
     * @param outId 转出账户的id
     * @param inId 转出账户的id
     * @param money 转账金额
     */
    public void transfer(Integer outId,Integer inId,Double money);
}
~~~~

~~~~java
@Service
public class AccountServiceImpl implements AccountService {

    @Autowired
    private AccoutDao accoutDao;

    public void transfer(Integer outId, Integer inId, Double money) {
        //增加
        accoutDao.updateMoney(inId,money);
        //减少
        accoutDao.updateMoney(outId,-money);
    }
}
~~~~

~~~~java
public interface AccoutDao {

    void updateMoney(@Param("id") Integer id,@Param("updateMoney") Double updateMoney);
}
~~~~

AccoutDao.xml如下：
~~~~xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd" >
<mapper namespace="com.sangeng.dao.AccoutDao">


    <update id="updateMoney">
        update  account set money = money + #{updateMoney} where id = #{id}
    </update>
</mapper>
~~~~



#### 3.2.1 注解实现

##### ①配置事务管理器和事务注解驱动

在spring的配置文件中添加如下配置：

~~~~xml
    <!--把事务管理器注入Spring容器，需要配置一个连接池-->
    <bean id="txManager" class="org.springframework.jdbc.datasource.DataSourceTransactionManager">
        <property name="dataSource" ref="dataSource"/>
    </bean>
    <!--开启事务注解驱动，配置使用的事务管理器-->
    <tx:annotation-driven transaction-manager="txManager"/>
~~~~

##### ②添加注解

在需要进行事务控制的方法或者类上添加@Transactional注解就可以实现事务控制。

~~~~java
    @Transactional
    public void transfer(Integer outId, Integer inId, Double money) {
        //增加
        accoutDao.updateMoney(inId,money);
//        System.out.println(1/0);
        //减少
        accoutDao.updateMoney(outId,-money);
    }
~~~~

**注意：如果加在类上，这个类的所有方法都会受事务控制，如果加在方法上，就是那一个方法受事务控制。**

注意，因为声明式事务底层是通过AOP实现的，所以最好把AOP相关依赖都加上。

~~~~xml
       <dependency>
            <groupId>org.aspectj</groupId>
            <artifactId>aspectjweaver</artifactId>
            <version>1.9.6</version>
        </dependency>
~~~~



#### 3.2.2 xml方式实现



##### ①配置事务管理器

~~~~xml
    <bean id="txManager" class="org.springframework.jdbc.datasource.DataSourceTransactionManager">
        <property name="dataSource" ref="dataSource"/>
    </bean>
~~~~

##### ②配置事务切面

~~~~xml
 	<!--定义事务管理的通知类-->
    <tx:advice transaction-manager="txManager" id="txAdvice">
        <tx:attributes>
            <tx:method name="trans*"/>
        </tx:attributes>
    </tx:advice>

    <aop:config>
        <aop:pointcut id="pt" expression="execution(* com.sangeng.service..*.*(..))"></aop:pointcut>
        <aop:advisor advice-ref="txAdvice" pointcut-ref="pt"></aop:advisor>
    </aop:config>
~~~~

注意，因为声明式事务底层是通过AOP实现的，所以最好把AOP相关依赖都加上。

~~~~xml
       <dependency>
            <groupId>org.aspectj</groupId>
            <artifactId>aspectjweaver</artifactId>
            <version>1.9.6</version>
        </dependency>
~~~~





### 3.3 属性配置

#### 3.3.1 事务传播行为propagation

​	当事务方法嵌套调用时，需要控制是否开启新事务，可以使用事务传播行为来控制。



测试案例:

~~~~java
@Service
public class TestServiceImpl {
    @Autowired
    AccountService accountService;

    @Transactional
    public void test(){
        accountService.transfer(1,2,10D);
        accountService.log();
    }
}
~~~~

~~~~java
public class AccountServiceImpl implements AccountService {
	//...省略其他不相关代码
    @Transactional
    public void log() {
        System.out.println("打印日志");
        int i = 1/0;
    }

}
~~~~



| 属性值                         | 行为                                                   |
| ------------------------------ | ------------------------------------------------------ |
| REQUIRED（必须要有）           | 外层方法有事务，内层方法就加入。外层没有，内层就新建   |
| REQUIRES_NEW（必须要有新事务） | 外层方法有事务，内层方法新建。外层没有，内层也新建     |
| SUPPORTS（支持有）             | 外层方法有事务，内层方法就加入。外层没有，内层就也没有 |
| NOT_SUPPORTED（支持没有）      | 外层方法有事务，内层方法没有。外层没有，内层也没有     |
| MANDATORY（强制要求外层有）    | 外层方法有事务，内层方法加入。外层没有。内层就报错     |
| NEVER(绝不允许有)              | 外层方法有事务，内层方法就报错。外层没有。内层就也没有 |



例如：

~~~~java
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void transfer(Integer outId, Integer inId, Double money) {
        //增加
        accoutDao.updateMoney(inId,money);
        //减少
        accoutDao.updateMoney(outId,-money);
    }
~~~~





#### 3.3.2 隔离级别isolation

Isolation.DEFAULT 使用数据库默认隔离级别

Isolation.READ_UNCOMMITTED 

Isolation.READ_COMMITTED

Isolation.REPEATABLE_READ

Isolation.SERIALIZABLE

~~~~java
   @Transactional(propagation = Propagation.REQUIRES_NEW,isolation = Isolation.READ_COMMITTED)
    public void transfer(Integer outId, Integer inId, Double money) {
        //增加
        accoutDao.updateMoney(inId,money);
        //减少
        accoutDao.updateMoney(outId,-money);
    }
~~~~



#### 3.3.3 只读readOnly

​	如果事务中的操作都是读操作，没涉及到对数据的写操作可以设置readOnly为true。这样可以提高效率。

~~~~java
    @Transactional(readOnly = true)
    public void log() {
        System.out.println("打印日志");
        int i = 1/0;
    }
~~~~


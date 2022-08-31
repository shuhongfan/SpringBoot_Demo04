# Spring-01

## 1.Spring简介

​	 Spring是一个开源框架，它由[Rod Johnson](https://baike.baidu.com/item/Rod Johnson)创建。它是为了解决企业应用开发的复杂性而创建的。 

​	 目前是JavaEE开发的灵魂框架。他可以简化JavaEE开发，可以非常方便整合其他框架，无侵入的进行功能增强。

​	 Spring的核心就是 控制反转(IoC)和面向切面(AOP) 。

## 2.IOC控制反转

### 2.1 概念

​	控制反转，之前对象的控制权在类手上，现在反转后到了Spring手上。

​	

### 2.2 入门案例

#### ①导入依赖

导入SpringIOC相关依赖

~~~~xml
        <dependency>
            <groupId>org.springframework</groupId>
            <artifactId>spring-context</artifactId>
            <version>5.1.9.RELEASE</version>
        </dependency>
~~~~

#### ②编写配置文件

在resources目录下创建applicationContext.xml文件，文件名可以任意取。但是建议叫applicationContext。

内容如下：

~~~~xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd">

    <!--
        classs:配置类的全类名
        id:配置一个唯一标识
    -->
    <bean class="com.sangeng.dao.impl.StudentDaoImpl" id="studentDao"  >
    </bean>


</beans>
~~~~



#### ③创建容器从容器中获取对象并测试

~~~~java
    public static void main(String[] args) {

//        1.获取StudentDaoImpl对象
        //创建Spring容器，指定要读取的配置文件路径
        ApplicationContext app = new ClassPathXmlApplicationContext("applicationContext.xml");
        //从容器中获取对象
        StudentDao studentDao = (StudentDao) app.getBean("studentDao");
        //调用对象的方法进行测试
        System.out.println(studentDao.getStudentById(1));
    }
~~~~



### 2.3 Bean的常用属性配置

#### 2.3.1 id 

​	bean的唯一标识，同一个Spring容器中不允许重复

#### 2.3.2 class

​	全类名，用于反射创建对象

#### 2.3.3 scope 

​	scope主要有两个值：singleton和prototype

​	如果设置为singleton则一个容器中只会有这个一个bean对象。默认容器创建的时候就会创建该对象。

​	如果设置为prototype则一个容器中会有多个该bean对象。每次调用getBean方法获取时都会创建一个新对象。



## 3.DI依赖注入

​	依赖注入可以理解成IoC的一种应用场景，反转的是对象间依赖关系维护权。

​	

### 3.1 set方法注入

在要注入属性的bean标签中进行配置。前提是该类有提供属性对应的set方法。

~~~~java
package com.sangeng.domain;

public class Student {

    private String name;
    private int id;
    private int age;

    private Dog dog;

    public Dog getDog() {
        return dog;
    }

    public void setDog(Dog dog) {
        this.dog = dog;
    }

    @Override
    public String toString() {
        return "Student{" +
                "name='" + name + '\'' +
                ", id=" + id +
                ", age=" + age +
                '}';
    }

    public Student() {

    }

    public Student(String name, int id, int age) {
        this.name = name;
        this.id = id;
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}

~~~~

~~~~xml
    <bean class="com.sangeng.domain.Dog" id="dog">
        <property name="name" value="小白"></property>
        <property name="age" value="6"></property>
    </bean>

    <bean class="com.sangeng.domain.Student" id="student" >
        <!--
            name属性用来指定要设置哪个属性
            value属性用来设置要设置的值
            ref属性用来给引用类型的属性设置值，可以写上Spring容器中bean的id
        -->
        <property name="name" value="东南枝"></property>
        <property name="age" value="20"></property>
        <property name="id" value="1"></property>
        <property name="dog" ref="dog"></property>
    </bean>
~~~~



### 3.2 有参构造注入

在要注入属性的bean标签中进行配置。前提是该类有提供对应的有参构造。

~~~~java
public class Student {

    private String name;
    private int id;
    private int age;

    private Dog dog;

    public Student(String name, int id, int age, Dog dog) {
        this.name = name;
        this.id = id;
        this.age = age;
        this.dog = dog;
    }
    //.....省略其他
}
~~~~

~~~~xml
    <!--使用有参构造进行注入-->
    <bean class="com.sangeng.domain.Student" id="student2" >
        <constructor-arg name="name" value="自挂东南枝"></constructor-arg>
        <constructor-arg name="age" value="20"></constructor-arg>
        <constructor-arg name="id" value="30"></constructor-arg>
        <constructor-arg name="dog" ref="dog"></constructor-arg>
    </bean>
~~~~



### 3.3 复杂类型属性注入

实体类如下：

~~~~java
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    private int age;
    private String name;
    private Phone phone;
    private List<String> list;
    private List<Phone> phones;
    private Set<String> set;
    private Map<String, Phone> map;
    private int[] arr;
    private Properties properties;
}
~~~~

~~~~java
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Phone {
    private double price;
    private String name;
    private String password;
    private String path;

}
~~~~



配置如下：

~~~~xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd">

    <bean class="com.sangeng.domain.Phone" id="phone">
        <property name="price" value="3999"></property>
        <property name="name" value="黑米"></property>
        <property name="password" value="123"></property>
        <property name="path" value="qqqq"></property>
    </bean>
    
    <bean class="com.sangeng.domain.User" id="user">
        <property name="age" value="10"></property>
        <property name="name" value="大队长"></property>
        <property name="phone" ref="phone"></property>
        <property name="list">
            <list>
                <value>三更</value>
                <value>西施</value>
            </list>
        </property>

        <property name="phones">
            <list>
                <ref bean="phone"></ref>
            </list>
        </property>

        <property name="set">
            <set>
                <value>setEle1</value>
                <value>setEle2</value>
            </set>
        </property>

        <property name="map">
            <map>
                <entry key="k1" value-ref="phone"></entry>
                <entry key="k2" value-ref="phone"></entry>
            </map>
        </property>

        <property name="arr">
            <array>
                <value>10</value>
                <value>11</value>
            </array>
        </property>

        <property name="properties">
            <props>
                <prop key="k1">v1</prop>
                <prop key="k2">v2</prop>
            </props>
        </property>
    </bean>
</beans>
~~~~



## 4.Lombok

### ①导入依赖

~~~~xml
        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
            <version>1.18.16</version>
        </dependency>
~~~~

### ②增加注解

~~~~java
@Data //根据属性生成set，get方法
@NoArgsConstructor //生成空参构造
@AllArgsConstructor //生成全参构造
public class Phone {
    private double price;
    private String name;
    private String password;
    private String path;

}
~~~~



## 5.SPEL

​	我们可以再配置文件中使用SPEL表达式。写法如下:

~~~~xml
        <property name="age" value="#{20}"/>
        <property name="car" value="#{car}"/>
~~~~

​	注意：SPEL需要写到value属性中，不能写到ref属性。



## 6.配置文件

### 6.1 读取properties文件

​	我们可以让Spring读取properties文件中的key/value，然后使用其中的值。

#### ①设置读取properties

在Spring配置文件中加入如下标签：指定要读取的文件的路径。

~~~~xml
<context:property-placeholder location="classpath:filename.properties">
~~~~

其中的classpath表示类加载路径下。

我们也会用到如下写法：classpath:**.properties  其中的*  * 表示文件名任意。

**注意：context命名空间的引入是否正确**

#### ②使用配置文件中的值

在我们需要使用的时候可以使用${key}来表示具体的值。注意要再value属性中使用才可以。例如：

~~~~xml
<property name="propertyName" value="${key}"/>
~~~~



### 6.2 引入Spring配置文件

​	我们可以在主的配置文件中通过import标签的resource属性，引入其他的xml配置文件

~~~~xml
<import resource="classpath:applicationContext-book.xml"/>
~~~~



## 7. 低频知识点

### 7.1 bean的配置

#### 7.1.1 name属性

​	我们可以用name属性来给bean取名。例如：

~~~~xml
    <bean class="com.alibaba.druid.pool.DruidDataSource" id="dataSource" name="dataSource2,dataSource3">
        <property name="driverClassName" value="${jdbc.driver}"></property>
        <property name="url" value="${jdbc.url}"></property>
        <property name="username" value="${jdbc.username}"></property>
        <property name="password" value="${jdbc.password}"></property>
    </bean>
~~~~

​	获取的时候就可以使用这个名字来获取了

~~~~java
    public static void main(String[] args) {

        ApplicationContext app = new ClassPathXmlApplicationContext("applicationContext.xml");
        DruidDataSource dataSource = (DruidDataSource) app.getBean("dataSource3");
        System.out.println(dataSource);

    }
~~~~



#### 7.1.2 lazy-init

​	可以控制bean的创建时间，如果设置为true就是在第一次获取该对象的时候才去创建。

~~~~xml
    <bean class="com.alibaba.druid.pool.DruidDataSource" lazy-init="true"  id="dataSource" name="dataSource2,dataSource3">
        <property name="driverClassName" value="${jdbc.driver}"></property>
        <property name="url" value="${jdbc.url}"></property>
        <property name="username" value="${jdbc.username}"></property>
        <property name="password" value="${jdbc.password}"></property>
    </bean>
~~~~



#### 7.1.3 init-method

​	可以用来设置初始化方法，设置完后容器创建完对象就会自动帮我们调用对应的方法。

~~~~java
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Student {

    private String name;
    private int id;
    private int age;
	//初始化方法
    public void init(){
        System.out.println("对学生对象进行初始化操作");
    }

}

~~~~

~~~~xml
<bean class="com.sangeng.domain.Student" id="student" init-method="init"></bean>
~~~~

**注意：配置的初始化方法只能是空参的。**



#### 7.1.4 destroy-method

​	可以用来设置销毁之前调用的方法，设置完后容器销毁对象前就会自动帮我们调用对应的方法。

~~~~xml
    <bean class="com.sangeng.domain.Student" id="student"  destroy-method="close"></bean>
~~~~

~~~~java
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Student {

    private String name;
    private int id;
    private int age;

    public void init(){
        System.out.println("对学生对象进行初始化操作");
    }

    public void close(){
        System.out.println("对象销毁之前调用，用于释放资源");
    }
}

~~~~

**注意：配置的方法只能是空参的。**



#### 7.1.5 factory-bean&factory-method

​	当我们需要让Spring容器使用工厂类来创建对象放入Spring容器的时候可以使用factory-bean和factory-method属性。



##### 7.1.5.1 配置实例工厂创建对象

配置文件中进行配置

~~~~xml
    <!--创建实例工厂-->
    <bean class="com.sangeng.factory.CarFactory" id="carFactory"></bean>
    <!--使用实例工厂创建Car放入容器-->
    <!--factory-bean 用来指定使用哪个工厂对象-->
    <!--factory-method 用来指定使用哪个工厂方法-->
    <bean factory-bean="carFactory" factory-method="getCar" id="car"></bean>
~~~~

创建容器获取对象测试

~~~~java
        ClassPathXmlApplicationContext app = new ClassPathXmlApplicationContext("applicationContext.xml");
        //获取car对象
        Car c = (Car) app.getBean("car");
        System.out.println(c);
~~~~



##### 7.1.5.2 配置静态工厂创建对象

配置文件中进行配置

~~~~xml
    <!--使用静态工厂创建Car放入容器-->
    <bean class="com.sangeng.factory.CarStaticFactory" factory-method="getCar" id="car2"></bean>
~~~~

创建容器获取对象测试

~~~~java
        ClassPathXmlApplicationContext app = new ClassPathXmlApplicationContext("applicationContext.xml");
        //获取car对象
        Car c = (Car) app.getBean("car2");
        System.out.println(c);
~~~~


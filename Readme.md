# SpringBoot-基础入门-三更草堂

## 1. SpringBoot简介

### 1.1 为什么要学习SpringBoot

 我们之前的SSM还是使用起来不够爽。

- 还需要写很多的配置才能进行正常的使用。
- 实现一个功能需要引入很多的依赖，尤其是要自己去维护依赖的版本，特别容易出现依赖冲突等问题。

 SpringBoot就能很好的解决上述问题。

### 1.2 SpringBoot是什么

 Spring Boot是基于Spring开发的全新框架，相当于对Spring做了又一层封装。

 其设计目的是用来简化Spring应用的初始搭建以及开发过程。该框架使用了特定的方式来进行配置，从而使开发人员不再需要定义样板化的配置。（自动配置）

 并且对第三方依赖的添加也进行了封装简化。（起步依赖）

 所以Spring能做的他都能做，并且简化了配置。

 并且还提供了一些Spring所没有的比如：

- 内嵌web容器，不再需要部署到web容器中

  提供准备好的特性，如指标、健康检查和外部化配置

 最大特点：**自动配置**、**起步依赖**

 官网：https://spring.io/projects/spring-boot

## 2. 快速入门

### 2.1 基本环境要求

 JDK : 8

 Maven ：3.5.x

#### Maven配置

```xml
  <mirrors>
    <mirror>
      <id>aliyunmaven</id>
      <mirrorOf>central</mirrorOf>
      <name>aliyun maven</name>
      <url>https://maven.aliyun.com/repository/public </url>
    </mirror>
  </mirrors>
  <profiles>
    <profile>
      <id>jdk-1.8</id>
      <activation>
        <activeByDefault>true</activeByDefault>
        <jdk>1.8</jdk>
      </activation>
      <properties>
        <maven.compiler.source>1.8</maven.compiler.source>
        <maven.compiler.target>1.8</maven.compiler.target>
        <maven.compiler.compilerVersion>1.8</maven.compiler.compilerVersion>
      </properties>
    </profile>
  </profiles>
```

#### 清理Maven仓库脚本

```
@echo off
rem create by NettQun
  
rem 这里写你的仓库路径
set REPOSITORY_PATH=E:\Develop\maven_rep
rem 正在搜索...
for /f "delims=" %%i in ('dir /b /s "%REPOSITORY_PATH%\*lastUpdated*"') do (
    echo %%i
    del /s /q "%%i"
)
rem 搜索完毕
pause
```

创建一个bat文件，然后复制上述脚本进去，修改其中maven本地仓库的地址，保存后双击执行即可。

### 2.2 HelloWorld

①继承父工程

在pom.xml中添加一下配置，继承spring-boot-starter-parent这个父工程

```xmL
    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>2.5.0</version>
    </parent>
```

②添加依赖

```xml
   <dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
    </dependencies>
```

③创建启动类

创建一个类在其实加上@SpringBootApplication注解标识为启动类。

```java
@SpringBootApplication
public class HelloApplication {

    public static void main(String[] args) {
        SpringApplication.run(HelloApplication.class, args);
    }
}
```

④定义Controller

创建Controller,主要Controller要放在启动类所在包或者其子包下。

```java
@RestController
public class HelloController {
    @RequestMapping("/hello")
    public String hello(){
        return "hello";
    }
}
```

⑤运行测试

直接**运行启动类的main方法**即可。

### 2.3 常见问题及解决方案

①访问时404

检查Controller是不是在启动类所在的包或者其子包下，如果不是需要进行修改。

②依赖爆红

配置阿里云镜像后刷新maven项目让其下载。

### 2.4 打包运行

 我们可以把springboot的项目打成jar包直接去运行。

①添加maven插件

```xml
    <build>
        <plugins>
            <!--springboot打包插件-->
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
            </plugin>
        </plugins>
    </build>
```

②maven打包

[外链图片转存失败,源站可能有防盗链机制,建议将图片保存下来直接上传(img-5EyOUQp5-1658560436318)(img\SpringBoot项目打包.png)]

③运行jar包

在jar包所在目录执行命令

```
java -jar jar包名称
```

即可运行。

### 2.5 快速构建

 https://start.spring.io/

## 3.起步依赖

 SpringBoot依靠父项目中的版本锁定和starter机制让我们能更轻松的实现对依赖的管理。

### 3.0 依赖冲突及其解决方案

#### 3.0.1 依赖冲突

 一般程序在运行时发生类似于 java.lang.ClassNotFoundException，Method not found: ‘……’，或者莫名其妙的异常信息，这种情况一般很大可能就是 jar包依赖冲突的问题引起的了。

 一般在是A依赖C(低版本)，B也依赖C(高版本)。 都是他们依赖的又是不同版本的C的时候会出现。



#### 3.0.2 解决方案

 如果出现了类似于 java.lang.ClassNotFoundException，Method not found: 这些异常检查相关的依赖冲突问题，排除掉低版本的依赖，留下高版本的依赖。





### 3.1 版本锁定

 我们的SpringBoot模块都需要继承一个父工程：**spring-boot-starter-parent**。在spring-boot-starter-parent的父工程**spring-boot-dependencies**中对常用的依赖进行了版本锁定。这样我们在添加依赖时，很多时候都不需要添加依赖的版本号了。



 我们也可以采用覆盖properties配置或者直接指定版本号的方式修改依赖的版本。

例如：

直接指定版本号

```xml
        <dependency>
            <groupId>org.aspectj</groupId>
            <artifactId>aspectjweaver</artifactId>
            <version>1.7.2</version>
        </dependency>
```

覆盖properties配置

```xml
    <properties>
        <aspectj.version>1.7.2</aspectj.version>
    </properties>
```

### 3.2 starter机制

 当我们需要使用某种功能时只需要引入对应的starter即可。一个starter针对一种特定的场景，其内部引入了该场景所需的依赖。这样我们就不需要单独引入多个依赖了。

 命名规律

- 官方starter都是以 `spring-boot-starter`开头后面跟上场景名称。例如：spring-boot-starter-data-jpa
- 非官方starter则是以 `场景名-spring-boot-starter`的格式，例如：mybatis-spring-boot-starter

## 4.自动配置

 SpringBoot中最重要的特性就是自动配置。

 Springboot遵循“**约定优于配置**”的原则，自动进行了默认配置。这样我们就不需要做大量的配置。

 当我们需要使用什么场景时，就会自动配置这个场景相关的配置。

 如果他的默认配置不符合我们的需求时修改这部分配置即可。





## 5.YML配置

### 5.1.简介

#### 5.1.1YML是什么

 YAML (YAML Ain’t a Markup Language)YAML不是一种标记语言，通常以.yml为后缀的文件，是一种直观的能够被电脑识别的数据序列化格式，并且容易被人类阅读，容易和脚本语言交互的，可以被支持YAML库的不同的编程语言程序导入，一种专门用来写配置文件的语言。

 YAML试图用一种比XML更敏捷的方式，来完成XML所完成的任务。

 例如：

```yml
student:
    name: sangeng
    age: 15
    
<student>
    <name>sangeng</name>
    <age>15</age>
</student>
```

#### 5.1.2YML优点

1. YAML易于人们阅读。
2. 更加简洁明了

### 5.2.语法

#### 5.2.1约定

- k: v 表示键值对关系，**冒号后面必须有一个空格**
- 使用空格的缩进表示层级关系，空格数目不重要，**只要是左对齐的一列数据，都是同一个层级的**
- 大小写敏感
- 缩进时**不允许使用Tab键，只允许使用空格**。
- java中对于驼峰命名法，可用原名或使用-代替驼峰，如java中的lastName属性,在yml中使用lastName或 last-name都可正确映射。
- yml中注释前面要加#

#### 5.2.2键值关系

##### 普通值(字面量)

k: v：字面量直接写；

字符串默认不用加上单引号或者双绰号；

“”: 双引号；转意字符能够起作用

 name: “sangeng \n caotang”：输出；sangeng 换行 caotang

‘’：单引号；会转义特殊字符，特殊字符最终只是一个普通的字符串数据

```yml
name1: sangeng 
name2: 'sangeng  \n caotang'
name3: "sangeng  \n caotang"
age: 15
flag: true
```

##### 日期

```yml
date: 2019/01/01
```

##### 对象(属性和值)、Map(键值对)

多行写法：

在下一行来写对象的属性和值的关系，注意缩进

```yml
student:
  name: zhangsan
  age: 20
```

行内写法：

```yml
student: {name: zhangsan,age: 20}
```

##### 数组、list、set

用- 值表示数组中的一个元素

多行写法：

```yml
pets:
  - dog
  - pig
  - cat
```

行内写法：

```yml
pets: [dog,pig,cat]
```

##### 对象数组、对象list、对象set

```yml
students:
 - name: zhangsan
   age: 22
 - name: lisi
   age: 20
 - {name: wangwu,age: 18}
```

#### 5.2.3 占位符赋值

可以使用 **${key:defaultValue}** 的方式来赋值，若key不存在，则会使用defaultValue来赋值。

例如

```yml
server:
  port: ${myPort:88}

myPort: 80   
```

### 5.3.SpringBoot读取YML

#### 5.3.1 @Value注解

 注意使用此注解只能获取简单类型的值（8种基本数据类型及其包装类，String,Date）

```yml
student:
  lastName: sangeng
12
@RestController
public class TestController {
    @Value("${student.lastName}")
    private String lastName;
    @RequestMapping("/test")
    public String test(){
        System.out.println(lastName);
        return "hi";
    }
    
}
```

**注意：加了@Value的类必须是交由Spring容器管理的**

#### 5.3.2 @ConfigurationProperties

 yml配置

```yml
student:
  lastName: sangeng
  age: 17
student2:
  lastName: sangeng2
  age: 15
```

 在类上添加注解@Component 和@ConfigurationProperties(prefix = “配置前缀”)

```java
@Data
@AllArgsConstructor
@NoArgsConstructor
@Component
@ConfigurationProperties(prefix = "student")
public class Student {
    private String lastName;
    private Integer age;
}
```

 从spring容器中获取Student对象

```java
@RestController
public class TestController {

    @Autowired
    private Student student;
    @RequestMapping("/test")
    public String test(){
        System.out.println(student);
        return "hi";
    }
}

```

 **注意事项：要求对应的属性要有set/get方法，并且key要和成员变量名一致才可以对应的上。**

**lombok讲解视频：**

https://www.bilibili.com/video/BV1G54y1V7VG?p=12

https://www.bilibili.com/video/BV1G54y1V7VG?p=13

### 5.4.练习

要求把下面实体类中的各个属性在yml文件中进行赋值。然后想办法读取yml配置的属性值，进行输出测试。

```java
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Student {
    private String lastName;
    private Integer age;
    private Boolean boss;

    private Date birthday;
    private Map<String,String> maps;
    private Map<String,String> maps2;
    private List<Dog> list;

    private Dog dog;
    private String[] arr;
    private String[] arr2;

    private Map<String,Dog> dogMap;
}
@Data
@AllArgsConstructor
@NoArgsConstructor
class Dog {
    private String name;
    private Integer age;
}
```

#### 答案

```yml
# 练习
student:
  lastName: sangeng
  age: 15
  boss: true
  birthday: 2006/2/3
  maps:
    name: sangeng
    age: 11
  maps2: {name: caotang,age: 199}
  list:
    - name: 小白
      age: 3
    - name: 小黄
      age: 4
    - {name: 小黑,age: 1}
  dog:
    name: 小红
    age: 5
  arr:
    - sangeng
    - caotang

  arr2: [sangeng,caotang]
  dogMap:
    xb: {name: 小白,age: 9}
    xh:
      name: 小红
      age: 6

123456789101112131415161718192021222324252627282930
@Data
@AllArgsConstructor
@NoArgsConstructor
@Component
@ConfigurationProperties(prefix = "student")
public class Student {
    private String lastName;
    private Integer age;
    private Boolean boss;

    private Date birthday;
    private Map<String,String> maps;
    private Map<String,String> maps2;
    private List<Dog> list;

    private Dog dog;
    private String[] arr;
    private String[] arr2;

    private Map<String,Dog> dogMap;
}
@Data
@AllArgsConstructor
@NoArgsConstructor
class Dog {
    private String name;
    private Integer age;
}
```

### 5.5 YML和properties配置的相互转换

 我们可以使用一些网站非常方便的实现YML和properties格式配置的相互转换。

转换网站：https://www.toyaml.com/index.html

### 5.6 配置提示

 如果使用了@ConfigurationProperties注解，可以增加以下依赖，让我们在书写配置时有相应的提示。

```xml
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-configuration-processor</artifactId>
            <optional>true</optional>
        </dependency>
```

 **注意：添加完依赖加完注解后要运行一次程序才会有相应的提示。**
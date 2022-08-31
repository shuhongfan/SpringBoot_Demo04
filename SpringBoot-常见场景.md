# SpringBoot-常见场景

## 1.热部署

 SpringBoot为我们提供了一个方便我们开发测试的工具dev-tools。使用后可以实现热部署的效果。当我们运行了程序后对程序进行了修改，程序会自动重启。

 原理是使用了两个ClassLoder,一个ClassLoader加载哪些不会改变的类(第三方jar包),另一个ClassLoader加载会更改的类.称之为Restart ClassLoader,这样在有代码更改的时候,原来的Restart Classloader被丢弃,重新创建一个Restart ClassLoader,由于需要加载的类相比较少,所以实现了较快的重启。



### 1.1 准备工作

①设置IDEA自动编译

 在idea中的setting做下面配置

[外链图片转存失败,源站可能有防盗链机制,建议将图片保存下来直接上传(img-Z21WqerX-1658560763035)(img/自动编译配置.png)]

②设置允许程序运行时自动启动

 ctrl + shift + alt + / 这组快捷键后会有一个小弹窗，点击Registry 就会进入下面的界面，找到下面的配置项并勾选，勾选后直接点close

[外链图片转存失败,源站可能有防盗链机制,建议将图片保存下来直接上传(img-oBa5HnLi-1658560763037)(img/允许运行时自动启动.png)]

### 1.2使用

①添加依赖

```xml
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-devtools</artifactId>
            <optional>true</optional>
        </dependency>
12345
```

②触发热部署

 当我们在修改完代码或者静态资源后可以切换到其它软件，让IDEA自动进行编译，自动编译后就会触发热部署。

 或者使用Ctrl+F9手动触发重新编译。

## 2.单元测试

 我们可以使用SpringBoot整合Junit进行单元测试。

 **Spring Boot 2.2.0 版本开始引入 JUnit 5 作为单元测试默认库**。

 Junit5功能相比于Junit4也会更强大。但是本课程是SpringBoot的课程，所以主要针对SpringBoot如何整合Junit进行单元测试做讲解。暂不针对Junit5的新功能做介绍。如有需要会针对Junit5录制专门的课程进行讲解。



### 2.1 使用

#### ①添加依赖

```xml
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
        </dependency>
1234
```

#### ②编写测试类

```java
import com.sangeng.controller.HelloController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class ApplicationTest {

    @Autowired
    private HelloController helloController;

    @Test
    public void testJunit(){
        System.out.println(1);
        System.out.println(helloController);
    }
}
1234567891011121314151617
```

**注意：测试类所在的包需要和启动类是在同一个包下。否则就要使用如下写法指定启动类。**

```java
import com.sangeng.controller.HelloController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

//classes属性来指定启动类
@SpringBootTest(classes = HelloApplication.class)
public class ApplicationTest {

    @Autowired
    private HelloController helloController;

    @Test
    public void testJunit(){
        System.out.println(1);
        System.out.println(helloController);
    }
}

12345678910111213141516171819
```

### 2.2 兼容老版本

 如果是对老项目中的SpringBoot进行了版本升级会发现之前的单元测试代码出现了一些问题。

 因为Junit5和之前的Junit4有比较大的不同。

 先看一张图：

![img](http://shfs.cf/img/202208302053134.jpeg)

 从上图可以看出 **JUnit 5 = JUnit Platform + JUnit Jupiter + JUnit Vintage**

- **JUnit Platform**： 这是Junit提供的平台功能模块，通过它，其它的测试引擎也可以接入
- **JUnit JUpiter**：这是JUnit5的核心，是一个基于JUnit Platform的引擎实现，它包含许多丰富的新特性来使得自动化测试更加方便和强大。
- **JUnit Vintage**：这个模块是兼容JUnit3、JUnit4版本的测试引擎，使得旧版本的自动化测试也可以在JUnit5下正常运行。

 虽然Junit5包含了**JUnit Vintage**来兼容JUnit3和Junit4，但是**SpringBoot 2.4 以上版本对应的spring-boot-starter-test移除了默认对** **Vintage 的依赖。**所以当我们仅仅依赖spring-boot-starter-test时会发现之前我们使用的@Test注解和@RunWith注解都不能使用了。

 我们可以单独在依赖vintage来进行兼容。

```xml
        <dependency>
            <groupId>org.junit.vintage</groupId>
            <artifactId>junit-vintage-engine</artifactId>
            <scope>test</scope>
        </dependency>
12345
```

**注意：**

 **org.junit.Test对应的是Junit4的版本，就搭配@RunWith注解来使用。**

SpringBoot2.2.0之前版本的写法

```java
import com.sangeng.controller.HelloController;
//import org.junit.jupiter.api.Test;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

//classes属性来指定启动类
@SpringBootTest
@RunWith(SpringRunner.class)
public class ApplicationTest {

    @Autowired
    private HelloController helloController;

    @Test
    public void testJunit(){
        System.out.println(1);
        System.out.println(helloController);
    }
}
12345678910111213141516171819202122
```

## 3.整合mybatis

### 3.1 准备工作

①数据准备

```mysql
/*Table structure for table `user` */
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(50) DEFAULT NULL,
  `age` int(11) DEFAULT NULL,
  `address` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8;

/*Data for the table `user` */

insert  into `user`(`id`,`username`,`age`,`address`) values (2,'pdd',25,'上海'),(3,'UZI',19,'上海11'),(4,'RF',19,NULL),(6,'三更',14,'请问2'),(8,'test1',11,'cc'),(9,'test2',12,'cc2');

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

12345678910111213141516171819
```

②实体类

```java
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    private Integer id;
    private String username;
    private Integer age;
    private String address;
}

1234567891011121314
```

### 3.2 整合步骤

 github: https://github.com/mybatis/spring-boot-starter/

#### ①依赖

```xml
        <!--mybatis启动器-->
        <dependency>
            <groupId>org.mybatis.spring.boot</groupId>
            <artifactId>mybatis-spring-boot-starter</artifactId>
            <version>2.2.0</version>
        </dependency>
        <!--mysql驱动-->
        <dependency>
            <groupId>mysql</groupId>
            <artifactId>mysql-connector-java</artifactId>
            <scope>runtime</scope>
        </dependency>
123456789101112
```

#### ②配置数据库信息

```yml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/test?characterEncoding=utf-8&serverTimezone=UTC
    username: root
    password: root
    driver-class-name: com.mysql.cj.jdbc.Driver
123456
```

#### ③配置mybatis相关配置

```yml
mybatis:
  mapper-locations: classpath:mapper/*Mapper.xml # mapper映射文件路径
  type-aliases-package: com.sangeng.domain   # 配置哪个包下的类有默认的别名

1234
```

#### ④编写Mapper接口

注意在接口上加上@Mapper 和@Repository 注解

```java
@Repository
@Mapper
public interface UserMapper {
    public List<User> findAll();
}

123456
```

#### ⑤编写mapper接口对应的[xml](https://so.csdn.net/so/search?q=xml&spm=1001.2101.3001.7020)文件

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd" >
<mapper namespace="com.sangeng.mapper.UserMapper">
    <select id="findAll" resultType="com.sangeng.domain.User">
        select * from user
    </select>
</mapper>
1234567
```

#### ⑥测试

```java
@SpringBootTest(classes = HelloApplication.class)
public class SpringMyTest {

    @Autowired
    UserMapper userMapper;


    @Test
    public void tesMapper(){
        System.out.println(userMapper.findAll());
    }
}
123456789101112
```

## 4.Web开发

### 4.1 静态资源访问

 由于SpringBoot的项目是打成jar包的所以没有之前web项目的那些web资源目录(webapps)。

 那么我们的静态资源要放到哪里呢？

 从SpringBoot官方文档中我们可以知道，我们可以把静态资源放到 `resources/static` (或者 `resources/public` 或者`resources/resources` 或者 `resources/META-INF/resources`) 中即可。

 静态资源放完后，

 例如我们想访问文件：resources/static/index.html 只需要在访问时资源路径写成/index.html即可。

 例如我们想访问文件：resources/static/pages/login.html 访问的资源路径写成： /pages/login.html

#### 4.1.1 修改静态资源访问路径

 SpringBoot默认的静态资源路径匹配为/** 。如果想要修改可以通过 `spring.mvc.static-path-pattern` 这个配置进行修改。

 例如想让访问静态资源的url必须前缀有/res。例如/res/index.html 才能访问到static目录中的。我们可以修改如下：

在application.yml中

```yml
spring:
  mvc:
    static-path-pattern: /res/** #修改静态资源访问路径
123
```

#### 4.1.2 修改静态资源存放目录

 我们可以修改 spring.web.resources.static-locations 这个配置来修改静态资源的存放目录。

 例如:

```yml
spring:
  web:
    resources:
      static-locations:
        - classpath:/sgstatic/ 
        - classpath:/static/
123456
```

### 4.2 设置请求映射规则@RequestMapping

 详细讲解：https://www.bilibili.com/video/BV1AK4y1o74Y P5-P12

 该注解可以加到方法上或者是类上。（查看其源码可知）

 我们可以用其来设定所能匹配请求的要求。只有符合了设置的要求，请求才能被加了该注解的方法或类处理。

#### 4.2.1 指定请求路径

 path或者value属性都可以用来指定请求路径。

例如：

 我们期望让请求的资源路径为**/test/testPath**的请求能够被**testPath**方法处理则可以写如下代码

```java
@RestController
@RequestMapping("/test")
public class HelloController {
    @RequestMapping("/testPath")
    public String testPath(){
        return "testPath";
    }
}
12345678
@RestController
public class HelloController {

    @RequestMapping("/test/testPath")
    public String testPath(){
        return "testPath";
    }
}
123456789
```

#### 4.2.2 指定请求方式

 method属性可以用来指定可处理的请求方式。

例如：

 我们期望让请求的资源路径为**/test/testMethod**的**POST**请求能够被**testMethod**方法处理。则可以写如下代码

```java
@RestController
@RequestMapping("/test")
public class TestController {

    @RequestMapping(value = "/testMethod",method = RequestMethod.POST)
    public String testMethod(){
        System.out.println("testMethod处理了请求");
        return "testMethod";
    }
}

1234567891011
```

注意：我们可以也可以运用如下注解来进行替换

-  @PostMapping 等价于 @RequestMapping(method = RequestMethod.POST)
-  @GetMapping 等价于 @RequestMapping(method = RequestMethod.GET)
-  @PutMapping 等价于 @RequestMapping(method = RequestMethod.PUT)
-  @DeleteMapping 等价于 @RequestMapping(method = RequestMethod.DELETE)

例如：

 上面的需求我们可以使用下面的写法实现

```java
@RestController
@RequestMapping("/test")
public class TestController {

    @PostMapping(value = "/testMethod")
    public String testMethod(){
        System.out.println("testMethod处理了请求");
        return "testMethod";
    }
}
12345678910
```

#### 4.2.3 指定请求参数

 我们可以使用**params**属性来对请求参数进行一些限制。可以要求必须具有某些参数，或者是某些参数必须是某个值，或者是某些参数必须不是某个值。

例如：

 我们期望让请求的资源路径为**/test/testParams**的**GET**请求,并且请求参数中**具有code参数**的请求能够被testParams方法处理。则可以写如下代码

```java
@RestController
@RequestMapping("/test")
public class TestController {
    @RequestMapping(value = "/testParams",method = RequestMethod.GET,params = "code")
    public String testParams(){
        System.out.println("testParams处理了请求");
        return "testParams";
    }
}
123456789
```



 如果是要求**不能有code**这个参数可以把改成如下形式

```java
@RestController
@RequestMapping("/test")
public class TestController {
    @RequestMapping(value = "/testParams",method = RequestMethod.GET,params = "!code")
    public String testParams(){
        System.out.println("testParams处理了请求");
        return "testParams";
    }
}
123456789
```



 如果要求有code这参数，并且这参数值必须**是某个值**可以改成如下形式

```java
@RestController
@RequestMapping("/test")
public class TestController {
    @RequestMapping(value = "/testParams",method = RequestMethod.GET,params = "code=sgct")
    public String testParams(){
        System.out.println("testParams处理了请求");
        return "testParams";
    }
}
123456789
```

 如果要求有code这参数，并且这参数值必须**不是某个值**可以改成如下形式

```java
@RestController
@RequestMapping("/test")
public class TestController {
    @RequestMapping(value = "/testParams",method = RequestMethod.GET,params = "code!=sgct")
    public String testParams(){
        System.out.println("testParams处理了请求");
        return "testParams";
    }
}
123456789
```

#### 4.2.4 指定请求头

 我们可以使用**headers**属性来对请求头进行一些限制。

例如：

 我们期望让请求的资源路径为**/test/testHeaders的**GET**请求,并且请求头中**具有**deviceType**的请求能够被testHeaders方法处理。则可以写如下代码

```java
@RestController
@RequestMapping("/test")
public class TestController {
    
    @RequestMapping(value = "/testHeaders",method = RequestMethod.GET,headers = "deviceType")
    public String testHeaders(){
        System.out.println("testHeaders处理了请求");
        return "testHeaders";
    }
}
12345678910
```

 如果是要求不能有**deviceType**这个请求头可以把改成如下形式

```java
@RestController
@RequestMapping("/test")
public class TestController {
    
    @RequestMapping(value = "/testHeaders",method = RequestMethod.GET,headers = "!deviceType")
    public String testHeaders(){
        System.out.println("testHeaders处理了请求");
        return "testHeaders";
    }
}
12345678910
```

 如果要求有deviceType这个请求头，并且其值必须**是某个值**可以改成如下形式

```java
@RestController
@RequestMapping("/test")
public class TestController {
    
    @RequestMapping(value = "/testHeaders",method = RequestMethod.GET,headers = "deviceType=ios")
    public String testHeaders(){
        System.out.println("testHeaders处理了请求");
        return "testHeaders";
    }
}
12345678910
```

 如果要求有deviceType这个请求头，并且其值必须**不是某个值**可以改成如下形式

```java
@RestController
@RequestMapping("/test")
public class TestController {
    
    @RequestMapping(value = "/testHeaders",method = RequestMethod.GET,headers = "deviceType!=ios")
    public String testHeaders(){
        System.out.println("testHeaders处理了请求");
        return "testHeaders";
    }
}
12345678910
```

#### 4.2.5 指定请求头Content-Type

 我们可以使用**consumes**属性来对**Content-Type**这个请求头进行一些限制。

##### 范例一

 我们期望让请求的资源路径为**/test/testConsumes**的POST请求,并且请求头中的Content-Type头必须为 **multipart/from-data** 的请求能够被testConsumes方法处理。则可以写如下代码

```java
    @RequestMapping(value = "/testConsumes",method = RequestMethod.POST,consumes = "multipart/from-data")
    public String testConsumes(){
        System.out.println("testConsumes处理了请求");
        return "testConsumes";
    }
12345
```

##### 范例二

 如果我们要求请求头Content-Type的值必须**不能为某个multipart/from-data**则可以改成如下形式：

```java
    @RequestMapping(value = "/testConsumes",method = RequestMethod.POST,consumes = "!multipart/from-data")
    public String testConsumes(){
        System.out.println("testConsumes处理了请求");
        return "testConsumes";
    }
12345
```

### 4.3 获取请求参数

#### 4.3.1 获取路径参数

 RestFul风格的接口一些参数是在请求路径上的。类似： /user/1 这里的1就是id。

 如果我们想获取这种格式的数据可以使用**@PathVariable**来实现。

##### 范例一

 要求定义个RestFul风格的接口，该接口可以用来根据id查询用户。请求路径要求为 /user ，请求方式要求为GET。

 而请求参数id要写在请求路径上，例如 /user/1 这里的1就是id。

 我们可以定义如下方法，通过如下方式来获取路径参数：

```java
@RestController
public class UserController {

    @RequestMapping(value = "/user/{id}",method = RequestMethod.GET)
    public String findUserById( @PathVariable("id")Integer id){
        System.out.println("findUserById");
        System.out.println(id);
        return "findUserById";
    }
}
12345678910
```

##### 范例二

 如果这个接口，想根据id和username查询用户。请求路径要求为 /user ，请求方式要求为GET。

 而请求参数id和name要写在请求路径上，例如 /user/1/zs 这里的1就是id，zs是name

 我们可以定义如下方法，通过如下方式来获取路径参数：

```java
@RestController
public class UserController {
    @RequestMapping(value = "/user/{id}/{name}",method = RequestMethod.GET)
    public String findUser(@PathVariable("id") Integer id,@PathVariable("name") String name){
        System.out.println("findUser");
        System.out.println(id);
        System.out.println(name);
        return "findUser";
    }
}

1234567891011
```

#### 4.3.2 获取请求体中的Json格式参数

 RestFul风格的接口一些比较复杂的参数会转换成Json通过请求体传递过来。这种时候我们可以使用**@RequestBody**注解获取请求体中的数据。

##### 4.3.2.1 配置

 SpringBoot的web启动器已经默认导入了jackson的依赖，不需要再额外导入依赖了。

##### 4.3.2.2 使用

###### 范例一

 要求定义个RestFul风格的接口，该接口可以用来新建用户。请求路径要求为 /user ，请求方式要求为POST。

用户数据会转换成json通过请求体传递。
​ 请求体数据

```json
{"name":"三更","age":15}
1
```



1.获取参数封装成实体对象

 如果我们想把Json数据获取出来封装User对象,我们可以这样定义方法：

```java
@RestController
public class UserController {
    @RequestMapping(value = "/user",method = RequestMethod.POST)
    public String insertUser(@RequestBody User user){
        System.out.println("insertUser");
        System.out.println(user);
        return "insertUser";
    }
}
123456789
```

 User实体类如下：

```java
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    private Integer id;
    private String name;
    private Integer age;
}

123456789
```



2.获取参数封装成Map集合

 也可以把该数据获取出来封装成Map集合：

```java
    @RequestMapping(value = "/user",method = RequestMethod.POST)
    public String insertUser(@RequestBody Map map){
        System.out.println("insertUser");
        System.out.println(map);
        return "insertUser";
    }
123456
```

###### 范例二

 如果请求体传递过来的数据是一个User集合转换成的json，Json数据可以这样定义：

```java
[{"name":"三更1","age":14},{"name":"三更2","age":15},{"name":"三更3","age":16}]
1
```

 方法定义：

```java
    @RequestMapping(value = "/users",method = RequestMethod.POST)
    public String insertUsers(@RequestBody List<User> users){
        System.out.println("insertUsers");
        System.out.println(users);
        return "insertUser";
    }
123456
```

##### 4.3.2.3 注意事项

 如果需要使用**@RequestBody**来获取请求体中Json并且进行转换，要求请求头 Content-Type 的值要为： application/json 。

#### 4.3.3 获取QueryString格式参数

 如果接口的参数是使用QueryString的格式的话，我们也可以使用SpringMVC快速获取参数。

 我们可以使用**@RequestParam**来获取QueryString格式的参数。

##### 4.3.3.1 使用

###### 范例一

 要求定义个接口，该接口请求路径要求为 /testRequestParam，请求方式无要求。参数为id和name和likes。使用QueryString的格式传递。

1.参数单独的获取

 如果我们想把id，name，likes单独获取出来可以使用如下写法：

 在方法中定义方法参数，方法参数名要和请求参数名一致，这种情况下我们可以省略**@RequestParam**注解。

```java
    @RequestMapping("/testRquestParam")
    public String testRquestParam(Integer id, String name, String[] likes){
        System.out.println("testRquestParam");
        System.out.println(id);
        System.out.println(name);
        System.out.println(Arrays.toString(likes));
        return "testRquestParam";
    }

123456789
```

 如果方法参数名和请求参数名不一致，我们可以加上**@RequestParam**注解例如：

```java
    @RequestMapping("/testRquestParam")
    public String testRquestParam(@RequestParam("id") Integer uid,@RequestParam("name") String name, @RequestParam("likes")String[] likes){
        System.out.println("testRquestParam");
        System.out.println(uid);
        System.out.println(name);
        System.out.println(Arrays.toString(likes));
        return "testRquestParam";
    }
12345678
```

2.获取参数封装成实体对象

 如果我们想把这些参数封装到一个User对象中可以使用如下写法：

```java
    @RequestMapping("/testRquestParam")
    public String testRquestParam(User user){
        System.out.println("testRquestParam");
        System.out.println(user);
        return "testRquestParam";
    }
123456
```

 User类定义如下：

```java
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    private Integer id;
    private String name;
    private Integer age;
    private String[] likes;
}
123456789
```

 测试时请求url如下：

```java
http://localhost:8080/testRquestParam?id=1&name=三更草堂&likes=编程&likes=录课&likes=烫头
1
```

 **注意：实体类中的成员变量要和请求参数名对应上。并且要提供对应的set/get方法。**

#### 4.3.4 相关注解其他属性

##### 4.3.4.1 required

 代表是否必须，默认值为true也就是必须要有对应的参数。如果没有就会报错。

 如果对应的参数可传可不传则可以把其设置为fasle

例如：

```java
    @RequestMapping("/testRquestParam")
    public String testRquestParam(@RequestParam(value = "id",required = false) Integer uid,@RequestParam("name") String name, @RequestParam("likes")String[] likes){
        System.out.println("testRquestParam");
        System.out.println(uid);
        System.out.println(name);
        System.out.println(Arrays.toString(likes));
        return "testRquestParam";
    }
12345678
```

##### 4.3.4.2 defaultValue

 如果对应的参数没有，我们可以用defaultValue属性设置默认值。

例如：

```java
    @RequestMapping("/testRquestParam")
    public String testRquestParam(@RequestParam(value = "id",required = false,defaultValue = "777") Integer uid,@RequestParam("name") String name, @RequestParam("likes")String[] likes){
        System.out.println("testRquestParam");
        System.out.println(uid);
        System.out.println(name);
        System.out.println(Arrays.toString(likes));
        return "testRquestParam";
    }
12345678
```

### 4.4 响应体响应数据

 无论是RestFul风格还是我们之前web阶段接触过的异步请求，都需要把数据转换成Json放入响应体中。

#### 4.4.1 数据放到响应体

 我们的SpringMVC为我们提供了**@ResponseBody**来非常方便的把Json放到响应体中。

 **@ResponseBody**可以加在哪些东西上面？类上和方法上

 具体代码请参考范例。

#### 4.4.2 数据转换成Json

##### 4.4.2.1 配置

 SpringBoot项目中使用了web的start后，不需要进行额外的依赖和配置

##### 4.4.2.2 使用

 只要把要转换的数据直接作为方法的返回值返回即可。SpringMVC会帮我们把返回值转换成json。具体代码请参考范例。

#### 4.4.3 范例

##### 范例一

 要求定义个RestFul风格的接口，该接口可以用来根据id查询用户。请求路径要求为 /response/user ，请求方式要求为GET。

 而请求参数id要写在请求路径上，例如 /response/user/1 这里的1就是id。

 要求获取参数id,去查询对应id的用户信息（模拟查询即可，可以选择直接new一个User对象），并且转换成json响应到响应体中。

```java
@Controller
@RequestMapping("/response")
public class ResponseController {

    @RequestMapping("/user/{id}")
    @ResponseBody
    public User findById(@PathVariable("id") Integer id){
        User user = new User(id, "三更草堂", 15, null);
        return user;
    }
}
1234567891011
```

### 4.5 跨域请求

#### 4.5.1 什么是跨域

 浏览器出于安全的考虑，使用 XMLHttpRequest对象发起 HTTP请求时必须遵守[同源策略](https://so.csdn.net/so/search?q=同源策略&spm=1001.2101.3001.7020)，否则就是跨域的HTTP请求，默认情况下是被禁止的。 同源策略要求源相同才能正常进行通信，即协议、域名、端口号都完全一致。

#### 4.5.2 CORS解决跨域

 CORS是一个W3C标准，全称是”跨域资源共享”（Cross-origin resource sharing），允许浏览器向跨源服务器，发出XMLHttpRequest请求，从而克服了AJAX只能同源使用的限制。

 它通过服务器增加一个特殊的Header[Access-Control-Allow-Origin]来告诉客户端跨域的限制，如果浏览器支持CORS、并且判断Origin通过的话，就会允许XMLHttpRequest发起跨域请求。



#### 4.5.3 SpringBoot使用CORS解决跨域

##### 1.使用@CrossOrigin

可以在支持跨域的方法上或者是Controller上加上@CrossOrigin注解

```java
@RestController
@RequestMapping("/user")
@CrossOrigin
public class UserController {

    @Autowired
    private UserServcie userServcie;

    @RequestMapping("/findAll")
    public ResponseResult findAll(){
        //调用service查询数据 ，进行返回
        List<User> users = userServcie.findAll();

        return new ResponseResult(200,users);
    }
}

1234567891011121314151617
```

##### 2.使用 WebMvcConfigurer 的 addCorsMappings 方法配置CorsInterceptor

```java
@Configuration
public class CorsConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
      // 设置允许跨域的路径
        registry.addMapping("/**")
                // 设置允许跨域请求的域名
                .allowedOriginPatterns("*")
                // 是否允许cookie
                .allowCredentials(true)
                // 设置允许的请求方式
                .allowedMethods("GET", "POST", "DELETE", "PUT")
                // 设置允许的header属性
                .allowedHeaders("*")
                // 跨域允许时间
                .maxAge(3600);
    }
}
12345678910111213141516171819
```

### 4.6 拦截器

#### 4.6.0 登录案例

##### 4.6.0.1 思路分析

 在前后端分离的场景中，很多时候会采用token的方案进行登录校验。

 登录成功时，后端会根据一些用户信息生成一个token字符串返回给前端。

 前端会存储这个token。以后前端发起请求时如果有token就会把token放在请求头中发送给后端。

 后端接口就可以获取请求头中的token信息进行解析，如果解析不成功说明token超时了或者不是正确的token，相当于是未登录状态。

 如果解析成功，说明前端是已经登录过的。

##### 4.6.0.2 Token生成方案-JWT

 本案例采用目前企业中运用比较多的JWT来生成token。

 使用时先引入相关依赖

```xml
        <dependency>
            <groupId>io.jsonwebtoken</groupId>
            <artifactId>jjwt</artifactId>
            <version>0.9.0</version>
        </dependency>
12345
```

 然后可以使用下面的工具类来生成和解析token

```java
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;
import java.util.Date;
import java.util.UUID;

/**
 * JWT工具类
 */
public class JwtUtil {

    //有效期为
    public static final Long JWT_TTL = 60 * 60 *1000L;// 60 * 60 *1000  一个小时
    //设置秘钥明文
    public static final String JWT_KEY = "sangeng";

    /**
     * 创建token
     * @param id
     * @param subject
     * @param ttlMillis
     * @return
     */
    public static String createJWT(String id, String subject, Long ttlMillis) {

        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);
        if(ttlMillis==null){
            ttlMillis=JwtUtil.JWT_TTL;
        }
        long expMillis = nowMillis + ttlMillis;
        Date expDate = new Date(expMillis);
        SecretKey secretKey = generalKey();

        JwtBuilder builder = Jwts.builder()
                .setId(id)              //唯一的ID
                .setSubject(subject)   // 主题  可以是JSON数据
                .setIssuer("sg")     // 签发者
                .setIssuedAt(now)      // 签发时间
                .signWith(signatureAlgorithm, secretKey) //使用HS256对称加密算法签名, 第二个参数为秘钥
                .setExpiration(expDate);// 设置过期时间
        return builder.compact();
    }

    /**
     * 生成加密后的秘钥 secretKey
     * @return
     */
    public static SecretKey generalKey() {
        byte[] encodedKey = Base64.getDecoder().decode(JwtUtil.JWT_KEY);
        SecretKey key = new SecretKeySpec(encodedKey, 0, encodedKey.length, "AES");
        return key;
    }
    
    /**
     * 解析
     *
     * @param jwt
     * @return
     * @throws Exception
     */
    public static Claims parseJWT(String jwt) throws Exception {
        SecretKey secretKey = generalKey();
        return Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(jwt)
                .getBody();
    }


}
1234567891011121314151617181920212223242526272829303132333435363738394041424344454647484950515253545556575859606162636465666768697071727374757677
```

##### 4.6.0.3 登录接口实现

数据准备

```sql
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(50) DEFAULT NULL,
  `password` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

/*Data for the table `sys_user` */

insert  into `sys_user`(`id`,`username`,`password`) values (1,'root','root'),(2,'sangeng','caotang');


12345678910111213
```

实体类

```java
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SystemUser {

    private Integer id;
    private String username;
    private String password;
}

12345678910
```

SystemUserController

```java
import com.sangeng.domain.ResponseResult;
import com.sangeng.domain.SystemUser;
import com.sangeng.service.SystemUserService;
import com.sangeng.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/sys_user")
public class SystemUserController {
    @Autowired
    private SystemUserService userService;

    @PostMapping("/login")
    public ResponseResult login(@RequestBody SystemUser user) {
        //校验用户名密码是否正确
        SystemUser loginUser = userService.login(user);
        Map<String, Object> map;
        if (loginUser != null) {
            //如果正确 生成token返回
            map = new HashMap<>();
            String token = JwtUtil.createJWT(UUID.randomUUID().toString(), String.valueOf(loginUser.getId()), null);
            map.put("token", token);
        } else {
            //如果不正确 给出相应的提示
            return new ResponseResult(300, "用户名或密码错误，请重新登录");
        }
        return new ResponseResult(200, "登录成功", map);
    }
}

1234567891011121314151617181920212223242526272829303132333435363738
```

Service

```java
public interface SystemUserService {

    public SystemUser login(SystemUser user);
}
@Service
public class SystemUserServcieImpl implements SystemUserService {
    @Autowired
    private SystemUserMapper systemUserMapper;

    @Override
    public SystemUser login(SystemUser user) {
        SystemUser loginUser = systemUserMapper.login(user);
        return loginUser;
    }
}
123456789101112131415
```

dao

```java
@Mapper
@Repository
public interface UserMapper {
    List<User> findAll();
}

123456
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd" >
<mapper namespace="com.sangeng.mapper.SystemUserMapper">
    <select id="login" resultType="com.sangeng.domain.SystemUser">
        select * from sys_user where username = #{username} and password = #{password}
    </select>
</mapper>
1234567
```

##### 4.6.0.4 登录页面

 见资料

#### 4.6.1 拦截器的概念

 如果我们想在多个[Handler](https://so.csdn.net/so/search?q=Handler&spm=1001.2101.3001.7020)方法执行之前或者之后都进行一些处理，甚至某些情况下需要拦截掉，不让Handler方法执行。那么可以使用SpringMVC为我们提供的拦截器。

 详情见 https://space.bilibili.com/663528522 SpringMVC课程中拦截器相关章节。

#### 4.6.1 使用步骤

##### ①创建类实现HandlerInterceptor接口

```java
public class LoginInterceptor implements HandlerInterceptor {
}
12
```

##### ②实现方法

```java
@Component
public class LoginInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //获取请求头中的token
        String token = request.getHeader("token");
        //判断token是否为空，如果为空也代表未登录 提醒重新登录（401）
        if(!StringUtils.hasText(token)){
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            return false;
        }
        //解析token看看是否成功
        try {
            Claims claims = JwtUtil.parseJWT(token);
            String subject = claims.getSubject();
            System.out.println(subject);
        } catch (Exception e) {
            e.printStackTrace();
            //如果解析过程中没有出现异常说明是登录状态
            //如果出现了异常，说明未登录，提醒重新登录（401）
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            return false;
        }
        return true;
    }
}
123456789101112131415161718192021222324252627
```

##### ③配置拦截器

```java
@Configuration
public class LoginConfig implements WebMvcConfigurer {

    @Autowired
    private LoginInterceptor loginInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(loginInterceptor)//添加拦截器
            .addPathPatterns("/**")  //配置拦截路径
            .excludePathPatterns("/sys_user/login");//配置排除路径
    }
}
12345678910111213
```

### 4.7 异常统一处理

#### ①创建类加上@ControllerAdvice注解进行标识

```java
@ControllerAdvice
public class MyControllerAdvice {

}
1234
```

#### ②定义[异常处理](https://so.csdn.net/so/search?q=异常处理&spm=1001.2101.3001.7020)方法

 定义异常处理方法，使用**@ExceptionHandler**标识可以处理的异常。

```java
@ControllerAdvice
public class MyControllerAdvice {

    @ExceptionHandler(RuntimeException.class)
    @ResponseBody
    public ResponseResult handlerException(Exception e){
        //获取异常信息，存放如ResponseResult的msg属性
        String message = e.getMessage();
        ResponseResult result = new ResponseResult(300,message);
        //把ResponseResult作为返回值返回，要求到时候转换成json存入响应体中
        return result;
    }
}
12345678910111213
```

### 4.8 获取web原生对象

 我们之前在web阶段我们经常要使用到request对象，response，session对象等。我们也可以通过SpringMVC获取到这些对象。（不过在MVC中我们很少获取这些对象，因为有更简便的方式，避免了我们使用这些原生对象相对繁琐的API。）

 我们只需要在方法上添加对应类型的参数即可，但是注意数据类型不要写错了，SpringMVC会把我们需要的对象传给我们的形参。

```java
@RestController
public class TestController {

    @RequestMapping("/getRequestAndResponse")
    public ResponseResult getRequestAndResponse(HttpServletRequest request, HttpServletResponse response, HttpSession session){
        System.out.println(request);
        return new ResponseResult(200,"成功");
    }
}

12345678910
```

### 4.9 自定义参数解析

 如果我们想实现像获取请求体中的数据那样，在Handler方法的参数上增加一个@RepuestBody注解就可以获取到对应的数据的话。

 可以使用HandlerMethodArgumentResolver来实现自定义的参数解析。

①定义用来标识的注解

```java
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface CurrentUserId {

}
12345
```

②创建类实现HandlerMethodArgumentResolver接口并重写其中的方法

**注意加上@Component注解注入Spring容器**

```java
public class UserIdArgumentResolver implements HandlerMethodArgumentResolver {

    //判断方法参数使用能使用当前的参数解析器进行解析
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        //如果方法参数有加上CurrentUserId注解，就能把被我们的解析器解析
        return parameter.hasParameterAnnotation(CurrentUserId.class);
    }
    //进行参数解析的方法，可以在方法中获取对应的数据，然后把数据作为返回值返回。方法的返回值就会赋值给对应的方法参数
    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        //获取请求头中的token
        String token = webRequest.getHeader("token");
        if(StringUtils.hasText(token)){
            //解析token，获取userId
            Claims claims = JwtUtil.parseJWT(token);
            String userId = claims.getSubject();
            //返回结果
            return userId;
        }
        return null;
    }
}
1234567891011121314151617181920212223
```

③配置参数解析器

```java
@Configuration
public class ArgumentResolverConfig implements WebMvcConfigurer {

    @Autowired
    private UserIdArgumentResolver userIdArgumentResolver;

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(userIdArgumentResolver);
    }
}
1234567891011
```

④测试

在需要获取UserId的方法中增加对应的方法参数然后使用@CurrentUserId进行标识即可获取到数据

```java
@RestController
@RequestMapping("/user")
//@CrossOrigin
public class UserController {

    @Autowired
    private UserServcie userServcie;

    @RequestMapping("/findAll")
    public ResponseResult findAll(@CurrentUserId String userId) throws Exception {
        System.out.println(userId);
        //调用service查询数据 ，进行返回s
        List<User> users = userServcie.findAll();

        return new ResponseResult(200,users);
    }
}
1234567891011121314151617
```

### 4.10 声明式事务

 直接在需要事务控制的方法上加上对应的注解**@Transactional**

```java
@Service
public class UserServiceImpl implements UserServcie {

    @Autowired
    private UserMapper userMapper;

    @Override
    public List<User> findAll() {
        return userMapper.findAll();
    }

    @Override
    @Transactional
    public void insertUser() {
        //添加2个用户到数据库
        User user = new User(null,"sg666",15,"上海");
        User user2 = new User(null,"sg777",16,"北京");
        userMapper.insertUser(user);
        System.out.println(1/0);
        userMapper.insertUser(user2);
    }


}
123456789101112131415161718192021222324
```

### 4.11 AOP

 AOP详细知识学习见：https://space.bilibili.com/663528522 中的Spring教程

 在SpringBoot中默认是开启AOP功能的。如果不想开启AOP功能可以使用如下配置设置为false

```yml
spring:
  aop:
    auto: false
123
```

#### 4.11.1 使用步骤

①添加依赖

```xml
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-aop</artifactId>
        </dependency>
1234
```

②自定义注解

```java
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface InvokeLog {
}

12345
```

③定义切面类

```java
@Aspect  //标识这是一个切面类
@Component
public class InvokeLogAspect {

    //确定切点
    @Pointcut("@annotation(com.sangeng.aop.InvokeLog)")
    public void pt(){
    }

    @Around("pt()")
    public Object printInvokeLog(ProceedingJoinPoint joinPoint){
        //目标方法调用前
        Object proceed = null;
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        String methodName = signature.getMethod().getName();
        System.out.println(methodName+"即将被调用");
        try {
            proceed = joinPoint.proceed();
            //目标方法调用后
            System.out.println(methodName+"被调用完了");
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            //目标方法出现异常了
            System.out.println(methodName+"出现了异常");
        }
        return proceed;
    }
}

1234567891011121314151617181920212223242526272829
```

④在需要正确的地方增加对应的注解

```Java
@Service
public class UserServiceImpl implements UserServcie {

    @Autowired
    private UserMapper userMapper;

    @Override
    @InvokeLog  //需要被增强方法需要加上对应的注解
    public List<User> findAll() {
        return userMapper.findAll();
    }
}
123456789101112
```

#### 4.11.2 切换动态代理

 有的时候我们需要修改AOP的代理方式。

 我们可以使用以下方式修改：

 在配置文件中配置spring.aop.proxy-target-class为false这为使用jdk动态代理。该配置默认值为true，代表使用cglib动态代理。

```java
@SpringBootApplication
@EnableAspectJAutoProxy(proxyTargetClass = false)//修改代理方式
public class WebApplication {
    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(WebApplication.class, args);
    }
}
1234567
```

 如果想生效还需要在配置文件中做如下配置

```yml
spring:
  aop:
    proxy-target-class: false #切换动态代理的方式
123
```

### 4.12 模板引擎相关-Thymeleaf

#### 4.12.1 快速入门

##### 4.12.1.1依赖

```xml
        <!--thymeleaf依赖-->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-thymeleaf</artifactId>
        </dependency>
12345
```

##### 4.12.1.2定义Controller

在controller中往域中存数据，并且跳转

```java
@Controller
public class ThymeleafController {

    @Autowired
    private UserServcie userServcie;

    @RequestMapping("/thymeleaf/users")
    public String users(Model model){
        //获取数据
        List<User> users = userServcie.findAll();
        //望域中存入数据
        model.addAttribute("users",users);
        model.addAttribute("msg","hello thymeleaf");
        //页面跳转
        return "table-standard";
    }
}
1234567891011121314151617
```

##### 4.12.1.3 htmL

在**resources\templates**下存放模板页面。

在html标签中加上 xmlns:th=“http://www.thymeleaf.org”

获取域中的name属性的值可以使用： ${name} 注意要在th开头的属性中使用

```html
<html lang="en" class="no-ie" xmlns:th="http://www.thymeleaf.org">
 .....
 <div class="panel-heading" th:text="${msg}">Kitchen Sink</div>
123
```

如果需要引入静态资源，需要使用如下写法。

```html
   <link rel="stylesheet" th:href="@{/app/css/bootstrap.css}">
   <!-- Vendor CSS-->
   <link rel="stylesheet" th:href="@{/vendor/fontawesome/css/font-awesome.min.css}">
   <link rel="stylesheet" th:href="@{/vendor/animo/animate+animo.css}">
   <link rel="stylesheet" th:href="@{/vendor/csspinner/csspinner.min.css}">
   <!-- START Page Custom CSS-->
   <!-- END Page Custom CSS-->
   <!-- App CSS-->
   <link rel="stylesheet" th:href="@{/app/css/app.css}">
   <!-- Modernizr JS Script-->
   <script th:src="@{/vendor/modernizr/modernizr.js}" type="application/javascript"></script>
   <!-- FastClick for mobiles-->
   <script th:src="@{/vendor/fastclick/fastclick.js}" type="application/javascript"></script>
12345678910111213
```

遍历语法：遍历的语法 th:each=“自定义的元素变量名称 : ${集合变量名称}”

```html
<tr th:each="user:${users}">
    <td th:text="${user.id}"></td>
    <td th:text="${user.username}"></td>
    <td th:text="${user.age}"></td>
    <td th:text="${user.address}"></td>
</tr>
123456
```

## 5.整合Redis

### ①依赖

```xml
        <!--redis-->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-data-redis</artifactId>
        </dependency>
12345
```

### ②配置Redis地址和端口号

```yml
spring:
  redis:
    host: 127.0.0.1 #redis服务器ip地址
    port: 6379  #redis端口号
1234
```

### ③注入RedisTemplate使用

```java
    @Autowired
    private StringRedisTemplate redisTemplate;

    @Test
    public void testRedis(){
        redisTemplate.opsForValue().set("name","三更");
    }
1234567
```

## 6.环境切换

### 6.1 为什么要使用profile

 在实际开发环境中，我们存在开发环境的配置，部署环境的配置，测试环境的配置等等，里面的配置信息很多时，例如：端口、上下文路径、数据库配置等等，若每次切换环境时，我们都需要进行修改这些配置信息时，会比较麻烦，profile的出现就是为了解决这个问题。它可以让我们针对不同的环境进行不同的配置，然后可以通过激活、指定参数等方式快速切换环境。

### 6.2 使用

#### 6.2.1 创建profile配置文件

 我们可以用**application-xxx.yml**的命名方式 创建配置文件，其中xxx可以根据自己的需求来定义。

 例如

 我们需要一个测试环境的配置文件，则可以命名为：**application-test.yml**

 需要一个生产环境的配置文件，可以命名为：**application-prod.yml**

 我们可以不同环境下不同的配置放到对应的profile文件中进行配置。然后把不同环境下都相同的配置放到application.yml文件中配置。

#### 6.2.2 激活环境

 我们可以再**application.yml**文件中使用**spring.profiles.active**属性来配置激活哪个环境。

 也可以使用虚拟机参数来指定激活环境。例如 ： **-Dspring.profiles.active=test**

 也可以使用命令行参数来激活环境。例如： **–spring.profiles.active =test**



## 7.日志

 开启日志

```yml
debug: true #开启日志
logging:
  level:
    com.sangeng: debug #设置日志级别
1234
```

## 8.指标监控

 我们在日常开发中需要对程序内部的运行情况进行监控， 比如：健康度、运行指标、日志信息、线程状况等等 。而SpringBoot的监控Actuator就可以帮我们解决这些问题。

### 8.1 使用

①添加依赖

```xml
<dependency>
 	<groupId>org.springframework.boot</groupId>
 	<artifactId>spring-boot-starter-actuator</artifactId>
</dependency>
1234
```

②访问监控接口

http://localhost:81/actuator

③配置启用监控端点

```yml
management:
  endpoints:
    enabled-by-default: true #配置启用所有端点
	web:
      exposure:
        include: "*" #web端暴露所有端点
123456
```

### 8.2 常用端点

| 端点名称         | 描述                                      |
| ---------------- | ----------------------------------------- |
| `beans`          | 显示应用程序中所有Spring Bean的完整列表。 |
| `health`         | 显示应用程序运行状况信息。                |
| `info`           | 显示应用程序信息。                        |
| `loggers`        | 显示和修改应用程序中日志的配置。          |
| `metrics`        | 显示当前应用程序的“指标”信息。            |
| `mappings`       | 显示所有`@RequestMapping`路径列表。       |
| `scheduledtasks` | 显示应用程序中的计划任务。                |

### 8.3 图形化界面 SpringBoot Admin

①创建SpringBoot Admin Server应用

要求引入spring-boot-admin-starter-server依赖

```xml
        <dependency>
            <groupId>de.codecentric</groupId>
            <artifactId>spring-boot-admin-starter-server</artifactId>
        </dependency>
1234
```

然后在启动类上加上@EnableAdminServer注解

②配置SpringBoot Admin client应用

在需要监控的应用中加上spring-boot-admin-starter-client依赖

```xml
        <dependency>
            <groupId>de.codecentric</groupId>
            <artifactId>spring-boot-admin-starter-client</artifactId>
            <version>2.3.1</version>
        </dependency>
12345
```

然后配置SpringBoot Admin Server的地址

```yml
spring:
  boot:
    admin:
      client:
        url: http://localhost:8888 #配置 Admin Server的地址
```
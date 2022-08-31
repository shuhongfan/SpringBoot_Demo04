# SpringMVC-02

## 1.类型转换器

​	虽然我们前面在获取参数时看起来非常轻松，但是在这个过程中是有可能出现一些问题的。

​	例如，请求参数为success=1 我们期望把这个请求参数获取出来赋值给一个Boolean类型的变量。

​    这里就会涉及到  Stirng-——>Boolean的类型转换了。实际上SpringMVC中内置了很多类型转换器来进行类型转换。也有专门进行Stirng-——>Boolean类型转换的转换器**StringToBooleanConverter**。

​	如果是符合SpringMVC内置转换器的转换规则就可以很轻松的实现转换。但是如果不符合转换器的规则呢？

​	例如，请求参数为birthday=2004-12-12 我们期望把这个请求参数获取出来赋值给一个Date类型的变量。就不符合内置的规则了。内置的可以把 2004/12/12 这种格式进行转换。这种情况下我们就可以选择自定义类型转换。



### 1.1 自定义类型转换器

#### ①创建类实现Converter接口

~~~~java
public class StringToDateConverter implements Converter<String, Date> {
    public Date convert(String source) {
        return null;
    }
}
~~~~

#### ②实现convert方法

~~~~java
public class StringToDateConverter implements Converter<String, Date> {
    public Date convert(String source) {
        //String->Date   2005-12-12 
        Date date = null;
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            date = simpleDateFormat.parse(source);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }
}
~~~~

#### ③配置让SpringMVC使用自定义转换器

~~~~~xml
    <!--解决响应乱码-->
    <mvc:annotation-driven conversion-service="myConversionService">
        <mvc:message-converters>
            <bean class="org.springframework.http.converter.StringHttpMessageConverter">
                <constructor-arg value="utf-8"/>
            </bean>
        </mvc:message-converters>
    </mvc:annotation-driven>

    <bean class="org.springframework.context.support.ConversionServiceFactoryBean" id="myConversionService">
        <property name="converters">
            <set>
                <bean class="com.sangeng.converter.StringToDateConverter"></bean>
            </set>
        </property>
    </bean>
~~~~~





### 1.2 日期转换简便解决方案

​	如果是String到Date的转换我们也可以使用另外一种更方便的方式。使用@DateTimeFormat来指定字符串的格式。

~~~~java
    @RequestMapping("/testDateConverter")
    public String testDateConverter(@DateTimeFormat(pattern = "yyyy-MM-dd") Date birthday){
        System.out.println("testDateConverter");
        System.out.println(birthday);
        return "/success.jsp";
    }
~~~~



## 2.响应体响应数据（重点）

​	无论是RestFul风格还是我们之前web阶段接触过的异步请求，都需要把数据转换成Json放入响应体中。



### 2.1 数据放到响应体

​	我们的SpringMVC为我们提供了**@ResponseBody**来非常方便的把Json放到响应体中。

​	**@ResponseBody**可以加在哪些东西上面？类上和方法上

​	具体代码请参考范例。



### 2.2 数据转换成Json

​	SpringMVC可以把我们进行Json的转换，不过需要进行相应配置（已经配置过）。



#### 2.2.1 配置

##### ①导入jackson依赖

~~~~xml
        <!-- jackson，帮助进行json转换-->
        <dependency>
            <groupId>com.fasterxml.jackson.core</groupId>
            <artifactId>jackson-databind</artifactId>
            <version>2.9.0</version>
        </dependency>
~~~~

##### ②开启mvc的注解驱动

~~~~xml
    <mvc:annotation-driven></mvc:annotation-driven>
~~~~



#### 2.2.2 使用

​	只要把要转换的数据直接作为方法的返回值返回即可。SpringMVC会帮我们把返回值转换成json。具体代码请参考范例。



### 2.3 范例

#### 范例一

​	要求定义个RestFul风格的接口，该接口可以用来根据id查询用户。请求路径要求为  /response/user  ，请求方式要求为GET。

​	而请求参数id要写在请求路径上，例如   /response/user/1   这里的1就是id。

​	要求获取参数id,去查询对应id的用户信息（模拟查询即可，可以选择直接new一个User对象），并且转换成json响应到响应体中。

~~~~java
@Controller
@RequestMapping("/response")
public class ResponseController {
    @GetMapping("/user/{id}")
    @ResponseBody //这方法的返回值放入响应体中
    public User testResponse(@PathVariable Integer id){
        User user = new User(id,null,null,null);
        return user;//因为以及做过配置，所以会把返回值转换成json
    }
}

~~~~



#### 范例二

​	要求定义个RestFul风格的接口，该接口可以查询所有用户。请求路径要求为  /response/user  ，请求方式要求为GET。

​	去查询所有的用户信息（模拟查询即可，可以选择直接创建集合，添加几个User对象），并且转换成json响应到响应体中。

~~~~java
@Controller
@RequestMapping("/response")
@ResponseBody  //这类中所有方法的返回值都会放到响应体中
public class ResponseController {

    @GetMapping("/user/{id}")
    public User testResponse(@PathVariable Integer id){
        User user = new User(id,null,null,null);
        return user;
    }

    @GetMapping("/user")
    public List<User> testResponse2(){
        List<User> list = new ArrayList<User>();
        list.add(new User(1,"三更",15,null));
        list.add(new User(2,"四更",16,null));
        list.add(new User(3,"五更",17,null));
        return list;
    }
}
~~~~

​	如果一个Controller中的所有方法返回值都要放入响应体，那么我们可以直接在Controller类上加@ResponseBody。

​	我们可以使用**@RestController** 注解替换@Controller和@ResponseBody两个注解

~~~java
@RequestMapping("/response")
@RestController //相当于  @Controller+@ResponseBody
public class ResponseController {

    @GetMapping("/user/{id}")
    public User testResponse(@PathVariable Integer id){
        User user = new User(id,null,null,null);
        return user;
    }

    @GetMapping("/user")
    public List<User> testResponse2(){
        List<User> list = new ArrayList<User>();
        list.add(new User(1,"三更",15,null));
        list.add(new User(2,"四更",16,null));
        list.add(new User(3,"五更",17,null));
        return list;
    }
}

~~~



## 3.页面跳转

​	在SpringMVC中我们可以非常轻松的实现页面跳转，只需要把方法的返回值写成要跳转页面的路径即可。

例如：

~~~~java
@Controller
public class PageJumpController {
    @RequestMapping("/testJump")
    public String testJump(){
        return "/success.jsp";
    }
}
~~~~

​	

​	默认的跳转其实是转发的方式跳转的。我们也可以选择加上标识，在要跳转的路径前加上**forward:** 。这样SpringMVC也会帮我们进行请求转发。

例如：

~~~~java
@Controller
public class PageJumpController {
    @RequestMapping("/testJump")
    public String testJump(){
        return "forward:/success.jsp";
    }
}
~~~~



​	如果想实现重定向跳转则可以在跳转路径前加上 **redirect:**  进行标识。这样SpringMVC就会帮我们进行重定向跳转。

例如：

~~~~java
@Controller
public class PageJumpController {
    @RequestMapping("/testJump")
    public String testJump(){
        return "redirect:/success.jsp";
    }
}

~~~~



## 4.视图解析器

​	如果我们经常需要跳转页面，并且页面所在的路径比较长，我们每次写完整路径会显的有点麻烦。我们可以配置视图解析器，设置跳转路径的前缀和后缀。这样可以简化我们的书写。



### 4.1使用步骤

#### ①配置视图解析器

​	我们需要完SpringMVC容器中注入InternalResourceViewResolver对象。

~~~~xml
    <!--配置视图解析器-->
    <bean class="org.springframework.web.servlet.view.InternalResourceViewResolver" id="viewResolver">
        <!--要求拼接的前缀-->
        <property name="prefix" value="/WEB-INF/page/"></property>
        <!--要拼接的后缀-->
        <property name="suffix" value=".jsp"></property>
    </bean>
~~~~

#### ②页面跳转

​	视图解析器会在逻辑视图的基础上拼接得到物理视图。

~~~~java
    @RequestMapping("/testJumpToJsp")
    public String testJumpToJsp(){
//        return "/WEB-INF/page/test.jsp";
        return "test";
    }
~~~~



### 4.2 不进行前后缀拼接

​	如果在配置了视图解析器的情况下，某些方法中并不想拼接前后缀去跳转。这种情况下我们可以在跳转路径前加**forward:** 或者**redirect:**进行标识。这样就不会进行前后缀的拼接了。

​	例如:

~~~~java
    @RequestMapping("/testJumpHtml")
    public String testJumpHtml(){
        //如果加了forward:  或者redirect: 就不会进行前后缀的拼接
        return "forward:/hello1.html";
    }
~~~~





## 5.获取原生对象

​	我们之前在web阶段我们经常要使用到request对象，response，session对象等。我们也可以通过SpringMVC获取到这些对象。（不过在MVC中我们很少获取这些对象，因为有更简便的方式，避免了我们使用这些原生对象相对繁琐的API。）

​	我们只需要在方法上添加对应类型的参数即可，但是注意数据类型不要写错了，SpringMVC会把我们需要的对象传给我们的形参。

​	例如：

~~~~java
@Controller
public class RequestResponseController {
    @RequestMapping("/getReqAndRes")
    public String getReqAndRes(HttpServletRequest request, HttpServletResponse response, HttpSession session){
        System.out.println();
        return "test";
    }
}
~~~~



​	

## 6.获取请求头和Cookie

### 6.1获取请求头

​	在方法中定义一个参数，参数前加上**@RequestHeader**注解，知道要获取的请求头名即可获取对应请求头的值。

例如：

​	想要获取 device-type 这个请求头则可以按照如下方式定义方法。

~~~~java
@Controller
public class RequestResponseController {


    @RequestMapping("/getHeader")
    public String getHeader(@RequestHeader(value = "device-type") String deviceType){
        System.out.println(deviceType);
        return "test";
    }
}

~~~~



### 6.2 获取Cookie

​	在方法中定义一个参数，参数前加上**@CookieValue** 注解，知道要获取的cookie名即可获取对应cookie的值。

例如：

​	想要获取 JSESSIONID 的cookie值。则可以按照如下方式定义方法。

~~~~java
@Controller
public class RequestResponseController {

    @RequestMapping("/getCookie")
    public String getCookie(@CookieValue("JSESSIONID") String sessionId){
        System.out.println(sessionId);
        return "test";
    }
}

~~~~



## 7.JSP开发模式（了解）

​	如果我们使用JSP进行开发，那我们就需要在域中存数据，然后跳转到对应的JSP页面中，在JSP页面中获取域中的数据然后进行相关处理。

​	使用如果是类似JSP的开发模式就会涉及到**往域中存数据**和**携带数据跳转页面**的操作。

​	所以我们来看下如果用SpringMVC进行相关操作。



### 7.1 往Requet域存数据并跳转

#### 7.1.1 使用Model

​	我们可以使用**Model**来往域中存数据。然后使用之前的方式实现页面跳转。



例如

​	我们要求访问  **/testRequestScope** 这个路径时能往Request域中存name和title数据，然后跳转到 **/WEB-INF/page/testScope.jsp** 这个页面。在Jsp中获取域中的数据。

​	则可以使用如下写法:

~~~~java
@Controller
public class JspController {
    @RequestMapping("/testRquestScope")
    public String testRquestScope(Model model){
        //往请求域存数据
        model.addAttribute("name","三更");
        model.addAttribute("title","不知名Java教程UP主");
        return "testScope";
    }
}
~~~~



#### 7.1.2 使用ModelAndView

​	我们可以使用**ModelAndView**来往域中存数据和页面跳转。

例如

​	我们要求访问  **/testRequestScope2**  这个路径时能往域中存name和title数据，然后跳转到 **/WEB-INF/page/testScope.jsp** 这个页面。在Jsp中获取域中的数据。

​	则可以使用如下写法:

~~~~java
@Controller
public class JspController {
    @RequestMapping("/testRquestScope2")
    public ModelAndView testRquestScope2(ModelAndView modelAndView){
        //往域中添加数据
        modelAndView.addObject("name","三更");
        modelAndView.addObject("title","不知名Java教程UP主");
        //页面跳转
        modelAndView.setViewName("testScope");
        return modelAndView;
    }
}
~~~~

​	**注意要把modelAndView对象作为方法的返回值返回。**



### 7.2 从Request域中获取数据

​	我们可以使用**@RequestAttribute** 把他加在方法参数上，可以让SpringMVC帮我们从Request域中获取相关数据。



例如

~~~~java
@Controller
public class JspController {

    @RequestMapping("/testGetAttribute")
    public String testGetAttribute(@RequestAttribute("org.springframework.web.servlet.HandlerMapping.bestMatchingPattern")
                                               String value,HttpServletRequest request){
        System.out.println(value);
        return "testScope";
    }
}

~~~~





### 7.3 往Session域存数据并跳转

​	我们可以使用**@SessionAttributes**注解来进行标识，用里面的属性来标识哪些数据要存入Session域。



例如

​	我们要求访问  **/testSessionScope **这个路径时能往域中存**name**和**title**数据，然后跳转到 **/WEB-INF/page/testScope.jsp** 这个页面。在jsp中获取**Session域**中的数据。



​	则可以使用如下写法

~~~~java
@Controller
@SessionAttributes({"name"})//表示name这个数据也要存储一份到session域中
public class JspController {


    @RequestMapping("/testSessionScope")
    public String testSessionScope(Model model){
        model.addAttribute("name","三更");
        model.addAttribute("title","不知名Java教程UP主");
        return "testScope";
    }
}
~~~~





### 7.4 获取Session域中数据

​	我们可以使用**@SessionAttribute**把他加在方法参数上，可以让SpringMVC帮我们从**Session域**中获取相关数据。



例如：

~~~~java
@Controller
@SessionAttributes({"name"})
public class JspController {
    @RequestMapping("/testGetSessionAttr")
    public String testGetSessionAttr(@SessionAttribute("name") String name){
        System.out.println(name);
        return "testScope";
    }

}

~~~~


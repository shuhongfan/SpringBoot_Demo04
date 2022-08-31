package com.shf.sg02springbootstatic.controller;

import com.shf.sg02springbootstatic.domain.User;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Map;

@RestController
public class HelloController {
    @RequestMapping("/hello")
    public String hello() {
        return "Hello World";
    }

    @RequestMapping(value = "/testMethod",method = RequestMethod.POST)
    public String testMethod(){
        System.out.println("testMethod处理了请求");
        return "testMethod";
    }


    @RequestMapping(value = "/testParams",method = RequestMethod.GET,params = "code")
    public String testParams(){
        System.out.println("testParams处理了请求");
        return "testParams";
    }

    /**
     * 不能有code这个参数
     * @return
     */
    @RequestMapping(value = "/testParams",method = RequestMethod.GET,params = "!code")
    public String testParams2(){
        System.out.println("testParams处理了请求");
        return "testParams";
    }

    /**
     * 如果要求有code这参数，并且这参数值必须是某个值可以改成如下形式
     * @return
     */
    @RequestMapping(value = "/testParams",method = RequestMethod.GET,params = "code=sgct")
    public String testParams3(){
        System.out.println("testParams处理了请求");
        return "testParams";
    }

    /**
     * 如果要求有code这参数，并且这参数值必须不是某个值可以改成如下形式
     * @return
     */
    @RequestMapping(value = "/testParams",method = RequestMethod.GET,params = "code!=sgct")
    public String testParams4(){
        System.out.println("testParams处理了请求");
        return "testParams";
    }

    /**
     * 指定请求头
     * @return
     */
    @RequestMapping(value = "/testHeaders",method = RequestMethod.GET,headers = "deviceType")
    public String testHeaders(){
        System.out.println("testHeaders处理了请求");
        return "testHeaders";
    }

    /**
     * 不能有deviceType
     * @return
     */
    @RequestMapping(value = "/testHeaders",method = RequestMethod.GET,headers = "!deviceType")
    public String testHeaders2(){
        System.out.println("testHeaders处理了请求");
        return "testHeaders";
    }

    /**
     * 如果要求有deviceType这个请求头，并且其值必须是某个值可以改成如下形式
     * @return
     */
    @RequestMapping(value = "/testHeaders",method = RequestMethod.GET,headers = "deviceType=ios")
    public String testHeaders3(){
        System.out.println("testHeaders处理了请求");
        return "testHeaders";
    }

    /**
     * 如果要求有deviceType这个请求头，并且其值必须不是某个值可以改成如下形式
     * @return
     */
    @RequestMapping(value = "/testHeaders",method = RequestMethod.GET,headers = "deviceType!=ios")
    public String testHeaders4(){
        System.out.println("testHeaders处理了请求");
        return "testHeaders";
    }

    /**
     * Content-Type头必须为 multipart/from-data 的请求能够被testConsumes方法处理
     * @return
     */
    @RequestMapping(value = "/testConsumes",method = RequestMethod.POST,consumes = "multipart/from-data")
    public String testConsumes(){
        System.out.println("testConsumes处理了请求");
        return "testConsumes";
    }


    /**
     * 如果我们要求请求头Content-Type的值必须不能为某个multipart/from-data则可以改成如下形式：
     * @return
     */
    @RequestMapping(value = "/testConsumes",method = RequestMethod.POST,consumes = "!multipart/from-data")
    public String testConsumes2(){
        System.out.println("testConsumes处理了请求");
        return "testConsumes";
    }

    /**
     *  获取路径参数
     * @param id
     * @return
     */
    @RequestMapping(value = "/user/{id}",method = RequestMethod.GET)
    public String findUserById( @PathVariable("id") Integer id){
        System.out.println("findUserById");
        System.out.println(id);
        return "findUserById";
    }

    @RequestMapping(value = "/user/{id}/{name}",method = RequestMethod.GET)
    public String findUser(@PathVariable("id") Integer id,
                           @PathVariable("name") String name){
        System.out.println("findUser");
        System.out.println(id);
        System.out.println(name);
        return "findUser";
    }

    /**
     *  获取请求体中的Json格式参数  @RequestBody
     *  如果需要使用**@RequestBody**来获取请求体中Json并且进行转换，要求请求头 Content-Type 的值要为： application/json 。
     * @param user
     * @return
     */
    @RequestMapping(value = "/user",method = RequestMethod.POST)
    public String insertUser(@RequestBody User user){
        System.out.println("insertUser");
        System.out.println(user);
        return "insertUser";
    }

    /**
     * 获取参数封装成Map集合
     * @param map
     * @return
     */
    @RequestMapping(value = "/user",method = RequestMethod.POST)
    public String insertUser2(@RequestBody Map map){
        System.out.println("insertUser");
        System.out.println(map);
        return "insertUser";
    }

    @RequestMapping("/testRequestParam")
    public String testRequestParam(Integer id, String name, String[] likes){
        System.out.println("testRquestParam");
        System.out.println(id);
        System.out.println(name);
        System.out.println(Arrays.toString(likes));
        return "testRquestParam";
    }

    @RequestMapping("/testRequestParam2")
    public String testRequestParam2(@RequestParam("id") Integer uid,@RequestParam("name") String name, @RequestParam("likes")String[] likes){
        System.out.println("testRquestParam");
        System.out.println(uid);
        System.out.println(name);
        System.out.println(Arrays.toString(likes));
        return "testRquestParam";
    }

    @RequestMapping("/testRequestParam3")
    public String testRequestParam3(User user){
        System.out.println("testRquestParam");
        System.out.println(user);
        return "testRquestParam";
    }

    @RequestMapping("/testRequestParam4")
    public String testRequestParam4(
            @RequestParam(value = "id",required = false) Integer uid,
            @RequestParam("name") String name,
            @RequestParam("likes")String[] likes){
        System.out.println("testRquestParam");
        System.out.println(uid);
        System.out.println(name);
        System.out.println(Arrays.toString(likes));
        return "testRquestParam";
    }

    @RequestMapping("/testRequestParam5")
    public String testRequestParam5(
            @RequestParam(value = "id",required = false,defaultValue = "777") Integer uid,
            @RequestParam("name") String name,
            @RequestParam("likes")String[] likes){
        System.out.println("testRquestParam");
        System.out.println(uid);
        System.out.println(name);
        System.out.println(Arrays.toString(likes));
        return "testRquestParam";
    }
}

package com.sangeng;

import com.sangeng.dao.UserDao;
import com.sangeng.domain.User;
import com.sangeng.service.UserService;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.InputStream;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:applicationContext.xml")
public class SpingTest {

    @Autowired
    private UserService userService;

    @Autowired
    private UserDao userDao;

    @Test
    public void testJunit(){
//        userService.findById(10);
        List<User> all = userService.findAll();
        System.out.println(all);
//        System.out.println(1);
    }
    @Test
    public void testUserDao(){
        List<User> all = userDao.findAll();
        System.out.println(all);
    }

    @Test
    public void testMybatis() throws Exception {
        //定义mybatis配置文件的路径
        String resource = "mybatis-config.xml";
        InputStream inputStream = Resources.getResourceAsStream(resource);
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
        //获取Sqlsession对象
        SqlSession sqlSession = sqlSessionFactory.openSession();
        //获取UserDao实现类对象
        UserDao userDao = sqlSession.getMapper(UserDao.class);
        //调用方法测试
        List<User> userList = userDao.findAll();
        System.out.println(userList);
        //释放资源
        sqlSession.close();
    }


    public void test01(){
//        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
//        sqlSessionFactoryBean.setConfiguration();
//        SqlSessionFactory object = sqlSessionFactoryBean.getObject();
    }
}

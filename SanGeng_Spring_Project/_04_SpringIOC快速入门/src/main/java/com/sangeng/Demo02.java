package com.sangeng;

import com.sangeng.domain.Car;
import com.sangeng.domain.Motor;
import com.sangeng.domain.SteeringWheel;
import com.sangeng.domain.Tyre;
import com.sangeng.factory.CarFactory;
import com.sangeng.factory.CarStaticFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Demo02 {
    public static void main(String[] args) {
//        Car car = new Car();
//        //设置属性
//        car.setMotor(new Motor());
//        car.setSteeringWheel(new SteeringWheel());
//        car.setTyre(new Tyre());
//        //调用相关方法进行调试
//        car.testSteeringWheel();
//        car.testType();
//        car.testMontor();

        //使用实例工厂来创建Car
//        CarFactory carFactory = new CarFactory();
//        Car car1 = carFactory.getCar();
//
//        //使用静态工厂来创建Car
//        Car car = CarStaticFactory.getCar();

        ClassPathXmlApplicationContext app = new ClassPathXmlApplicationContext("applicationContext.xml");
        //获取car对象
//        Car c = (Car) app.getBean("car");
//        System.out.println(c);
        Car c = (Car) app.getBean("car2");
        System.out.println(c);
    }
}

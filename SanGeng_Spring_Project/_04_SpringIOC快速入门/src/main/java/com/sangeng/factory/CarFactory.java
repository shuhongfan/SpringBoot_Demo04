package com.sangeng.factory;


import com.sangeng.domain.Car;
import com.sangeng.domain.Motor;
import com.sangeng.domain.SteeringWheel;
import com.sangeng.domain.Tyre;

public class CarFactory {

    public Car getCar(){
        Car c = new Car();
        c.setMotor(new Motor());
        c.setSteeringWheel(new SteeringWheel());
        c.setTyre(new Tyre());
        c.testMontor();
        c.testSteeringWheel();
        c.testType();
        return c;
    }
}

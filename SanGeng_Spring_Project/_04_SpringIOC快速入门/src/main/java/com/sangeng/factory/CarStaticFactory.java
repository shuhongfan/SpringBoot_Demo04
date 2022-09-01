package com.sangeng.factory;

import com.sangeng.domain.Car;
import com.sangeng.domain.Motor;
import com.sangeng.domain.SteeringWheel;
import com.sangeng.domain.Tyre;

public class CarStaticFactory {

    public static Car getCar(){
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

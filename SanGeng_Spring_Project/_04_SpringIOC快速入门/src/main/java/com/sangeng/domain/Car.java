package com.sangeng.domain;

public class Car {

    //方向盘
    private SteeringWheel steeringWheel;
    //轮胎
    private Tyre tyre;
    //发动机
    private Motor motor;

    public Car(){

    }

    public void testType(){
        System.out.println("调试轮胎");
    }
    public void testMontor(){
        System.out.println("调试发动机");
    }

    public void testSteeringWheel(){
        System.out.println("调试方向盘");
    }

    public SteeringWheel getSteeringWheel() {
        return steeringWheel;
    }

    public void setSteeringWheel(SteeringWheel steeringWheel) {
        this.steeringWheel = steeringWheel;
    }

    public Tyre getTyre() {
        return tyre;
    }

    public void setTyre(Tyre tyre) {
        this.tyre = tyre;
    }

    public Motor getMotor() {
        return motor;
    }

    public void setMotor(Motor motor) {
        this.motor = motor;
    }
}

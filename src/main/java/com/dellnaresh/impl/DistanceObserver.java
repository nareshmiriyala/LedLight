package com.dellnaresh.impl;

import com.dellnaresh.ledlight.ControlledLed;
import com.dellnaresh.sensors.Buzzer;
import com.dellnaresh.sensors.Subject;

/**
 * Created by nareshm on 6/08/2015.
 */
public class DistanceObserver implements Observer {
    private double distance;
    private Subject distanceSensor;
    private ControlledLed led;
    private Buzzer buzzer;
    public DistanceObserver(Subject distanceSensor) {
        this.distanceSensor = distanceSensor;
        distanceSensor.registerObserver(this);
        led=ControlledLed.getInstance();
        buzzer=Buzzer.getInstance();
    }

    @Override
    public void update(double distance) {
        this.distance = distance;
//        display();
        lightOnOrOff();
    }

    public void lightOnOrOff(){
        if(distance<200){
            System.out.println("Distance Less than 400,Switch on light");
            led.On();
            buzzer.On();
        }else {
            System.out.println("Switch off light");
            led.off();
            buzzer.off();
        }
    }

    @Override
    public void display() {
        System.out.println("Current distance:" + distance);
    }

}

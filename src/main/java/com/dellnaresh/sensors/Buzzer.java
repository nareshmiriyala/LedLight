package com.dellnaresh.sensors;

import com.pi4j.io.gpio.*;

/**
 * Created by nareshm on 5/2/2016.
 */
public class Buzzer implements Sensor {
    // create gpio controller
    final GpioController gpio ;
    final GpioPinDigitalOutput pin;

    private static Buzzer buzzer;

    public Buzzer() {
        gpio =  GpioFactory.getInstance();;
        pin = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_28, "MyLED");
    }

    public static Buzzer getInstance() {
        if(buzzer==null){
            buzzer=new Buzzer();

        }
        return buzzer;
    }
    public void On() {

        pin.high();

        System.out.println("Buzzer should be: ON");


    }
    public void off(){
        // turn off gpio pin #01
        pin.low();
        System.out.println("Buzzer should be: OFF");
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dellnaresh.ledlight;

import com.pi4j.io.gpio.*;

/**
 *
 * @author nareshm
 */
public class ControlledLed {
    // create gpio controller
    final GpioController gpio ;
    final GpioPinDigitalOutput pin;

    private static ControlledLed controlledLed;

    public ControlledLed() {
        gpio =  GpioFactory.getInstance();;
        pin = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_29, "MyLED");
    }

    public static ControlledLed getInstance() {
        if(controlledLed==null){
            controlledLed=new ControlledLed();

        }
        return controlledLed;
    }


    
    public void On() {

        pin.high();

//        System.out.println("--> GPIO state should be: ON");
        

    }
    public void off(){
        // turn off gpio pin #01
        pin.low();
//        System.out.println("--> GPIO state should be: OFF");
    }
    
}

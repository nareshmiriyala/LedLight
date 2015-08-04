/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dellnaresh.ledlight;

import com.pi4j.io.gpio.*;
import com.pi4j.io.gpio.trigger.GpioCallbackTrigger;

import java.util.concurrent.Callable;

/**
 *
 * @author nareshm
 */
public class Controlled{

    public static void main(String[] args) throws InterruptedException {

        System.out.printf("PIR Module Test (CTRL+C to exit)\n");

        // create gpio controller
        final GpioController gpio = GpioFactory.getInstance();
        // provision gpio pin #29, (header pin 40) as an input pin with its internal pull down resistor enabled
        final GpioPinDigitalInput pir = gpio.provisionDigitalInputPin(RaspiPin.GPIO_29);
        // provision gpio pin #01 as an output pin and turn on
        final GpioPinDigitalOutput pin = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_07, "MyLED", PinState.LOW);
        System.out.printf("Ready\n");

        // create a gpio callback trigger on the gpio pin
        Callable<Void> callback = new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                System.out.println(" --> GPIO TRIGGER CALLBACK RECEIVED ");
                pin.toggle();
                return null;
            }
        };
        // create a gpio callback trigger on the PIR device pin for when it's state goes high
        pir.addTrigger(new GpioCallbackTrigger(PinState.HIGH, callback));

        // stop all GPIO activity/threads by shutting down the GPIO controller
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                System.out.println("Interrupted, stopping...\n");
                gpio.shutdown();
            }
        });

        // keep program running until user aborts (CTRL-C)
        for (;;) {
            Thread.sleep(100);
        }

    }

}

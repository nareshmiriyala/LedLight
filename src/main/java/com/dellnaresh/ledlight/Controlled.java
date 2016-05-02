package com.dellnaresh.ledlight;

import com.dellnaresh.display.LCDDisplay;
import com.dellnaresh.impl.DistanceObserver;
import com.dellnaresh.sensors.DistanceSensor;
import com.pi4j.io.gpio.*;

/**
 * DistanceMonitor class to monitor distance measured by sensor
 *
 */
public class Controlled {

    public static void main(String[] args) {

        DistanceSensor distanceSensor=new DistanceSensor(RaspiPin.GPIO_26,RaspiPin.GPIO_25);
        DistanceObserver distanceObserver = new DistanceObserver(distanceSensor);
        distanceSensor.registerObserver(distanceObserver);
        distanceSensor.senseDistance();


//        LCDDisplay lcdDisplay=new LCDDisplay();
//        try {
//            lcdDisplay.displayMessage();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
    }

}
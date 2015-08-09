package com.dellnaresh.ledlight;

import com.dellnaresh.display.LCDDisplay;
import com.dellnaresh.sensors.DistanceSensor;
import com.pi4j.io.gpio.*;

/**
 * DistanceMonitor class to monitor distance measured by sensor
 *
 */
public class Controlled {

    public static void main(String[] args) {

//        DistanceSensor distanceSensor=new DistanceSensor(RaspiPin.GPIO_29,RaspiPin.GPIO_28);
//        distanceSensor.senseDistance();
        LCDDisplay lcdDisplay=new LCDDisplay();
        try {
            lcdDisplay.displayMessage();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
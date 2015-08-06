package com.dellnaresh.sensors;

import com.dellnaresh.impl.Observer;
import com.pi4j.io.gpio.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nareshm on 6/08/2015.
 */
public class DistanceSensor implements Sensor, Subject {
    //list to hold the observers
    private List<Observer> observerList;
    private double distance;
    private final static float SOUND_SPEED = 340.29f;  // speed of sound in m/s
    private final static int TRIG_DURATION_IN_MICROS = 10; // trigger duration of 10 micro s
    private final static int WAIT_DURATION_IN_MILLIS = 60; // wait 60 milli s
    private final static int TIMEOUT = 2100;
    private final static GpioController gpio = GpioFactory.getInstance();
    private final GpioPinDigitalInput echoPinInput;
    private final GpioPinDigitalOutput triggerPinOutput;

    DistanceSensor(Pin echoPin, Pin triggerPin) {
        this.echoPinInput = gpio.provisionDigitalInputPin(echoPin);
        this.triggerPinOutput = gpio.provisionDigitalOutputPin( triggerPin );
        observerList = new ArrayList<>();
    }

    @Override
    public void registerObserver(Observer observer) {
        observerList.add(observer);
    }

    @Override
    public void removeObserver(Observer observer) {
        int index = observerList.indexOf(observer);
        if (index >= 0)
            observerList.remove(observer);
    }

    @Override
    public void notifyObservers() {
        observerList.forEach(observer -> observer.update(distance));
    }

    public void distanceChanged() {
        notifyObservers();
    }


}

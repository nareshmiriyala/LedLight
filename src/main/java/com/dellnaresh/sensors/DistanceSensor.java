package com.dellnaresh.sensors;

import com.dellnaresh.controller.DistanceController;
import com.dellnaresh.impl.Observer;
import com.dellnaresh.ledlight.Controlled;
import com.pi4j.io.gpio.*;

import java.util.ArrayList;
import java.util.List;
/**
 * Class to monitor distance measured by an HC-SR04 distance sensor on a
 * Raspberry Pi.
 *
 * Output of the program are comma separated lines
 * where the first value is the number of milliseconds since unix epoch, and the
 * second value is the measured distance in centimeters.
 * @see http://www.oracle.com/technetwork/articles/java/cruz-gpio-2295970.html
 */
public class DistanceSensor implements Sensor, Subject {
    //list to hold the observers
    private List<Observer> observerList;
    private double distance;
    private final static int WAIT_DURATION_IN_MILLIS = 60; // wait 60 milli s
    private final static GpioController gpio = GpioFactory.getInstance();
    private final GpioPinDigitalInput echoPinInput;
    private final GpioPinDigitalOutput triggerPinOutput;
    private DistanceController distanceController;

    public DistanceSensor(Pin echoPin, Pin triggerPin) {
        this.echoPinInput = gpio.provisionDigitalInputPin(echoPin);
        this.triggerPinOutput = gpio.provisionDigitalOutputPin(triggerPin);
        distanceController = new DistanceController(echoPinInput, triggerPinOutput);
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

    public void senseDistance() {
        while (true) {
            try {
                distance = distanceController.measureDistance();
            } catch (DistanceController.TimeoutException e) {
                e.printStackTrace();
            }

            try {
                Thread.sleep(WAIT_DURATION_IN_MILLIS);
            } catch (InterruptedException ex) {
                System.err.println("Interrupt during trigger");
            }
            if (distance > 0)
                notifyObservers();
        }
    }


}

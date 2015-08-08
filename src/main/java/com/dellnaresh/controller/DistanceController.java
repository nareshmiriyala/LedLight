package com.dellnaresh.controller;

import com.pi4j.io.gpio.GpioPinDigitalInput;
import com.pi4j.io.gpio.GpioPinDigitalOutput;

/**
 * Created by nareshm on 6/08/2015.
 */
public class DistanceController {
    private final static float SOUND_SPEED = 340.29f;  // speed of sound in m/s
    private final static int TRIG_DURATION_IN_MICROS = 10; // trigger duration of 10 micro s
    private final static int TIMEOUT = 2100;
    private final GpioPinDigitalInput echoPin;
    private final GpioPinDigitalOutput trigPin;

    public DistanceController(GpioPinDigitalInput echoPinInput, GpioPinDigitalOutput triggerPinOutput) {
        this.trigPin = triggerPinOutput;
        this.echoPin = echoPinInput;
        this.trigPin.low();
    }

    /*
     * This method returns the distance measured by the sensor in cm
     *
     * @throws TimeoutException if a timeout occurs
     */
    public float measureDistance() throws TimeoutException {
        this.triggerSensor();
        this.waitForSignal();
        long duration = this.measureSignal();

        return duration * SOUND_SPEED / (2 * 10000);
    }

    /**
     * Put a high on the trig pin for TRIG_DURATION_IN_MICROS
     */
    private void triggerSensor() {
        try {
            this.trigPin.high();
            Thread.sleep(0, TRIG_DURATION_IN_MICROS * 1000);
            this.trigPin.low();
        } catch (InterruptedException ex) {
            System.err.println("Interrupt during trigger");
        }
    }

    /**
     * Wait for a high on the echo pin
     */
    private void waitForSignal() throws TimeoutException {
        int countdown = TIMEOUT;

        while (this.echoPin.isLow() && countdown > 0) {
            countdown--;
        }

        if (countdown <= 0) {
            throw new TimeoutException("Timeout waiting for signal start");
        }
    }

    /**
     * @return the duration of the signal in micro seconds
     */
    private long measureSignal() throws TimeoutException {
        int countdown = TIMEOUT;
        long start = System.nanoTime();
        while (this.echoPin.isHigh() && countdown > 0) {
            countdown--;
        }
        long end = System.nanoTime();

        if (countdown <= 0) {
            throw new TimeoutException("Timeout waiting for signal end");
        }

        return (long) Math.ceil((end - start) / 1000.0);  // Return micro seconds
    }

    /**
     * Exception thrown when timeout occurs
     */
    public static class TimeoutException extends Exception {

        private final String reason;

        public TimeoutException(String reason) {
            this.reason = reason;
        }

        @Override
        public String toString() {
            return this.reason;
        }
    }
}

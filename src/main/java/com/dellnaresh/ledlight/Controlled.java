package com.dellnaresh.ledlight; /**
 * Class to monitor distance measured by an HC-SR04 distance sensor on a 
 * Raspberry Pi.
 *
 * The main method assumes the trig pin is connected to the pin # 7 and the echo
 * pin is connected to pin # 11.  Output of the program are comma separated lines
 * where the first value is the number of milliseconds since unix epoch, and the
 * second value is the measured distance in centimeters.
 * @see http://www.oracle.com/technetwork/articles/java/cruz-gpio-2295970.html
 */

import com.pi4j.io.gpio.*;

/**
 * DistanceMonitor class to monitor distance measured by sensor
 *
 * @author Rutger Claes <rutger.claes@cs.kuleuven.be>
 */
public class Controlled {

    private final static float SOUND_SPEED = 340.29f;  // speed of sound in m/s

    private final static int TRIG_DURATION_IN_MICROS = 10; // trigger duration of 10 micro s
    private final static int WAIT_DURATION_IN_MILLIS = 60; // wait 60 milli s

    private final static int TIMEOUT = 2100;

    private final static GpioController gpio = GpioFactory.getInstance();

    private final GpioPinDigitalInput echoPin;
    private final GpioPinDigitalOutput trigPin;
    private static GpioPinDigitalOutput ledPin;

    private Controlled( Pin echoPin, Pin trigPin,Pin ledPin ) {
        this.echoPin = gpio.provisionDigitalInputPin( echoPin );
        this.trigPin = gpio.provisionDigitalOutputPin( trigPin );
        // provision gpio pin #01 as an output pin and turn on
         this.ledPin= gpio.provisionDigitalOutputPin(ledPin);
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

        return duration * SOUND_SPEED / ( 2 * 10000 );
    }

    /**
     * Put a high on the trig pin for TRIG_DURATION_IN_MICROS
     */
    private void triggerSensor() {
        try {
            this.trigPin.high();
            Thread.sleep( 0, TRIG_DURATION_IN_MICROS * 1000 );
            this.trigPin.low();
        } catch (InterruptedException ex) {
            System.err.println( "Interrupt during trigger" );
        }
    }

    /**
     * Wait for a high on the echo pin
     *
     */
    private void waitForSignal() throws TimeoutException {
        int countdown = TIMEOUT;

        while( this.echoPin.isLow() && countdown > 0 ) {
            countdown--;
        }

        if( countdown <= 0 ) {
            throw new TimeoutException( "Timeout waiting for signal start" );
        }
    }

    /**
     * @return the duration of the signal in micro seconds
     */
    private long measureSignal() throws TimeoutException {
        int countdown = TIMEOUT;
        long start = System.nanoTime();
        while( this.echoPin.isHigh() && countdown > 0 ) {
            countdown--;
        }
        long end = System.nanoTime();

        if( countdown <= 0 ) {
            throw new TimeoutException( "Timeout waiting for signal end" );
        }

        return (long)Math.ceil( ( end - start ) / 1000.0 );  // Return micro seconds
    }

    public static void main(String[] args) {
        Pin echoPin = RaspiPin.GPIO_28;
        Pin trigPin = RaspiPin.GPIO_29;
        Pin ledPinId = RaspiPin.GPIO_07;
        Controlled monitor = new Controlled( echoPin, trigPin,ledPinId );

        while( true ) {
            try {
                float measureDistance = monitor.measureDistance();
                System.out.printf( "%1$d,%2$.3f%n", System.currentTimeMillis(), measureDistance );
                if(measureDistance>200){
                    ledPin.high();
                }else {
                    ledPin.low();
                }
            }
            catch( TimeoutException e ) {
                System.err.println( e );
            }

            try {
                Thread.sleep( WAIT_DURATION_IN_MILLIS );
            } catch (InterruptedException ex) {
                System.err.println( "Interrupt during trigger" );
            }
        }
    }

    /**
     * Exception thrown when timeout occurs
     */
    private static class TimeoutException extends Exception {

        private final String reason;

        public TimeoutException( String reason ) {
            this.reason = reason;
        }

        @Override
        public String toString() {
            return this.reason;
        }
    }

}
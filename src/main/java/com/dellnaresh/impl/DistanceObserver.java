package com.dellnaresh.impl;

import com.dellnaresh.sensors.Subject;

/**
 * Created by nareshm on 6/08/2015.
 */
public class DistanceObserver implements Observer {
    private double distance;
    private Subject distanceSensor;

    public DistanceObserver(Subject distanceSensor) {
        this.distanceSensor = distanceSensor;
        distanceSensor.registerObserver(this);
    }

    @Override
    public void update(double distance) {
        this.distance = distance;
    }

    @Override
    public void display() {
        System.out.println("Current distance:" + distance);
    }

}

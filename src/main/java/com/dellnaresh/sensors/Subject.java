package com.dellnaresh.sensors;

import com.dellnaresh.impl.Observer;

/**
 * Created by nareshm on 6/08/2015.
 */
public interface Subject {
    void registerObserver(Observer observer);

    void removeObserver(Observer observer);

    void notifyObservers();

}

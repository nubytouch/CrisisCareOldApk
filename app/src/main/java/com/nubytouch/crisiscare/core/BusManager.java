package com.nubytouch.crisiscare.core;

import com.squareup.otto.Bus;
import com.squareup.otto.ThreadEnforcer;

public class BusManager
{
    private final  Bus        busMainThread;
    private final  Bus        busAnyThread;
    private static BusManager instance;

    private BusManager()
    {
        busMainThread = new Bus(ThreadEnforcer.MAIN);
        busAnyThread = new Bus(ThreadEnforcer.ANY);
    }

    public static BusManager getInstance()
    {
        if (instance == null)
            instance = new BusManager();

        return instance;
    }

    public void postOnMainThread(Object object)
    {
        busMainThread.post(object);
    }

    public void registerOnMainThread(Object object)
    {
        busMainThread.register(object);
    }

    public void unregisterFromMainThread(Object object)
    {
        busMainThread.unregister(object);
    }

    public void postOnAnyThread(Object object)
    {
        busAnyThread.post(object);
    }

    public void registerOnAnyThread(Object object)
    {
        busAnyThread.register(object);
    }

    public void unregisterFromAnyThread(Object object)
    {
        busAnyThread.unregister(object);
    }
}

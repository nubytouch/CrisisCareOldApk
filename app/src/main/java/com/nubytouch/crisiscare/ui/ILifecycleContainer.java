package com.nubytouch.crisiscare.ui;


public interface ILifecycleContainer
{
    void addListener(ILifecyleListener listener);
    void removeListener(ILifecyleListener listener);
}

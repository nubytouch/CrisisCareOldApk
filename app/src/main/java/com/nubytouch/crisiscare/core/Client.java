package com.nubytouch.crisiscare.core;

import androidx.annotation.ColorInt;

public class Client
{
    public final String id;
    public final String name;
    public final String serverUrl;
    public final String logoUrl;
    @ColorInt
    public final int    primaryColor;


    Client(String id, String name, String serverUrl, String logoUrl, int primaryColor)
    {
        this.id = id;
        this.name = name;
        this.serverUrl = serverUrl;
        this.logoUrl = logoUrl;
        this.primaryColor = primaryColor;
    }
}

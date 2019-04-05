package com.nubytouch.crisiscare.rest;

import com.google.gson.annotations.SerializedName;
import com.nubytouch.crisiscare.core.Session;

public class Credentials
{
    @SerializedName("username")
    public final String username;

    public Credentials()
    {
        this.username = Session.getUser().getEmail();
    }
}

package com.nubytouch.crisiscare.rest;

import com.nubytouch.crisiscare.BuildConfig;

import androidx.annotation.StringDef;

public class Server
{
    public static final String CRISIS_CARE_SERVER = BuildConfig.MAIN_SERVER;
    public static final String NUBYTOUCH_SERVER = BuildConfig.NUBY_SERVER;
    public static final String AUTH_SERVER = BuildConfig.AUTH_SERVER;

    @StringDef({CRISIS_CARE_SERVER, NUBYTOUCH_SERVER, AUTH_SERVER})
    public @interface Server_Endpoint {}
}

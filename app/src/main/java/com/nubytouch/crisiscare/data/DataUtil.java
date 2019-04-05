package com.nubytouch.crisiscare.data;


public class DataUtil
{
    public static String generateUUID()
    {
        return "-" + String.valueOf(Math.round(Math.random() * 10000000));
    }

    public static String getOriginalUUID(String generatedUUID) {
        return generatedUUID.substring(0, generatedUUID.lastIndexOf("-"));
    }
}

package com.nubytouch.crisiscare.utils;


import android.content.Context;

import com.nubytouch.crisiscare.R;

public class LocationUtil
{
    public static String formatDistance(Context context, float distance, int round)
    {
        if (round < 0)
            round = 0;

        String format = "%." + round + "f %s";

        if (distance >= 1000)
            return String.format(format, distance / 1000f, context.getString(R.string.distance_km));

        return String.format(format, distance, context.getString(R.string.distance_m));
    }
}

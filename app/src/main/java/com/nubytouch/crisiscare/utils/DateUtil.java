package com.nubytouch.crisiscare.utils;

import android.text.format.DateUtils;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class DateUtil
{
    public static long getTimestampFromDotNetDate(String dotNetDate)
    {
        // "/Date(1385305542546+0100)/"
        int start = dotNetDate.indexOf('(') + 1;
        int end   = dotNetDate.indexOf('+');

        if (end < 0)
            end = dotNetDate.indexOf(')');

        try
        {
            String timestamp = dotNetDate.substring(start, end);
            return Long.parseLong(timestamp);
        }
        catch (Exception ex)
        {
        }

        return 0;
    }

    public static String getDotNetDateFromTimestamp(long timeMillis)
    {
        // A formatter that prints the timezone offset
        SimpleDateFormat df = new SimpleDateFormat("Z");

        // Current date+time in system default timezone.
        Calendar now = Calendar.getInstance();
        now.setTimeInMillis(timeMillis);

        // Don't forget this if you use a timezone other than system default:
        df.setTimeZone(now.getTimeZone());

        // Create the result
        return "/Date(" + now.getTimeInMillis() + df.format(now.getTime()) + ")/";
    }

    public static String getFormattedDateSmall(long timeStamp)
    {
        if (timeStamp > 0)
        {
            // ToDO define language declined formats in strings.xml
            SimpleDateFormat format = new SimpleDateFormat(DateUtils.isToday(timeStamp) ? "HH:mm" : "dd/MM HH:mm");
            return format.format(timeStamp);
        }

        return "";
    }

    public static String getFormattedDatePublication(long timeStamp)
    {
        if (timeStamp > 0)
        {
            String format;

            // ToDO define language declined formats in strings.xml
            if (DateUtils.isToday(timeStamp))
                format = "'Aujourd''hui à 'HH:mm";
            else if (isYesterday(timeStamp))
                format = "'Hier à 'HH:mm";
            else
                format = "EEEE dd MMMM' à 'HH:mm";

            return new SimpleDateFormat(format).format(timeStamp);
        }

        return "";
    }

    public static boolean isYesterday(long timeStamp)
    {
        Calendar c1 = Calendar.getInstance(); // today
        c1.add(Calendar.DAY_OF_YEAR, -1); // yesterday

        Calendar c2 = Calendar.getInstance();
        c2.setTimeInMillis(timeStamp); // your date

        return c1.get(Calendar.YEAR) == c2.get(Calendar.YEAR)
               && c1.get(Calendar.DAY_OF_YEAR) == c2.get(Calendar.DAY_OF_YEAR);
    }

    public static String addZeroIfNecessary(int timePart)
    {
        if (timePart < 10)
            return "0" + timePart;

        return "" + timePart;
    }

    public static boolean isSameDay(Calendar c, Calendar c2)
    {
        return c.get(Calendar.YEAR) == c2.get(Calendar.YEAR)
               && c.get(Calendar.DAY_OF_YEAR) == c2.get(Calendar.DAY_OF_YEAR);
    }
}

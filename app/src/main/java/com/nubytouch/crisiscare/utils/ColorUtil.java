package com.nubytouch.crisiscare.utils;

import android.graphics.Color;
import androidx.annotation.ColorInt;
import androidx.core.graphics.ColorUtils;

import timber.log.Timber;

public class ColorUtil
{
    @ColorInt
    public static int parseColor(String color, @ColorInt int defaultColor)
    {
        if (color != null && !color.isEmpty())
        {
            try
            {
                int parsedColor = Color.parseColor(color);
                parsedColor = ColorUtils.setAlphaComponent(parsedColor, 255);
                return parsedColor;
            }
            catch (IllegalArgumentException e)
            {
                Timber.d("error parsing client color %s", e.getMessage());
            }
        }

        return defaultColor;
    }

    public static int lighten(int color, double fraction)
    {
        int red   = Color.red(color);
        int green = Color.green(color);
        int blue  = Color.blue(color);
        int alpha = Color.alpha(color);

        red = lightenColor(red, fraction);
        green = lightenColor(green, fraction);
        blue = lightenColor(blue, fraction);

        return Color.argb(alpha, red, green, blue);
    }

    public static int darken(int color, double fraction)
    {
        int red   = Color.red(color);
        int green = Color.green(color);
        int blue  = Color.blue(color);
        int alpha = Color.alpha(color);

        red = darkenColor(red, fraction);
        green = darkenColor(green, fraction);
        blue = darkenColor(blue, fraction);

        return Color.argb(alpha, red, green, blue);
    }

    private static int darkenColor(int color, double fraction)
    {
        return (int) Math.max(color - (color * fraction), 0);
    }

    private static int lightenColor(int color, double fraction)
    {
        return (int) Math.min(color + (color * fraction), 255);
    }

    public static boolean isColorDark(int color)
    {
        double darkness = 1 - (0.299 * Color.red(color) + 0.587 * Color.green(color) + 0.114 * Color.blue(color)) / 255;

        return darkness > 0.5;
    }
}

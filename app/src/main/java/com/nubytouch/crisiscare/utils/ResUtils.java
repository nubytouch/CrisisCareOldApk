package com.nubytouch.crisiscare.utils;


import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import androidx.annotation.ArrayRes;
import androidx.annotation.BoolRes;
import androidx.annotation.ColorRes;
import androidx.annotation.DimenRes;
import androidx.annotation.DrawableRes;
import androidx.annotation.StringRes;
import androidx.core.content.ContextCompat;

import com.nubytouch.crisiscare.CrisisCare;

public class ResUtils
{
    private static Context getContext()
    {
        return CrisisCare.getInstance();
    }

    private static Resources getRes()
    {
        return CrisisCare.getInstance().getResources();
    }

    public static float getDimension(@DimenRes int resId)
    {
        return getRes().getDimension(resId);
    }

    public static int getDimensionPxSize(@DimenRes int resId)
    {
        return getRes().getDimensionPixelSize(resId);
    }

    public static boolean getBooleanString(@BoolRes int resId)
    {
        return getRes().getBoolean(resId);
    }

    public static String getString(@StringRes int resId)
    {
        return getRes().getString(resId);
    }

    public static String getString(@StringRes int resId, Object... args)
    {
        return getRes().getString(resId, args);
    }

    public static String[] getStringArray(@ArrayRes int resId)
    {
        return getRes().getStringArray(resId);
    }

    public static int[] getIntArray(@ArrayRes int resId)
    {
        return getRes().getIntArray(resId);
    }

    public static int getColor(@ColorRes int resId)
    {
        return ContextCompat.getColor(getContext(), resId);
    }

    public static Drawable getDrawable(@DrawableRes int resId)
    {
        return ContextCompat.getDrawable(getContext(), resId);
    }

    public static AssetManager getAssetManager(@ArrayRes int resId)
    {
        return getRes().getAssets();
    }
}

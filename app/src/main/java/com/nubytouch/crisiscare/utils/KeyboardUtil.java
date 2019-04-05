package com.nubytouch.crisiscare.utils;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.nubytouch.crisiscare.CrisisCare;

public class KeyboardUtil
{
    private KeyboardUtil()
    {

    }

    public static void hideKeyboard(Activity activity)
    {
        hideKeyboard(activity.getCurrentFocus());
    }

    public static void hideKeyboard(View view)
    {
        if (view != null)
        {
            InputMethodManager imm = (InputMethodManager) CrisisCare.getInstance()
                    .getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public static void showKeyboard(View view)
    {
        if (view != null)
        {
            InputMethodManager imm = (InputMethodManager) CrisisCare.getInstance()
                    .getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
        }
    }
}

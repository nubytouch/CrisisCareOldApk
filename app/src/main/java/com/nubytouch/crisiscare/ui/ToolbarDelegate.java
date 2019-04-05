package com.nubytouch.crisiscare.ui;

import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import androidx.annotation.DrawableRes;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.View;

import com.nubytouch.crisiscare.R;
import com.nubytouch.crisiscare.core.Session;

import java.lang.ref.WeakReference;

public class ToolbarDelegate
{
    private WeakReference<AppCompatActivity> activity;

    public ToolbarDelegate(AppCompatActivity activity)
    {
        this.activity = new WeakReference<>(activity);
    }

    public ToolbarDelegate setup(Toolbar toolbar, boolean main)
    {
        AppCompatActivity activity = this.activity.get();

        if (activity != null)
        {
            activity.setSupportActionBar(toolbar);
            activity.getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Session.getPrimaryColor()));

            if (main)
                activity.getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_drawer);
            else
                activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        toolbar.setNavigationOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                onNavigationClick();
            }
        });

        return this;
    }

    public ToolbarDelegate setup(Toolbar toolbar, boolean main, @StringRes int title)
    {
        setup(toolbar, main);

        AppCompatActivity activity = this.activity.get();

        if (activity != null)
        {
            activity.getSupportActionBar().setTitle(title);
        }

        return this;
    }

    public ToolbarDelegate setup(Toolbar toolbar, boolean main, String title)
    {
        setup(toolbar, main);

        AppCompatActivity activity = this.activity.get();

        if (activity != null)
        {
            activity.getSupportActionBar().setTitle(title);
        }

        return this;
    }

    public ToolbarDelegate setup(boolean main)
    {
        AppCompatActivity activity = this.activity.get();

        if (activity != null)
        {
            activity.getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Session.getPrimaryColor()));

            if (main)
                activity.getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_drawer);
            else
                activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        return this;
    }

    public ToolbarDelegate setup(boolean main, @StringRes int title)
    {
        setup(main);

        AppCompatActivity activity = this.activity.get();

        if (activity != null)
            activity.getSupportActionBar().setTitle(title);

        return this;
    }

    public ToolbarDelegate setup(boolean main, String title)
    {
        setup(main);

        AppCompatActivity activity = this.activity.get();

        if (activity != null)
            activity.getSupportActionBar().setTitle(title);

        return this;
    }

    public ToolbarDelegate setIcon(@DrawableRes int icon)
    {
        AppCompatActivity activity = this.activity.get();

        if (activity != null)
            activity.getSupportActionBar().setHomeAsUpIndicator(icon);

        return this;
    }

    private void onNavigationClick()
    {
        Activity activity = this.activity.get();

        if (activity != null)
            activity.finish();
    }
}

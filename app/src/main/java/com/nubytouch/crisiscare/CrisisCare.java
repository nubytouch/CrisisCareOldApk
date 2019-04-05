package com.nubytouch.crisiscare;

import android.content.Intent;

import com.crashlytics.android.Crashlytics;
import com.nubytouch.crisiscare.core.BusManager;
import com.nubytouch.crisiscare.job.JobManager;
import com.nubytouch.crisiscare.job.LogoutJob;
import com.nubytouch.crisiscare.ui.login.LoginActivity;

import androidx.multidex.MultiDexApplication;
import io.fabric.sdk.android.Fabric;
import timber.log.Timber;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

public class CrisisCare extends MultiDexApplication
{
    private static CrisisCare instance;

    @Override
    public void onCreate()
    {
        super.onCreate();

        instance = this;

        if (BuildConfig.DEBUG)
            Timber.plant(new Timber.DebugTree());

        if (!BuildConfig.DEBUG)
            Fabric.with(this, new Crashlytics());

        // ___ Database

        JobManager.init(this);

        BusManager.getInstance().registerOnMainThread(this);

        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                                              .setDefaultFontPath("fonts/Lato-Regular.ttf")
                                              .setFontAttrId(R.attr.fontPath)
                                              .build());
    }

    public static CrisisCare getInstance()
    {
        return instance;
    }

    public void logout()
    {
        JobManager.addJobInBackground(new LogoutJob(null));

        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}

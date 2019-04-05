package com.nubytouch.crisiscare.push;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import timber.log.Timber;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;
import com.nubytouch.crisiscare.CrisisCare;
import com.nubytouch.crisiscare.R;

public class PushManager
{
    private static final String TAG = "PushManager";

    private static final String PUSH_KEY = "gcm";

    public static void register()
    {
        Intent intent = new Intent(CrisisCare.getInstance(), RegistrationIntentService.class);
        CrisisCare.getInstance().startService(intent);
    }

    public static void unregister()
    {

    }

    static void setRegistered(boolean registered)
    {
        getSharedPreferences().edit().putBoolean(PUSH_KEY, registered).apply();
    }

    public static boolean isAlreadyRegistered()
    {
        return getSharedPreferences().getBoolean(PUSH_KEY, false);
    }

    private static SharedPreferences getSharedPreferences()
    {
        return PreferenceManager.getDefaultSharedPreferences(CrisisCare.getInstance());
    }

    /**
     * Do not call on main thread
     * @return the GCM InstanceId token
     */
    public static String getToken()
    {
        Timber.d("getToken: ");
        String token = null;

        try
        {
            InstanceID instanceID = InstanceID.getInstance(CrisisCare.getInstance());
            token = instanceID.getToken(CrisisCare.getInstance().getString(R.string.gcm_defaultSenderId),
                                               GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);
        }
        catch (Exception e)
        {
            Timber.d("Failed to complete token refresh", e);
        }

        return token;
    }
}

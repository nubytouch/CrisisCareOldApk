package com.nubytouch.crisiscare.core;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.nubytouch.crisiscare.CrisisCare;
import com.nubytouch.crisiscare.data.model.User;

import timber.log.Timber;

class SessionDataManager
{
    private static final String CLIENT                = "com.nubytouch.crisiscare.client";
    private static final String USER                  = "com.nubytouch.crisiscare.user";
    private static final String USER_LOGIN            = "com.nubytouch.crisiscare.userLoging";

    private static Client              client;
    private static User                user;

    static Client getClient()
    {
        if (client == null)
        {
            // initialize client with previously saved data
            String json = getString(CLIENT, null);

            if (json != null)
            {
                try
                {
                    client = new Gson().fromJson(json, Client.class);
                }
                catch (Exception e)
                {
                    Timber.d("SessionDataManager errror %s", e);
                }
            }
        }

        return client;
    }

    static void saveClient(Client client)
    {
        saveString(CLIENT, new Gson().toJson(client));
        SessionDataManager.client = client;
    }

    static User getUser()
    {
        if (user == null)
        {
            // initialize user with previously saved data
            String json = getString(USER, null);

            if (json != null)
            {
                try
                {
                    user = new Gson().fromJson(json, User.class);
                }
                catch (Exception e)
                {
                    Timber.d("SessionDataManager errror %s", e);
                }
            }
        }

        return user;
    }

    static void saveUser(User user)
    {
        saveString(USER, new Gson().toJson(user));

        SessionDataManager.user = null;
    }

    static void clear()
    {
        user = null;
    }

    static void setUserLogin(String login)
    {
        saveString(USER_LOGIN, login);
    }

    static String getUserLogin()
    {
        return getString(USER_LOGIN, "");
    }
    
    private static SharedPreferences getSharedPrefs()
    {
        return CrisisCare.getInstance().getSharedPreferences("HAWK", Context.MODE_PRIVATE);
    }

    private static String getString(String key, String defaultValue)
    {
        return getSharedPrefs().getString(key, defaultValue);
    }
    
    private static void saveString(String key, String value)
    {
        getSharedPrefs().edit().putString(key, value).commit();
    }

    private static void deleteKey(String key)
    {
        getSharedPrefs().edit().remove(key).commit();

    }
}

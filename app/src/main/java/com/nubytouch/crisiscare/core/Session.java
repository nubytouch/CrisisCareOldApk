package com.nubytouch.crisiscare.core;

import com.nubytouch.crisiscare.BuildConfig;
import com.nubytouch.crisiscare.data.model.User;

import androidx.annotation.ColorInt;

public class Session
{
    private Session()
    {
        //initialize stuff
    }

    //___ Client services ___

    public static Client getClient()
    {
        return SessionDataManager.getClient();
    }

    public static void setClientData(String id, String name, String serverUrl, String logoUrl, @ColorInt int
            primaryColor)
    {
        Client client = new Client(id, name, serverUrl, logoUrl, primaryColor);
        SessionDataManager.saveClient(client);
    }

    public static String getServerURL()
    {
        Client client = getClient();
        return client.serverUrl + BuildConfig.CLIENT_SERVICE;
    }

    public static String getAuthenticateServerURL()
    {
        Client client = getClient();
        return client.serverUrl + BuildConfig.AUTH_SERVICE;
    }

    public static String getClientLogoURL()
    {
        Client client = getClient();
        return client.serverUrl + "/" + client.logoUrl;
    }

    public static User getUser()
    {
        return SessionDataManager.getUser();
    }

    //___ User services ___
    public static void setUser(User user)
    {
        SessionDataManager.saveUser(user);
    }

    public static int getPrimaryColor() {
        return getClient().primaryColor;
    }

    public static boolean isLoggedIn()
    {
        return getUser() != null;
    }

    // User is being redirected to login screen
    // Clear all data
    public static void logout()
    {
        SessionDataManager.clear();
    }

    public static void setUserLogin(String login)
    {
        SessionDataManager.setUserLogin(login);
    }

    public static String getUserLogin()
    {
        return SessionDataManager.getUserLogin();
    }
}

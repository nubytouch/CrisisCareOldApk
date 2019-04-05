package com.nubytouch.crisiscare.ui.slashscreen;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import com.nubytouch.crisiscare.R;
import com.nubytouch.crisiscare.core.Session;
import com.nubytouch.crisiscare.ui.CCActivity;
import com.nubytouch.crisiscare.ui.activation.ActivationActivity;
import com.nubytouch.crisiscare.ui.home.HomeActivity;
import com.nubytouch.crisiscare.ui.login.LoginActivity;

public class SplashscreenActivity extends CCActivity
{
    private static final String GOOD_POLICY_KEY    = "crisiscare_activation_code";
    private static final String GOOD_USER_PASSWORD = "8c8f1a4bc75642ad82e543d8ce3e4076";

    private String         goodUserId;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_splashscreen);

        if (Session.isLoggedIn())
        {
            openHomeScreen();
        }
        else
        {
            if (Session.getClient() != null)
                openLoginScreen();
            else
                openActivateScreen();
        }
    }

    private void openActivateScreen()
    {
        openActivity(ActivationActivity.class);
    }

    private void openLoginScreen()
    {
        openActivity(LoginActivity.class);
    }

    private void openHomeScreen()
    {
        openActivity(HomeActivity.class);
    }

    private void openActivity(Class clazz)
    {
        startActivity(new Intent(this, clazz));
        finish();
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();

        if (progressDialog != null)
        {
            progressDialog.dismiss();
            progressDialog = null;
        }
    }
}

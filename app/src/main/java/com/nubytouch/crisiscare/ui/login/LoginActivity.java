package com.nubytouch.crisiscare.ui.login;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import androidx.browser.customtabs.CustomTabsIntent;
import com.google.android.material.textfield.TextInputEditText;
import androidx.core.view.GestureDetectorCompat;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.AppCompatEditText;
import timber.log.Timber;

import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.nubytouch.crisiscare.BuildConfig;
import com.nubytouch.crisiscare.R;
import com.nubytouch.crisiscare.core.Session;
import com.nubytouch.crisiscare.image.ImageLoader;
import com.nubytouch.crisiscare.job.JobManager;
import com.nubytouch.crisiscare.ui.JobActivity;
import com.nubytouch.crisiscare.ui.activation.ActivationActivity;
import com.nubytouch.crisiscare.ui.home.HomeActivity;
import com.nubytouch.crisiscare.ui.login.job.LoginEvent;
import com.nubytouch.crisiscare.ui.login.job.LoginJob;
import com.nubytouch.crisiscare.utils.KeyboardUtil;
import com.squareup.otto.Subscribe;

public class LoginActivity extends JobActivity
{
    static
    {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    private TextInputEditText login;
    private AppCompatEditText password;
    private ProgressBar       progressBar;
    private Button            button;

    private GestureDetectorCompat gestureDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE,
                             WindowManager.LayoutParams.FLAG_SECURE);

        setContentView(R.layout.activity_login);

        login = (TextInputEditText) findViewById(R.id.txtLogin);
        password = (AppCompatEditText) findViewById(R.id.txtPassword);
        ImageView logo = (ImageView) findViewById(R.id.logo);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        button = (Button) findViewById(R.id.btLogin);
        Button forgotPasswordButton = (Button) findViewById(R.id.forgot_password_button);

        button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                login();
            }
        });

        forgotPasswordButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
                builder.setToolbarColor(Session.getPrimaryColor());
                builder.setCloseButtonIcon(BitmapFactory.decodeResource(getResources(), R.drawable.ic_arrow_left));
                builder.setShowTitle(true);

                CustomTabsIntent customTabsIntent = builder.build();

                String url = Session.getClient().serverUrl + BuildConfig.FORGOTTEN_PWD_PAGE;

                if (login.length() > 0)
                    url += "?email=" + login.getText().toString();

                customTabsIntent.launchUrl(LoginActivity.this, Uri.parse(url));
            }
        });

        ImageLoader.fadeLoad(Session.getClientLogoURL(), logo);

        final int SWIPE_THRESHOLD          = Math.round(getResources().getDisplayMetrics().widthPixels / 2);
        final int SWIPE_VELOCITY_THRESHOLD = 1;
        gestureDetector = new GestureDetectorCompat(this, new GestureDetector
                .SimpleOnGestureListener()
        {
            @Override
            public boolean onDown(MotionEvent e)
            {
                return true;
            }

            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY)
            {
                boolean result = false;

                try
                {
                    float diffY = e2.getY() - e1.getY();
                    float diffX = e2.getX() - e1.getX();
                    if (Math.abs(diffX) > Math.abs(diffY))
                    {
                        if (Math.abs(diffX) > SWIPE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD)
                        {
                            onSwipe();
                        }

                        result = true;
                    }
                    result = true;

                }
                catch (Exception exception)
                {
                    exception.printStackTrace();
                }

                return result;
            }
        });

        /*if (BuildConfig.DEBUG)
        {
            login.setText("jonathan.gerbaud@abewy.com");
            password.setText("75b6e-bb28f-9e417");
        }*/
    }

    @Override
    protected void onPause()
    {
        super.onPause();

        if (login != null)
            Session.setUserLogin(login.getText().toString());
    }

    @Override
    protected void onResume()
    {
        super.onResume();

        if (login != null)
            login.setText(Session.getUserLogin());
    }

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        return gestureDetector.onTouchEvent(event);
    }

    private void login()
    {
        if (login.length() == 0)
            Toast.makeText(this, R.string.error_please_enter_login, Toast.LENGTH_SHORT).show();
        else if (password.length() == 0)
            Toast.makeText(this, R.string.error_please_enter_password, Toast.LENGTH_SHORT).show();
        else
            tryLogin();
    }

    private void tryLogin()
    {
        KeyboardUtil.hideKeyboard(this);

        progressBar.setVisibility(View.VISIBLE);
        button.setVisibility(View.GONE);

        JobManager.addJobInBackground(new LoginJob(login.getText().toString(), password.getText().toString()));
    }

    @Subscribe
    public void onLoginEvent(LoginEvent event)
    {
        if (event.isSuccess())
        {
            Session.setUser(event.getData().data.buildUser());

            login.setText("");
            Session.setUserLogin(null); // Clear user login so that it won't be preset when user comes back

            startActivity(new Intent(this, HomeActivity.class));

            finish();
        }
        else
        {
            Toast.makeText(this, R.string.echec_authentification, Toast.LENGTH_SHORT).show();

            progressBar.setVisibility(View.GONE);
            button.setVisibility(View.VISIBLE);
        }
    }

    private void onSwipe()
    {
        startActivity(new Intent(this, ActivationActivity.class));
        finish();
    }
}

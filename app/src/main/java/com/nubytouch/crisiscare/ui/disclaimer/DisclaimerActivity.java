package com.nubytouch.crisiscare.ui.disclaimer;

import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.appcompat.app.AppCompatDelegate;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import com.nubytouch.crisiscare.BuildConfig;
import com.nubytouch.crisiscare.R;
import com.nubytouch.crisiscare.core.Session;
import com.nubytouch.crisiscare.job.JobManager;
import com.nubytouch.crisiscare.ui.disclaimer.job.AcceptDisclaimerEvent;
import com.nubytouch.crisiscare.ui.disclaimer.job.AcceptDisclaimerJob;
import com.nubytouch.crisiscare.ui.web.BrowserActivity;
import com.squareup.otto.Subscribe;

public class DisclaimerActivity extends BrowserActivity
{
    private static final String TAG = "DisclaimerActivity";

    private static final String FILE = "infos.html";

    private ProgressBar progressBar;
    private Button      button;

    static
    {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    @Override
    protected int getContentView()
    {
        return R.layout.activity_disclaimer;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        getSupportActionBar().setTitle(R.string.disclaimer_title);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        button = (Button) findViewById(R.id.accept_button);
        button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                acceptDisclaimer();
            }
        });

        View buttonLayout = findViewById(R.id.button_layout);
        buttonLayout.setBackgroundColor(Session.getPrimaryColor());

        progressBar = (ProgressBar) findViewById(R.id.accept_progressbar);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {

            Drawable wrapDrawable = DrawableCompat.wrap(progressBar.getIndeterminateDrawable());
            DrawableCompat.setTint(wrapDrawable, ContextCompat.getColor(this, android.R.color.white));
            progressBar.setIndeterminateDrawable(DrawableCompat.unwrap(wrapDrawable));
        } else {
            progressBar.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(this, android.R.color
                    .white), PorterDuff.Mode.SRC_IN);
        }

        uri = BuildConfig.DISCLAIMER_URL;
        loadContent();
    }

    @Override
    public void onBackPressed()
    {
        // Prevent going away
    }

    private void acceptDisclaimer()
    {
        button.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);

        JobManager.addJobInBackground(new AcceptDisclaimerJob());
    }
    @Subscribe
    public void onActivationEvent(AcceptDisclaimerEvent event)
    {
        // No success/error data...
        finish();
    }
}

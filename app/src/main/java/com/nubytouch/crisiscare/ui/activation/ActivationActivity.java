package com.nubytouch.crisiscare.ui.activation;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatDelegate;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.marlonmafra.android.widget.EditTextPassword;
import com.nubytouch.crisiscare.R;
import com.nubytouch.crisiscare.core.Session;
import com.nubytouch.crisiscare.job.ErrorType;
import com.nubytouch.crisiscare.job.JobManager;
import com.nubytouch.crisiscare.ui.JobActivity;
import com.nubytouch.crisiscare.ui.activation.job.ActivateJob;
import com.nubytouch.crisiscare.ui.activation.job.ActivationEvent;
import com.nubytouch.crisiscare.ui.activation.rest.dto.ClientInfoDTO;
import com.nubytouch.crisiscare.ui.login.LoginActivity;
import com.nubytouch.crisiscare.utils.ColorUtil;
import com.squareup.otto.Subscribe;

public class ActivationActivity extends JobActivity
{
    private static final String TAG = "ActivationActivity";

    private EditText    txtActivationCode;
    private Button      button;
    private ProgressBar progressBar;

    static
    {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_activation);

        txtActivationCode = (EditTextPassword) findViewById(R.id.txtActivationCode);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        button = (Button) findViewById(R.id.btActivate);
        button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                activate();
            }
        });

        Session.setUserLogin(null); // Clear user login in case it was still saved
    }

    private void activate()
    {
        String code = txtActivationCode.getText().toString();

        if (!code.isEmpty())
        {
            button.setVisibility(View.GONE);
            progressBar.setVisibility(View.VISIBLE);
            txtActivationCode.setEnabled(false);
            JobManager.addJobInBackground(new ActivateJob(code));
        }
        else
            Toast.makeText(this, R.string.please_input_activation_code, Toast.LENGTH_SHORT).show();
    }

    @Subscribe
    public void onActivationEvent(ActivationEvent event)
    {
        if (event.isSuccess() && event.getData() != null)
        {
            ClientInfoDTO data = event.getData();

            int color = ColorUtil.parseColor(data.dominantColor, 0xFF000000);
            Session.setClientData(data.id, data.name, data.rootUrl, data.logoPath, color);

            Intent intent = new Intent(this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);

            finish();
        }
        else
        {
            button.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);
            txtActivationCode.setEnabled(true);

            int msg = event.getErrorType() == ErrorType.NETWORK_TIMEOUT
                      ? R.string.votre_connexion_internet_ne_semble_pas_active_
                      : R.string.error_activation_code;

            Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
        }
    }
}

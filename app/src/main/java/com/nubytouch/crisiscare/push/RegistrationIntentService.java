package com.nubytouch.crisiscare.push;

import android.app.IntentService;
import android.content.Intent;
import timber.log.Timber;

import com.nubytouch.crisiscare.job.JobManager;
import com.nubytouch.crisiscare.push.job.RegisterPushJob;

public class RegistrationIntentService extends IntentService
{
    private static final String TAG = "RegIntentService";

    public RegistrationIntentService()
    {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent)
    {
        Timber.d("onHandleIntent: " + intent);
        if (!PushManager.isAlreadyRegistered())
        {
            String token = PushManager.getToken();

            Timber.d("GCM Registration Token: " + token);

            if (token != null)
            {
                PushManager.setRegistered(true);
                sendRegistrationToServer(token);
            }
            else
            {
                PushManager.setRegistered(false);
            }
        }
    }

    /**
     * Persist registration to third-party servers.
     * <p/>
     * Modify this method to associate the user's GCM registration token with any server-side account
     * maintained by your application.
     *
     * @param token The new token.
     */
    private void sendRegistrationToServer(String token)
    {
        JobManager.addJobInBackground(new RegisterPushJob(token));
    }
}

package com.nubytouch.crisiscare.push;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import timber.log.Timber;

import com.google.android.gms.iid.InstanceID;
import com.google.android.gms.iid.InstanceIDListenerService;

public class CCInstanceIDListenerService extends InstanceIDListenerService {

    private static final String TAG = "MyInstanceIDLS";

    /**
     * Called if InstanceID token is updated. This may occur if the security of
     * the previous token had been compromised. This call is initiated by the
     * InstanceID provider.
     */
    @Override
    public void onTokenRefresh() {
        // Fetch updated Instance ID token and notify our app's server of any changes (if applicable).
        Timber.d("onTokenRefresh: ");
        Intent intent = new Intent(this, RegistrationIntentService.class);
        startService(intent);
    }
}

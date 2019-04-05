package com.nubytouch.crisiscare.ui.home;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import androidx.fragment.app.FragmentManager;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDelegate;
import android.view.View;
import android.widget.AdapterViewFlipper;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.novoda.merlin.MerlinsBeard;
import com.nubytouch.crisiscare.R;
import com.nubytouch.crisiscare.core.BusManager;
import com.nubytouch.crisiscare.core.Session;
import com.nubytouch.crisiscare.data.model.Alert;
import com.nubytouch.crisiscare.datapackage.DataPackageManager;
import com.nubytouch.crisiscare.datapackage.job.DataPackageEvent;
import com.nubytouch.crisiscare.datapackage.job.DataPackageJob;
import com.nubytouch.crisiscare.datapackage.job.DataPackageProgressEvent;
import com.nubytouch.crisiscare.job.JobManager;
import com.nubytouch.crisiscare.push.PushManager;
import com.nubytouch.crisiscare.ui.CCActivity;
import com.nubytouch.crisiscare.ui.ToolbarDelegate;
import com.nubytouch.crisiscare.ui.alerts.job.AlertEvent;
import com.nubytouch.crisiscare.ui.alerts.job.AlertJob;
import com.nubytouch.crisiscare.ui.disclaimer.DisclaimerActivity;
import com.nubytouch.crisiscare.ui.home.job.CheckVersionEvent;
import com.nubytouch.crisiscare.ui.home.job.CheckVersionJob;
import com.nubytouch.crisiscare.ui.login.LoginActivity;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import timber.log.Timber;

public class HomeActivity extends CCActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks
{
    private static final String TAG                 = "HomeActivity";
    private static final int    ALERT_FLIP_INTERVAL = 4000;
    public static final int    SEND_ALERT_CODE     = 1259;

    private AdapterViewFlipper alertFlipper;
    private TextView           versionText;
    private ImageView          versionCheckIcon;
    private ProgressBar        versionCheckProgress;

    private AlertAdapter alertAdapter;

    private boolean isCheckingVersion;
    private boolean newVersionAvailable;
    private boolean userPostponedUpdate;

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent)
        {
            checkNewVersion();
        }
    };

    static
    {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        checkSession();

        setContentView(R.layout.activity_home);

        //new ToolbarDelegate(this).setup(true);

        View view = findViewById(R.id.bottomLayout);
        view.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                checkNewVersion();
            }
        });

        alertFlipper = (AdapterViewFlipper) findViewById(R.id.view_flipper);
        versionText = (TextView) view.findViewById(R.id.version_text);
        versionCheckIcon = (ImageView) view.findViewById(R.id.version_check_icon);
        versionCheckProgress = (ProgressBar) view.findViewById(R.id.version_check_progress);

        alertFlipper.setInAnimation(this, R.animator.left_in);
        alertFlipper.setOutAnimation(this, R.animator.right_out);
        alertFlipper.setFlipInterval(ALERT_FLIP_INTERVAL);

        alertAdapter = new AlertAdapter(new ArrayList<Alert>());
        alertFlipper.setAdapter(alertAdapter);

        View bottomLayout = findViewById(R.id.bottomLayout);
        bottomLayout.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                downloadDataPackage();
            }
        });

        initContent();

        //PushManager.register();
    }

    private void initContent()
    {
        new ToolbarDelegate(this).setup(true);

        NavigationDrawerFragment navigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);

        navigationDrawerFragment.setUp(R.id.navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout));
        navigationDrawerFragment.refreshData();

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, HomeFragment.newInstance())
                .commit();

        if (DataPackageManager.getMetadata() != null)
        {
            versionText.setText("Version " + DataPackageManager.getMetadata().getVersion());
            versionCheckIcon.setImageResource(R.drawable.ic_status_success);

            if (!PushManager.isAlreadyRegistered())
                PushManager.register();
                //startRefreshingVersion();
        }
        else
        {
            versionText.setText(R.string.version_inconnue);
            versionCheckIcon.setImageResource(R.drawable.ic_status_warning);
        }
    }

    private void checkSession()
    {
        if (!Session.isLoggedIn())
        {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        }
    }

    private void checkNewVersion()
    {
        if (isCheckingVersion)
            return;

        if (newVersionAvailable)
        {
            popupNewVersionAvailable();
        }
        else
        {
            if (DataPackageManager.getMetadata() == null)
            {
                popupNewVersionAvailable();
            }
            else
            {
                isCheckingVersion = true;
                versionCheckProgress.setVisibility(View.VISIBLE);
                versionCheckIcon.setVisibility(View.GONE);
                JobManager.addJobInBackground(new CheckVersionJob());
            }
        }

        // WS version
        // if new version available, display popup
        // the best would be to display a snack bar
    }

    private void loadAlerts()
    {
        JobManager.addJobInBackground(new AlertJob());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == SEND_ALERT_CODE)
        {
            if (resultCode == RESULT_OK)
                loadAlerts();
        }
    }

    @Override
    public void onNavigationDrawerItemSelected(int position)
    {

    }

    private void downloadDataPackage()
    {
        if (MerlinsBeard.from(this).isConnected())
            startDownloadNewerVersion();
        else
            Toast.makeText(this, R.string.votre_connexion_internet_ne_semble_pas_active_, Toast.LENGTH_LONG).show();
    }

    private ProgressDialog progressDialog;

    public void startDownloadNewerVersion()
    {
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle(R.string.downloading_data_package);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setMax(100);
        progressDialog.setCancelable(true);
        progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener()
        {
            @Override
            public void onCancel(DialogInterface dialog)
            {
                JobManager.cancelJobsInBackground(DataPackageJob.JOB_TAG);
            }
        });
        progressDialog.show();

        JobManager.addJobInBackground(new DataPackageJob());
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        BusManager.getInstance().registerOnMainThread(this);

        refresh();
    }

    private void refresh()
    {
        if (!PushManager.isAlreadyRegistered() && DataPackageManager.getMetadata() != null)
        {
            startRefreshingVersion();
        }
        else if (DataPackageManager.getMetadata() != null)
        {
            initContent();
        }

        startAlertRefreshing();

        checkNewVersion();
        registerReceiver(receiver, new IntentFilter("com.nubytouch.crisiscare.NotificationEvent"));
    }

    @Override
    protected void onPause()
    {
        if (updatePopup != null && updatePopup.isShowing())
            updatePopup.dismiss();

        super.onPause();
        BusManager.getInstance().unregisterFromMainThread(this);

        unregisterReceiver(receiver);
        stopAlertRefreshing();
        stopVersionRefreshing();
    }

    @Subscribe
    public void onVersionChecked(CheckVersionEvent event)
    {
        versionCheckProgress.setVisibility(View.GONE);
        versionCheckIcon.setVisibility(View.VISIBLE);

        if (event.isSuccess())
        {
//            newVersionChecked = true;

            int localVersion  = DataPackageManager.getMetadata() != null ? (int) DataPackageManager.getMetadata().getVersion() : 0;
            int remoteVersion = event.getData().getVersion();

            versionCheckIcon.setImageResource(localVersion < remoteVersion ? R.drawable.ic_status_warning : R
                    .drawable.ic_status_success);

            if (localVersion < remoteVersion)
            {
                newVersionAvailable = true;
                stopVersionRefreshing(); // stop checking version, whatever the user chooses
                popupNewVersionAvailable();
            }
        }

        isCheckingVersion = false;
    }

    @Subscribe
    public void onAlertsLoaded(AlertEvent event)
    {
        if (event.isSuccess() && event.getData().getAlerts().size() > 0)
        {
            lastAlertsTime = System.currentTimeMillis();

            List<Alert> alerts = event.getData().getAlerts();
            Collections.reverse(alerts);

            alertFlipper.stopFlipping();
            alertAdapter.setData(alerts);
            alertFlipper.setVisibility(View.VISIBLE);
            alertFlipper.startFlipping();

            ((View) alertFlipper.getParent()).setVisibility(View.VISIBLE);
        }
        else if (event.isSuccess())
        {
            ((View) alertFlipper.getParent()).setVisibility(View.GONE);
        }
    }

    private AlertDialog updatePopup;

    private void popupNewVersionAvailable()
    {
        if (userPostponedUpdate)
            return;

        int titleRes   = R.string.mise_a_jour_disponible;
        int messageRes = R.string.une_nouvelle_version_est_disponible_voulez_vous_la_telecharger_;

        if (updatePopup == null)
        {
            updatePopup = new AlertDialog.Builder(this)
                    .setTitle(titleRes)
                    .setMessage(messageRes)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setPositiveButton(getString(R.string.update_now), new DialogInterface.OnClickListener()
                    {
                        public void onClick(DialogInterface dialog, int which)
                        {
                            downloadDataPackage();
                            updatePopup = null;
                        }
                    })
                    .setNegativeButton(getString(R.string.later), new DialogInterface.OnClickListener()
                    {
                        public void onClick(DialogInterface dialog, int which)
                        {
                            userPostponedUpdate = true;
                            updatePopup = null;
                        }
                    })
                    .create();
        }

        updatePopup.show();
    }

    @Subscribe
    public void onDataPackageProgress(DataPackageProgressEvent event)
    {
        if (progressDialog == null)
            return;

        progressDialog.setIndeterminate(false);
        progressDialog.setProgress(event.progress);
    }

    @Subscribe
    public void onDataPackageLoaded(DataPackageEvent event)
    {
        if (progressDialog != null)
        {
            progressDialog.dismiss();
            progressDialog = null;
        }

        userPostponedUpdate = false;
        newVersionAvailable = false;

        initContent();

        lastAlertsTime = 0;
        startAlertRefreshing();

        if (!Session.getUser().isHasAcceptedDisclaimer())
            startActivity(new Intent(this, DisclaimerActivity.class));
    }

    private long lastAlertsTime = 0;
    private ScheduledThreadPoolExecutor alertThread;
    private static final long ALERT_REFRESH_PERIOD = 60000;

    private void startAlertRefreshing()
    {
        long now         = System.currentTimeMillis();
        long elapsedTime = now - lastAlertsTime;

        long delay = ALERT_REFRESH_PERIOD - (Math.min(elapsedTime, ALERT_REFRESH_PERIOD));

        stopAlertRefreshing();

        alertThread = new ScheduledThreadPoolExecutor(1);
        alertThread.scheduleAtFixedRate(new Runnable()
        {
            @Override
            public void run()
            {
                loadAlerts();
            }
        }, delay, ALERT_REFRESH_PERIOD, TimeUnit.MILLISECONDS);
    }

    private void stopAlertRefreshing()
    {
        if (alertThread != null)
            alertThread.shutdownNow();
    }

    private ScheduledThreadPoolExecutor versionThread;
    private static final long VERSION_REFRESH_PERIOD = 30000;

    private void startRefreshingVersion()
    {
        if (versionThread != null)
            versionThread.shutdownNow();

        versionThread = new ScheduledThreadPoolExecutor(1);
        versionThread.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run()
            {
                if (!isCheckingVersion)
                {
                    isCheckingVersion = true;
                    setVersionLoading();
                    JobManager.addJobInBackground(new CheckVersionJob());
                }
            }
        }, 0, VERSION_REFRESH_PERIOD, TimeUnit.MILLISECONDS);
    }

    private void setVersionLoading()
    {
        runOnUiThread(new Runnable() {
            @Override
            public void run()
            {
                versionCheckProgress.setVisibility(View.VISIBLE);
                versionCheckIcon.setVisibility(View.GONE);
            }
        });
    }

    private void stopVersionRefreshing()
    {
        if (versionThread != null)
            versionThread.shutdownNow();

        isCheckingVersion = false;
    }
}

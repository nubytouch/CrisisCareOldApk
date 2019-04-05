package com.nubytouch.crisiscare.ui;


import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;

import java.lang.ref.WeakReference;

public class LocationDelegate implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient
        .OnConnectionFailedListener, LocationListener
{
    private static final int PERMISSION_REQUEST_LOCATION = 9435;
    private static final int REQUEST_CHECK_SETTINGS      = 4297;

    private static final int ACTION_NONE   = 0;
    private static final int ACTION_LAST   = 1;
    private static final int ACTION_UPDATE = 2;

    private WeakReference<Activity>                 activity;
    private WeakReference<LocationDelegateListener> listener;
    private GoogleApiClient                         apiClient;
    private LocationRequest                         locationRequest;
    private int                                     pendingAction;

    private static Location lastKnownLocation;
    private boolean permissionDenied;

    public LocationDelegate(Activity activity, LocationDelegateListener listener)
    {
        this.activity = new WeakReference<>(activity);
        this.listener = new WeakReference<>(listener);

        apiClient = new GoogleApiClient.Builder(activity)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this).build();

        locationRequest = new LocationRequest();
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(5000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    public void start()
    {
        apiClient.connect();
    }

    public void pause()
    {
        apiClient.disconnect();
        stopLocationUpdates();
    }

    public Location getLastKnownLocation()
    {
        if (isConnected() && checkIfPermissionGranted())
        {
            lastKnownLocation = LocationServices.FusedLocationApi.getLastLocation(apiClient);

            if (pendingAction == ACTION_LAST)
                pendingAction = ACTION_NONE;
        }
        else
        {
            pendingAction = ACTION_LAST;
        }

        return lastKnownLocation;
    }


    //___ Location updates

    public void listenToLocationUpdates()
    {
        if (isConnected() && checkIfPermissionGranted())
        {
            LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                    .addLocationRequest(locationRequest);

            PendingResult<LocationSettingsResult> result =
                    LocationServices.SettingsApi.checkLocationSettings(apiClient, builder.build());

            result.setResultCallback(new ResultCallback<LocationSettingsResult>()
            {
                @Override
                public void onResult(LocationSettingsResult result)
                {
                    final Status status = result.getStatus();
                    switch (status.getStatusCode())
                    {
                        case LocationSettingsStatusCodes.SUCCESS:
                            // All location settings are satisfied. The client can
                            // initialize location requests here.
                            startLocationUpdates();
                            break;
                        case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                            // Location settings are not satisfied, but this can be fixed
                            // by showing the user a dialog.
                            try
                            {
                                // Show the dialog by calling startResolutionForResult(),
                                // and check the result in onActivityResult().
                                if (activity != null && activity.get() != null)
                                {
                                    pendingAction = ACTION_UPDATE;
                                    status.startResolutionForResult(activity.get(), REQUEST_CHECK_SETTINGS);
                                }
                            }
                            catch (IntentSender.SendIntentException e)
                            {
                                // Ignore the error.
                            }
                            break;
                        case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                            // Location settings are not satisfied. However, we have no way
                            // to fix the settings so we won't show the dialog.
                            break;
                    }
                }
            });
        }
        else
        {
            pendingAction = ACTION_UPDATE;
        }
    }

    private void startLocationUpdates()
    {
        if (isConnected())
        {
            LocationServices.FusedLocationApi.requestLocationUpdates(apiClient, locationRequest, this);

            if (pendingAction == ACTION_UPDATE)
                pendingAction = ACTION_NONE;
        }
        else
        {
            pendingAction = ACTION_UPDATE;
        }
    }

    public void stopLocationUpdates()
    {
        if (isConnected())
            LocationServices.FusedLocationApi.removeLocationUpdates(apiClient, this);
    }

    private boolean isConnected()
    {
        return apiClient != null && apiClient.isConnected();
    }

    @Override
    public void onConnected(Bundle bundle)
    {
        if (pendingAction == ACTION_LAST)
            getLastKnownLocation();
        else if (pendingAction == ACTION_UPDATE)
            listenToLocationUpdates();

    }

    @Override
    public void onConnectionSuspended(int i)
    {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult)
    {

    }

    @Override
    public void onLocationChanged(Location location)
    {
        lastKnownLocation = location;

        if (listener != null && listener.get() != null)
            listener.get().onLocationUpdate(location);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (requestCode == REQUEST_CHECK_SETTINGS && resultCode == Activity.RESULT_OK)
        {
            startLocationUpdates();
        }
    }

    public interface LocationDelegateListener
    {
        void onLocationUpdate(Location location);
    }


    //___ Permissions stuff

    private boolean checkIfPermissionGranted()
    {
        if (permissionDenied)
            return false;

        if (ContextCompat.checkSelfPermission(activity.get(),
                                              Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager
                    .PERMISSION_GRANTED)
        {
            requestPermission();
            return false;
        }

        return true;
    }

    private void requestPermission()
    {
        if (activity != null && activity.get() != null)
        {
            ActivityCompat.requestPermissions(activity.get(),
                                              new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                                                           Manifest.permission.ACCESS_COARSE_LOCATION},
                                              PERMISSION_REQUEST_LOCATION);
        }
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        if (requestCode == PERMISSION_REQUEST_LOCATION)
        {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                if (pendingAction == ACTION_LAST)
                    getLastKnownLocation();
                else if (pendingAction == ACTION_UPDATE)
                    listenToLocationUpdates();
            }
            else
            {
                permissionDenied = true;
            }
        }
    }
}

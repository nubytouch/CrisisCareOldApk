package com.nubytouch.crisiscare.ui.alerts;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewStub;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.nubytouch.crisiscare.R;
import com.nubytouch.crisiscare.core.Session;
import com.nubytouch.crisiscare.data.model.Alert;
import com.nubytouch.crisiscare.data.model.User;
import com.nubytouch.crisiscare.datapackage.DataPackageManager;
import com.nubytouch.crisiscare.image.ImageLoader;
import com.nubytouch.crisiscare.ui.CCActivity;
import com.nubytouch.crisiscare.ui.ToolbarDelegate;
import com.nubytouch.crisiscare.ui.alerts.widget.AlertLevelView;
import com.nubytouch.crisiscare.ui.contacts.ContactInformationsDialog;
import com.nubytouch.crisiscare.ui.web.BrowserActivity;
import com.nubytouch.crisiscare.utils.DateUtil;
import com.nubytouch.crisiscare.utils.Utils;

import org.osmdroid.api.IMapController;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.ScaleBarOverlay;
import org.osmdroid.views.overlay.SimpleLocationOverlay;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.ViewCompat;

public class AlertInformationActivity extends CCActivity implements OnMapReadyCallback
{

    public static final  String EXTRA_ALERT                 = "com.nubytouch.crisiscare.ui.alerts.alert";

    private static final int    PERMISSION_REQUEST_LOCATION = 8739;

    private TextView           tvTitle;
    private TextView           tvLocation;
    private TextView           tvDate;
    private TextView           tvAlerter;
    private AlertLevelView     alertLevel;
    private TextView           tvStatus;
    private ImageView          ivPhoto;
    private Button             btShowDirect;
    private Button             btHandRail;
    private Button             btSendAlert;
    private MapView            osmMapView;

    private ViewStub osmMapStub;
    private SupportMapFragment mapFragment;

    private Alert alert;
    private User  author;

    private GoogleMap map;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_alert_information);

        alert = getIntent().getParcelableExtra(EXTRA_ALERT);
        new ToolbarDelegate(this).setup(false, alert.getTitle());

        tvLocation = (TextView) findViewById(R.id.tvAddress);
        tvDate = (TextView) findViewById(R.id.tvDate);
        tvAlerter = (TextView) findViewById(R.id.tvAuthor);
        tvStatus = (TextView) findViewById(R.id.tvStatus);
        tvTitle = (TextView) findViewById(R.id.tvTitle);
        alertLevel = (AlertLevelView) findViewById(R.id.alert_level_view);

        ivPhoto = (ImageView) findViewById(R.id.ivPhoto);
        btSendAlert = (Button) findViewById(R.id.btSendAlert);
        btShowDirect = (Button) findViewById(R.id.btViewDirect);
        btHandRail = (Button) findViewById(R.id.btHandRail);

        osmMapStub = (ViewStub) findViewById(R.id.map_osmandroid);
        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);

        tvTitle.setTextColor(Session.getPrimaryColor());

        tvTitle.post(new Runnable() {
            @Override
            public void run()
            {
                refreshAlert();
            }
        });


    }

    private void refreshAlert()
    {
        if (alert != null)
        {
            author = DataPackageManager.getUserByName(alert.getAuthor());

            tvTitle.setText(alert.getTitle());

            if (alert.getAddress() != null && alert.getAddress().length() > 0)
            {
                tvLocation.setText(alert.getAddress().trim());
                tvLocation.setVisibility(View.VISIBLE);
            }
            else
            {
                tvLocation.setVisibility(View.GONE);
            }

            tvDate.setText(DateUtil.getFormattedDatePublication(alert.getPubDate()));

            if (alert.getAuthor() != null)
            {
                if (author != null)
                    tvAlerter.setText(author.getFullName());
                else
                    tvAlerter.setText(alert.getAuthor());
            }
            else
            {
                tvAlerter.setVisibility(View.GONE);
            }

            alertLevel.setSelectedLevel(alert.getPriority());
            alertLevel.setEnabled(false);

            if (alert.getAuthor() != null)
            {
                ViewCompat.setBackgroundTintList(btSendAlert, ColorStateList.valueOf(Session.getPrimaryColor()));
                btSendAlert.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        if (author != null)
                        {
                            ContactInformationsDialog ct = new ContactInformationsDialog(AlertInformationActivity
                                                                                                 .this, author);
                            ct.show();
                        }
                        else
                        {
                            Intent i = new Intent(Intent.ACTION_SEND);
                            i.setType("message/rfc822");
                            i.putExtra(Intent.EXTRA_EMAIL, new String[]{alert.getAuthor()});
                            i.putExtra(Intent.EXTRA_SUBJECT, "Alerte \"" + alert.getTitle() + "\"");
                            // i.putExtra(Intent.EXTRA_TEXT   , "body of email");
                            try
                            {
                                startActivity(Intent.createChooser(i, "Envoyer un e-mail..."));
                            }
                            catch (android.content.ActivityNotFoundException ex)
                            {
                                Toast.makeText(AlertInformationActivity.this, "Aucun client mail installé", Toast
                                        .LENGTH_SHORT).show();
                            }
                        }
                    }
                });
            }
            else
            {
                btSendAlert.setVisibility(View.INVISIBLE);
            }

            if (alert.getUrl() != null && !alert.getUrl().isEmpty())
            {
                btShowDirect.setVisibility(View.VISIBLE);
                Drawable arr   = getResources().getDrawable(R.drawable.ic_action_next2);
                Drawable live  = getResources().getDrawable(R.drawable.ic_live);
                Drawable arrow = arr.getConstantState().newDrawable();
                arrow.setColorFilter(getResources().getColor(R.color.view_direct), PorterDuff.Mode.SRC_ATOP);
                btShowDirect.setCompoundDrawablesWithIntrinsicBounds(live, null, arrow, null);
                btShowDirect.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        Intent i = new Intent(AlertInformationActivity.this, BrowserActivity.class);
                        i.putExtra(BrowserActivity.EXTRA_URI, alert.getUrl());
                        i.putExtra(BrowserActivity.EXTRA_TITLE, alert.getTitle());
                        startActivity(i);
                    }
                });
            }
            else
            {
                btShowDirect.setVisibility(View.GONE);
            }

            if (tvStatus != null)
            {
                int stat = AlertUtil.getStatusName(alert.getStatusId());
                if (stat > 0)
                {
                    tvStatus.setVisibility(View.VISIBLE);
                    tvStatus.setText(stat);
                    ViewCompat.setBackgroundTintList(tvStatus,
                            ColorStateList.valueOf(AlertUtil.getColorForStatus(alert.getStatusId())));
                }
                else
                {
                    tvStatus.setVisibility(View.GONE);
                }
            }

            if (alert.getImageUrl() != null && !alert.getImageUrl().isEmpty())
            {
                ImageLoader.fadeLoad(alert.getImageUrl(), ivPhoto);

                ivPhoto.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {

                        String path = alert.getImageUrl();
                        Intent i    = new Intent(AlertInformationActivity.this, BrowserActivity.class);
                        i.putExtra(BrowserActivity.EXTRA_URI, path);
                        i.putExtra(BrowserActivity.EXTRA_TITLE, getTitle());
                        startActivity(i);
                    }
                });
            }
            else
            {
                ivPhoto.setVisibility(View.GONE);
            }

            Drawable arr   = getResources().getDrawable(R.drawable.ic_action_next2);
            Drawable live  = getResources().getDrawable(R.drawable.ic_live);
            Drawable arrow = arr.getConstantState().newDrawable();
            arrow.setColorFilter(getResources().getColor(R.color.view_direct), PorterDuff.Mode.SRC_ATOP);
            btHandRail.setCompoundDrawablesWithIntrinsicBounds(live, null, arrow, null);
            btHandRail.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    Intent i = new Intent(AlertInformationActivity.this, HandrailActivity.class);
                    i.putExtra(HandrailActivity.EXTRA_ALERT_ID, alert.getId());
                    i.putExtra(HandrailActivity.EXTRA_ALERT_TITLE, alert.getTitle());
                    startActivity(i);
                }
            });

            ivPhoto.post(new Runnable() {
                @Override
                public void run()
                {
                    initMaps();
                }
            });
        }
    }

    private void initMaps()
    {
        LatLng position = new LatLng(alert.getLatitude(), alert.getLongitude());

        if (Utils.getPlatform() == Utils.PLATFORM_ANDROID)
        {
            osmMapStub.setVisibility(View.GONE);

            if (position != null)
            {
                mapFragment.getMapAsync(this);
            }
            else
            {
                getSupportFragmentManager().beginTransaction().hide(mapFragment).commit();
            }
        }
        else
        {
            getSupportFragmentManager().beginTransaction().hide(mapFragment).commit();

            if (position != null && osmMapView != null)
            {
                osmMapStub.inflate();
                osmMapView = (MapView) findViewById(R.id.mapview);
                osmMapView.setClickable(true);
                osmMapView.setFocusable(true);
                osmMapView.setDuplicateParentStateEnabled(false);
                //osmMapView.setBuiltInZoomControls(true);
                osmMapView.setMultiTouchControls(true);
                IMapController mapController = this.osmMapView.getController();
                mapController.setZoom(17);
                GeoPoint point2 = new GeoPoint(position.latitude, position.longitude);
                mapController.setCenter(point2);

                SimpleLocationOverlay mMyLocationOverlay = new SimpleLocationOverlay(this);
                mMyLocationOverlay.setLocation(point2);
                osmMapView.getOverlays().add(mMyLocationOverlay);

                ScaleBarOverlay mScaleBarOverlay = new ScaleBarOverlay(this);
                this.osmMapView.getOverlays().add(mScaleBarOverlay);
            }
            else
            {
                osmMapStub.setVisibility(View.GONE);
            }
        }
    }

    public void startGoogleMaps(LatLng position)
    {
        // Create a Uri from an intent string. Use the result to create an Intent.
        Uri gmmIntentUri = Uri.parse("google.streetview:cbll=" + position.latitude + "," + position.longitude);

        // Create an Intent from gmmIntentUri. Set the action to ACTION_VIEW
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        // Make the Intent explicit by setting the Google Maps package
        mapIntent.setPackage("com.google.android.apps.maps");

        // Attempt to start an activity that can handle the Intent
        startActivity(mapIntent);

    }

    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(GoogleMap map)
    {
        UiSettings mapSettings;
        mapSettings = map.getUiSettings();
        mapSettings.setZoomControlsEnabled(false);
        mapSettings.setRotateGesturesEnabled(false);
        mapSettings.setTiltGesturesEnabled(false);

        if (alert.getLatitude() != 0 && alert.getLongitude() != 0)
        {
            this.map = map;
            LatLng position = new LatLng(alert.getLatitude(), alert.getLongitude());
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(position, 13));

            map.addMarker(new MarkerOptions()
                                  .title(alert.getTitle())
                                  .position(position));

            map.setOnMapClickListener(new GoogleMap.OnMapClickListener()
            {

                @Override
                public void onMapClick(LatLng point)
                {
                    // startGoogleMaps(alert.getPosition());
                }
            });

            if (checkIfPermissionGranted())
                map.setMyLocationEnabled(true);
            else
                requestPermission();

        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case android.R.id.home:
            {
                finish();
                return true;
            }
            default:
            {
                return super.onOptionsItemSelected(item);
            }
        }
    }

    private boolean checkIfPermissionGranted()
    {
        if(ContextCompat.checkSelfPermission(this,
                                              Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager
                    .PERMISSION_GRANTED)
        {
            return false;
        }

        return true;
    }

    private void requestPermission()
    {
        ActivityCompat.requestPermissions(this,
                                          new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                                                       Manifest.permission.ACCESS_COARSE_LOCATION},
                                          PERMISSION_REQUEST_LOCATION);
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        if (requestCode == PERMISSION_REQUEST_LOCATION)
        {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                if (map != null)
                    map.setMyLocationEnabled(true);
            }
        }
    }
}

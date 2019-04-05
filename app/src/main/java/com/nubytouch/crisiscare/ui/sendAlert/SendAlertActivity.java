package com.nubytouch.crisiscare.ui.sendAlert;

import android.app.ProgressDialog;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;

import androidx.annotation.NonNull;
import com.google.android.material.textfield.TextInputEditText;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.nubytouch.crisiscare.R;
import com.nubytouch.crisiscare.data.model.Alert;
import com.nubytouch.crisiscare.job.JobManager;
import com.nubytouch.crisiscare.ui.JobActivity;
import com.nubytouch.crisiscare.ui.LocationDelegate;
import com.nubytouch.crisiscare.ui.ToolbarDelegate;
import com.nubytouch.crisiscare.ui.alerts.widget.AlertLevelView;
import com.nubytouch.crisiscare.ui.selectrecipient.SelectRecipientsActivity;
import com.nubytouch.crisiscare.ui.sendAlert.job.AlertImageSentEvent;
import com.nubytouch.crisiscare.ui.sendAlert.job.AlertSentEvent;
import com.nubytouch.crisiscare.ui.sendAlert.job.SendAlertImageJob;
import com.nubytouch.crisiscare.ui.sendAlert.job.SendAlertJob;
import com.nubytouch.crisiscare.utils.KeyboardUtil;
import com.nubytouch.crisiscare.utils.Utils;
import com.squareup.otto.Subscribe;
import com.squareup.picasso.Picasso;
import com.vansuita.pickimage.bean.PickResult;
import com.vansuita.pickimage.bundle.PickSetup;
import com.vansuita.pickimage.dialog.PickImageDialog;
import com.vansuita.pickimage.listeners.IPickCancel;
import com.vansuita.pickimage.listeners.IPickResult;

import java.io.File;
import java.util.ArrayList;

import timber.log.Timber;

public class SendAlertActivity extends JobActivity implements LocationDelegate.LocationDelegateListener, IPickResult
{
    private static final int RECIPIENTS_CODE             = 585;
    private static final int PERMISSION_REQUEST_LOCATION = 786;
    /*private static final int PERMISSION_REQUEST_PHOTO    = 8923;
    private final        int PHOTO_REQUEST_CODE          = 43985;*/

    private final String TMP_FILE_NAME = "tmpAlertImage.jpg";

    private ArrayList<String> recipients;

    /*private Uri imageUri;
    private Uri selectedImageUri;*/
    private String selectedImagePath;

    private LocationDelegate locationDelegate;
    private Location         currentLocation;

    private TextWatcher textWatcher = new TextWatcher()
    {
        private int lengthBefore;

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after)
        {
            lengthBefore = s.length();
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) { }

        @Override
        public void afterTextChanged(Editable s)
        {
            int lengthNow = s.length();

            if ((lengthNow == 0 && lengthBefore > 0)
                || (lengthBefore == 0 && lengthNow > 0))
                supportInvalidateOptionsMenu();
        }
    };

    private ImageView         imageView;
    private TextInputEditText editText;
    private AlertLevelView alertLevelView;
    private TextView          recipientLabel;
    private ImageButton       recipientButton;
    private ProgressDialog    progressDialog;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_send_alert);

        new ToolbarDelegate(this).setup(false, R.string.create_new_alert).setIcon(R.drawable.ic_close);

        imageView = (ImageView) findViewById(R.id.image);
        editText = (TextInputEditText) findViewById(R.id.edit_text);
        recipientLabel = (TextView) findViewById(R.id.recipients_label);
        recipientButton = (ImageButton) findViewById(R.id.recipients_button);
        View recipientLayout = findViewById(R.id.recipients_layout);
        alertLevelView = findViewById(R.id.alert_level_view);

        alertLevelView.setEditMode(true);

        recipientButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(SendAlertActivity.this, SelectRecipientsActivity.class);
                intent.putStringArrayListExtra(SelectRecipientsActivity.EXTRA_RECIPIENTS, recipients);
                startActivityForResult(intent,
                                       RECIPIENTS_CODE);
            }
        });
        recipientLayout.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(SendAlertActivity.this, SelectRecipientsActivity.class);
                intent.putStringArrayListExtra(SelectRecipientsActivity.EXTRA_RECIPIENTS, recipients);
                startActivityForResult(intent,
                                       RECIPIENTS_CODE);
            }
        });

        recipients = new ArrayList<>();
        refreshRecipients();

        ViewGroup button = (ViewGroup) findViewById(R.id.image_layout);
        button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                PickImageDialog.build(new PickSetup()
                                              .setGalleryIcon(R.drawable.ic_gallery)
                                              .setCameraIcon(R.drawable.ic_camera)
                                              .setGalleryButtonText(getString(R.string.gallery))
                                              .setCameraButtonText(getString(R.string.camera))
                                              .setTitle(getString(R.string.choose_action))
                                              .setCancelText(getString(R.string.cancel)))
                        .setOnPickCancel(new IPickCancel() {
                            @Override
                            public void onCancelClick()
                            {
                                // do nothing()
                            }
                        })
                        .show(SendAlertActivity.this);
                /*if (checkIfPhotoPermissionGranted())
                    openImageIntent();
                else
                    requestPhotoPermission();*/
            }
        });

        locationDelegate = new LocationDelegate(this, this);
    }

    private void refreshRecipients()
    {
        if (recipients.size() > 0)
            recipientLabel.setText(getResources().getQuantityString(R.plurals.recipients_desc, recipients.size(),
                                                                    recipients.size()));
        else
            recipientLabel.setText(getString(R.string.alert_standby));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.send_alert, menu);

        MenuItem item = menu.findItem(R.id.action_send_alert);
        item.setEnabled(editText.getText().length() > 0);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        if (item.getItemId() == android.R.id.home)
        {
            finish();
            return true;
        }
        else if (item.getItemId() == R.id.action_send_alert)
        {
            sendAlert();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onResume()
    {
        super.onResume();

        if (editText != null)
            editText.addTextChangedListener(textWatcher);

        checkPlayServices();

        locationDelegate.start();
        currentLocation = locationDelegate.getLastKnownLocation();
        locationDelegate.listenToLocationUpdates();
    }

    @Override
    protected void onPause()
    {
        super.onPause();

        if (editText != null)
            editText.removeTextChangedListener(textWatcher);

        locationDelegate.pause();
    }

    protected boolean checkPlayServices()
    {
        if (Utils.getPlatform() == Utils.PLATFORM_ANDROID)
        {
            int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);

            if (resultCode != ConnectionResult.SUCCESS)
            {
                if (GooglePlayServicesUtil.isUserRecoverableError(resultCode))
                    GooglePlayServicesUtil.getErrorDialog(resultCode, this, 1).show();
                else
                    Timber.d("This device is not supported.");
//
                return false;
            }

            return true;
        }

        return false;
    }

    /*private void openImageIntent()
    {
        File file = new File(DataPackagePath.getTmpDirPath(), TMP_FILE_NAME);
        imageUri = Uri.fromFile(file);

        // Camera.
        final List<Intent>      cameraIntents = new ArrayList<>();
        final Intent            captureIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        final List<ResolveInfo> listCam       = getPackageManager().queryIntentActivities(captureIntent, 0);
        for (ResolveInfo res : listCam)
        {
            final String packageName = res.activityInfo.packageName;
            final Intent intent      = new Intent(captureIntent);
            intent.setComponent(new ComponentName(res.activityInfo.packageName, res.activityInfo.name));
            intent.setPackage(packageName);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
            cameraIntents.add(intent);
        }

        // Filesystem
        final Intent galleryIntent = new Intent();
        galleryIntent.setType("image/*");
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);

        // Chooser of filesystem options.
        final Intent chooserIntent = Intent.createChooser(galleryIntent, "Select Source");

        // Add the camera options.
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS,
                               cameraIntents.toArray(new Parcelable[cameraIntents.size()]));

        startActivityForResult(chooserIntent, PHOTO_REQUEST_CODE);
    }*/

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        /*if (requestCode == PHOTO_REQUEST_CODE)
        {
            if (resultCode == Activity.RESULT_OK)
            {
                final boolean isCamera;
                if (data == null)
                {
                    isCamera = true;
                }
                else
                {
                    final String action = data.getAction();

                    if (action == null)
                        isCamera = false;
                    else
                        isCamera = action.equals(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                }

                selectedImageUri = null;

                if (isCamera)
                    selectedImageUri = imageUri;
                else
                    selectedImageUri = data == null ? null : data.getData();

                String realUri = UriHelper.getPath(this, selectedImageUri);

                if (realUri != null)
                    Picasso.with(this)
                            .load(new File(realUri))
                            .resize(1024, 1024)
                            .centerCrop()
                            .into(imageView);
            }
        }
        else */
        if (requestCode == RECIPIENTS_CODE && resultCode == RESULT_OK)
        {
            recipients = data.getStringArrayListExtra(SelectRecipientsActivity.EXTRA_RECIPIENTS);
            refreshRecipients();
        }
        else
        {
            locationDelegate.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void sendAlert()
    {
        double lat = 0, lng = 0;

        if (currentLocation != null)
        {
            lat = currentLocation.getLatitude();
            lng = currentLocation.getLongitude();
        }

        progressDialog = ProgressDialog.show(this, null, getString(R.string.sending_alert), true, false);
        KeyboardUtil.hideKeyboard(editText);
        JobManager.addJobInBackground(new SendAlertJob(editText.getText().toString(), alertLevelView.getSelectedLevel(), recipients, lat,
                                                       lng));
    }

    /*public String getRealPathFromURI(Uri contentUri)
    {
        String[] proj   = {MediaStore.Images.ImageColumns.DATA};
        Cursor   cursor = getContentResolver().query(contentUri, proj, null, null, null);

        if (cursor != null)
        {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        }

        return null;
    }*/

    @Subscribe
    public void onAlertSent(AlertSentEvent event)
    {
        if (event.isSuccess())
        {
            if (selectedImagePath != null)
            {
                if (progressDialog != null)
                    progressDialog.setMessage(getString(R.string.sending_alert_image));

                String alertId = event.getData().alertId;


                File file = new File(selectedImagePath);

                //File file = new File(selectedImageUri.getPath());

                /*if (!file.exists())
                {
                    String uri = UriHelper.getPath(this, selectedImageUri);//getRealPathFromURI(selectedImageUri);
                    if (uri != null)
                    {
                        file = new File(uri);

                        if (!file.exists())
                        {
                            Timber.d("onAlertSent: file doesn't exist " + file.getAbsolutePath());
                            //return new TaskResult(TaskResult.STATUS_FAIL, null);
                        }
                    }
                }*/

                JobManager.addJobInBackground(new SendAlertImageJob(alertId, file));
            }
            else
            {
                alertComplete();
            }
        }
        else
        {
            if (progressDialog != null)
            {
                progressDialog.dismiss();
                progressDialog = null;
            }

            Toast.makeText(this, R.string.unable_to_send_alert, Toast.LENGTH_SHORT).show();
        }
    }

    @Subscribe
    public void onAlertImageSent(AlertImageSentEvent event)
    {
        Timber.d("onAlertImageSent: ");

        if (event.isSuccess() && event.getData() == true)
        {
            alertComplete();
        }
        else
        {
            if (progressDialog != null)
            {
                progressDialog.dismiss();
                progressDialog = null;
            }

            Toast.makeText(this, R.string.unable_to_send_alert_image, Toast.LENGTH_SHORT).show();
        }
    }

    private void alertComplete()
    {
        if (progressDialog != null)
        {
            progressDialog.dismiss();
            progressDialog = null;
        }

        Toast.makeText(this, R.string.alert_sent, Toast.LENGTH_SHORT).show();
        setResult(RESULT_OK);
        finish();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        /*if (requestCode == PERMISSION_REQUEST_PHOTO)
        {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                openImageIntent();
        }
        else
        {*/
        locationDelegate.onRequestPermissionsResult(requestCode, permissions, grantResults);
        /*}*/
    }

    /*private boolean checkIfPhotoPermissionGranted()
    {
        return ContextCompat
                       .checkSelfPermission(this,
                                            Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager
                       .PERMISSION_GRANTED;
    }

    private void requestPhotoPermission()
    {
        ActivityCompat.requestPermissions(this,
                                          new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                                          PERMISSION_REQUEST_PHOTO);
    }*/

    @Override
    public void onLocationUpdate(Location location)
    {
        Timber.d("onLocationUpdate %s", location);
        currentLocation = location;
    }

    @Override
    public void onPickResult(PickResult pickResult)
    {
        if (pickResult.getError() == null)
        {
            if (pickResult.getUri() != null)
            {
                //selectedImageUri = pickResult.getUri();
                selectedImagePath = pickResult.getPath();

                Picasso.with(this)
                        .load(pickResult.getUri())
                        .resize(1024, 1024)
                        .centerCrop()
                        .into(imageView);
            }
        }
    }


    // Pick image
}

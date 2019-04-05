package com.nubytouch.crisiscare.push;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import timber.log.Timber;

import com.google.android.gms.gcm.GcmListenerService;
import com.nubytouch.crisiscare.R;
import com.nubytouch.crisiscare.core.Session;
import com.nubytouch.crisiscare.ui.home.HomeActivity;

public class CCGcmListenerService extends GcmListenerService
{
    private static final String TAG = "MyGcmListenerService";

    public static final int NOTIFICATION_ID = 1;
    public static final String CHANNEL_ID = "alerts";

    /**
     * Called when message is received.
     *
     * @param from SenderID of the sender.
     * @param data Data bundle containing message data as key/value pairs.
     *             For Set of keys use data.keySet().
     */
    @Override
    public void onMessageReceived(String from, Bundle data)
    {
        Timber.d("onMessageReceived: ");
        String message = data.getString("message");
        Timber.d("onMessageReceived: ");
        Timber.d("From: " + from);
        Timber.d("Message: " + message);

        // ToDo If server handles topics, then handle them
        if (from.startsWith("/topics/"))
        {
            // message received from some topic.
        }
        else
        {
            // normal downstream message.
        }

        /**
         * Production applications would usually process the message here.
         * Eg: - Syncing with server.
         *     - Store message in local database.
         *     - Update UI.
         */

        /**
         * In some cases it may be useful to show a notification indicating to the user
         * that a message was received.
         */
        String msg   = data.getString("alert");
        String sound = data.getString("sound");
        sendNotification(msg, sound);
    }

    // Put the message into a notification and post it.
    // This is just one simple example of what you might choose to do with
    // a GCM message.
    private void sendNotification(String msg, String sound)
    {
        createNotificationChannel();

        Intent intent = new Intent(this, HomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this, CHANNEL_ID)
                        .setSmallIcon(R.drawable.ic_logo_notif)
                        .setContentTitle(getString(R.string.app_name))
                        .setColor(Session.getPrimaryColor())
                        // .setStyle(new NotificationCompat.BigTextStyle()                             .bigText(msg))
                        .setContentText(msg)
                        .setStyle(new NotificationCompat.BigTextStyle()
                                          .bigText(msg))
                        .setDefaults(NotificationCompat.DEFAULT_ALL)
                        .setTicker(msg)
                        .setPriority(NotificationCompat.PRIORITY_HIGH)
                        .setAutoCancel(true)
                        .setVisibility(NotificationCompat.VISIBILITY_PUBLIC);


        if (sound != null && sound.contains("wakeup"))
        {
            mBuilder.setSound(Uri.parse("android.resource://" + this.getPackageName() + "/" + R.raw.wakeup));
        }

        mBuilder.setContentIntent(contentIntent);

        NotificationManagerCompat manager = NotificationManagerCompat.from(this);
        manager.notify(NOTIFICATION_ID, mBuilder.build());

        sendBroadcast(new Intent("com.nubytouch.crisiscare.NotificationEvent"));
    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.notification_channel_name);
            String description = "";//getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}

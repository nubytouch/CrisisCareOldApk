package com.nubytouch.crisiscare.utils;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.util.DisplayMetrics;
import android.widget.Toast;

import com.nubytouch.crisiscare.ui.pdf.PdfViewerActivity;

import java.io.File;

import timber.log.Timber;

public class Utils {

    public static boolean isNullOrEmpty(String str) {
        return str == null || str.length() == 0;
    }

    public static Intent newEmailIntent(Context context, String address) {

        /*if (Session.isGoodEnabled())
        {
            if (sendGoodWorkMail(context, address))
                return null;
        }*/

        Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto", address, null));
        return Intent.createChooser(emailIntent, "Envoyer un email...");
    }

    //static RequestSendEmail request = null;

    /*public static boolean sendGoodWorkMail(Context context, String address) {
        if (request == null) request = new RequestSendEmail();

        String[] providers = request.queryProviders().getProviderNames();
        if (providers.length > 0) {

            // Add all parameters, select the provider, send the request.
            request.addToAddresses(address)
                    .setSubject("")
                    .setBody("").selectProvider(0);
            request.sendOrMessage();
            // The above returns a message if there is an error in the send. The
            // message is also inserted into the Request object, which is dumped
            // below, so there is no need to log it additionally.


            // Discard the request.
            request = null;
            return true;
        }
        return false;

    }*/

    public static Intent newPhoneIntent(Context context, String phoneNumber) {
        String nbr = phoneNumber.startsWith("tel:") ? phoneNumber : "tel:" + phoneNumber;
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse(nbr));
        return intent;
    }

    public static Intent newSMSIntent(Context context, String phoneNumber) {

        if (Utils.getPlatform() == Utils.PLATFORM_ANDROID) {

            Intent smsIntent = new Intent(Intent.ACTION_VIEW);
            smsIntent.setData(Uri.parse("sms:" + phoneNumber));
//            smsIntent.setType("vnd.android-dir/mms-sms");
//            smsIntent.putExtra("address", phoneNumber);
            return smsIntent;
        } else {
            Intent smsIntent = new Intent(Intent.ACTION_VIEW);
            smsIntent.setData(Uri.fromParts("sms", phoneNumber, null));
            // smsIntent.putExtra("sms_body", "SMSMessage");
            return smsIntent;

        }

    }

    /**
     * @return platform id 1 Android 2 Amazon 3 Blackberry
     */
    public final static int PLATFORM_BLACKBERRY = 2;
    public final static int PLATFORM_ANDROID = 1;

    public static int getPlatform() {
        if (System.getProperty("os.name").contains("qnx")) {
            return PLATFORM_BLACKBERRY;
        } else {
            return PLATFORM_ANDROID;
        }
    }

    public static void openPDF(Context context, String title, String path) {
        Timber.d("Opening PDF :" + path);
        boolean opened = false;
        Intent intent = null;
        File fileToOpen = new File(path);
        Timber.d("openPDF: " + fileToOpen.getAbsolutePath());
        Timber.d("openPDF: " + fileToOpen.getPath());
        if (Utils.getPlatform() == Utils.PLATFORM_ANDROID) {

            // Uri uri = Uri.parse(path);
            Uri uri = Uri.fromFile(fileToOpen);
            Timber.d("openPDF: " + uri.getPath());
            Timber.d("openPDF: " + uri);
            intent = new Intent(context, PdfViewerActivity.class);
            intent.setAction(Intent.ACTION_VIEW);
            intent.putExtra("title", title);
            intent.setData(uri);

            try {
                context.startActivity(intent);
                opened = true;

            } catch (ActivityNotFoundException e) {
                opened = false;
            }

        } else {

            // copying file into public directory
            File privateDocFile = new File(path);
            int index = path.lastIndexOf("/");
            if (index >= 0) {
                String filename = path.substring(index + 1);

                // clean cache path
//                UtilsFile.deleteFolder(Configuration.getPublicCachePath());
//                File publicDocFile = new File(Configuration.getPublicCachePath() + filename);
//                if (UtilsFile.copy(privateDocFile, publicDocFile)) {
//                    fileToOpen = publicDocFile;
//                }

            }
        }

        if (!opened) { // If muPDF doesn't work or not android platform, try
            // using external reader
            Timber.d("Opening PDF with muPDF failed :" + fileToOpen);
            if (fileToOpen.exists()) {

                try {
                    //	Toast.makeText(context, "Trying to open " + path + " => " + fileToOpen.getName(), Toast.LENGTH_LONG).show();

                    Uri fileUri = Uri.fromFile(fileToOpen);
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setDataAndType(fileUri, "application/pdf");
                    // i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    context.startActivity(i);

                } catch (Exception e) {
                    Logger.getInstance().LogException(e);
                    Toast.makeText(context, "Impossible d'ouvrir le fichier..." + e.getMessage() + " " + e.getClass().getName(), Toast.LENGTH_LONG).show();

                }
            } else {
                Toast.makeText(context, "Fichier introuvable..." + fileToOpen.getName(), Toast.LENGTH_LONG).show();

            }
        }

    }
    /**
     * This method converts dp unit to equivalent pixels, depending on device density.
     *
     * @param dp A value in dp (density independent pixels) unit. Which we need to convert into pixels
     * @param context Context to get resources and device specific display metrics
     * @return A float value to represent px equivalent to dp depending on device density
     */
    public static float convertDpToPixel(float dp, Context context){
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float px = dp * (metrics.densityDpi / 160f);
        return px;
    }

    /**
     * This method converts device specific pixels to density independent pixels.
     *
     * @param px A value in px (pixels) unit. Which we need to convert into db
     * @param context Context to get resources and device specific display metrics
     * @return A float value to represent dp equivalent to px value
     */
    public static float convertPixelsToDp(float px, Context context){
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float dp = px / (metrics.densityDpi / 160f);
        return dp;
    }

}

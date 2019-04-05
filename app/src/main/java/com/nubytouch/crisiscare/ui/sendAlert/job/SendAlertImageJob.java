package com.nubytouch.crisiscare.ui.sendAlert.job;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;

import timber.log.Timber;

import com.birbit.android.jobqueue.Params;
import com.nubytouch.crisiscare.job.AbstractEvent;
import com.nubytouch.crisiscare.job.FetchRemoteJob;
import com.nubytouch.crisiscare.rest.ServiceBuilder;
import com.nubytouch.crisiscare.ui.sendAlert.rest.SendAlertService;
import com.nubytouch.crisiscare.ui.sendAlert.rest.dto.SendAlertResultDTO;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;

public class SendAlertImageJob extends FetchRemoteJob<Boolean>
{
    private final String alertId;
    private final File   image;

    public SendAlertImageJob(String alertId, File image)
    {
        super(new Params(Priority.HIGH.value));

        this.alertId = alertId;
        this.image = image;
    }

    @Override
    protected AbstractEvent<Boolean> buildEvent(Boolean data)
    {
        return new AlertImageSentEvent(data);
    }

    private static final String TAG = "SendAlertImageJob";

    @Override
    public Call<Boolean> fetchServer()
    {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        Bitmap                src = BitmapFactory.decodeFile(image.getPath(), options);
        src = getUnRotatedImage(image, src);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        src.compress(Bitmap.CompressFormat.JPEG, 70, bos);

        Timber.d("fetchServer: src size " + (bos.size() / 1024));

        Bitmap scaledBitmap;
        int    maxSize      = 200; // 200 Ko
        float  factor       = 1f;
        float  factorStep   = 1.3f;
        int    bitmapWidth  = src.getWidth();
        int    bitmapHeight = src.getHeight();
        int    scaledWidth, scaledHeight;

        while (bos.size() / 1024 > maxSize)
        {
            Timber.d("fetchServer: while " + (bos.size() / 1024));
            scaledWidth = Math.round(bitmapWidth / factor);
            scaledHeight = Math.round(bitmapHeight / factor);
            bos = new ByteArrayOutputStream();
            scaledBitmap = Bitmap.createScaledBitmap(src, scaledWidth, scaledHeight, true);
            scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 70, bos);
            factor *= factorStep;
            Timber.d("fetchServer: size " + scaledBitmap.getWidth() + "x" + scaledBitmap.getHeight());
        }

        RequestBody requestBody = RequestBody.create(
                MediaType.parse("image/jpeg"), bos.toByteArray());

        Timber.d("fetchServer: " + alertId);

        SendAlertService service = new ServiceBuilder().build(SendAlertService.class);
        Call<Boolean>    call    = service.sendAlertImage(alertId, requestBody);

        return call;
    }

    @Override
    public boolean checkData(Boolean data)
    {
        return true;
    }

    public static Bitmap getUnRotatedImage(File imageFile, Bitmap rotattedBitmap)
    {
        int rotate = 0;

        try
        {
            ExifInterface exif      = new ExifInterface(imageFile.getAbsolutePath());
            int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                                                   ExifInterface.ORIENTATION_NORMAL);

            switch (orientation)
            {
                case ExifInterface.ORIENTATION_ROTATE_270:
                    rotate = 270;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    rotate = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_90:
                    rotate = 90;
                    break;
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }

        Matrix matrix = new Matrix();
        matrix.postRotate(rotate);

        return Bitmap.createBitmap(rotattedBitmap, 0, 0, rotattedBitmap.getWidth(),
                                   rotattedBitmap.getHeight(), matrix, true);
    }
}

package com.nubytouch.crisiscare.image;

import androidx.annotation.ColorRes;
import android.widget.ImageView;

import com.nubytouch.crisiscare.CrisisCare;
import com.squareup.picasso.Picasso;

public class ImageLoader
{
    private ImageLoader()
    {

    }

    public static void load(String url, ImageView view)
    {
        if (url != null && !url.isEmpty())
        {
            Picasso.with(CrisisCare.getInstance())
                    .load(url)
                    .noFade()
                    .into(view);
        }
        else
        {
            Picasso.with(CrisisCare.getInstance()).cancelRequest(view);
        }
    }

    public static void load(@ColorRes int res, ImageView view)
    {
        Picasso.with(CrisisCare.getInstance())
                .load(res)
                .noFade()
                .into(view);
    }

    public static void fadeLoad(String url, ImageView view)
    {
        if (url != null && !url.isEmpty())
        {
            Picasso.with(CrisisCare.getInstance())
                    .load(url)
                    .into(view);
        }
        else
        {
            Picasso.with(CrisisCare.getInstance()).cancelRequest(view);
        }
    }

    public static void fadeLoad(@ColorRes int res, ImageView view)
    {
        Picasso.with(CrisisCare.getInstance())
                .load(res)
                .into(view);
    }
}

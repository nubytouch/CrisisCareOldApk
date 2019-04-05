package com.nubytouch.crisiscare.ui;


import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.ColorStateList;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.nubytouch.crisiscare.R;
import com.nubytouch.crisiscare.core.Session;
import com.nubytouch.crisiscare.utils.ResUtils;

import androidx.annotation.AttrRes;
import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.annotation.StyleRes;
import androidx.core.view.MarginLayoutParamsCompat;
import androidx.core.view.ViewCompat;

public class DataStateView extends LinearLayout
{
    @StringRes
    private static final int DEFAULT_EMPTY_MESSAGE = R.string.empty;
    @DrawableRes
    private static final int DEFAULT_EMPTY_IMAGE   = 0;
    @StringRes
    private static final int DEFAULT_ERROR_MESSAGE = R.string.error_generic;
    @DrawableRes
    private static final int DEFAULT_ERROR_IMAGE   = 0;


    private ProgressBar progressBar;
    private ImageView   image;
    private TextView    text;
    private Button      retryButton;

    @StringRes
    private int emptyMessage;
    @DrawableRes
    private int emptyImage;
    @StringRes
    private int errorMessage;
    @DrawableRes
    private int errorImage;

    public DataStateView(@NonNull Context context)
    {
        super(context);
        init(context);
    }

    public DataStateView(@NonNull Context context, @Nullable AttributeSet attrs)
    {
        super(context, attrs);
        init(context);
    }

    public DataStateView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @TargetApi(21)
    public DataStateView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr, @StyleRes
            int defStyleRes)
    {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private void init(Context context)
    {
        setOrientation(LinearLayout.VERTICAL);

        MarginLayoutParams params = new MarginLayoutParams(
                new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));

        int margin = ResUtils.getDimensionPxSize(R.dimen.dp_32);

        params.topMargin = margin;
        MarginLayoutParamsCompat.setMarginStart(params, margin);
        MarginLayoutParamsCompat.setMarginEnd(params, margin);
        setLayoutParams(params);

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.abewy_support_view_data_state, this, true);

        progressBar = (ProgressBar) findViewById(R.id.progress);
        image = (ImageView) findViewById(R.id.image);
        text = (TextView) findViewById(R.id.error_label);
        retryButton = (Button) findViewById(R.id.retry_button);

        ViewCompat.setBackgroundTintList(retryButton, ColorStateList.valueOf(Session.getPrimaryColor()));

        //ToDo define attrs in XML

        emptyMessage = DEFAULT_EMPTY_MESSAGE;
        emptyImage = DEFAULT_EMPTY_IMAGE;
        errorMessage = DEFAULT_ERROR_MESSAGE;
        errorImage = DEFAULT_ERROR_IMAGE;
    }

    public void setStateLoading()
    {
        progressBar.setVisibility(View.VISIBLE);
        image.setVisibility(View.GONE);
        text.setVisibility(View.GONE);
        retryButton.setVisibility(View.GONE);
    }

    public void setStateEmpty()
    {
        progressBar.setVisibility(View.GONE);

        if (emptyImage != 0)
        {
            image.setImageResource(emptyImage);
            image.setVisibility(VISIBLE);
        }
        else
        {
            image.setVisibility(GONE);
        }

        text.setText(emptyMessage);
        text.setVisibility(VISIBLE);

        retryButton.setVisibility(View.GONE);
    }

    public void setStateError()
    {
        progressBar.setVisibility(View.GONE);

        if (errorImage != 0)
        {
            image.setImageResource(errorImage);
            image.setVisibility(VISIBLE);
        }
        else
        {
            image.setVisibility(GONE);
        }

        text.setText(errorMessage);
        text.setVisibility(VISIBLE);

        retryButton.setVisibility(View.VISIBLE);
    }

    public DataStateView setEmptyImage(@DrawableRes int resId)
    {
        emptyImage = resId;
        return this;
    }

    public DataStateView setErrorImage(@DrawableRes int resId)
    {
        errorImage = resId;
        return this;
    }

    public DataStateView setEmptyMessage(@StringRes int resId)
    {
        emptyMessage = resId;
        return this;
    }

    public DataStateView setErrorMessage(@StringRes int resId)
    {
        errorMessage = resId;
        return this;
    }

    public void setRetryCallback(final RetryCallback callback)
    {
        if (retryButton != null)
        {
            retryButton.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v)
                {
                    if (callback != null)
                        callback.retry();
                }
            });
        }
    }
}

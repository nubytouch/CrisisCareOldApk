package com.nubytouch.crisiscare.ui;


import android.annotation.TargetApi;
import android.content.Context;
import androidx.annotation.AttrRes;
import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.annotation.StyleRes;
import androidx.core.view.MarginLayoutParamsCompat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nubytouch.crisiscare.R;
import com.nubytouch.crisiscare.utils.ResUtils;

import timber.log.Timber;

public class DataErrorView extends LinearLayout
{
    private ImageView image;
    private TextView  text;
    private Button    retryButton;

    public DataErrorView(@NonNull Context context)
    {
        super(context);
        init(context);
    }

    public DataErrorView(@NonNull Context context, @Nullable AttributeSet attrs)
    {
        super(context, attrs);
        init(context);
    }

    public DataErrorView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @TargetApi(21)
    public DataErrorView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr, @StyleRes
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
        inflater.inflate(R.layout.view_error, this, true);

        image = (ImageView) findViewById(R.id.image);
        text = (TextView) findViewById(R.id.error_label);
        retryButton = (Button) findViewById(R.id.retry_button);
    }

    public DataErrorView setImage(@DrawableRes int resId)
    {
        if (image != null)
            image.setImageResource(resId);

        return this;
    }

    public DataErrorView setText(@StringRes int resId)
    {
        if (text != null)
            text.setText(resId);

        return this;
    }

    public DataErrorView setText(String str)
    {
        if (text != null)
            text.setText(str);

        return this;
    }

    public void setRetryListener(final onDataErrorViewListener listener)
    {
        Timber.d("setRetryListener " + listener);
        if (retryButton != null)
        {
            retryButton.setOnClickListener(new OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    Timber.d("onclick");
                    if (listener != null)
                        listener.onRetryButtonClick();
                }
            });
        }
    }

    public interface onDataErrorViewListener
    {
        void onRetryButtonClick();
    }
}

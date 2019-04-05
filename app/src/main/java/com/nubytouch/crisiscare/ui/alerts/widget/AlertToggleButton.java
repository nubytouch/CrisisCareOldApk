package com.nubytouch.crisiscare.ui.alerts.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.nubytouch.crisiscare.R;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.annotation.StringRes;
import androidx.core.content.ContextCompat;

public class AlertToggleButton extends FrameLayout {

    private TextView textView;
    private ImageView imageView;
    private boolean editMode;

    public AlertToggleButton(Context context) {
        super(context);
        init(null);
    }

    public AlertToggleButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public AlertToggleButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public AlertToggleButton(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(attrs);
    }

    public void init(AttributeSet attrs) {

        LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);

        textView = new TextView(getContext());
        textView.setTextColor(0xffffffff);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 24f);
        textView.setGravity(Gravity.CENTER);
        textView.setLayoutParams(params);

        imageView = new ImageView(getContext());
        imageView.setScaleType(ImageView.ScaleType.CENTER);
        imageView.setLayoutParams(params);

        if (attrs != null) {
            TypedArray a = getContext().getTheme().obtainStyledAttributes(
                    attrs,
                    R.styleable.AlertToggleButton,
                    0, 0);

            Drawable icon;
            String text;

            try {
                icon = a.getDrawable(R.styleable.AlertToggleButton_selectedIcon);
                text = a.getString(R.styleable.AlertToggleButton_unselectedText);
            } finally {
                a.recycle();
            }

            textView.setText(text);
            imageView.setImageDrawable(icon);
        }

        TypedValue outValue = new TypedValue();
        getContext().getTheme().resolveAttribute(android.R.attr.selectableItemBackground, outValue, true);
        setForeground(ContextCompat.getDrawable(getContext(), outValue.resourceId));

        /*setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                setSelected(!isSelected());
            }
        });*/

        addView(textView);
        addView(imageView);

        setSelected(isSelected());
        setEditMode(false);
    }

    @Override
    public void setSelected(boolean selected) {
        super.setSelected(selected);

        textView.setVisibility(selected ? View.GONE : View.VISIBLE);
        imageView.setVisibility(selected ? View.VISIBLE : View.GONE);

        /*if (editMode)
        {
            textView.setVisibility(selected ? View.GONE : View.VISIBLE);
            imageView.setVisibility(selected ? View.VISIBLE : View.GONE);
            setAlpha(1.0f);
        }
        else
        {
            textView.setVisibility(VISIBLE);
            imageView.setVisibility(GONE);
            setAlpha(selected ? 1.0f : 0.4f);
        }*/
    }

    public void setText(String text) {
        textView.setText(text);
    }

    public void setText(@StringRes int textResId) {
        textView.setText(textResId);
    }

    public void setDrawable(Drawable drawable) {
        imageView.setImageDrawable(drawable);
    }

    public void setDrawable(@DrawableRes int drawableResId) {
        imageView.setImageResource(drawableResId);
    }

    public void setEditMode(boolean editMode)
    {
        this.editMode = editMode;
        setEnabled(editMode);
        setSelected(isSelected());
    }
}

package com.nubytouch.crisiscare.ui.login;

import android.content.Context;
import android.graphics.drawable.Drawable;
import androidx.core.content.ContextCompat;
import androidx.appcompat.widget.AppCompatEditText;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.nubytouch.crisiscare.R;
import com.nubytouch.crisiscare.ui.activation.PasswordTransformationMethod;

public class PasswordEditText extends AppCompatEditText
{
    private boolean updating;

    private boolean isPasswordVisible = false;
    private Drawable icon;
    private int      showPasswordIcon;
    private int      hidePasswordIcon;

    public PasswordEditText(Context context)
    {
        super(context);
        init();
    }

    public PasswordEditText(Context context, String mask)
    {
        super(context);
        init();
    }

    public PasswordEditText(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        init();
    }

    public PasswordEditText(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init()
    {
        showPasswordIcon = R.drawable.ic_eye;
        hidePasswordIcon = R.drawable.ic_eye_off;

        setTransformationMethod(new PasswordTransformationMethod());
        addTextChangedListener(new TextWatcher()
        {
            private int previous;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after)
            {
                previous = s.length();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
            }

            @Override
            public void afterTextChanged(Editable s)
            {
                if (updating)
                    return;

                updating = true;
                if (previous < s.length() && (s.length() == 5 || s.length() == 11))
                    s.append("-");

                updating = false;
            }
        });

        setupIcon();
    }

    private void togglePassword()
    {
        this.isPasswordVisible = !this.isPasswordVisible;

        if (!isPasswordVisible)
            setTransformationMethod(new PasswordTransformationMethod());
        else
            setTransformationMethod(null);

        int textLength = getText().length();
        setSelection(textLength, textLength);
        setupIcon();
    }

    private Drawable getDrawable(int id)
    {
        return ContextCompat.getDrawable(getContext(), id);
    }

    private void setupIcon()
    {
        this.icon = this.isPasswordVisible ? getDrawable(this.showPasswordIcon) : getDrawable(hidePasswordIcon);

        setCompoundDrawablesWithIntrinsicBounds(null, null, this.icon, null);

        setCompoundDrawablePadding(10);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        if (event.getAction() == MotionEvent.ACTION_UP)
        {
            final int x = (int) event.getX();

            int iconWidth = this.icon.getBounds().width();
            if (x >= (getWidth() - getPaddingLeft()) - iconWidth && x <= getWidth() + iconWidth - getPaddingRight())
            {
                togglePassword();
                event.setAction(MotionEvent.ACTION_CANCEL);
            }
            ;
        }


        return super.onTouchEvent(event);
    }
}

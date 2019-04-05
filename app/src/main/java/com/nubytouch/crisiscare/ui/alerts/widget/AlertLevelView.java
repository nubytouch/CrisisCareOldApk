package com.nubytouch.crisiscare.ui.alerts.widget;

import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import com.nubytouch.crisiscare.R;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

public class AlertLevelView extends LinearLayout {

    private List<AlertToggleButton> buttons;

    private static final int[] COLORS = new int[]{
            R.color.alert_level_none,
            R.color.alert_level_low,
            R.color.alert_level_medium,
            R.color.alert_level_high
    };


    public AlertLevelView(Context context) {
        super(context);
        init();
    }

    public AlertLevelView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public AlertLevelView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public AlertLevelView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init()
    {
        setOrientation(LinearLayout.HORIZONTAL);
        setBackgroundColor(0xff333333);

        OnClickListener listener = new OnClickListener() {
            @Override
            public void onClick(View v) {
                for (AlertToggleButton button : buttons) {
                    button.setSelected(button == v);
                }
            }
        };

        LayoutParams params = new LayoutParams(0, LayoutParams.MATCH_PARENT);
        params.weight = 1;

        buttons = new ArrayList<>();

        for (int i  = 0; i < 4; i++)
        {
            AlertToggleButton button = new AlertToggleButton(getContext());
            button.setText(String.valueOf(i+1));
            button.setDrawable(R.drawable.ic_check);
            button.setBackgroundResource(COLORS[i]);
            button.setLayoutParams(params);
            button.setOnClickListener(listener);
            buttons.add(button);
            addView(button);
        }

        setSelectedLevel(0);
    }

    public void setSelectedLevel(int level)
    {
        for (int i = 0; i < buttons.size(); i++) {
            buttons.get(i).setSelected(i == level);
        }
    }

    public int getSelectedLevel()
    {
        int level = 0;

        for (int i = 0; i < buttons.size(); i++) {
            if (buttons.get(i).isSelected())
                return i;
        }

        return level;
    }

    public void setEditMode(boolean editMode)
    {
        for (AlertToggleButton button : buttons) {
            button.setEditMode(editMode);
        }
    }
}

package com.nubytouch.crisiscare.ui.settings;

import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.nubytouch.crisiscare.R;
import com.nubytouch.crisiscare.core.Session;
import com.nubytouch.crisiscare.data.model.User;

import java.util.List;

public class DeputyAdapter extends ArrayAdapter<User>
{
    private List<String>   selectedDeputies;

    public DeputyAdapter(Context context, int textViewResourceId)
    {
        super(context, textViewResourceId);

    }

    public DeputyAdapter(Context context, int resource, List<User> items)
    {
        super(context, resource, items);
    }

    public void setDeputies(List<String> selectedDeputies)
    {
        this.selectedDeputies = selectedDeputies;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent)
    {
        if (view == null)
        {
            view = LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_list_item_multiple_choice,
                                                                    parent,
                                                                    false);
        }

        User deputy = getItem(position);
        CheckedTextView textView = (CheckedTextView) view;

        textView.setText(deputy.getFullName());
        textView.setChecked(selectedDeputies.contains(deputy.getEmail()));

        return view;
    }
}

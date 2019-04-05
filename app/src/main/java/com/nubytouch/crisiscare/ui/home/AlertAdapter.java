package com.nubytouch.crisiscare.ui.home;

import android.content.Intent;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import timber.log.Timber;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.nubytouch.crisiscare.R;
import com.nubytouch.crisiscare.data.model.Alert;
import com.nubytouch.crisiscare.ui.alerts.AlertInformationActivity;
import com.nubytouch.crisiscare.ui.alerts.AlertUtil;
import com.nubytouch.crisiscare.utils.DateUtil;

import java.util.List;

public class AlertAdapter extends BaseAdapter
{
    private List<Alert> data;

    public AlertAdapter(List<Alert> data)
    {
        this.data = data;
    }

    public void setData(List<Alert> data)
    {
        this.data = data;
        notifyDataSetChanged();
    }

    @Override
    public int getCount()
    {
        return data.size();
    }

    @Override
    public Object getItem(int position)
    {
        return data.get(position);
    }

    @Override
    public long getItemId(int position)
    {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent)
    {
        if (view == null)
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_alert_flip, parent, false);

        final Alert alert = (Alert) getItem(position);

        String    date           = DateUtil.getFormattedDateSmall(alert.getPubDate());
        String    prefix         = date + " " /*+ AlertUtil.getPriorityString(alert.getPriority())*/;
        Spannable spannable     = new SpannableString(prefix + " " + alert.getTitle());
        spannable.setSpan(new ForegroundColorSpan(Color.RED), 0, prefix.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        ((TextView) view.findViewById(R.id.textview)).setText(spannable);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(v.getContext(), AlertInformationActivity.class);
                intent.putExtra(AlertInformationActivity.EXTRA_ALERT, alert);
                v.getContext().startActivity(intent);
            }
        });

        return view;
    }
}

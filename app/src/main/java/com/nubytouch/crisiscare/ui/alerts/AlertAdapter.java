package com.nubytouch.crisiscare.ui.alerts;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nubytouch.crisiscare.R;
import com.nubytouch.crisiscare.data.model.Alert;
import com.nubytouch.crisiscare.utils.DateUtil;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.RecyclerView;

class AlertAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
{
    private List<Alert> data;

    AlertAdapter(List<Alert> data)
    {
        this.data = data;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        return new AlertViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_alert, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position)
    {
        AlertViewHolder viewHolder = (AlertViewHolder) holder;
        Alert alert = data.get(position);

        viewHolder.alertLevel.setText(String.valueOf(alert.getPriority() + 1));
        ViewCompat.setBackgroundTintList(viewHolder.alertLevel,
                ColorStateList.valueOf(AlertUtil.getColorForLevel(alert.getPriority())));

        int stat = AlertUtil.getStatusName(alert.getStatusId());

        if (stat > 0)
        {
            viewHolder.statusLevel.setText(stat);
            ViewCompat.setBackgroundTintList(viewHolder.statusLevel,
                    ColorStateList.valueOf(AlertUtil.getColorForStatus(alert.getStatusId())));
            viewHolder.statusLevel.setVisibility(View.VISIBLE);
        }
        else
        {
            viewHolder.statusLevel.setVisibility(View.GONE);
        }

        viewHolder.date.setText(DateUtil.getFormattedDateSmall(alert.getPubDate()));
        viewHolder.text.setText(alert.getTitle());
    }

    @Override
    public int getItemCount()
    {
        return data.size();
    }

    public class AlertViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        final TextView alertLevel;
        final TextView statusLevel;
        final TextView date;
        final TextView text;

        private AlertViewHolder(View itemView)
        {
            super(itemView);

            alertLevel = itemView.findViewById(R.id.alert_level);
            statusLevel = itemView.findViewById(R.id.status_level);
            date = itemView.findViewById(R.id.date);
            text = itemView.findViewById(R.id.text);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v)
        {
            Alert alert = data.get(getAdapterPosition());

            Intent intent = new Intent(itemView.getContext(), AlertInformationActivity.class);
            intent.putExtra(AlertInformationActivity.EXTRA_ALERT, alert);
            itemView.getContext().startActivity(intent);
        }
    }
}

package com.nubytouch.crisiscare.ui.alerts;


import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nubytouch.crisiscare.R;
import com.nubytouch.crisiscare.core.Session;
import com.nubytouch.crisiscare.data.model.HandRail;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

class HandrailAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
{
    private ArrayList<Object> data;
    private DateFormat        df;
    private DateFormat        tdf;

    HandrailAdapter()
    {
        data = new ArrayList<>();
        df = SimpleDateFormat.getDateInstance(SimpleDateFormat.MEDIUM);
        tdf = SimpleDateFormat.getTimeInstance(SimpleDateFormat.SHORT);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        if (viewType == 0)
        {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_header, parent, false);
            return new HeaderViewHolder(view);
        }
        else
        {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_handrail, parent, false);
            return new ViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position)
    {
        if (getItemViewType(position) == 0)
        {
            Header           header     = (Header) data.get(position);
            HeaderViewHolder viewHolder = (HeaderViewHolder) holder;


            viewHolder.label.setText(df.format(new Date(header.date)));
            viewHolder.label.setTextColor(Session.getPrimaryColor());
        }
        else
        {
            HandRail   handRail   = (HandRail) data.get(position);
            Object     previous   = data.get(position - 1);
            ViewHolder viewHolder = (ViewHolder) holder;

            viewHolder.origin.setText(handRail.getSource());
            viewHolder.date.setText(tdf.format(new Date(handRail.getDate())));
            viewHolder.description.setText(handRail.getDescription());
            viewHolder.divider.setVisibility(previous instanceof HandRail
                                             ? View.VISIBLE
                                             : View.GONE);
        }
    }

    public void setData(ArrayList<Object> data)
    {
        this.data = data;
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position)
    {
        return data.get(position) instanceof Header ? 0 : 1;
    }

    @Override
    public int getItemCount()
    {
        return data.size();
    }

    public static class Header
    {
        public final long date;

        public Header(long date) {this.date = date;}
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        public final TextView origin;
        public final TextView date;
        public final TextView description;
        public final View     divider;

        public ViewHolder(View itemView)
        {
            super(itemView);

            origin = (TextView) itemView.findViewById(R.id.origin);
            date = (TextView) itemView.findViewById(R.id.date);
            description = (TextView) itemView.findViewById(R.id.description);
            divider = itemView.findViewById(R.id.divider);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v)
        {

        }
    }

    public static class HeaderViewHolder extends RecyclerView.ViewHolder
    {
        public final TextView label;

        public HeaderViewHolder(View itemView)
        {
            super(itemView);

            label = (TextView) itemView.findViewById(R.id.label);
        }
    }
}

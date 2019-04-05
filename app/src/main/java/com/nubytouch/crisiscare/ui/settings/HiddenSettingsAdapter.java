package com.nubytouch.crisiscare.ui.settings;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.nubytouch.crisiscare.R;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class HiddenSettingsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
{
    private static final int TYPE_HEADER = 0;
    private static final int TYPE_COLOR = 1;
    private List<Object> data;

    public HiddenSettingsAdapter(List<Object> data)
    {
        this.data = data;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        if (viewType == TYPE_COLOR)
            return new ColorHolder(inflater.inflate(R.layout.item_hidden_settings_color, parent, false));

        return new HeaderHolder(inflater.inflate(R.layout.item_hidden_settings_header, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position)
    {
        int viewType = getItemViewType(position);

        if (viewType == TYPE_HEADER)
        {
            HeaderHolder viewHolder = (HeaderHolder) holder;
            Header item = (Header) data.get(position);

            viewHolder.label.setText(item.label + " : ");
            viewHolder.name.setText(item.name);
        }
        else if (viewType == TYPE_COLOR)
        {
            ColorHolder viewHolder = (ColorHolder) holder;
            EntityColor item = (EntityColor) data.get(position);

            viewHolder.colorView.setBackgroundColor(item.color);
            viewHolder.colorValue.setText("#" + Integer.toHexString(item.color).toUpperCase());
        }
    }

    @Override
    public int getItemViewType(int position)
    {
        Object o = data.get(position);

        if (o instanceof Header)
            return TYPE_HEADER;
        if (o instanceof EntityColor)
            return TYPE_COLOR;

        return TYPE_HEADER;
    }

    @Override
    public int getItemCount()
    {
        return data.size();
    }

    class HeaderHolder extends RecyclerView.ViewHolder
    {
        final TextView label;
        final TextView name;

        HeaderHolder(@NonNull View itemView)
        {
            super(itemView);

            label = itemView.findViewById(R.id.label);
            name = itemView.findViewById(R.id.name);
        }
    }

    class ColorHolder extends RecyclerView.ViewHolder
    {
        final View colorView;
        final TextView colorValue;

        ColorHolder(@NonNull View itemView)
        {
            super(itemView);

            colorView = itemView.findViewById(R.id.color_view);
            colorValue = itemView.findViewById(R.id.color_value);
        }
    }

    class RuleHolder extends RecyclerView.ViewHolder
    {
        final CheckBox viewHandrail;
        final CheckBox editHandrail;
        final CheckBox submitChecklist;
        final CheckBox forwardDocument;
        final CheckBox createAlerts;

        RuleHolder(@NonNull View itemView)
        {
            super(itemView);

            viewHandrail = itemView.findViewById(R.id.view_handrail);
            editHandrail = itemView.findViewById(R.id.edit_handrail);
            submitChecklist = itemView.findViewById(R.id.submit_checklist);
            forwardDocument = itemView.findViewById(R.id.forward_document);
            createAlerts = itemView.findViewById(R.id.create_alerts);
        }
    }

   public static class Header
    {
        final String label;
        final String name;

        Header(String label, String name)
        {
            this.label = label;
            this.name = name;
        }
    }

    public static class EntityColor
    {
        final int color;

        EntityColor(int color) {this.color = color;}
    }
}

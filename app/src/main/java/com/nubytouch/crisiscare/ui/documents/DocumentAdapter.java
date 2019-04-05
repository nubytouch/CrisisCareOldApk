package com.nubytouch.crisiscare.ui.documents;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nubytouch.crisiscare.R;
import com.nubytouch.crisiscare.data.model.Document;

class DocumentAdapter extends BaseAdapter
{
    private List<Object> data;

    public DocumentAdapter(List<Object> data)
    {
        this.data = data;
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
    public int getItemViewType(int position)
    {
        if (getItem(position) instanceof DocumentFolder)
            return 0;

        return 1;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent)
    {
        if (view == null)
        {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_document, parent, false);
            view.setTag(new ViewHolder(view));
        }

        ViewHolder holder = (ViewHolder) view.getTag();

        if (getItemViewType(position) == 0)
        {
            DocumentFolder document = (DocumentFolder) getItem(position);
            holder.name.setText(document.name);
            holder.icon.setVisibility(View.VISIBLE);
        }
        else
        {
            Document document = (Document) getItem(position);
            holder.name.setText(document.getTitle());
            holder.icon.setVisibility(View.GONE);
        }

        return view;
    }

    private static final class ViewHolder
    {
        public final ImageView icon;
        public final TextView  name;

        private ViewHolder(View view)
        {
            icon = (ImageView) view.findViewById(R.id.icon);
            name = (TextView) view.findViewById(R.id.name);
        }
    }
}

package com.nubytouch.crisiscare.ui.home;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.nubytouch.crisiscare.R;
import com.nubytouch.crisiscare.core.Session;
import com.nubytouch.crisiscare.data.model.User;
import com.nubytouch.crisiscare.ui.contacts.ContactInformationsDialog;

public class NavigationDrawerAdapter extends BaseAdapter
{
    @Override
    public int getCount()
    {
        return Session.getUser().getDeputies().size();
    }

    @Override
    public Object getItem(int position)
    {
        return Session.getUser().getDeputies().get(position);
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
        {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_drawer_deputy, parent, false);
            TextView    name   = (TextView) view.findViewById(R.id.name);
            TextView    email  = (TextView) view.findViewById(R.id.email);
            ImageButton button = (ImageButton) view.findViewById(R.id.button);
            view.setTag(new ViewHolder(name, email, button));
        }

        ViewHolder viewHolder = (ViewHolder) view.getTag();
        final User deputy     = (User) getItem(position);

        viewHolder.name.setText(deputy.getFullName());
        viewHolder.email.setText(deputy.getEmail());

//        viewHolder.button.setColorFilter(Session.getPrimaryColor());

        viewHolder.button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                new ContactInformationsDialog(v.getContext(), deputy).show();
            }
        });

        return view;
    }

    private class ViewHolder
    {
        public final TextView    name;
        public final TextView    email;
        public final ImageButton button;

        private ViewHolder(TextView name, TextView email, ImageButton button)
        {
            this.name = name;
            this.email = email;
            this.button = button;
        }
    }
}

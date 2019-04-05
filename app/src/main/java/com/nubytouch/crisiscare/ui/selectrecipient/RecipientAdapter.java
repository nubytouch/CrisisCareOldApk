package com.nubytouch.crisiscare.ui.selectrecipient;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckedTextView;

import com.nubytouch.crisiscare.R;
import com.nubytouch.crisiscare.data.model.User;
import com.nubytouch.crisiscare.ui.contacts.GroupWrapper;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import static android.view.View.GONE;

public class RecipientAdapter extends RecyclerView.Adapter<RecipientAdapter.RecipientViewHolder>
{
    private static final int TYPE_USER  = 0;
    private static final int TYPE_GROUP = 1;

    private  List<RecipientWrapper>                                                          data;
    private Map<RecipientWrapper, List<RecipientWrapper>> map;

    public RecipientAdapter(List<RecipientWrapper> data, Map<RecipientWrapper, List<RecipientWrapper>> map)
    {
        this.data = data;
        this.map = map;
    }

    @Override
    public int getItemViewType(int position)
    {
        return data.get(position).isUser() ? TYPE_USER : TYPE_GROUP;
    }

    @NonNull
    @Override
    public RecipientViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        int layout = viewType == TYPE_USER
                     ? android.R.layout.simple_list_item_multiple_choice
                     : R.layout.item_selectable_department_header;

        return new RecipientViewHolder(LayoutInflater.from(parent.getContext()).inflate(layout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecipientViewHolder holder, int position)
    {
        final RecipientWrapper wrapper = data.get(position);

        if (getItemViewType(position) == TYPE_USER)
        {
            holder.textView.setText(((User) wrapper.recipient).getFullName());
        }
        else
        {
            holder.textView.setText(((GroupWrapper) wrapper.recipient).name);
            holder.divider.setVisibility(position == 0 ? GONE : View.VISIBLE);
        }

        holder.textView.setChecked(wrapper.selected);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                updateWrapper(wrapper);
            }
        });
    }

    @Override
    public int getItemCount()
    {
        return data.size();
    }

    public static class RecipientViewHolder extends RecyclerView.ViewHolder
    {
        final CheckedTextView textView;
        public final View            divider;

        RecipientViewHolder(@NonNull View itemView)
        {
            super(itemView);

            textView = itemView.findViewById(android.R.id.text1);
            divider = itemView.findViewById(R.id.divider);
        }
    }

    private void updateWrapper(RecipientWrapper wrapper)
    {
        wrapper.selected = !wrapper.selected;

        // If group
        if (!wrapper.isUser())
        {
            for (RecipientWrapper wrapper1 : map.get(wrapper))
            {
                wrapper1.selected = wrapper.selected;
                updateUserWrapper(wrapper1);
            }
        }
        else
        {
            updateUserWrapper(wrapper);
        }


        // Update Groups state
        for (RecipientWrapper recipientWrapper : map.keySet())
        {
            boolean selected = true;

            for (RecipientWrapper recipientWrapper1 : map.get(recipientWrapper))
            {
                selected &= recipientWrapper1.selected;
            }

            recipientWrapper.selected = selected;
        }

        notifyDataSetChanged();
    }

    private void updateUserWrapper(RecipientWrapper wrapper)
    {
        //if (wrapper.selected)
        //{
            User   user   = (User) wrapper.recipient;
            String userId = user.getId();

            for (RecipientWrapper rec : data)
            {
                if (rec.isUser() && wrapper != rec)
                {
                    User recUser = (User) rec.recipient;

                    if (userId.equals(recUser.getId()))
                    {
                        rec.selected = wrapper.selected;
                    }
                }
            }
        //}
    }

    public Set<String> getSelectedUsers()
    {
        Set<String> selectedUsers = new HashSet<>();

        for (RecipientWrapper wrapper : data)
        {
            if (wrapper.isUser() && wrapper.selected)
                selectedUsers.add(((User) wrapper.recipient).getEmail());
        }

        return selectedUsers;
    }
}

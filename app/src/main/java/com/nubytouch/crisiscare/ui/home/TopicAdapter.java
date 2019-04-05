package com.nubytouch.crisiscare.ui.home;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.DrawableRes;
import androidx.recyclerview.widget.RecyclerView;
import com.nubytouch.crisiscare.R;
import com.nubytouch.crisiscare.core.Session;
import com.nubytouch.crisiscare.data.model.Topic;
import com.nubytouch.crisiscare.image.ImageLoader;
import com.nubytouch.crisiscare.ui.documents.DocumentsActivity;
import com.nubytouch.crisiscare.ui.web.BrowserActivity;

import java.util.ArrayList;
import java.util.List;

class TopicAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
{
    private ArrayList<Topic> data;

    TopicAdapter(List<Topic> data)
    {
        this.data = new ArrayList<>(data);

        // Add header
        this.data.add(0, new Topic());
    }

    @Override
    public int getItemViewType(int position)
    {
        return position == 0 ? 0 : 1;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        if (viewType == 0)
        {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_topic_header, parent, false);
            return new HeaderViewHolder(view);
        }
        else
        {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_topic, parent, false);
            return new ViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position)
    {
        if (getItemViewType(position) == 0)
        {
            HeaderViewHolder viewHolder = (HeaderViewHolder) holder;

            viewHolder.filiale.setText(Session.getUser().getSiteName());

            viewHolder.filiale.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
            viewHolder.filiale.setEnabled(false);

            ImageLoader.fadeLoad(Session.getClientLogoURL(), viewHolder.logo);
        }
        else
        {
            Topic topic = data.get(position);
            ViewHolder viewHolder = (ViewHolder) holder;

            viewHolder.title.setText(topic.getTitle());

            if (topic.isDocuments())
                setTitleDrawable(viewHolder.title, R.drawable.ic_action_document);
            else if (topic.isVideos())
                setTitleDrawable(viewHolder.title, R.drawable.ic_action_videos);
            else if (topic.isContacts())
                setTitleDrawable(viewHolder.title, R.drawable.ic_action_contacts);
            else if (topic.isNews())
                setTitleDrawable(viewHolder.title, R.drawable.ic_action_news);
            else if (topic.isAlerts())
                setTitleDrawable(viewHolder.title, R.drawable.ic_action_alert);
            else if (topic.isChecklists())
                setTitleDrawable(viewHolder.title, R.drawable.ic_checklists);
            else if (topic.isNotes())
                setTitleDrawable(viewHolder.title, R.drawable.ic_note);
            else if (topic.isMessages())
                setTitleDrawable(viewHolder.title, R.drawable.ic_action_message);
            else if (topic.isFallbackSites())
                setTitleDrawable(viewHolder.title, R.drawable.ic_map);
            else
                setTitleDrawable(viewHolder.title, R.drawable.ic_action_about);

            ((View) viewHolder.title.getParent()).setBackgroundColor(topic.getBackgroundColor());
        }
    }

    private void setTitleDrawable(TextView title, @DrawableRes int drawableRes)
    {
        title.setCompoundDrawablesWithIntrinsicBounds(drawableRes, 0, 0, 0);
    }

    @Override
    public int getItemCount()
    {
        return data.size();
    }

    public class HeaderViewHolder extends RecyclerView.ViewHolder
    {
        public final ImageView logo;
        final TextView filiale;

        HeaderViewHolder(View itemView)
        {
            super(itemView);

            logo = (ImageView) itemView.findViewById(R.id.logo);
            filiale = (TextView) itemView.findViewById(R.id.tvFiliale);

        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        public final TextView title;

        private ViewHolder(View itemView)
        {
            super(itemView);

            title = (TextView) itemView.findViewById(R.id.tvTitle);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v)
        {
            Topic topic = data.get(getAdapterPosition());

            if (topic.isDocuments())
            {
                Intent intent = new Intent(v.getContext(), DocumentsActivity.class);
                intent.putExtra(DocumentsActivity.EXTRA_TITLE, topic.getTitle());
                v.getContext().startActivity(intent);
            }
            else
            {

                // Everything's that not a document is handled by the webview
                String url = topic.getLocalLink();

                if (topic.getExternalLink() != null && topic.getExternalLink().length() > 0)
                    url = topic.getExternalLink();

                Intent intent = new Intent(v.getContext(), BrowserActivity.class);
                intent.putExtra(BrowserActivity.EXTRA_URI, url);
                intent.putExtra(BrowserActivity.EXTRA_TITLE, topic.getTitle());
                v.getContext().startActivity(intent);

            }
        }
    }
}

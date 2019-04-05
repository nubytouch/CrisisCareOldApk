package com.nubytouch.crisiscare.data.dto;

import android.graphics.Color;
import androidx.core.content.ContextCompat;

import com.google.gson.annotations.SerializedName;
import com.nubytouch.crisiscare.CrisisCare;
import com.nubytouch.crisiscare.R;
import com.nubytouch.crisiscare.data.DataUtil;
import com.nubytouch.crisiscare.data.model.Topic;

public class TopicDTO
{
    @SerializedName("Guid")
    private String id;
    @SerializedName("Title")
    private String title;
    @SerializedName("Link")
    private String link;
    @SerializedName("Category")
    private String category;
    @SerializedName("Order")
    private int    order;
    @SerializedName("BackColor")
    private String backgroundColor;
    @SerializedName("ExternalLink")
    private String externalLink;

    public void setId(String id)
    {
        this.id = id;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public void setLink(String link)
    {
        this.link = link;
    }

    public void setCategory(String category)
    {
        this.category = category;
    }

    public void setOrder(int order)
    {
        this.order = order;
    }

    public void setBackgroundColor(String backgroundColor)
    {
        this.backgroundColor = backgroundColor;
    }

    public void setExternalLink(String externalLink)
    {
        this.externalLink = externalLink;
    }

    public Topic buildTopic()
    {
        Topic topic = new Topic();
        topic.setId(id + DataUtil.generateUUID());
        topic.setTitle(title);
        topic.setLink(link);
        topic.setCategory(category);
        topic.setOrder(order);
        topic.setBackgroundColor(parseColor());
        topic.setExternalLink(externalLink);
        return topic;
    }

    private int parseColor()
    {
        if (backgroundColor != null)
            return Color.parseColor(backgroundColor);

        return ContextCompat.getColor(CrisisCare.getInstance(), R.color.dark_gray);
    }
}

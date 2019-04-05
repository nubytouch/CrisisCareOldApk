package com.nubytouch.crisiscare.data.dto;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TopicWrapperDTO
{
    @SerializedName("Items")
    private List<TopicDTO> topics;

    public List<TopicDTO> getTopics()
    {
        return topics;
    }

    public void setTopics(List<TopicDTO> topics)
    {
        this.topics = topics;
    }
}

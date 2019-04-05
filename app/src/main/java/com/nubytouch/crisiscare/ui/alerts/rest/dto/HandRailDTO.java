package com.nubytouch.crisiscare.ui.alerts.rest.dto;

import com.google.gson.annotations.SerializedName;
import com.nubytouch.crisiscare.data.model.Alert;
import com.nubytouch.crisiscare.data.model.HandRail;
import com.nubytouch.crisiscare.utils.DateUtil;

public class HandRailDTO
{
    @SerializedName("guid")
    public final String   guid;
    @SerializedName("description")
    public final String   description;
    @SerializedName("pubDate")
    public final String   date;
    @SerializedName("source")
    public final String   source;
    @SerializedName("sharedWithIds")
    public final String[] sharedWith;


    public HandRailDTO(String guid, String description, String date, String source, String[] sharedWith)
    {
        this.guid = guid;
        this.description = description;
        this.date = date;
        this.source = source;
        this.sharedWith = sharedWith;
    }

    public HandRail buildModel()
    {
        HandRail model = new HandRail();

        model.setGuid(guid);
        model.setDescription(description);
        model.setDate(DateUtil.getTimestampFromDotNetDate(date));
        model.setSource(source);
        model.setSharedWith(sharedWith);

        return model;
    }
}

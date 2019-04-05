package com.nubytouch.crisiscare.ui.alerts.rest.dto;

import com.google.gson.annotations.SerializedName;
import com.nubytouch.crisiscare.data.model.Alert;
import com.nubytouch.crisiscare.utils.DateUtil;

public class AlertDTO
{
    @SerializedName("guid")
    public final String guid;
    @SerializedName("author")
    public final String author;
    @SerializedName("title")
    public final String title;
    @SerializedName("pictureLink")
    public final String picture;
    @SerializedName("address")
    public final String address;
    @SerializedName("latitude")
    public final double latitude;
    @SerializedName("longitude")
    public final double longitude;
    @SerializedName("link")
    public final String link;
    @SerializedName("priority")
    public final int    priority;
    @SerializedName("statusId")
    public final int    statusId;
    @SerializedName("statusName")
    public final String statusName;
    @SerializedName("pubDate")
    public final String pubDate;

    public AlertDTO(String guid, String author, String title, String picture, String address, double latitude, double
            longitude, String link, int priority, int statusId, String statusName, String pubDate)
    {
        this.guid = guid;
        this.author = author;
        this.title = title;
        this.picture = picture;
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
        this.link = link;
        this.priority = priority;
        this.statusId = statusId;
        this.statusName = statusName;
        this.pubDate = pubDate;
    }

    public Alert buildAlert()
    {
        Alert alert = new Alert();

        alert.setId(guid);
        alert.setAuthor(author);
        alert.setTitle(title);
        alert.setImageUrl(picture);
        alert.setAddress(address);
        alert.setLatitude(latitude);
        alert.setLongitude(longitude);
        alert.setUrl(link);
        alert.setPriority(priority);
        alert.setStatusId(statusId);
        alert.setStatusName(statusName);
        alert.setPubDate(DateUtil.getTimestampFromDotNetDate(pubDate));

        return alert;
    }
}

package com.nubytouch.crisiscare.data.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Alert implements Parcelable
{
    public static final int PRIORITY_NONE   = 0;
    public static final int PRIORITY_LOW    = 1;
    public static final int PRIORITY_MEDIUM = 2;
    public static final int PRIORITY_HIGH   = 3;

    public static final int STATUS_NEW         = 0;
    public static final int STATUS_OPEN        = 1; // Red
    public static final int STATUS_RESOLVED    = 2; // Green
    public static final int STATUS_REJECTED    = 3; // Black
    public static final int STATUS_ARCHIVED    = 9;

    private String id;
    private String author;
    private String title;
    private String imageUrl;
    private String address;
    private double latitude;
    private double longitude;
    private String url;
    private int    priority;
    private int    statusId;
    private String statusName;
    private long   pubDate;

    public Alert()
    {

    }

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public String getAddress()
    {
        return address;
    }

    public void setAddress(String address)
    {
        this.address = address;
    }

    public String getAuthor()
    {
        return author;
    }

    public void setAuthor(String author)
    {
        this.author = author;
    }

    public String getImageUrl()
    {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl)
    {
        this.imageUrl = imageUrl;
    }

    public double getLatitude()
    {
        return latitude;
    }

    public void setLatitude(double latitude)
    {
        this.latitude = latitude;
    }

    public double getLongitude()
    {
        return longitude;
    }

    public void setLongitude(double longitude)
    {
        this.longitude = longitude;
    }

    public int getPriority()
    {
        return priority;
    }

    public void setPriority(int priority)
    {
        this.priority = priority;
    }

    public long getPubDate()
    {
        return pubDate;
    }

    public void setPubDate(long pubDate)
    {
        this.pubDate = pubDate;
    }

    public int getStatusId()
    {
        return statusId;
    }

    public void setStatusId(int statusId)
    {
        this.statusId = statusId;
    }

    public String getStatusName()
    {
        return statusName;
    }

    public void setStatusName(String statusName)
    {
        this.statusName = statusName;
    }

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public String getUrl()
    {
        return url;
    }

    public void setUrl(String url)
    {
        this.url = url;
    }


    //___ Parcelable stuff ___

    protected Alert(Parcel in) {
        id = in.readString();
        author = in.readString();
        title = in.readString();
        imageUrl = in.readString();
        address = in.readString();
        latitude = in.readDouble();
        longitude = in.readDouble();
        url = in.readString();
        priority = in.readInt();
        statusId = in.readInt();
        statusName = in.readString();
        pubDate = in.readLong();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(author);
        dest.writeString(title);
        dest.writeString(imageUrl);
        dest.writeString(address);
        dest.writeDouble(latitude);
        dest.writeDouble(longitude);
        dest.writeString(url);
        dest.writeInt(priority);
        dest.writeInt(statusId);
        dest.writeString(statusName);
        dest.writeLong(pubDate);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Alert> CREATOR = new Parcelable.Creator<Alert>() {
        @Override
        public Alert createFromParcel(Parcel in) {
            return new Alert(in);
        }

        @Override
        public Alert[] newArray(int size) {
            return new Alert[size];
        }
    };
}

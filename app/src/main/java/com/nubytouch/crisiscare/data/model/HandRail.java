package com.nubytouch.crisiscare.data.model;


public class HandRail
{
    private String   guid;
    private String   description;
    private long     date;
    private String   source;
    private String[] sharedWith;

    public HandRail()
    {

    }

    public String getGuid()
    {
        return guid;
    }

    public void setGuid(String guid)
    {
        this.guid = guid;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public long getDate()
    {
        return date;
    }

    public void setDate(long date)
    {
        this.date = date;
    }

    public String getSource()
    {
        return source;
    }

    public void setSource(String source)
    {
        this.source = source;
    }

    public String[] getSharedWith()
    {
        return sharedWith;
    }

    public void setSharedWith(String[] sharedWith)
    {
        this.sharedWith = sharedWith;
    }
}

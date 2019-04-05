package com.nubytouch.crisiscare.data.model;


public class Metadata
{
    private String id;
    private float version;
    private int updateDate;

    public Metadata()
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

    public float getVersion()
    {
        return version;
    }

    public void setVersion(float version)
    {
        this.version = version;
    }

    public int getUpdateDate()
    {
        return updateDate;
    }

    public void setUpdateDate(int updateDate)
    {
        this.updateDate = updateDate;
    }
}

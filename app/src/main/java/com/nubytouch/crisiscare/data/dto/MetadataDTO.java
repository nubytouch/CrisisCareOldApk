package com.nubytouch.crisiscare.data.dto;

import com.google.gson.annotations.SerializedName;
import com.nubytouch.crisiscare.data.model.Metadata;

public class MetadataDTO
{
    @SerializedName("Version")
    private String version;
    @SerializedName("UpdateDate")
    private int updateDate;

    // Other variables not defined because not used


    public void setVersion(String version)
    {
        this.version = version;
    }

    public void setUpdateDate(int updateDate)
    {
        this.updateDate = updateDate;
    }

    public Metadata buildMetadata()
    {
        Metadata metadata = new Metadata();
        try
        {
            metadata.setVersion(Float.parseFloat(version));
        }
        catch (Exception e)
        {
            metadata.setVersion(0);
        }

        metadata.setUpdateDate(updateDate);

        return metadata;
    }
}

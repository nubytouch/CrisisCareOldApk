package com.nubytouch.crisiscare.data.dto;

import com.google.gson.annotations.SerializedName;

public class DataPackageDTO
{
    @SerializedName("Version")
    private int version;
    @SerializedName("HasModifiedDocs")
    private boolean hasModifiedDocs;
    @SerializedName("hasToDownloadDocs")
    private boolean hasToDownoaldDocs;
    @SerializedName("hasAlerts")
    private boolean alertsEnabled;
    @SerializedName("DateUpdated")
    private String updateDate;

    public DataPackageDTO()
    {

    }

    public int getVersion()
    {
        return version;
    }

    public void setVersion(int version)
    {
        this.version = version;
    }

    public boolean hasModifiedDocs()
    {
        return hasModifiedDocs;
    }

    public void setHasModifiedDocs(boolean hasModifiedDocs)
    {
        this.hasModifiedDocs = hasModifiedDocs;
    }

    public boolean hasToDownoaldDocs()
    {
        return hasToDownoaldDocs;
    }

    public void setHasToDownoaldDocs(boolean hasToDownoaldDocs)
    {
        this.hasToDownoaldDocs = hasToDownoaldDocs;
    }

    public boolean isAlertsEnabled()
    {
        return alertsEnabled;
    }

    public void setAlertsEnabled(boolean alertsEnabled)
    {
        this.alertsEnabled = alertsEnabled;
    }

    public String getUpdateDate()
    {
        return updateDate;
    }

    public void setUpdateDate(String updateDate)
    {
        this.updateDate = updateDate;
    }
}

package com.nubytouch.crisiscare.ui.alerts.rest.dto;

import com.google.gson.annotations.SerializedName;
import com.nubytouch.crisiscare.data.model.Alert;

import java.util.ArrayList;
import java.util.List;

public class AlertWrapperDTO
{
    @SerializedName("GetAlertsByUsernameResult")
    public final ArrayList<AlertDTO> alerts;

    public AlertWrapperDTO(ArrayList<AlertDTO> alerts)
    {
        this.alerts = alerts;
    }


    public List<Alert> getAlerts()
    {
        List<Alert> alerts = new ArrayList<>();

        if (this.alerts != null)
        {
            for (AlertDTO alert : this.alerts)
            {
                alerts.add(alert.buildAlert());
            }
        }

        return alerts;
    }
}

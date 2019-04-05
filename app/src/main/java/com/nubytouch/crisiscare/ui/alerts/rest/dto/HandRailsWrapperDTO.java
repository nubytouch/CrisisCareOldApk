package com.nubytouch.crisiscare.ui.alerts.rest.dto;

import com.google.gson.annotations.SerializedName;
import com.nubytouch.crisiscare.data.model.HandRail;

import java.util.ArrayList;

public class HandRailsWrapperDTO
{
    @SerializedName("GetHandrailByUsernameResult")
    public final ArrayList<HandRailDTO> handRails;

    public HandRailsWrapperDTO(ArrayList<HandRailDTO> handRails)
    {
        this.handRails = handRails;
    }


    public ArrayList<HandRail> getHandRails()
    {
        ArrayList<HandRail> models = new ArrayList<>();

        if (this.handRails != null)
        {
            for (HandRailDTO dto : this.handRails)
            {
                models.add(dto.buildModel());
            }
        }

        return models;
    }
}

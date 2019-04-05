package com.nubytouch.crisiscare.ui.alerts.rest;

import com.google.gson.annotations.SerializedName;
import com.nubytouch.crisiscare.rest.Credentials;
import com.nubytouch.crisiscare.ui.alerts.rest.dto.AlertWrapperDTO;
import com.nubytouch.crisiscare.ui.alerts.rest.dto.HandRailsWrapperDTO;
import com.nubytouch.crisiscare.ui.alerts.rest.dto.SendHandrailResultDTO;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface AlertService
{
    @POST("alerts")
    Call<AlertWrapperDTO> getAlerts(@Body Credentials credentials);

    @POST("handrails")
    Call<HandRailsWrapperDTO> getHandRail(@Body GetHandrailsData data);

    @POST("handrails/insert")
    Call<SendHandrailResultDTO> sendHandRail(@Body SendHandrailData data);

    class GetHandrailsData extends Credentials
    {
        @SerializedName("guid")
        public final String guid;

        public GetHandrailsData(String guid) {this.guid = guid;}
    }

    class SendHandrailData extends Credentials
    {
        @SerializedName("guid")
        public final String alertId;
        @SerializedName("source")
        public final String origin;
        @SerializedName("pubDate")
        public final String date;
        @SerializedName("description")
        public final String description;

        public SendHandrailData(String alertId, String origin, String date, String description)
        {
            super();

            this.alertId = alertId;
            this.origin = origin;
            this.date = date;
            this.description = description;
        }
    }
}

package com.nubytouch.crisiscare.ui.home.rest;

import com.google.gson.annotations.SerializedName;
import com.nubytouch.crisiscare.rest.Credentials;
import com.nubytouch.crisiscare.ui.home.rest.dto.SendAlertDTO;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface AlertService
{
    @POST("alert/insert")
    Call<SendAlertDTO> sendAlert(@Body SendAlert alert);

    class SendAlert extends Credentials
    {
        @SerializedName("title")
        public final String title;
        @SerializedName("author")
        public final String author;
        @SerializedName("priority")
        public final String priority;
        @SerializedName("latitude")
        public final String latitude;
        @SerializedName("longitude")
        public final String longitude;

        public SendAlert(String title, String priority, double latitude, double longitude)
        {
            author = username;
            this.title = title;
            this.priority = priority;
            this.latitude = String.valueOf(latitude);
            this.longitude = String.valueOf(longitude);
        }
    }
}

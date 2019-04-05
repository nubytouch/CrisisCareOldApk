package com.nubytouch.crisiscare.ui.sendAlert.rest;

import com.google.gson.annotations.SerializedName;
import com.nubytouch.crisiscare.rest.Credentials;
import com.nubytouch.crisiscare.ui.sendAlert.rest.dto.SendAlertResultDTO;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface SendAlertService
{
    @POST("alert/insert")
    Call<SendAlertResultDTO> sendAlert(@Body AlertData data);

    @Headers("Content-type: application/x-www-form-urlencoded; charset=UTF8")
    @POST("upload-image")
    Call<Boolean> sendAlertImage(@Header("guid") String alertId,
                                            @Body RequestBody photo);

    class AlertData extends Credentials
    {
        @SerializedName("title")
        public final String title;
        @SerializedName("author")
        public final String author;
        @SerializedName("priority")
        public final String priority;
        @SerializedName("recipients")
        public final String recipients;
        @SerializedName("latitude")
        public final double latitude;
        @SerializedName("longitude")
        public final double longitude;

        public AlertData(String title, int priority, String recipients, double latitude, double longitude)
        {
            super();
            author = username;
            this.title = title;
            this.priority = String.valueOf(priority);
            this.recipients = recipients;
            this.latitude = latitude;
            this.longitude = longitude;
        }
    }
}

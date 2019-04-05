package com.nubytouch.crisiscare.push.rest;

import com.google.gson.annotations.SerializedName;
import com.nubytouch.crisiscare.push.rest.dto.PushResultDTO;
import com.nubytouch.crisiscare.rest.Credentials;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface PushService
{
    @POST("join-notification")
    Call<PushResultDTO> register(@Body PushData data);

    @POST("quit-notification")
    Call<PushResultDTO> unregister(@Body PushData data);

    class PushData extends Credentials
    {
        @SerializedName("token")
        public final String token;
        @SerializedName("notificationtype")
        public final String notificationtype;

        public PushData(String token)
        {
            super();
            this.token = token;
            this.notificationtype = "1";
        }
    }
}

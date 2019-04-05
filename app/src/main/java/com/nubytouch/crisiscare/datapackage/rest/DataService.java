package com.nubytouch.crisiscare.datapackage.rest;

import com.google.gson.annotations.SerializedName;
import com.nubytouch.crisiscare.core.Session;
import com.nubytouch.crisiscare.datapackage.DataPackageManager;
import com.nubytouch.crisiscare.datapackage.dto.DataResponseDTO;
import com.nubytouch.crisiscare.push.PushManager;
import com.nubytouch.crisiscare.rest.Credentials;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Streaming;

public interface DataService
{
    @POST("zipversion")
    @Streaming
    public Call<DataResponseDTO> getData(@Body Params data);

    class Params extends Credentials
    {
        @SerializedName("userCurrentVersion")
        public final int userCurrentVersion;
        @SerializedName("token")
        public final String token;

        public Params()
        {
            super();

            // Normal case
            if (DataPackageManager.getMetadata() != null)
                userCurrentVersion = (int) DataPackageManager.getMetadata().getVersion();
            // First launch case
            else
                userCurrentVersion = 0;

            token = PushManager.getToken();
        }
    }
}

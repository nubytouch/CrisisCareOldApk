package com.nubytouch.crisiscare.ui.activation.rest;

import com.google.gson.annotations.SerializedName;
import com.nubytouch.crisiscare.ui.activation.rest.dto.ClientInfoDTO;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ActivationService
{
    @POST("activation_code")
    @Headers("Cache-Control: no-store")
    Call<ClientInfoDTO> activate(@Body ActivationData data);

    class ActivationData
    {
        @SerializedName("id")
        public final String id;

        public ActivationData(String id)
        {
            this.id = id;
        }
    }
}

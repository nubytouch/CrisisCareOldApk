package com.nubytouch.crisiscare.ui.settings.rest;

import com.google.gson.annotations.SerializedName;
import com.nubytouch.crisiscare.rest.Credentials;
import com.nubytouch.crisiscare.ui.settings.rest.dto.UpdateDeputyResultDTO;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface DeputyService
{
    @POST("deputies/update")
    Call<UpdateDeputyResultDTO> updateDeputy(@Body DeputyData deputyData);

    class DeputyData extends Credentials
    {
        @SerializedName("deputyUsernames")
        public final String deputyUsernames;

        public DeputyData(String deputyUsernames)
        {
            super();
            this.deputyUsernames = deputyUsernames;
        }
    }
}

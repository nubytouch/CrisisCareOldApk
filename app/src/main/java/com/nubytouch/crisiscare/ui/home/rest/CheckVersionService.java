package com.nubytouch.crisiscare.ui.home.rest;


import com.google.gson.annotations.SerializedName;
import com.nubytouch.crisiscare.core.Session;
import com.nubytouch.crisiscare.datapackage.DataPackageManager;
import com.nubytouch.crisiscare.rest.Credentials;
import com.nubytouch.crisiscare.data.dto.DataPackageDTO;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface CheckVersionService
{
    @POST("versioninfo")
    public Call<DataPackageDTO> checkVersion(@Body VersionInfoData data);

    class VersionInfoData extends Credentials
    {
        @SerializedName("useCurrentVersion")
        public final int userCurrentVersion;

        public VersionInfoData()
        {
            super();
            userCurrentVersion = (int) DataPackageManager.getMetadata().getVersion();
        }
    }
}

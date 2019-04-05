package com.nubytouch.crisiscare.ui.login.rest;

import com.google.gson.annotations.SerializedName;
import com.nubytouch.crisiscare.ui.login.rest.dto.UserWrapperDTO;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface LoginService
{
    @POST("authenticate")
    Call<UserWrapperDTO> login(@Body LoginData data);

    class LoginData
    {
        @SerializedName("username")
        public final String username;
        @SerializedName("password")
        public final String password;

        public LoginData(String username, String password)
        {
            this.username = username;
            this.password = password;
        }
    }
}

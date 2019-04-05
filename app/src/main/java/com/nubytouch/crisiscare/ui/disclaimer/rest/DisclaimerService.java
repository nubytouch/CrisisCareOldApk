package com.nubytouch.crisiscare.ui.disclaimer.rest;

import com.nubytouch.crisiscare.ui.disclaimer.rest.dto.AcceptDisclaimerDTO;

import retrofit2.Call;
import retrofit2.http.POST;

public interface DisclaimerService
{
    @POST("users/accept-disclaimer")
    Call<AcceptDisclaimerDTO> accept();
}

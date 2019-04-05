package com.nubytouch.crisiscare.ui.login.rest.dto;

import com.google.gson.annotations.SerializedName;
import com.nubytouch.crisiscare.data.dto.UserDTO;

public class UserWrapperDTO
{
    @SerializedName("AuthenticateResult")
    public final UserDTO data;

    public UserWrapperDTO(UserDTO data) {this.data = data;}
}

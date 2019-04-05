package com.nubytouch.crisiscare.ui.activation.rest.dto;

import com.google.gson.annotations.SerializedName;

public class ClientInfoDTO
{
    @SerializedName("Id")
    public final String id;
    @SerializedName("Name")
    public final String name;
    @SerializedName("RootUrl")
    public final String rootUrl;
    @SerializedName("DominantColor")
    public final String dominantColor;
    @SerializedName("LogoPath")
    public final String logoPath;

    public ClientInfoDTO(String id, String name, String rootUrl, String dominantColor, String logoPath)
    {
        this.id = id;
        this.name = name;
        this.rootUrl = rootUrl;
        this.dominantColor = dominantColor;
        this.logoPath = logoPath;
    }
}

package com.nubytouch.crisiscare.data.dto;

import com.google.gson.annotations.SerializedName;
import com.nubytouch.crisiscare.data.DataUtil;
import com.nubytouch.crisiscare.data.model.Group;

public class GroupDTO
{
    @SerializedName("guid")
    private String            id;
    @SerializedName("title")
    private String            name;

    public void setId(String id)
    {
        this.id = id;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public Group buildModel()
    {
        Group group = new Group();
        group.setId(id);
        group.setName(name);

        return group;
    }

}

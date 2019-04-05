package com.nubytouch.crisiscare.ui.contacts;


import com.nubytouch.crisiscare.data.model.User;

import java.util.ArrayList;
import java.util.List;

public class GroupWrapper
{
    public final String id;
    public final String name;

    public final List<User> contacts;

    public GroupWrapper(String id, String name)
    {
        this.id = id;
        this.name = name;

        contacts = new ArrayList<>();
    }
}

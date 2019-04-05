package com.nubytouch.crisiscare.ui.selectrecipient;

import com.nubytouch.crisiscare.data.model.User;

public class RecipientWrapper
{
    public final Object  recipient;
    public       boolean selected;

    public RecipientWrapper(Object recipient)
    {
        this.recipient = recipient;
    }

    public boolean isUser()
    {
        return recipient instanceof User;
    }
}

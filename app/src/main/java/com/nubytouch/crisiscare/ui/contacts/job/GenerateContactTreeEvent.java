package com.nubytouch.crisiscare.ui.contacts.job;


import com.nubytouch.crisiscare.job.AbstractEvent;
import com.nubytouch.crisiscare.ui.contacts.GroupWrapper;

import java.util.List;

public class GenerateContactTreeEvent extends AbstractEvent<List<GroupWrapper>>
{
    public GenerateContactTreeEvent(List<GroupWrapper> data)
    {
        super(data);
    }
}

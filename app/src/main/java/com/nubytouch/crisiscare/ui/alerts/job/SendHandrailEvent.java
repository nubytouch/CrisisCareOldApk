package com.nubytouch.crisiscare.ui.alerts.job;

import com.nubytouch.crisiscare.job.AbstractEvent;
import com.nubytouch.crisiscare.ui.alerts.rest.dto.SendHandrailResultDTO;

public class SendHandrailEvent extends AbstractEvent<SendHandrailResultDTO>
{
    public SendHandrailEvent(SendHandrailResultDTO data)
    {
        super(data);
    }
}

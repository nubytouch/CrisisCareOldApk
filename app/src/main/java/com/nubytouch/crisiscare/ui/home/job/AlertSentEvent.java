package com.nubytouch.crisiscare.ui.home.job;

import com.nubytouch.crisiscare.job.AbstractEvent;
import com.nubytouch.crisiscare.ui.home.rest.dto.SendAlertDTO;

public class AlertSentEvent extends AbstractEvent<SendAlertDTO>
{
    public AlertSentEvent(SendAlertDTO data)
    {
        super(data);
    }
}

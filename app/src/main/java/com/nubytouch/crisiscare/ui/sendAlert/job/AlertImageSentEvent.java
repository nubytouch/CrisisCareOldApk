package com.nubytouch.crisiscare.ui.sendAlert.job;

import com.nubytouch.crisiscare.job.AbstractEvent;
import com.nubytouch.crisiscare.ui.sendAlert.rest.dto.SendAlertResultDTO;

public class AlertImageSentEvent extends AbstractEvent<Boolean>
{
    public AlertImageSentEvent(Boolean data) {
        super(data);
    }
}

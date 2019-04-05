package com.nubytouch.crisiscare.ui.sendAlert.job;

import com.nubytouch.crisiscare.job.AbstractEvent;
import com.nubytouch.crisiscare.ui.sendAlert.rest.dto.SendAlertResultDTO;

public class AlertSentEvent extends AbstractEvent<SendAlertResultDTO>
{
    public AlertSentEvent(SendAlertResultDTO data) {
        super(data);
    }
}

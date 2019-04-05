package com.nubytouch.crisiscare.ui.alerts.job;

import com.nubytouch.crisiscare.job.AbstractEvent;
import com.nubytouch.crisiscare.ui.alerts.rest.dto.AlertWrapperDTO;

public class AlertEvent extends AbstractEvent<AlertWrapperDTO>
{
    public AlertEvent(AlertWrapperDTO data)
    {
        super(data);
    }
}

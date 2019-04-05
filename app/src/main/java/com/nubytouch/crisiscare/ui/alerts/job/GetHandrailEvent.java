package com.nubytouch.crisiscare.ui.alerts.job;

import com.nubytouch.crisiscare.job.AbstractEvent;
import com.nubytouch.crisiscare.ui.alerts.rest.dto.HandRailsWrapperDTO;

public class GetHandrailEvent extends AbstractEvent<HandRailsWrapperDTO>
{
    public GetHandrailEvent(HandRailsWrapperDTO data) {
        super(data);
    }
}

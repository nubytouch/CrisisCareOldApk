package com.nubytouch.crisiscare.ui.disclaimer.job;

import com.nubytouch.crisiscare.job.AbstractEvent;
import com.nubytouch.crisiscare.ui.disclaimer.rest.dto.AcceptDisclaimerDTO;

public class AcceptDisclaimerEvent extends AbstractEvent<AcceptDisclaimerDTO>
{
    public AcceptDisclaimerEvent(AcceptDisclaimerDTO data)
    {
        super(data);
    }
}

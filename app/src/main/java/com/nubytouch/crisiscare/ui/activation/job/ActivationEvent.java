package com.nubytouch.crisiscare.ui.activation.job;

import com.nubytouch.crisiscare.job.AbstractEvent;
import com.nubytouch.crisiscare.ui.activation.rest.dto.ClientInfoDTO;

public class ActivationEvent extends AbstractEvent<ClientInfoDTO>
{
    public ActivationEvent(ClientInfoDTO data)
    {
        super(data);
    }
}

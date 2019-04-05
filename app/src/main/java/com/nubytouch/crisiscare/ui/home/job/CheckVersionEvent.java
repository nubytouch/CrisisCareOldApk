package com.nubytouch.crisiscare.ui.home.job;

import com.nubytouch.crisiscare.data.dto.DataPackageDTO;
import com.nubytouch.crisiscare.job.AbstractEvent;

public class CheckVersionEvent extends AbstractEvent<DataPackageDTO>
{
    public CheckVersionEvent(DataPackageDTO data) {
        super(data);
    }
}

package com.nubytouch.crisiscare.datapackage.job;

import com.nubytouch.crisiscare.job.AbstractEvent;

public class DataPackageProgressEvent extends AbstractEvent
{
    public final int progress;

    public DataPackageProgressEvent(int progress)
    {
        this.progress = progress;
    }
}

package com.nubytouch.crisiscare.ui.alerts.job;

import com.birbit.android.jobqueue.Params;
import com.nubytouch.crisiscare.job.AbstractEvent;
import com.nubytouch.crisiscare.job.FetchRemoteJob;
import com.nubytouch.crisiscare.rest.ServiceBuilder;
import com.nubytouch.crisiscare.ui.alerts.rest.AlertService;
import com.nubytouch.crisiscare.ui.alerts.rest.dto.HandRailsWrapperDTO;

import retrofit2.Call;

public class GetHandrailJob extends FetchRemoteJob<HandRailsWrapperDTO>
{
    private final String alertId;

    public GetHandrailJob(String alertId)
    {
        super(new Params(Priority.MEDIUM.value));
        this.alertId = alertId;
    }

    @Override
    protected AbstractEvent<HandRailsWrapperDTO> buildEvent(HandRailsWrapperDTO data)
    {
        return new GetHandrailEvent(data);
    }

    @Override
    public Call<HandRailsWrapperDTO> fetchServer()
    {
        AlertService          service = new ServiceBuilder().build(AlertService.class);
        Call<HandRailsWrapperDTO> call    = service.getHandRail(new AlertService.GetHandrailsData(alertId));
        return call;
    }

    @Override
    public boolean checkData(HandRailsWrapperDTO data)
    {
        return data != null;
    }
}

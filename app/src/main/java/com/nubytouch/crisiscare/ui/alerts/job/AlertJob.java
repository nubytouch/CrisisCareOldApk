package com.nubytouch.crisiscare.ui.alerts.job;

import com.birbit.android.jobqueue.Params;
import com.nubytouch.crisiscare.job.AbstractEvent;
import com.nubytouch.crisiscare.job.FetchRemoteJob;
import com.nubytouch.crisiscare.rest.Credentials;
import com.nubytouch.crisiscare.rest.ServiceBuilder;
import com.nubytouch.crisiscare.ui.alerts.rest.AlertService;
import com.nubytouch.crisiscare.ui.alerts.rest.dto.AlertWrapperDTO;

import retrofit2.Call;

public class AlertJob extends FetchRemoteJob<AlertWrapperDTO>
{
    public AlertJob()
    {
        super(new Params(Priority.MEDIUM.value));
    }

    @Override
    protected AbstractEvent<AlertWrapperDTO> buildEvent(AlertWrapperDTO data)
    {
        return new AlertEvent(data);
    }

    @Override
    public Call<AlertWrapperDTO> fetchServer()
    {
        AlertService          service = new ServiceBuilder().build(AlertService.class);
        Call<AlertWrapperDTO> call    = service.getAlerts(new Credentials());
        return call;
    }

    @Override
    public boolean checkData(AlertWrapperDTO data)
    {
        return data != null;
    }
}

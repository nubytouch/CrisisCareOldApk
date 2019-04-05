package com.nubytouch.crisiscare.push.job;

import com.birbit.android.jobqueue.Params;
import com.nubytouch.crisiscare.job.AbstractEvent;
import com.nubytouch.crisiscare.job.FetchRemoteJob;
import com.nubytouch.crisiscare.push.PushManager;
import com.nubytouch.crisiscare.push.rest.PushService;
import com.nubytouch.crisiscare.push.rest.dto.PushResultDTO;
import com.nubytouch.crisiscare.rest.ServiceBuilder;

import retrofit2.Call;

public class UnregisterPushJob extends FetchRemoteJob<PushResultDTO>
{
    public UnregisterPushJob()
    {
        super(new Params(Priority.HIGH.value).requireNetwork());
    }

    @Override
    protected AbstractEvent<PushResultDTO> buildEvent(PushResultDTO data)
    {
        return new PushUnregisteredEvent();
    }

    @Override
    public Call<PushResultDTO> fetchServer()
    {
        String token = PushManager.getToken();

        if (token == null)
            return null;

        PushService         service = new ServiceBuilder().build(PushService.class);
        Call<PushResultDTO> call    = service.unregister(new PushService.PushData(token));

        return call;
    }

    @Override
    public boolean checkData(PushResultDTO data)
    {
        return data != null;
    }
}

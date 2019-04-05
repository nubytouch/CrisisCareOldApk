package com.nubytouch.crisiscare.push.job;

import com.birbit.android.jobqueue.Params;
import com.nubytouch.crisiscare.job.AbstractEvent;
import com.nubytouch.crisiscare.job.FetchRemoteJob;
import com.nubytouch.crisiscare.push.PushManager;
import com.nubytouch.crisiscare.push.rest.PushService;
import com.nubytouch.crisiscare.push.rest.dto.PushResultDTO;
import com.nubytouch.crisiscare.rest.ServiceBuilder;

import retrofit2.Call;

public class RegisterPushJob extends FetchRemoteJob<PushResultDTO>
{
    private final String token;

    public RegisterPushJob(String token)
    {
        super(new Params(Priority.HIGH.value).requireNetwork());
        this.token = token;
    }

    @Override
    protected AbstractEvent<PushResultDTO> buildEvent(PushResultDTO data)
    {
        return new PushRegisteredEvent();
    }

    @Override
    public Call<PushResultDTO> fetchServer()
    {
        PushService         service = new ServiceBuilder().build(PushService.class);
        Call<PushResultDTO> call    = service.register(new PushService.PushData(token));

        return call;
    }

    @Override
    public boolean checkData(PushResultDTO data)
    {
        return data != null;
    }
}

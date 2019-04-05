package com.nubytouch.crisiscare.ui.activation.job;

import com.birbit.android.jobqueue.Params;
import com.nubytouch.crisiscare.job.AbstractEvent;
import com.nubytouch.crisiscare.job.FetchRemoteJob;
import com.nubytouch.crisiscare.rest.Server;
import com.nubytouch.crisiscare.rest.ServiceBuilder;
import com.nubytouch.crisiscare.ui.activation.rest.ActivationService;
import com.nubytouch.crisiscare.ui.activation.rest.dto.ClientInfoDTO;

import retrofit2.Call;

public class ActivateJob extends FetchRemoteJob<ClientInfoDTO>
{
    private final String activationCode;

    public ActivateJob(String activationCode)
    {
        super(new Params(Priority.HIGH.value));

        this.activationCode = activationCode;
    }

    @Override
    protected AbstractEvent<ClientInfoDTO> buildEvent(ClientInfoDTO data)
    {
        return new ActivationEvent(data);
    }

    @Override
    public Call<ClientInfoDTO> fetchServer()
    {
        ActivationService service = new ServiceBuilder(Server.CRISIS_CARE_SERVER).build(ActivationService.class);
        Call<ClientInfoDTO> call = service.activate(new ActivationService.ActivationData(activationCode));

        return call;
    }

    @Override
    public boolean checkData(ClientInfoDTO data)
    {
        return data != null && data.id != null && !data.id.isEmpty();
    }
}

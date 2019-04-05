package com.nubytouch.crisiscare.ui.disclaimer.job;

import com.birbit.android.jobqueue.Params;
import com.nubytouch.crisiscare.job.AbstractEvent;
import com.nubytouch.crisiscare.job.FetchRemoteJob;
import com.nubytouch.crisiscare.rest.Server;
import com.nubytouch.crisiscare.rest.ServiceBuilder;
import com.nubytouch.crisiscare.ui.activation.job.ActivationEvent;
import com.nubytouch.crisiscare.ui.activation.rest.ActivationService;
import com.nubytouch.crisiscare.ui.activation.rest.dto.ClientInfoDTO;
import com.nubytouch.crisiscare.ui.disclaimer.rest.DisclaimerService;
import com.nubytouch.crisiscare.ui.disclaimer.rest.dto.AcceptDisclaimerDTO;

import retrofit2.Call;

public class AcceptDisclaimerJob extends FetchRemoteJob<AcceptDisclaimerDTO>
{
    public AcceptDisclaimerJob()
    {
        super(new Params(Priority.HIGH.value));
    }

    @Override
    protected AbstractEvent<AcceptDisclaimerDTO> buildEvent(AcceptDisclaimerDTO data)
    {
        return new AcceptDisclaimerEvent(data);
    }

    @Override
    public Call<AcceptDisclaimerDTO> fetchServer()
    {
        DisclaimerService         service = new ServiceBuilder().build(DisclaimerService.class);
        Call<AcceptDisclaimerDTO> call    = service.accept();

        return call;
    }

    @Override
    public boolean checkData(AcceptDisclaimerDTO data)
    {
        return true;
    }
}

package com.nubytouch.crisiscare.ui.settings.job;

import com.birbit.android.jobqueue.Params;
import com.nubytouch.crisiscare.data.model.User;
import com.nubytouch.crisiscare.job.AbstractEvent;
import com.nubytouch.crisiscare.job.FetchRemoteJob;
import com.nubytouch.crisiscare.rest.ServiceBuilder;
import com.nubytouch.crisiscare.ui.settings.rest.DeputyService;
import com.nubytouch.crisiscare.ui.settings.rest.dto.UpdateDeputyResultDTO;

import retrofit2.Call;

public class UpdateDeputyJob extends FetchRemoteJob<UpdateDeputyResultDTO>
{
    public static final String JOB_TAG = "UpdateDeputyJob";

    private String deputyUsernames;

    public UpdateDeputyJob(String deputyUsernames)
    {
        super(new Params(Priority.MEDIUM.value).setDelayMs(1000).requireNetwork().persist().addTags(JOB_TAG));
        this.deputyUsernames = deputyUsernames;
    }

    @Override
    protected AbstractEvent<UpdateDeputyResultDTO> buildEvent(UpdateDeputyResultDTO data)
    {
        return new UpdateDeputyEvent(data, deputyUsernames);
    }

    @Override
    public Call<UpdateDeputyResultDTO> fetchServer()
    {
        DeputyService        service = new ServiceBuilder().build(DeputyService.class);
        Call<UpdateDeputyResultDTO> call    = service.updateDeputy(new DeputyService.DeputyData(deputyUsernames));
        return call;
    }

    @Override
    public boolean checkData(UpdateDeputyResultDTO data)
    {
        return data != null;
    }
}

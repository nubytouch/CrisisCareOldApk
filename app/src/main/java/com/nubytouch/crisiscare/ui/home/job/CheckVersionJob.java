package com.nubytouch.crisiscare.ui.home.job;

import timber.log.Timber;

import com.birbit.android.jobqueue.Params;
import com.nubytouch.crisiscare.data.dto.DataPackageDTO;
import com.nubytouch.crisiscare.job.AbstractEvent;
import com.nubytouch.crisiscare.job.FetchRemoteJob;
import com.nubytouch.crisiscare.rest.ServiceBuilder;
import com.nubytouch.crisiscare.ui.home.rest.CheckVersionService;

import retrofit2.Call;

public class CheckVersionJob extends FetchRemoteJob<DataPackageDTO>
{
    private static final String TAG = "CheckVersionJob";
    public CheckVersionJob()
    {
        super(new Params(Priority.HIGH.value));
    }

    @Override
    protected AbstractEvent<DataPackageDTO> buildEvent(DataPackageDTO data)
    {
        return new CheckVersionEvent(data);
    }

    @Override
    public Call<DataPackageDTO> fetchServer()
    {
        CheckVersionService  service = new ServiceBuilder().build(CheckVersionService.class);
        Call<DataPackageDTO> call    = service.checkVersion(new CheckVersionService.VersionInfoData());
        return call;
    }

    @Override
    public boolean checkData(DataPackageDTO data)
    {
        Timber.d("checkData: " + data);
        if (data != null)
            Timber.d("checkData: " + data.getVersion());
        return data != null && data.getVersion() != 0;
    }
}

package com.nubytouch.crisiscare.datapackage.job;

import com.birbit.android.jobqueue.Params;
import com.nubytouch.crisiscare.datapackage.DataPackageManager;
import com.nubytouch.crisiscare.job.AbstractJob;
import com.nubytouch.crisiscare.job.ErrorType;

import timber.log.Timber;

public class DataPackageJob extends AbstractJob
{
    public static final String JOB_TAG = "DataPackageJob";

    private DataPackageManager dataPackageManager;

    public DataPackageJob()
    {
        super(new Params(Priority.HIGH.value).requireNetwork().addTags(JOB_TAG));
    }

    @Override
    protected void execute() throws Exception
    {
        dataPackageManager = new DataPackageManager();
        dataPackageManager.loadLatestPackage(new DataPackageManager.DataPackageCallback() {
            @Override
            public void onNetworkError()
            {
                setErrorType(ErrorType.NETWORK_ERROR);
                postCompleteEvent(new DataPackageEvent());
            }

            @Override
            public void onDownloadProgress(int progress)
            {
                postCompleteEvent(new DataPackageProgressEvent(progress));
            }

            @Override
            public void onDownloadComplete()
            {
                postCompleteEvent(new DataPackageProgressEvent(100));
            }
        });
    }

    @Override
    protected void onRequestError()
    {
        postCompleteEvent(new DataPackageEvent());
    }

    @Override
    public void onAdded()
    {

    }

    private static final String TAG = "DataPackageJob";
    @Override
    protected void onCancel(int cancelReason, Throwable throwable)
    {
        Timber.d("onCancel: ");
        super.onCancel(cancelReason, throwable);
        dataPackageManager.cancel();
    }
}

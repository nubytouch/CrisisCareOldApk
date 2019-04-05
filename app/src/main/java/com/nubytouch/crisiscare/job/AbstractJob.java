package com.nubytouch.crisiscare.job;

import android.os.Handler;
import android.os.Looper;
import timber.log.Timber;

import com.birbit.android.jobqueue.Job;
import com.birbit.android.jobqueue.Params;
import com.birbit.android.jobqueue.RetryConstraint;
import com.nubytouch.crisiscare.BuildConfig;
import com.nubytouch.crisiscare.core.BusManager;

public abstract class AbstractJob extends Job
{
    private static final String TAG = "AbstractJob";

    protected enum Priority
    {
        LOW(0),
        MEDIUM(500),
        HIGH(1000);
        public final int value;

        Priority(final int piValue)
        {
            value = piValue;
        }
    }

    // transient is for not serializing the variable
    transient private  Handler   handler;
    private       ErrorType errorType;
    private       Throwable error;

    public AbstractJob(Params params)
    {
        super(params);
    }

    @Override
    public void onRun() throws Throwable
    {
        try
        {
            execute();
        }
        catch (java.net.SocketTimeoutException e)
        {
            if (BuildConfig.DEBUG)
            {
                Timber.d(getClass().getName() + " onRun: " + e);
                e.printStackTrace();
            }

            errorType = ErrorType.NETWORK_TIMEOUT;
            error = e;

            onRequestError();
        }
        catch (Throwable throwable)
        {
            if (BuildConfig.DEBUG)
            {
                Timber.d(getClass().getName() + " onRun: " + throwable);
                throwable.printStackTrace();
            }

            errorType = ErrorType.UNKNOWN;
            error = throwable;

            onRequestError();
        }

//        postEventQueryFinished();
    }

    protected void setErrorType(ErrorType errorType)
    {
        this.errorType = errorType;
    }

    protected void setError(Throwable error)
    {
        this.error = error;
    }

    protected abstract void execute() throws Exception;
    protected abstract void onRequestError();

    protected void postCompleteEvent(final AbstractEvent event)
    {
        event.setErrorType(errorType);
        event.setError(error);

        if (handler == null)
            handler = new Handler(Looper.getMainLooper());

        handler.post(new Runnable()
        {
            @Override
            public void run()
            {
                BusManager.getInstance().postOnMainThread(event);
            }
        });
    }

    protected void postEvent(final Object event)
    {
        if (handler == null)
            handler = new Handler(Looper.getMainLooper());

        handler.post(new Runnable()
        {
            @Override
            public void run()
            {
                BusManager.getInstance().postOnMainThread(event);
            }
        });
    }

    @Override
    protected void onCancel(int cancelReason, Throwable throwable)
    {

    }

    @Override
    protected RetryConstraint shouldReRunOnThrowable(Throwable throwable, int runCount, int maxRunCount)
    {
        return null;
    }
}

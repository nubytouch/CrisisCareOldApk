package com.nubytouch.crisiscare.job;


import com.birbit.android.jobqueue.Params;
import retrofit2.Call;
import retrofit2.Response;

/**
 * A job to fetch from the server, store and return the result
 * If there is a server side error, whatever it is,
 * then the data is fetched from the disk and returned
 *
 * @param <T>
 */
public abstract class FetchRemoteJob<T> extends AbstractJob implements RemoteRequest<T>
{
    public FetchRemoteJob(Params params)
    {
        super(params);
    }

    @Override
    protected void execute() throws Exception
    {
        Call<T> call = fetchServer();

        if (call == null)
        {
            setErrorType(ErrorType.IO_ERROR);
            return;
        }

        Response<T> response = call.execute();

        if (response.code() == 500 || (response.isSuccessful() && !checkData(response.body())))
        {
            setErrorType(ErrorType.NETWORK_ERROR);
        }

        T data = response.body();

        AbstractEvent<T> event = buildEvent(data);

        postCompleteEvent(event);
    }

    @Override
    protected void onRequestError()
    {
        AbstractEvent<T> event = buildEvent(null);
        postCompleteEvent(event);
    }

    protected abstract AbstractEvent<T> buildEvent(T data);

    @Override
    public void onAdded()
    {

    }
}

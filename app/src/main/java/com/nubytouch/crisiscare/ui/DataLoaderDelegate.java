package com.nubytouch.crisiscare.ui;


import androidx.annotation.NonNull;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import android.view.View;

import com.nubytouch.crisiscare.core.BusManager;
import com.nubytouch.crisiscare.job.AbstractEvent;
import com.nubytouch.crisiscare.job.JobFactory;
import com.nubytouch.crisiscare.job.JobManager;
import com.squareup.otto.Subscribe;

import java.lang.ref.WeakReference;
import java.util.Collection;

public class DataLoaderDelegate<T> implements ILifecyleListener
{
    private final WeakReference<DataLoaderCallback<T>> callback;
    private       UIStateManager                       stateManager;
    private final JobFactory                           jobFactory;
    private final Class                                eventClass;

    private boolean dataLoaded;

    public DataLoaderDelegate(@NonNull DataLoaderCallback<T> callback,
                              @NonNull UIStateManager stateManager,
                              @NonNull JobFactory jobFactory,
                              Class eventClass)
    {
        this.callback = new WeakReference<>(callback);
        this.jobFactory = jobFactory;
        this.eventClass = eventClass;

        initViews(stateManager);

        callback.addListener(this);
    }

    private void initViews(@NonNull UIStateManager stateManager)
    {
        this.stateManager = stateManager;

        View dataView = stateManager.getDataView();
        if (dataView != null && dataView instanceof SwipeRefreshLayout)
        {
            ((SwipeRefreshLayout) dataView).setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh()
                {
                    load(true);
                }
            });
        }

        DataStateView dataStateView = stateManager.getDataStateView();
        if (dataStateView != null)
        {
            dataStateView.setRetryCallback(new RetryCallback() {
                @Override
                public void retry()
                {
                    load(true);
                }
            });
        }
    }

    @Override
    public void onLifecycleContainerResume()
    {
        BusManager.getInstance().registerOnMainThread(this);
        load(false);
    }

    @Override
    public void onLifecycleContainerPause()
    {
        BusManager.getInstance().unregisterFromMainThread(this);
    }

    public void load(boolean force)
    {
        if (!dataLoaded || force)
        {
            stateManager.setState(dataLoaded ? UIStateManager.State.LOADING : UIStateManager.State.LOADING_FIRST);
            JobManager.addJobInBackground(jobFactory.build());
        }
    }

    @Subscribe
    public void onDataLoaded(AbstractEvent<T> event)
    {
        boolean isRightEvent = eventClass.isInstance(event);

        if (callback.get() != null)
            isRightEvent &= callback.get().checkEvent(event);

        if (isRightEvent)
        {
            if (event.isSuccess())
            {
                if (callback.get() != null)
                {
                    if (event.getData() instanceof Collection && ((Collection) event.getData()).isEmpty())
                        stateManager.setState(UIStateManager.State.EMPTY);
                    else
                        stateManager.setState(UIStateManager.State.DATA);

                    callback.get().onDataLoaded(event.getData());

                    dataLoaded = true;
                }
            }
            else
            {
                stateManager.setState(UIStateManager.State.ERROR);
            }
        }
    }

    public void setStateManager(UIStateManager stateManager)
    {
        initViews(stateManager);

        if (dataLoaded)
            stateManager.setState(UIStateManager.State.DATA);
    }


    public interface DataLoaderCallback<T> extends ILifecycleContainer
    {
        void onDataLoaded(T data);

        /*default*/ boolean checkEvent(AbstractEvent<T> event);
        /*{
            return true;
        }*/
    }
}

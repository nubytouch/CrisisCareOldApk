package com.nubytouch.crisiscare.ui;

import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import android.view.View;

import java.lang.ref.WeakReference;

public class UIStateManager
{
    public enum State
    {
        DATA,
        LOADING,
        LOADING_FIRST,
        EMPTY,
        ERROR,
        EXTRA
    }

    private final WeakReference<View>          dataView;
    private final WeakReference<DataStateView> dataStateView;
    private final WeakReference<View>          extraView;

    private final boolean isSRL;

    public UIStateManager(View dataView, DataStateView dataStateView)
    {
        this(dataView, dataStateView, null);
    }

    public UIStateManager(View dataView, DataStateView dataStateView, View extraView)
    {
        this.dataView = new WeakReference<>(dataView);
        this.dataStateView = new WeakReference<>(dataStateView);
        this.extraView = new WeakReference<>(extraView);

        isSRL = dataView != null && dataView instanceof SwipeRefreshLayout;
    }

    public void setState(State state)
    {
        if (dataView.get() != null)
        {
            if (state == State.DATA || (state == State.LOADING && isSRL))
            {
                dataView.get().setVisibility(View.VISIBLE);

                if (isSRL)
                    ((SwipeRefreshLayout) dataView.get()).setRefreshing(state == State.LOADING);
            }
            else
            {
                dataView.get().setVisibility(View.INVISIBLE);
            }
        }

        if (dataStateView.get() != null)
        {
            if (state == State.DATA || state == State.EXTRA || (state == State.LOADING && isSRL))
            {
                dataStateView.get().setVisibility(View.GONE);
            }
            else
            {
                dataStateView.get().setVisibility(View.VISIBLE);

                if (state == State.LOADING || state == State.LOADING_FIRST)
                    dataStateView.get().setStateLoading();
                else if (state == State.EMPTY)
                    dataStateView.get().setStateEmpty();
                else if (state == State.ERROR)
                    dataStateView.get().setStateError();
            }
        }

        if (extraView != null && extraView.get() != null)
            extraView.get().setVisibility(state == State.EXTRA ? View.VISIBLE : View.GONE);
    }

    public View getDataView()
    {
        if (dataView != null)
            return dataView.get();

        return null;
    }

    public DataStateView getDataStateView()
    {
        if (dataStateView != null)
            return dataStateView.get();

        return null;
    }

    public View getExtraView()
    {
        if (extraView != null)
            return extraView.get();

        return null;
    }
}

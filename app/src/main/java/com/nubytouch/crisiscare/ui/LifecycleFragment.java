package com.nubytouch.crisiscare.ui;


import androidx.fragment.app.Fragment;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

public class LifecycleFragment extends Fragment implements ILifecycleContainer
{
    private ArrayList<WeakReference<ILifecyleListener>> listeners;

    public LifecycleFragment()
    {
        super();
        listeners = new ArrayList<>();
    }

    @Override
    public void addListener(ILifecyleListener listener)
    {
        listeners.add(new WeakReference<>(listener));
    }

    @Override
    public void removeListener(ILifecyleListener listener)
    {

    }

    @Override
    public void onResume()
    {
        super.onResume();

        WeakReference<ILifecyleListener> listener;

        for (int i = 0; i < listeners.size(); i++)
        {
            listener = listeners.get(i);

            if (listener != null && listener.get() != null)
            {
                listener.get().onLifecycleContainerResume();
            }
            else
            {
                listeners.remove(i);
                i--;
            }
        }
    }

    @Override
    public void onPause()
    {
        super.onPause();

        WeakReference<ILifecyleListener> listener;

        for (int i = 0; i < listeners.size(); i++)
        {
            listener = listeners.get(i);

            if (listener != null && listener.get() != null)
            {
                listener.get().onLifecycleContainerPause();
            }
            else
            {
                listeners.remove(i);
                i--;
            }
        }
    }
}

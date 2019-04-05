package com.nubytouch.crisiscare.ui;


import androidx.appcompat.app.AppCompatActivity;
import android.view.MenuItem;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

public class LifecycleActivity extends CCActivity implements ILifecycleContainer
{
    private ArrayList<WeakReference<ILifecyleListener>> listeners;

    public LifecycleActivity()
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
    protected void onResume()
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
    protected void onPause()
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        if (item.getItemId() == android.R.id.home)
        {
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

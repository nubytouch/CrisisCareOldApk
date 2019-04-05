package com.nubytouch.crisiscare.ui;


import androidx.appcompat.app.AppCompatActivity;
import android.view.MenuItem;

import com.nubytouch.crisiscare.core.BusManager;

public class JobActivity extends CCActivity
{
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

    @Override
    protected void onResume()
    {
        super.onResume();
        BusManager.getInstance().registerOnMainThread(this);
    }

    @Override
    protected void onPause()
    {
        super.onPause();
        BusManager.getInstance().unregisterFromMainThread(this);
    }
}

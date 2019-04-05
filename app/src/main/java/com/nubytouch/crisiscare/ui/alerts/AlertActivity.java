package com.nubytouch.crisiscare.ui.alerts;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.nubytouch.crisiscare.R;
import com.nubytouch.crisiscare.job.AbstractEvent;
import com.nubytouch.crisiscare.job.AbstractJob;
import com.nubytouch.crisiscare.job.JobFactory;
import com.nubytouch.crisiscare.ui.DataLoaderDelegate;
import com.nubytouch.crisiscare.ui.DataStateView;
import com.nubytouch.crisiscare.ui.LifecycleActivity;
import com.nubytouch.crisiscare.ui.ToolbarDelegate;
import com.nubytouch.crisiscare.ui.UIStateManager;
import com.nubytouch.crisiscare.ui.alerts.job.AlertEvent;
import com.nubytouch.crisiscare.ui.alerts.job.AlertJob;
import com.nubytouch.crisiscare.ui.alerts.rest.dto.AlertWrapperDTO;
import com.nubytouch.crisiscare.ui.sendAlert.SendAlertActivity;

import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import timber.log.Timber;

public class AlertActivity extends LifecycleActivity implements DataLoaderDelegate
                                                                        .DataLoaderCallback<AlertWrapperDTO>
{
    public static final String EXTRA_TITLE = "com.nubytouch.crisiscare.ui.alerts.title";
    private static final int SEND_ALERT_CODE = 6985;

    private RecyclerView recyclerView;
    private DataLoaderDelegate dataLoaderDelegate;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_alert);

        if (getIntent().hasExtra(EXTRA_TITLE))
            new ToolbarDelegate(this).setup(false, getIntent().getStringExtra(EXTRA_TITLE));
        else
            new ToolbarDelegate(this).setup(false);


        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        recyclerView.setHasFixedSize(true);

        SwipeRefreshLayout refreshLayout = findViewById(R.id.alert_swipe_refresh_layout);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setBackgroundTintList(ColorStateList.valueOf(0xffd72b27));//Session.getPrimaryColor()));
        fab.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                startActivityForResult(new Intent(AlertActivity.this, SendAlertActivity.class),
                        SEND_ALERT_CODE);
            }
        });

        DataStateView dataStateView = findViewById(R.id.data_state_layout);

        UIStateManager stateManager = new UIStateManager(refreshLayout, dataStateView);

        dataLoaderDelegate = new DataLoaderDelegate<>(this,
                stateManager,
                new JobFactory()
                {
                    @Override
                    public AbstractJob build()
                    {
                        return new AlertJob();
                    }
                },
                AlertEvent.class);
    }

    @Override
    public void onDataLoaded(AlertWrapperDTO data)
    {
        Timber.d("Hello data loaded " + data.getAlerts().size());
        recyclerView.setAdapter(new AlertAdapter(data.getAlerts()));
    }

    @Override
    public boolean checkEvent(AbstractEvent<AlertWrapperDTO> event)
    {
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == SEND_ALERT_CODE && resultCode == RESULT_OK)
            dataLoaderDelegate.load(true);
    }
}

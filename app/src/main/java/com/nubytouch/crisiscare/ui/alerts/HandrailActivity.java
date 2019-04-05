package com.nubytouch.crisiscare.ui.alerts;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;

import com.nubytouch.crisiscare.R;
import com.nubytouch.crisiscare.core.Session;
import com.nubytouch.crisiscare.data.model.HandRail;
import com.nubytouch.crisiscare.job.AbstractEvent;
import com.nubytouch.crisiscare.job.AbstractJob;
import com.nubytouch.crisiscare.job.JobFactory;
import com.nubytouch.crisiscare.ui.DataLoaderDelegate;
import com.nubytouch.crisiscare.ui.DataStateView;
import com.nubytouch.crisiscare.ui.LifecycleActivity;
import com.nubytouch.crisiscare.ui.ToolbarDelegate;
import com.nubytouch.crisiscare.ui.UIStateManager;
import com.nubytouch.crisiscare.ui.alerts.job.GetHandrailJob;
import com.nubytouch.crisiscare.ui.alerts.job.GetHandrailEvent;
import com.nubytouch.crisiscare.ui.alerts.rest.dto.HandRailsWrapperDTO;
import com.nubytouch.crisiscare.utils.DateUtil;

import java.util.ArrayList;
import java.util.Calendar;

public class HandrailActivity extends LifecycleActivity implements DataLoaderDelegate
                                                                           .DataLoaderCallback<HandRailsWrapperDTO>
{
    public static final String EXTRA_ALERT_ID    = "AlertEventsActivity.alertId";
    public static final String EXTRA_ALERT_TITLE = "AlertEventsActivity.alertTitle";

    private static final int SEND_HANDRAIL_CODE = 7649;

    private DataLoaderDelegate dataLoaderDelegate;
    private HandrailAdapter    adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_handrail);

        new ToolbarDelegate(this).setup(false, getIntent().getStringExtra(EXTRA_ALERT_TITLE));

        final String alertId = getIntent().getStringExtra(EXTRA_ALERT_ID);

        View                 emptyView     = findViewById(R.id.empty_layout);
        View                 errorView     = findViewById(R.id.error_layout);
        RecyclerView         recyclerView  = (RecyclerView) findViewById(R.id.recycler_view);
        SwipeRefreshLayout   refreshLayout = (SwipeRefreshLayout) findViewById(R.id.alert_swipe_refresh_layout);
        FloatingActionButton fab           = (FloatingActionButton) findViewById(R.id.fab);

        fab.setBackgroundTintList(ColorStateList.valueOf(Session.getPrimaryColor()));

        adapter = new HandrailAdapter();

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        //recyclerView.addItemDecoration(new DividerItemDecoration(ContextCompat.getDrawable(this, R.drawable
          //      .list_divider)));
        recyclerView.setAdapter(adapter);

        DataStateView dataStateView = (DataStateView) findViewById(R.id.data_state_layout);

        fab.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(HandrailActivity.this, SendHandrailActivity.class);
                intent.putExtra(SendHandrailActivity.EXTRA_ALERT_ID, alertId);
                startActivityForResult(intent, SEND_HANDRAIL_CODE);
            }
        });

        UIStateManager stateManager = new UIStateManager(refreshLayout, dataStateView);

        if (dataLoaderDelegate == null)
        {
            dataLoaderDelegate = new DataLoaderDelegate<>(this,
                                                          stateManager,
                                                          new JobFactory()
                                                          {
                                                              @Override
                                                              public AbstractJob build()
                                                              {
                                                                  return new GetHandrailJob(alertId);
                                                              }
                                                          },
                                                          GetHandrailEvent.class);
        }
        else
        {
            dataLoaderDelegate.setStateManager(stateManager);
        }
    }

    @Override
    public void onDataLoaded(HandRailsWrapperDTO data)
    {
        Calendar            c = null, c2;

        ArrayList<Object> list = new ArrayList<>();

        for (HandRail handRail : data.getHandRails())
        {
            if (c == null)
            {
                c = Calendar.getInstance();
                c.setTimeInMillis(handRail.getDate());
                list.add(new HandrailAdapter.Header(handRail.getDate()));
            }
            else
            {
                c2 = Calendar.getInstance();
                c2.setTimeInMillis(handRail.getDate());

                if (!DateUtil.isSameDay(c, c2))
                {
                    c = c2;
                    list.add(new HandrailAdapter.Header(handRail.getDate()));
                }
            }

            list.add(handRail);
        }

        adapter.setData(list);
    }

    @Override
    public boolean checkEvent(AbstractEvent<HandRailsWrapperDTO> event)
    {
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        // If user just send a new entry to the handrail
        if (requestCode == SEND_HANDRAIL_CODE && resultCode == RESULT_OK)
            dataLoaderDelegate.load(true);
    }
}

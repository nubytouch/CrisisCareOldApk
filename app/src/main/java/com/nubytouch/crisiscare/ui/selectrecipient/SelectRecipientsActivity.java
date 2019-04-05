package com.nubytouch.crisiscare.ui.selectrecipient;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.nubytouch.crisiscare.R;
import com.nubytouch.crisiscare.core.BusManager;
import com.nubytouch.crisiscare.data.model.User;
import com.nubytouch.crisiscare.job.AbstractEvent;
import com.nubytouch.crisiscare.job.AbstractJob;
import com.nubytouch.crisiscare.job.JobFactory;
import com.nubytouch.crisiscare.ui.DataLoaderDelegate;
import com.nubytouch.crisiscare.ui.DataStateView;
import com.nubytouch.crisiscare.ui.LifecycleActivity;
import com.nubytouch.crisiscare.ui.ToolbarDelegate;
import com.nubytouch.crisiscare.ui.UIStateManager;
import com.nubytouch.crisiscare.ui.contacts.GroupWrapper;
import com.nubytouch.crisiscare.ui.contacts.job.GenerateContactTreeEvent;
import com.nubytouch.crisiscare.ui.contacts.job.GenerateContactTreeJob;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class SelectRecipientsActivity extends LifecycleActivity implements DataLoaderDelegate
                                                                                   .DataLoaderCallback<List<GroupWrapper>>
{
    public static final String EXTRA_RECIPIENTS = "SelectRecipientsActivity.recipients";

    private RecyclerView      recyclerView;
    private RecipientAdapter  adapter;
    private ArrayList<String> recipients;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_select_recipients);

        new ToolbarDelegate(this).setup(false, R.string.recipients);

        recipients = getIntent().getStringArrayListExtra(EXTRA_RECIPIENTS);

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        DataStateView dataStateView = findViewById(R.id.data_state_layout);

        UIStateManager stateManager = new UIStateManager(recyclerView, dataStateView);

        new DataLoaderDelegate<>(this,
                                 stateManager,
                                 new JobFactory()
                                 {
                                     @Override
                                     public AbstractJob build()
                                     {
                                         return new GenerateContactTreeJob();
                                     }
                                 },
                                 GenerateContactTreeEvent.class);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuItem item = menu.add(Menu.NONE, R.id.menu_validate, Menu.NONE, R.string.okay);
        item.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        item.setIcon(R.drawable.ic_check);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case android.R.id.home:
            {
                finish();
                return true;
            }
            case R.id.menu_validate:
            {
                sendResult();
                finish();
                return true;
            }
            default:
            {
                return super.onOptionsItemSelected(item);
            }
        }
    }

    @Override
    public void onBackPressed()
    {
        sendResult();

        super.onBackPressed();
    }

    private void sendResult()
    {
        recipients = new ArrayList<>(adapter.getSelectedUsers());

        Intent data = new Intent();
        data.putStringArrayListExtra(EXTRA_RECIPIENTS, recipients);
        setResult(RESULT_OK, data);
    }

    @Override
    public void onDataLoaded(List<GroupWrapper> data)
    {
        List<RecipientWrapper> recList = new ArrayList<>();

        Map<RecipientWrapper, List<RecipientWrapper>> map = new HashMap<>();
        List<RecipientWrapper>                        list;


        RecipientWrapper dwrapper, uwrapper;
        boolean          selected;

        for (GroupWrapper gw : data)
        {
            selected = true;

            if (gw.contacts.size() > 0)
            {
                dwrapper = new RecipientWrapper(gw);
                recList.add(dwrapper);

                list = new ArrayList<>();

                for (User contact : gw.contacts)
                {
                    uwrapper = new RecipientWrapper(contact);

                    if (recipients.contains(contact.getEmail()))
                        uwrapper.selected = true;

                    selected &= uwrapper.selected;

                    recList.add(uwrapper);
                    list.add(uwrapper);
                }

                dwrapper.selected = selected;

                map.put(dwrapper, list);
            }

            adapter = new RecipientAdapter(recList, map);
            recyclerView.setAdapter(adapter);
        }
    }

    @Override
    public boolean checkEvent(AbstractEvent<List<GroupWrapper>> event)
    {
        return true;
    }
}

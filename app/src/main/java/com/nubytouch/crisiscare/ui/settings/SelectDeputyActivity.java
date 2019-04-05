package com.nubytouch.crisiscare.ui.settings;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.nubytouch.crisiscare.R;
import com.nubytouch.crisiscare.core.Session;
import com.nubytouch.crisiscare.data.model.User;
import com.nubytouch.crisiscare.datapackage.DataPackageManager;
import com.nubytouch.crisiscare.job.JobManager;
import com.nubytouch.crisiscare.ui.JobActivity;
import com.nubytouch.crisiscare.ui.ToolbarDelegate;
import com.nubytouch.crisiscare.ui.settings.job.UpdateDeputyEvent;
import com.nubytouch.crisiscare.ui.settings.job.UpdateDeputyJob;
import com.squareup.otto.Subscribe;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import timber.log.Timber;

public class SelectDeputyActivity extends JobActivity
{
    private ListView      listview;
    private DeputyAdapter adapter;
    private List<String>  deputyUsernames;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_select_deputy);

        new ToolbarDelegate(this).setup(false, R.string.suppleants);

        listview = (ListView) findViewById(R.id.listviewDeputies);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                User deputy = (User) listview.getItemAtPosition(position);
                updateDeputy(deputy);
            }
        });

        deputyUsernames = new ArrayList<>();

        loadContent();
    }

    private static final String TAG = "SelectDeputyActivity";

    private void updateDeputy(User deputy)
    {
        if (deputyUsernames.contains(deputy.getEmail()))
            deputyUsernames.remove(deputy.getEmail());
        else
            deputyUsernames.add(deputy.getEmail());

        adapter.notifyDataSetChanged();

        // Replace session data
        Session.getUser().getDeputies().clear();

        if (!deputy.getEmail().isEmpty())
            Session.getUser().setDeputyUsernames(StringUtils.join(deputyUsernames, ","));

        Timber.d("updateDeputy: " + Session.getUser().hasDeputy());

        // Save session data
        Session.setUser(Session.getUser());

        JobManager.cancelJobsInBackground(UpdateDeputyJob.JOB_TAG);
        JobManager.addJobInBackground(new UpdateDeputyJob(StringUtils.join(deputyUsernames, ",")));
    }

    private void loadContent()
    {
        List<User> deputies = DataPackageManager.getContacts();

        User deputy;
        // Remove current user from deputy list
        for (int i = 0, n = deputies.size(); i < n; i++)
        {
            deputy = deputies.get(i);

            if (deputy.getEmail() != null && deputy.getEmail().equals(Session.getUser().getEmail()))
            {
                deputies.remove(deputy);
                break;
            }
        }

        Collections.sort(deputies, new Comparator<User>()
        {
            @Override
            public int compare(User lhs, User rhs)
            {
                return lhs.getFullName().toLowerCase().compareTo(rhs.getFullName().toLowerCase());
            }
        });

        deputyUsernames = new ArrayList<>();
        if (Session.getUser().hasDeputy())
            deputyUsernames = new ArrayList<>(Arrays.asList(StringUtils.split(
                    Session.getUser().getDeputyUsernames(), ",")));

        adapter = new DeputyAdapter(this, R.layout.item_checklist, deputies);
        adapter.setDeputies(deputyUsernames);

        listview.setAdapter(adapter);
    }

    @Subscribe
    public void onUpdateDeputyEvent(UpdateDeputyEvent event)
    {
        if (event.isSuccess())
        {
            String message = getString(R.string.deputy_saved);

            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        }
        else
        {
            // Replace session data
            Session.getUser().setDeputyUsernames("");
            // Save session data
            Session.setUser(Session.getUser());

            Toast.makeText(this, getString(R.string.unable_to_update_deputies), Toast.LENGTH_LONG).show();
        }
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
            default:
            {
                return super.onOptionsItemSelected(item);
            }
        }
    }
}

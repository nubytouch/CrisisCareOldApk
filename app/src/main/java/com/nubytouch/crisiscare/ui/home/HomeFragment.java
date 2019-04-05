package com.nubytouch.crisiscare.ui.home;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.nubytouch.crisiscare.R;
import com.nubytouch.crisiscare.data.model.Topic;
import com.nubytouch.crisiscare.datapackage.DataPackageManager;
import com.nubytouch.crisiscare.ui.sendAlert.SendAlertActivity;
import com.nubytouch.crisiscare.ui.utils.DividerItemDecoration;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class HomeFragment extends Fragment
{
    public static HomeFragment newInstance()
    {
        HomeFragment fragment = new HomeFragment();
        return fragment;
    }

    public HomeFragment()
    {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {

        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);

        List<Topic> topics = DataPackageManager.getTopics();
        Collections.sort(topics, new Comparator<Topic>()
        {
            @Override
            public int compare(Topic lhs, Topic rhs)
            {
                int lo = lhs.getOrder();
                int ro = rhs.getOrder();

                if (lo < ro)
                    return -1;

                if (lo > ro)
                    return 1;

                return 0;
            }
        });

        TopicAdapter adapter = new TopicAdapter(topics);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new DividerItemDecoration(ContextCompat.getDrawable(getActivity(), R.drawable
                .home_list_divider)));
        recyclerView.setAdapter(adapter);

        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fab);
        fab.setBackgroundTintList(ColorStateList.valueOf(0xffd72b27));//Session.getPrimaryColor()));
        fab.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                getActivity().startActivityForResult(new Intent(getActivity(), SendAlertActivity.class),
                                                     HomeActivity.SEND_ALERT_CODE);
            }
        });
    }
}

package com.nubytouch.crisiscare.ui.settings;

import android.content.res.ColorStateList;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;

import com.nubytouch.crisiscare.R;
import com.nubytouch.crisiscare.core.Session;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class HiddenSettingsDialogFragment extends DialogFragment
{
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, 0);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        final View view = inflater.inflate(R.layout.layout_hidden_settings_dialog, container, false);

        List<Object> data = new ArrayList<>();
        data.add(new HiddenSettingsAdapter.Header("Client", Session.getClient().name));
        data.add(new HiddenSettingsAdapter.EntityColor(Session.getClient().primaryColor));

        RecyclerView recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(new HiddenSettingsAdapter(data));

        Button button = view.findViewById(R.id.button);
        ViewCompat.setBackgroundTintList(button, ColorStateList.valueOf(Session.getPrimaryColor()));
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Toast.makeText(getActivity(), "Crash Test in 2 seconds...", Toast.LENGTH_SHORT).show();

                view.postDelayed(new Runnable() {
                    @Override
                    public void run()
                    {
                        throw new RuntimeException("If you are seeing this crash, it means the hidden setting crash test button worked like a charm!");
                    }
                }, 2000);
            }
        });

        return view;
    }

    @Override
    public void onResume()
    {
        super.onResume();
        Window window = getDialog().getWindow();

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        // Set the width and height for the layout parameters
        // This will bet the width and height of alert dialog
        int width = (int) ( displayMetrics.widthPixels * 0.85f);
        int height = (int) ( displayMetrics.heightPixels * 0.85f);

        window.setLayout(width, height);
    }
}

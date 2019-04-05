package com.nubytouch.crisiscare.ui.settings;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nubytouch.crisiscare.CrisisCare;
import com.nubytouch.crisiscare.R;
import com.nubytouch.crisiscare.core.Session;
import com.nubytouch.crisiscare.ui.CCActivity;
import com.nubytouch.crisiscare.ui.ToolbarDelegate;

public class SettingsActivity extends CCActivity
{
    private TextView tvEmail;
    private TextView tvNom;
    private TextView tvDeputy;


    private int clickCount = 0;
    private long firstClickTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parameters);

        new ToolbarDelegate(this).setup(false, R.string.parameters);

        tvEmail = (TextView) findViewById(R.id.tvEmail);
        tvNom = (TextView) findViewById(R.id.tvNomPrenom);
        tvDeputy = (TextView) findViewById(R.id.tvSuppleant);

        Button button = findViewById(R.id.button_hidden);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v)
            {
                long currentTime = System.currentTimeMillis();
                long diff = currentTime - firstClickTime;

                if (diff > 1000) // 1 sec
                {
                    clickCount = 0;
                    firstClickTime = currentTime;
                }

                clickCount++;

                if (diff < 1000 && clickCount >= 3)
                {
                    showHiddenSettings();
                    firstClickTime = 0;
                }
            }
        });


        ImageView iv = (ImageView) findViewById(R.id.select_deputy_arrow);
        iv.setColorFilter(Color.argb(255, 133, 133, 133));

        LinearLayout btDeputy = (LinearLayout) findViewById(R.id.layoutDeputy);
        btDeputy.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(SettingsActivity.this, SelectDeputyActivity.class);
                startActivity(intent);
            }
        });


        Button btLogout = (Button) findViewById(R.id.btLogout);
        btLogout.setTextColor(Session.getPrimaryColor());

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK)
            finish();
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

    public void refreshUserInfo()
    {
        tvEmail.setText(Session.getUser().getEmail());
        tvNom.setText(Session.getUser().getFullName());
    }

    public void refreshDeputyInfo()
    {
        if (Session.getUser().hasDeputy())
        {
            if (Session.getUser().getDeputies().size() > 1)
                tvDeputy.setText(Session.getUser().getDeputies().size() + "");
            else
                tvDeputy.setText(Session.getUser().getDeputies().get(0).getFullName());
        }
        else
        {
            tvDeputy.setText("");
        }
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        refreshData();
    }

    private void refreshData()
    {
        refreshUserInfo();
        refreshDeputyInfo();
    }

    public void onClickLogout(View v)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle(R.string.se_deconnecter);
        builder.setMessage(R.string.etes_vous_sur_de_vouloir_vous_deconnecter_);

        builder.setPositiveButton(R.string.oui, new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int which)
            {
                logout();
                finish();
                dialog.dismiss();
            }
        });

        builder.setNegativeButton(R.string.non, new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                dialog.dismiss();
            }
        });

        AlertDialog alert = builder.create();
        alert.show();
    }

    private void logout()
    {
        CrisisCare.getInstance().logout();
    }

    private void showHiddenSettings()
    {
        new HiddenSettingsDialogFragment().show(getSupportFragmentManager(), "");
    }
}

package com.nubytouch.crisiscare.ui.settings;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.nubytouch.crisiscare.BuildConfig;
import com.nubytouch.crisiscare.R;
import com.nubytouch.crisiscare.core.Session;
import com.nubytouch.crisiscare.datapackage.DataPackageManager;
import com.nubytouch.crisiscare.ui.CCActivity;
import com.nubytouch.crisiscare.ui.ToolbarDelegate;
import com.nubytouch.crisiscare.ui.web.BrowserActivity;
import com.nubytouch.crisiscare.utils.DateUtil;

public class InformationsActivity extends CCActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_informations);

        new ToolbarDelegate(this).setup(false, R.string.informations);


        TextView tvAppVersion = (TextView) findViewById(R.id.tvAppVersion);
        TextView tvVersion    = (TextView) findViewById(R.id.tvVersion);
        TextView tvDate       = (TextView) findViewById(R.id.tvDate);

        String appVersion = "";
        try
        {
            PackageInfo packageInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            appVersion = packageInfo.versionName;
        }
        catch (PackageManager.NameNotFoundException e)
        {
            e.printStackTrace();
        }

        tvAppVersion.setText("" + appVersion);

        if (DataPackageManager.getMetadata() != null)
        {
            tvVersion.setText("" + DataPackageManager.getMetadata().getVersion());
            tvDate.setText(DateUtil.getFormattedDatePublication(DataPackageManager.getMetadata().getUpdateDate()));
        }
        else
        {
            tvVersion.setText("/");
            tvDate.setText("/");
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

    public void onClickCredits(View v)
    {
        Intent i = new Intent(this, BrowserActivity.class);
        i.putExtra(BrowserActivity.EXTRA_URI, BuildConfig.CREDITS_URL);
        i.putExtra(BrowserActivity.EXTRA_TITLE, getString(R.string.mentions_et_credits));
        startActivity(i);
    }
}

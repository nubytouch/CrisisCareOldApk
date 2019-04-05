package com.nubytouch.crisiscare.ui.alerts;


import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.nubytouch.crisiscare.R;
import com.nubytouch.crisiscare.job.JobManager;
import com.nubytouch.crisiscare.ui.JobActivity;
import com.nubytouch.crisiscare.ui.ToolbarDelegate;
import com.nubytouch.crisiscare.ui.alerts.job.SendHandrailEvent;
import com.nubytouch.crisiscare.ui.alerts.job.SendHandrailJob;
import com.nubytouch.crisiscare.utils.DateUtil;
import com.squareup.otto.Subscribe;

import java.util.Calendar;

public class SendHandrailActivity extends JobActivity implements DatePickerDialog.OnDateSetListener
{
    public static final String EXTRA_ALERT_ID = "SendEventActivity.alertId";

    private EditText origin;
    private TextView date;
    private TextView time;
    private EditText description;

    private MenuItem menuItem;

    private String alertId;

    private int day;
    private int month;
    private int year;
    private int hour;
    private int minutes;

    private TextWatcher textWatcher = new TextWatcher()
    {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after)
        {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count)
        {

        }

        @Override
        public void afterTextChanged(Editable s)
        {
            checkInput();
        }
    };
    private ProgressDialog dialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_handrail);

        new ToolbarDelegate(this).setup(false, R.string.new_event).setIcon(R.drawable.ic_close);

        alertId = getIntent().getStringExtra(EXTRA_ALERT_ID);

        origin = (EditText) findViewById(R.id.origin);
        date = (TextView) findViewById(R.id.date);
        time = (TextView) findViewById(R.id.time);
        description = (EditText) findViewById(R.id.description);

        final long now = System.currentTimeMillis();

        date.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                DatePickerDialog picker = new DatePickerDialog(SendHandrailActivity.this,
                                                               SendHandrailActivity.this,
                                                               year,
                                                               month,
                                                               day);
                picker.getDatePicker().setMaxDate(now);
                picker.show();
            }
        });

        time.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                new TimePickerDialog(SendHandrailActivity.this,
                                     new TimePickerDialog.OnTimeSetListener()
                                     {
                                         @Override
                                         public void onTimeSet(TimePicker view, int hourOfDay, int minute)
                                         {
                                             hour = hourOfDay;
                                             minutes = minute;
                                             updateTime();
                                         }
                                     },
                                     hour,
                                     minutes,
                                     DateFormat.is24HourFormat(SendHandrailActivity.this))
                        .show();
            }
        });

        year = Calendar.getInstance().get(Calendar.YEAR);
        month = Calendar.getInstance().get(Calendar.MONTH);
        day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
        hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
        minutes = Calendar.getInstance().get(Calendar.MINUTE);

        updateDate();
        updateTime();
    }

    private void checkInput()
    {
        if (menuItem != null)
            menuItem.setEnabled(origin.length() >= 2 && description.length() >= 2);
    }

    private void updateDate()
    {
        Calendar c = Calendar.getInstance();
        c.set(year, month, day);
        date.setText(DateFormat.getLongDateFormat(this).format(c.getTime()));
    }

    private void updateTime()
    {
        Calendar c = Calendar.getInstance();
        c.set(year, month, day, hour, minutes);

        Calendar c2 = Calendar.getInstance();

        if (c.after(c2))
        {
            hour = c2.get(Calendar.HOUR_OF_DAY);
            minutes = c2.get(Calendar.MINUTE);
        }


        time.setText(getString(R.string.time, DateUtil.addZeroIfNecessary(hour), DateUtil.addZeroIfNecessary(minutes)));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        menuItem = menu.add(Menu.NONE, R.id.menu_send, Menu.NONE, R.string.send)
                .setIcon(R.drawable.ic_send_selector);
        menuItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        menuItem.setEnabled(false);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        if (item.getItemId() == R.id.menu_send)
        {
            send();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth)
    {
        this.year = year;
        this.month = monthOfYear;
        this.day = dayOfMonth;

        updateDate();
    }

    private void send()
    {
        dialog = ProgressDialog.show(this, null, getString(R.string.sending_alert), true, false);

        JobManager.addJobInBackground(new SendHandrailJob(alertId,
                                                          origin.getText().toString(),
                                                          getDate(),
                                                          description.getText().toString()));
    }

    public long getDate()
    {
        Calendar c = Calendar.getInstance();
        c.set(year, month, day, hour, minutes);
        return c.getTimeInMillis();
    }

    @Subscribe
    public void onSendHandrailEvent(SendHandrailEvent event)
    {
        dialog.dismiss();

        if (event.isSuccess())
        {
            setResult(RESULT_OK);
            finish();
        }
        else
        {
            Toast.makeText(this, R.string.message_send_error, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onResume()
    {
        super.onResume();

        if (origin != null)
            origin.addTextChangedListener(textWatcher);

        if (description != null)
            description.addTextChangedListener(textWatcher);
    }

    @Override
    protected void onPause()
    {
        super.onPause();

        if (origin != null)
            origin.removeTextChangedListener(textWatcher);

        if (description != null)
            description.removeTextChangedListener(textWatcher);
    }
}

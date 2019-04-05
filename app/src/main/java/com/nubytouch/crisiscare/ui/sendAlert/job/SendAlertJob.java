package com.nubytouch.crisiscare.ui.sendAlert.job;

import com.birbit.android.jobqueue.Params;
import com.nubytouch.crisiscare.job.AbstractEvent;
import com.nubytouch.crisiscare.job.FetchRemoteJob;
import com.nubytouch.crisiscare.rest.ServiceBuilder;
import com.nubytouch.crisiscare.ui.sendAlert.rest.SendAlertService;
import com.nubytouch.crisiscare.ui.sendAlert.rest.dto.SendAlertResultDTO;

import org.apache.commons.lang3.StringUtils;

import java.util.List;

import retrofit2.Call;

public class SendAlertJob extends FetchRemoteJob<SendAlertResultDTO>
{
    private final String title;
    private final int priority;
    private final List<String> recipients;
    private final double latitude;
    private final double longitude;

    public SendAlertJob(String title, int priority, List<String> recipients, double latitude, double longitude)
    {
        super(new Params(Priority.MEDIUM.value));

        this.title = title;
        this.priority = priority;
        this.recipients = recipients;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    @Override
    protected AbstractEvent<SendAlertResultDTO> buildEvent(SendAlertResultDTO data)
    {
        return new AlertSentEvent(data);
    }

    @Override
    public Call<SendAlertResultDTO> fetchServer()
    {
        SendAlertService service = new ServiceBuilder().build(SendAlertService.class);
        String rec = StringUtils.join(recipients, ",");
        Call<SendAlertResultDTO> call = service.sendAlert(new SendAlertService.AlertData(title, priority, rec, latitude,
                                                                                         longitude));
        return call;
    }

    @Override
    public boolean checkData(SendAlertResultDTO data)
    {
        return data != null && data.alertId != null && !data.alertId.isEmpty();
    }
}

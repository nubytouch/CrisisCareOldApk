package com.nubytouch.crisiscare.ui.alerts.job;

import com.birbit.android.jobqueue.Params;
import com.nubytouch.crisiscare.job.AbstractEvent;
import com.nubytouch.crisiscare.job.FetchRemoteJob;
import com.nubytouch.crisiscare.rest.ServiceBuilder;
import com.nubytouch.crisiscare.ui.alerts.rest.AlertService;
import com.nubytouch.crisiscare.ui.alerts.rest.dto.SendHandrailResultDTO;
import com.nubytouch.crisiscare.utils.DateUtil;

import retrofit2.Call;

public class SendHandrailJob extends FetchRemoteJob<SendHandrailResultDTO>
{
    private final String alertId;
    private final String origin;
    private final long   date;
    private final String description;

    public SendHandrailJob(String alertId, String origin, long date, String description)
    {
        super(new Params(Priority.HIGH.value));

        this.alertId = alertId;
        this.origin = origin;
        this.date = date;
        this.description = description;
    }

    @Override
    protected AbstractEvent<SendHandrailResultDTO> buildEvent(SendHandrailResultDTO data)
    {
        return new SendHandrailEvent(data);
    }

    @Override
    public Call<SendHandrailResultDTO> fetchServer()
    {
        AlertService service = new ServiceBuilder().build(AlertService.class);
        return service.sendHandRail(new AlertService.SendHandrailData(alertId,
                                                                      origin,
                                                                      formatDate(),
                                                                      description));
    }

    private String formatDate()
    {
        return DateUtil.getDotNetDateFromTimestamp(date);
    }

    @Override
    public boolean checkData(SendHandrailResultDTO data)
    {
        return data != null && data.alertId != null && !data.alertId.isEmpty();
    }
}

package com.nubytouch.crisiscare.ui.settings.job;

import com.nubytouch.crisiscare.data.model.User;
import com.nubytouch.crisiscare.job.AbstractEvent;
import com.nubytouch.crisiscare.ui.settings.rest.dto.UpdateDeputyResultDTO;

public class UpdateDeputyEvent extends AbstractEvent<UpdateDeputyResultDTO>
{
    private final String deputyUsernames;

    public UpdateDeputyEvent(UpdateDeputyResultDTO data, String deputyUsernames)
    {
        super(data);
        this.deputyUsernames = deputyUsernames;
    }

    public String getDeputyUsernames()
    {
        return deputyUsernames;
    }
}

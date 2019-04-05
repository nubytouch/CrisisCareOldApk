package com.nubytouch.crisiscare.ui.login.job;

import com.nubytouch.crisiscare.job.AbstractEvent;
import com.nubytouch.crisiscare.ui.login.rest.dto.UserWrapperDTO;

public class LoginEvent extends AbstractEvent<UserWrapperDTO>
{
    public LoginEvent(UserWrapperDTO data)
    {
        super(data);
    }

    @Override
    public boolean isSuccess()
    {
        return getData() != null && getData().data != null;
    }
}

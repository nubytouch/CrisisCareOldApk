package com.nubytouch.crisiscare.job;

import com.birbit.android.jobqueue.Params;
import com.nubytouch.crisiscare.core.Session;
import com.nubytouch.crisiscare.data.model.User;
import com.nubytouch.crisiscare.datapackage.DataPackageManager;
import com.nubytouch.crisiscare.push.PushManager;

public class LogoutJob extends AbstractJob
{
    private final User user;

    public LogoutJob(User user)
    {
        super(new Params(Priority.HIGH.value));
        this.user = user;
    }

    @Override
    protected void execute() throws Exception
    {

        new DataPackageManager().clearData();
        Session.logout();
        PushManager.unregister();

        postCompleteEvent(new LogoutEvent());
    }

    @Override
    protected void onRequestError()
    {
        postCompleteEvent(new LogoutEvent());
    }

    @Override
    public void onAdded()
    {

    }
}

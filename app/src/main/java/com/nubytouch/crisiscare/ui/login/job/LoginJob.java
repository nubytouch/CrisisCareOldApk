package com.nubytouch.crisiscare.ui.login.job;

import com.birbit.android.jobqueue.Params;
import com.nubytouch.crisiscare.core.Session;
import com.nubytouch.crisiscare.job.AbstractEvent;
import com.nubytouch.crisiscare.job.AbstractJob;
import com.nubytouch.crisiscare.job.ErrorType;
import com.nubytouch.crisiscare.rest.ServiceBuilder;
import com.nubytouch.crisiscare.ui.login.rest.LoginService;
import com.nubytouch.crisiscare.ui.login.rest.dto.UserWrapperDTO;
import retrofit2.Response;

public class LoginJob extends AbstractJob
{
    private final String login;
    private final String password;

    public LoginJob(String login, String password)
    {
        super(new Params(Priority.HIGH.value));

        this.login = login;
        this.password = password;
    }

    @Override
    protected void execute() throws Exception
    {
        LoginService             service = new ServiceBuilder(Session.getAuthenticateServerURL()).build(LoginService.class);
        Response<UserWrapperDTO> response    = service.login(new LoginService.LoginData(login, password)).execute();

        if (response.code() == 401)
        {
            setErrorType(ErrorType.NETWORK_ERROR);
            onRequestError();
            return;
        }

        if (response.isSuccessful())
        {
            if (!checkData(response.body()))
                setErrorType(ErrorType.NETWORK_ERROR);
        }

        postCompleteEvent(buildEvent(response.body()));
    }

    protected AbstractEvent<UserWrapperDTO> buildEvent(UserWrapperDTO data)
    {
        return new LoginEvent(data);
    }

    public boolean checkData(UserWrapperDTO data)
    {
        return data != null && data.data != null && data.data.getId() != null && !data.data.getId().isEmpty();
    }

    @Override
    protected void onRequestError()
    {
        postCompleteEvent(buildEvent(null));
    }

    @Override
    public void onAdded()
    {

    }
}

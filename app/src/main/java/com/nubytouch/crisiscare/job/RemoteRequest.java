package com.nubytouch.crisiscare.job;

import retrofit2.Call;

public interface RemoteRequest<T>
{
    Call<T> fetchServer();

    boolean checkData(T data);
}

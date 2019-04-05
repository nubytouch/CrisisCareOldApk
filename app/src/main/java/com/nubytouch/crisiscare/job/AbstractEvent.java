package com.nubytouch.crisiscare.job;

public class AbstractEvent<T>
{
    private final T data;
    private ErrorType errorType;
    private Throwable error;

    public AbstractEvent()
    {
        this.data = null;
    }

    public AbstractEvent(T data)
    {
        this.data = data;
    }

    public T getData()
    {
        return data;
    }

    public Throwable getError()
    {
        return error;
    }

    void setError(Throwable error)
    {
        this.error = error;
    }

    public ErrorType getErrorType()
    {
        return errorType;
    }

    void setErrorType(ErrorType errorType)
    {
        this.errorType = errorType;
    }

    public boolean isSuccess()
    {
        return errorType == null && error == null;
    }
}

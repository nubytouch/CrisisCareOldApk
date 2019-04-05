package com.nubytouch.crisiscare.rest;

import android.util.Base64;
import android.util.Log;

import com.nubytouch.crisiscare.BuildConfig;
import com.nubytouch.crisiscare.CrisisCare;
import com.nubytouch.crisiscare.core.Session;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.Locale;

import okhttp3.Cache;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import okio.Buffer;
import okio.BufferedSource;
import okio.ForwardingSource;
import okio.Okio;
import okio.Source;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class ServiceBuilder
{
    private final String                          server;
    private       long                            cacheDuration;
    private       boolean                         useCacheIfNoConnection;
    private       WeakReference<ProgressListener> progressListener;

    public static long SECOND = 1;
    public static long MINUTE = 60;
    public static long HOUR   = 60 * 60;
    public static long DAY    = 24 * 60 * 60;

    private static Retrofit.Builder     retrofitBuilder;
    private static OkHttpClient.Builder okHttpBuilder;

    public ServiceBuilder(String server)
    {
        this.server = server;
    }

    public ServiceBuilder()
    {
        server = Session.getServerURL();
    }

    /**
     * Enable cache use when no connection available
     *
     * @return The ServiceBuilder instance for method chaining
     */
    public ServiceBuilder progressListener(ProgressListener listener)
    {
        progressListener = new WeakReference<>(listener);
        return this;
    }

    public <T> T build(final Class<T> service)
    {
        initBuilders();

        OkHttpClient.Builder client = okHttpBuilder;

        if (progressListener != null && progressListener.get() != null)
        {
            final ProgressListener listener = progressListener != null && progressListener.get() != null ?
                                              progressListener.get() : null;

            client.addNetworkInterceptor(new Interceptor()
            {
                @Override
                public Response intercept(Chain chain) throws IOException
                {
                    Response originalResponse = chain.proceed(chain.request());
                    return originalResponse.newBuilder()
                            .body(new ProgressResponseBody(originalResponse.body(), listener))
                            .build();
                }
            });
        }

        Retrofit retrofit = retrofitBuilder
                .client(client.build())
                .baseUrl(server)
                .build();

        return retrofit.create(service);
    }

    private static void initBuilders()
    {
        if (okHttpBuilder == null)
        {
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);
            okHttpBuilder = new OkHttpClient.Builder()
                    .addInterceptor(new Interceptor()
                    {
                        @Override
                        public Response intercept(Chain chain) throws IOException
                        {
                            Request.Builder builder = chain
                                    .request()
                                    .newBuilder()
                                    // ToDo set correct language
                                    .addHeader("lang", Locale.getDefault().getLanguage() + "-" + Locale.getDefault().getCountry())
                                    .addHeader("Content-type", "application/json");

                            builder.addHeader("Authorization", BuildConfig.AUTH_TOKEN);

                            return chain.proceed(builder.build());
                        }
                    });

            if (BuildConfig.DEBUG)
              okHttpBuilder.addInterceptor(logging);
        }

        if (retrofitBuilder == null)
        {
            retrofitBuilder = new Retrofit.Builder()
                    .addConverterFactory(GsonConverterFactory.create())
                    .addConverterFactory(ScalarsConverterFactory.create());
        }
    }

    private static class ProgressResponseBody extends ResponseBody
    {

        private final ResponseBody     responseBody;
        private final ProgressListener progressListener;
        private       BufferedSource   bufferedSource;

        public ProgressResponseBody(ResponseBody responseBody, ProgressListener progressListener)
        {
            this.responseBody = responseBody;
            this.progressListener = progressListener;
        }

        @Override
        public MediaType contentType()
        {
            return responseBody.contentType();
        }

        @Override
        public long contentLength()
        {
            return responseBody.contentLength();
        }

        @Override
        public BufferedSource source()
        {
            if (bufferedSource == null)
            {
                bufferedSource = Okio.buffer(source(responseBody.source()));
            }
            return bufferedSource;
        }

        private Source source(Source source)
        {
            return new ForwardingSource(source)
            {
                long totalBytesRead = 0L;

                @Override
                public long read(Buffer sink, long byteCount) throws IOException
                {
                    long bytesRead = super.read(sink, byteCount);
                    // read() returns the number of bytes read, or -1 if this source is exhausted.
                    totalBytesRead += bytesRead != -1 ? bytesRead : 0;

                    if (progressListener != null)
                        progressListener.update(totalBytesRead, responseBody.contentLength(), bytesRead == -1);

                    return bytesRead;
                }
            };
        }
    }

    public interface ProgressListener
    {
        void update(long bytesRead, long contentLength, boolean done);
    }


    //___ Security issues

    /**
     * Define the max valid age before cache is expired
     *
     * @param duration in seconds
     * @return The ServiceBuilder instance for method chaining
     */
    /*public ServiceBuilder cacheDuration(long duration)
    {
        cacheDuration = duration;
        return this;
    }*/


    /**
     * Set the cache duration to a default 10 minutes
     * and enable cache use when no connection available
     *
     * @return The ServiceBuilder instance for method chaining
     */
    /*public ServiceBuilder defaultCache()
    {
        cacheDuration = 10 * MINUTE;
        useCacheIfNoConnection = true;
        return this;
    }*/

    /**
     * Enable cache use when no connection available
     *
     * @return The ServiceBuilder instance for method chaining
     */
    /*public ServiceBuilder useCacheIfNoConnection()
    {
        useCacheIfNoConnection = true;
        return this;
    }*/
}

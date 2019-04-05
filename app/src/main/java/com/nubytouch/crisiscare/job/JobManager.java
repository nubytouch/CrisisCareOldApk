package com.nubytouch.crisiscare.job;

import android.content.Context;
import timber.log.Timber;

import com.birbit.android.jobqueue.Constraint;
import com.birbit.android.jobqueue.Job;
import com.birbit.android.jobqueue.TagConstraint;
import com.birbit.android.jobqueue.config.Configuration;
import com.birbit.android.jobqueue.log.CustomLogger;

public class JobManager
{
    private static com.birbit.android.jobqueue.JobManager INSTANCE;

    public static void init(Context context)
    {
        if (INSTANCE == null)
        {
            Configuration.Builder builder = new Configuration.Builder(context)
                    .customLogger(new CustomLogger()
                    {
                        private static final String TAG = "JOBS";

                        @Override
                        public boolean isDebugEnabled()
                        {
                            return false;
                        }

                        @Override
                        public void d(String text, Object... args)
                        {
                            //Timber.d(String.format(text, args));
                        }

                        @Override
                        public void e(Throwable t, String text, Object... args)
                        {
                            //Timber.e(String.format(text, args), t);
                        }

                        @Override
                        public void e(String text, Object... args)
                        {
                            //Timber.e(String.format(text, args));
                        }

                        @Override
                        public void v(String text, Object... args)
                        {

                        }
                    })
                    .minConsumerCount(1)//always keep at least one consumer alive
                    .maxConsumerCount(3)//up to 3 consumers at a time
                    .loadFactor(3)//3 jobs per consumer
                    .consumerKeepAlive(120);//wait 2 minute

            INSTANCE = new com.birbit.android.jobqueue.JobManager(builder.build());
        }
    }

    public static void addJob(Job job)
    {
        INSTANCE.addJob(job);
    }

    public static void addJobInBackground(Job job)
    {
        INSTANCE.addJobInBackground(job);
    }

    public static void cancelJobsInBackground(String tag)
    {
        INSTANCE.cancelJobsInBackground(null, TagConstraint.ANY, tag);
    }
}

package com.example.NewsFeed;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

public class SimpleService extends JobService {

    private static final String TAG = "1234";
    private boolean isJobCancelled;

    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        Log.d(TAG,"Job Cancelled");
        isJobCancelled = true;
        return false;
    }
    @Override
    public boolean onStartJob(JobParameters jobParameters) {
        Log.d(TAG, "Job Started");
        doSomeService(jobParameters);
        return true;
    }
    private void doSomeService(final JobParameters parameters) {
        Log.d(TAG, "Job Finished Swati Meena");
        System.out.println("HELLO Love");
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    if(NewsData.checkApi()==true ){
                        createNotification();
                    }
                   else{

                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (XmlPullParserException e) {
                    e.printStackTrace();
                }
                Log.d(TAG, "Job Finished Swati");
                jobFinished(parameters, false);
            }
        }).start();
    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "notif";//getString(R.string.channel_name);
            String description = "first noti";//getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("lemubitA", name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
    public void createNotification() {
        createNotificationChannel();

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "lemubitA")
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentTitle("News updated")
                .setContentText("Checkout for new news!")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);

// notificationId is a unique int for each notification that you must define
        notificationManager.notify(0, builder.build());
    }

}

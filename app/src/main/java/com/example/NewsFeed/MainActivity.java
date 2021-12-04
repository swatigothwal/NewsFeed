package com.example.NewsFeed;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {


        private ListView newsListView;
        private ArrayList<RssItem> rssItemList;
        private ArrayAdapter<RssItem> newsArrayAdapter;

        private IntentFilter intentFilter;


        private BroadcastReceiver intentReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                //overwrites the RssList
                ArrayList<RssItem> tempRssList =  (ArrayList<RssItem>)intent.getSerializableExtra("stories");
                if (tempRssList != null) {
                    rssItemList.clear();
                    rssItemList.addAll(tempRssList);
                    newsArrayAdapter.notifyDataSetChanged();
                }
            }
        };


        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);

            ComponentName name = new ComponentName(this, SimpleService.class);
            JobInfo info = new JobInfo.Builder(123, name)
                    .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
                    .setRequiresCharging(true)
                    .setPersisted(true)
                    .setPeriodic(1000).build();
            JobScheduler scheduler =    (JobScheduler)getSystemService(JOB_SCHEDULER_SERVICE);
            int response = scheduler.schedule(info);
            if(scheduler.schedule(info)>0){
                Log.d("1234","Success");
                Toast.makeText(MainActivity.this,
                        "Successfully scheduled job: " + response,
                        Toast.LENGTH_SHORT).show();
            }else{
                Log.d("1234","Fu");

                Toast.makeText(MainActivity.this,
                        "RESULT_FAILURE: " + response,
                        Toast.LENGTH_SHORT).show();
            }
          //  new SimpleService();

            newsListView = findViewById(R.id.newsListView);

            rssItemList = new ArrayList<RssItem>();
            newsArrayAdapter = new RssItemAdapter(this, android.R.layout.simple_list_item_1, rssItemList);

            newsListView.setAdapter(newsArrayAdapter);

            intentFilter = new IntentFilter();
            intentFilter.addAction("RSS_RETRIEVED");
            registerReceiver(intentReceiver,intentFilter);
            startService(new Intent(this, RetrieveFeedService.class));

            newsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView parent, View view, int position, long id) {
                    Log.d("IT472", " clicked news " + position);

                    String url = rssItemList.get(position).getLink();
                    try {
                        Uri uri = Uri.parse("googlechrome://navigate?url=" + url);
                        Intent i = new Intent(Intent.ACTION_VIEW, uri);
                        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(i);
                    } catch (ActivityNotFoundException e) {
                        Log.d("IT472", "error found" + position);
                        // Chrome is probably not installed
                    }

                }
            });
        }

        @Override
        public void onDestroy() {
            super.onDestroy();
            unregisterReceiver(intentReceiver);
        }

private void createNotificationChannel() {
    // Create the NotificationChannel, but only on API 26+ because
    // the NotificationChannel class is new and not in the support library
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        CharSequence name = "news updated";//getString(R.string.channel_name);
        String description = "news updated";//getString(R.string.channel_description);
        int importance = NotificationManager.IMPORTANCE_DEFAULT;
        NotificationChannel channel = new NotificationChannel("lemubitA", name, importance);
        channel.setDescription(description);
        // Register the channel with the system; you can't change the importance
        // or other notification behaviors after this
        NotificationManager notificationManager = getSystemService(NotificationManager.class);
        notificationManager.createNotificationChannel(channel);
    }
}
public void createNotification(View view) {
            createNotificationChannel();

    NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "lemubitA")
            .setSmallIcon(R.drawable.ic_launcher_background)
            .setContentTitle("Notification by NewsFeed")
            .setContentText("News Updated, Check it now!")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT);
    NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);

// notificationId is a unique int for each notification that you must define
    notificationManager.notify(0, builder.build());
}
    }
package com.example.termplanner;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class MyReceiver extends BroadcastReceiver {
    static int notifId =3;
    String channel = "test";

    @Override
    public void onReceive(Context context, Intent intent) {
        String text = intent.getStringExtra("text");
        String title = intent.getStringExtra("title");
        createNotificationChannel(context, channel);
        Notification nb = new NotificationCompat.Builder(context, channel)
                .setSmallIcon(R.drawable.ic_baseline_school_24)
                .setContentText(text)
                .setContentTitle(title)
                .build();

        NotificationManagerCompat nm = NotificationManagerCompat.from(context);
        nm.notify(notifId++,nb);
    }
    private void createNotificationChannel(Context context, String ch){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Log.i("createNotificationChannel","This code was reached");
            NotificationChannel channel = new NotificationChannel(ch, "TPNotify", NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription("Student notification for courses and assessment due dates/start dates");

            NotificationManager nm = context.getSystemService(NotificationManager.class);
            nm.createNotificationChannel(channel);
        }
    }
}

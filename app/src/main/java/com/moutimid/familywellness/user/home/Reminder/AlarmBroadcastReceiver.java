package com.moutimid.familywellness.user.home.Reminder;


import static com.moutimid.familywellness.user.home.Reminder.App.CHANNEL_ID;

import android.Manifest;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.provider.Settings;

import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.moutamid.familywellness.R;

public class AlarmBroadcastReceiver extends BroadcastReceiver {
    private NotificationManagerCompat notificationManager;
    public static int NOTIFICATION_NUMBER = 1;


    @Override
    public void onReceive(Context context, Intent intent) {
        String medName = intent.getStringExtra("medName");
        String medQty = intent.getStringExtra("medQty");
        String userName = intent.getStringExtra("userName");

        MediaPlayer player = MediaPlayer.create(context, Settings.System.DEFAULT_RINGTONE_URI);
        player.start();


        notificationManager = NotificationManagerCompat.from(context);
        sendOnChannel(context, userName + ", please take " + medQty + " dose of " + medName + ".", intent);

    }

    public void sendOnChannel(Context context, String message, Intent intent) {
        Intent resultIntent = new Intent(context, AlarmActivity.class);
        resultIntent.putExtra("medName", intent.getStringExtra("medName"));
        resultIntent.putExtra("medTime", intent.getStringExtra("medTime"));
        resultIntent.putExtra("medQty", intent.getStringExtra("medQty"));
        resultIntent.putExtra("userName", intent.getStringExtra("userName"));
        PendingIntent resultPendingIntent = PendingIntent.getActivity(context, 1, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        Notification notification = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_baseline_add_alarm_24)
                .setContentTitle("Medicine Reminder")
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_ALARM)
                .setAutoCancel(true)
                .setContentIntent(resultPendingIntent)
                .build();

        notificationManager.notify(NOTIFICATION_NUMBER++, notification);

        }
    }

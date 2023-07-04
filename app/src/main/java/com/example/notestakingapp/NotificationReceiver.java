package com.example.notestakingapp;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Vibrator;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

public class NotificationReceiver extends BroadcastReceiver {

    String newTitle;
    String updatedTitle;
    static final String CHANNEL_ID="100";
    @RequiresApi(api= Build.VERSION_CODES.Q)
    @Override
    public void onReceive(Context context, Intent intent) {
        Vibrator vibrator=(Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(4000);

        newTitle="";
        updatedTitle="";

         newTitle=intent.getStringExtra("newTitle");
         updatedTitle=intent.getStringExtra("updatedTitle");


        Toast.makeText(context,"Alarm Wake Up! Wake Up! ",Toast.LENGTH_LONG).show();
        Uri alarmUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        if(alarmUri==null){
            alarmUri=RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        }

        //Setting default ringtone
        Ringtone ringtone=RingtoneManager.getRingtone(context,alarmUri);

        makeNotification(context);
        //play ringtone
        ringtone.play();

        Log.d("while adding a new note","reached notification receiver");

    }
    public void makeNotification(Context context){
        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(context, CHANNEL_ID)
                        .setSmallIcon(R.drawable.notification_icon)
                        .setContentTitle(newTitle)
                        .setContentText("Your task is scheduled for today!")
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT);
        builder.setAutoCancel(true);

        NotificationManager notificationManager=
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel=
                    notificationManager.getNotificationChannel(CHANNEL_ID);
            if(notificationChannel==null){
                int importance=NotificationManager.IMPORTANCE_HIGH;
                notificationChannel=new NotificationChannel(CHANNEL_ID,
                        "Some description",importance);
                notificationChannel.setLightColor(Color.GREEN);
                notificationChannel.enableVibration(true);
                notificationManager.createNotificationChannel(notificationChannel);
            }
        }
        notificationManager.notify(0,builder.build());
    }
}

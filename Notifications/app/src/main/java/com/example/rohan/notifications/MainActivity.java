package com.example.rohan.notifications;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.NotificationCompat;
import android.view.View;

public class MainActivity extends AppCompatActivity {
    NotificationCompat.Builder notif;
    private static final int id = 40111;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        notif= new NotificationCompat.Builder(this);
        notif.setAutoCancel(true);
    }
    public void function(View view){
        notif.setSmallIcon(R.drawable.ic_stat_brightness_medium);

        notif.setTicker("HEllo Rohan");
        notif.setWhen(System.currentTimeMillis());
        notif.setContentTitle("This is the title");
        notif.setContentText("Her" +
                "" +
                "e is the text");
        Intent i = new Intent(this,MainActivity.class);

        PendingIntent p = PendingIntent.getActivity(this,0,i,PendingIntent.FLAG_UPDATE_CURRENT);
        notif.setContentIntent(p);
        NotificationManager m = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        m.notify(id,notif.build());
        System.out.println("Rohan Rohit");

    }
}

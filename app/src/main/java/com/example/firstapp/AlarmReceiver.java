package com.example.firstapp;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;



public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        PendingIntent Sender = PendingIntent.getBroadcast(context, 0, intent, 0);

        Log.e("We are at the AlarmReceiver", "good!");

        Intent serviceIntent = new Intent(context, RingtoneService.class);

        String getStatus = intent.getExtras().getString("extra");
        String vibrate = intent.getExtras().getString("vibration");
        String songNum = intent.getExtras().getString("ringtone");
        Log.e("extra in AlarmReceiver", getStatus);
        Log.e("AlarmReceiver songNum", songNum);
        Log.e("AlarmReceiver vibrate", vibrate);

        serviceIntent.putExtra("vibration", vibrate);
        serviceIntent.putExtra("ringtone", songNum);

        serviceIntent.putExtra("extra",getStatus);

        context.startService(serviceIntent);


    }


}

package com.example.firstapp;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;

import androidx.annotation.Nullable;

public class RingtoneService extends Service {


    MediaPlayer song;
    boolean isRunning;
    int startId;
    Vibrator v;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e("We are in Service", "OnStartCommand");



        String getStatus = intent.getExtras().getString("extra");
        String vStatus = intent.getExtras().getString("vibration");


        String songNum = intent.getExtras().getString("ringtone");

        Log.e("ringtone", "number "+songNum);
        Log.e("vibration", vStatus);







        assert  getStatus != null;
        if(getStatus.equals("on")){
            startId = 1;

        }
        else{
            startId = 0;

        }



        // no music and wants on
        if(!this.isRunning && startId == 1){

            if(songNum.equals("0")){
                song = MediaPlayer.create(this, R.raw.happy);
            }
            else if(songNum.equals("1")){
                song = MediaPlayer.create(this, R.raw.rooster);
            }
            else{
                song = MediaPlayer.create(this, R.raw.classic);
            }

            if(vStatus.equals("on")){
                v = (Vibrator) getSystemService(VIBRATOR_SERVICE);
                v.vibrate(VibrationEffect.createOneShot(30000, VibrationEffect.DEFAULT_AMPLITUDE));
            }

            song.setLooping(true);
            song.start();
            this.isRunning = true;
            this.startId = 0;


        }
        else if(this.isRunning && startId == 0){    //music and wants off

            song.stop();
            song.reset();
            this.isRunning = false;
            this.startId = 0;

            if(vStatus.equals("on")){
                v.cancel();
            }



            NotificationManager notifyManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

            Intent intentMain = new Intent(this.getApplicationContext(), MainActivity.class);
            PendingIntent pendingIntentMain = PendingIntent.getActivity(this, 0, intentMain, 0);

            Notification notification = new Notification.Builder(this).setContentTitle("Alarm notification")
                    .setContentText("Click on this").setContentIntent(pendingIntentMain).setAutoCancel(true)
                    .setSmallIcon(R.drawable.alarm)
                    .build();

            notifyManager.notify(0, notification);

        }
        else if(!this.isRunning && startId == 0){   //no music and wants end

           this.isRunning = false;
           this.startId = 0;
        }
        else if (this.isRunning && startId == 1) {    //music and wants on
            this.isRunning = true;
            this.startId = 1;
        }






        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {

        super.onDestroy();
        this.isRunning =false;

    }
}

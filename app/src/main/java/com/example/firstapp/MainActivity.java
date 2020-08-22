package com.example.firstapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    private Button add;
    private TextView text;
    public final static int maxAlarm = 10;

    private AlarmFactory alarmFactory;
    Intent myIntent;
    PendingIntent alarmIntent;
    String song;
    String v;
    AlarmManager alarmManager;
    Calendar calendar;
    int hour;
    int min;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.alarmFactory = new AlarmFactory(this.getSupportFragmentManager());
        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        myIntent = new Intent(this, AlarmReceiver.class);

        add = findViewById(R.id.add);
        text = findViewById(R.id.text);
        song = "0";
        v = "off";
        setAddButton();


    }


    private void setAddButton(){

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(MainActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        MainActivity.this.createAlarm(selectedHour, selectedMinute);
                        MainActivity.this.setText("");
                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();
            }
        });

    }

    public void setText(String text){

        this.text.setText(text);
    }

   public void setRepeat(Calendar calendar, int sourceId){
        Log.e("SetRepeat Day:", Integer.toString(calendar.get(Calendar.DAY_OF_WEEK)));

       calendar.set(Calendar.MINUTE, min);
       calendar.set(Calendar.HOUR_OF_DAY, hour);
       calendar.set(Calendar.SECOND, 0);
       this.calendar = calendar;

       myIntent.putExtra("extra", "on");
       myIntent.putExtra("vibration", v);
       myIntent.putExtra("ringtone", song);


       alarmIntent = PendingIntent.getBroadcast(MainActivity.this, sourceId,myIntent,PendingIntent.FLAG_UPDATE_CURRENT);

       alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), alarmIntent);

   }

   public Calendar getCalendar(){
        return calendar;
   }

    public void createAlarm(int hour, int min){

        alarmFactory.createAlarmFragment("fragment1", hour, min, 0);
        this.hour = hour;
        this.min = min;
        setAlarm(hour, min, 0);
        text.setText("");


    }


    public void setAlarm(int hour, int min, int sourceId){

        calendar = Calendar.getInstance();

        if(hour == calendar.get(Calendar.HOUR_OF_DAY)){
            if(min <= calendar.get(Calendar.MINUTE)){
                calendar.add(Calendar.DATE,1);
            }
        }
        else if(hour < calendar.get(Calendar.HOUR_OF_DAY)){
            calendar.add(Calendar.DATE,1);
        }

        calendar.set(Calendar.MINUTE, min);
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.SECOND, 0);

        myIntent.putExtra("extra", "on");
        myIntent.putExtra("vibration", v);
        myIntent.putExtra("ringtone", song);


        alarmIntent = PendingIntent.getBroadcast(MainActivity.this, sourceId,myIntent,PendingIntent.FLAG_UPDATE_CURRENT);

        alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), alarmIntent);

    }

    public void cancelAlarm(){


        myIntent.putExtra("extra", "off");
        myIntent.putExtra("vibration", v);
        myIntent.putExtra("ringtone",song);
        alarmManager.cancel(alarmIntent);

        sendBroadcast(myIntent);
        Log.e("cancelAlarm", "cancelling alarm");
    }

    public void setRingtone(int tone){

        song = Integer.toString(tone);

        setAlarm(hour, min, 0);
        Log.e("setRingtone", Integer.toString(tone));


    }

    public void setVibrate(String on){
        v = on;
        Log.e("setVibrate", on);

        setAlarm(hour, min, 0);

    }


}
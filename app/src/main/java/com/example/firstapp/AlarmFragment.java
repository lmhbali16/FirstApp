package com.example.firstapp;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import com.github.aakira.expandablelayout.ExpandableRelativeLayout;

import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;


public class AlarmFragment extends Fragment implements AdapterView.OnItemSelectedListener {


    private Spinner ringtone;
    private Button detailButton;
    private Button delete;
    private Button alarmOff;
    private Button mon;
    private Button tue;
    private Button wed;
    private Button thu;
    private Button fri;
    private Button sat;
    private Button sun;
    private HashMap<Integer, Button> days;
    private CheckBox vibrate;

    private ExpandableRelativeLayout detailLayout;

    private TextView text;
    private Switch aSwitch;

    private int hour;
    private int min;
    private String fragmentId;
    boolean expand;
    private int sourceId;
    private MainActivity mainActivity;


    public AlarmFragment(int hour, int min, String fragmentId, int sourceId){
        this.fragmentId = fragmentId;
        expand = false;
        this.hour = hour;
        this.min = min;
        this.sourceId = sourceId;



    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        if(savedInstanceState != null){

        }

        mainActivity = (MainActivity) getActivity();
        mainActivity.setVibrate("off");
        mainActivity.setRingtone(0);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        return inflater.inflate(R.layout.fragment_alarm, container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        detailButton = view.findViewById(R.id.details);
        ringtone = view.findViewById(R.id.ringtone);

        String[] items = {"happy", "rooster", "classic"};

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_spinner_dropdown_item,items);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ringtone.setAdapter(adapter);
        ringtone.setSelection(0);


        ringtone.setOnItemSelectedListener(this);
        vibrate = view.findViewById(R.id.vibrate);

        delete = view.findViewById(R.id.delete);

        alarmOff = view.findViewById(R.id.alarmoff);
        this.setWeek(view);

        text = (TextView) view.findViewById(R.id.fragmentText);

        text.setText(this.getHour(this.hour)+":"+this.getMin(this.min));


        aSwitch = view.findViewById(R.id.switch_button);

        aSwitch.setChecked(true);
        this.setButtons();



        detailLayout = (ExpandableRelativeLayout) view.findViewById(R.id.extra_details);
        detailLayout.collapse();



    }


    private void setButtons(){

        this.aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if(!isChecked){
                    mainActivity.cancelAlarm();
                }
                else{
                    mainActivity.setAlarm(hour, min,sourceId);
                    setRepeat();
                }


            }
        });


        this.vibrate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = ((MainActivity) getActivity()).myIntent;

                if(vibrate.isChecked()){
                    mainActivity.setVibrate("on");
                }
                else{
                    mainActivity.setVibrate("off");
                }

            }
        });





        this.detailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlarmFragment.this.showInfo();
            }
        });

        this.delete.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {

                mainActivity.cancelAlarm();
                mainActivity.setText("No Alarm");
                getFragmentManager().beginTransaction().remove(AlarmFragment.this).commit();
            }
        });

        this.alarmOff.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                mainActivity.cancelAlarm();
                setRepeat();
            }
        });

        this.setButtonWeek();

    }

    private void setRepeat(){

        Calendar c = Calendar.getInstance();
        int day = c.get(Calendar.DAY_OF_WEEK);
        int i = day+1;
        if(i == 8){
            i = 1;
        }

        int dayCounter = 1;

        boolean flag = true;


        while (flag){

            Button button = days.get(i);
            ColorDrawable colorButton = (ColorDrawable) button.getBackground();
            int colour = colorButton.getColor();

            if(colour == Color.parseColor("#3DDC84")){


                c.add(Calendar.DATE,dayCounter);
                mainActivity.setRepeat(c, sourceId);
                flag = false;
                break;
            }

            if(i == 7){
                i = 0;
            }

            if(dayCounter > 7){
                flag = false;
            }

            i+= 1;
            dayCounter += 1;
        }

    }





    private void showInfo(){


        if(expand){
            detailButton.setText("Show Details");
            detailLayout.collapse();
        }
        else{

            detailButton.setText("Hide Details");
            detailLayout.expand();
        }
        expand = !expand;
    }

    private void setWeek(View view){
        mon = view.findViewById(R.id.mon);
        tue = view.findViewById(R.id.tue);
        wed = view.findViewById(R.id.wed);
        thu = view.findViewById(R.id.thu);
        fri = view.findViewById(R.id.fri);
        sat = view.findViewById(R.id.sat);
        sun = view.findViewById(R.id.sun);


        mon.setBackgroundColor(Color.parseColor("#dcdee0"));
        tue.setBackgroundColor(Color.parseColor("#dcdee0"));
        wed.setBackgroundColor(Color.parseColor("#dcdee0"));
        thu.setBackgroundColor(Color.parseColor("#dcdee0"));
        fri.setBackgroundColor(Color.parseColor("#dcdee0"));
        sat.setBackgroundColor(Color.parseColor("#dcdee0"));
        sun.setBackgroundColor(Color.parseColor("#dcdee0"));

        days = new HashMap<>();

        days.put(1, sun);
        days.put(2,mon);
        days.put(3,tue);
        days.put(4,wed);
        days.put(5,thu);
        days.put(6,fri);
        days.put(7,sat);


    }

    private void setButtonWeek(){

        mon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlarmFragment.this.changeColour(mon);

            }
        });

        tue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlarmFragment.this.changeColour(tue);

            }
        });
        wed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlarmFragment.this.changeColour(wed);

            }
        });
        thu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlarmFragment.this.changeColour(thu);

            }
        });
        fri.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlarmFragment.this.changeColour(fri);

            }
        });
        sat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlarmFragment.this.changeColour(sat);

            }
        });
        sun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlarmFragment.this.changeColour(sun);

            }
        });


    }

    private void changeColour(Button button){

        int i = 1;

        for(;i <=7;i++){
            if(days.get(i) == button){
                break;
            }
        }

        Calendar c = Calendar.getInstance();
        int today = c.get(Calendar.DAY_OF_WEEK);

        if(i == today){
            Button todaButton = days.get(i);

            ColorDrawable colorToday = (ColorDrawable) todaButton.getBackground();
            int numToday = colorToday.getColor();

            if(numToday == Color.parseColor("#3DDC84")){
                return;
            }

        }

        Calendar recentAlarm = mainActivity.getCalendar();

        int alarmDay = recentAlarm.get(Calendar.DAY_OF_WEEK);

        Button alarmButton = days.get(alarmDay);

        if(alarmDay < i){
            ColorDrawable alarmColor = (ColorDrawable) alarmButton.getBackground();
            int numColor = alarmColor.getColor();

            if(numColor == Color.parseColor("#3DDC84")){
                return;
            }

        }


        ColorDrawable colorButton = (ColorDrawable) button.getBackground();

        int colour = colorButton.getColor();

        int calendarDay = mainActivity.getCalendar().get(Calendar.DAY_OF_WEEK);


        if(colour == Color.parseColor("#3DDC84")){
            button.setBackgroundColor(Color.parseColor("#dcdee0"));

            if(i == calendarDay){
                mainActivity.cancelAlarm();
                setRepeat();
            }

        }
        else{

            button.setBackgroundColor(Color.parseColor("#3DDC84"));
            setRepeat();
        }

    }

    private String getHour(int hour){
        if(hour < 10){
            return "0"+hour;
        }
        else{
            return String.valueOf(hour);
        }
    }

    private String getMin(int min){

        if(min < 10){
            return "0"+min;
        }
        else{
            return String.valueOf(min);
        }

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        mainActivity.setRingtone(position);

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        mainActivity.setRingtone(0);
        ringtone.setSelection(0);

    }
}
package com.example.firstapp;

import android.app.AlarmManager;

import androidx.fragment.app.FragmentManager;

import java.util.HashMap;

public class AlarmFactory {

    private FragmentManager fragmentManager;
    public HashMap<String, Integer> ids;

    public AlarmFactory(FragmentManager fragmentManager){
        this.fragmentManager = fragmentManager;

        this.ids = new HashMap<>();
        ids.put("fragment1", R.id.fragment1);
        ids.put("fragment2", R.id.fragment2);
        ids.put("fragment3", R.id.fragment3);
        ids.put("fragment4", R.id.fragment4);
        ids.put("fragment5", R.id.fragment5);
        ids.put("fragment6", R.id.fragment6);
        ids.put("fragment7", R.id.fragment7);
        ids.put("fragment8", R.id.fragment8);
        ids.put("fragment9", R.id.fragment9);
        ids.put("fragment10", R.id.fragment10);


    }



    public void createAlarmFragment(String fragmentId, int hour, int min, int sourceId){



        AlarmFragment alarm = new AlarmFragment(hour, min, fragmentId, sourceId);

        this.setId(fragmentId,alarm);



    }

    private void setId(String fragmentId, AlarmFragment alarm){


        fragmentManager.beginTransaction().add(this.ids.get(fragmentId),alarm).commit();


    }
}

package com.augmentedsociety.myphr.presentation.vitalsigns;

import java.util.Calendar;

import android.app.Application;
import android.content.SharedPreferences;

public class MyApp extends Application{

	private static SharedPreferences sPreference;

    private static final long MIN_SAVE_TIME=1000;
    private static final String PREF_KEY_LAST_ACTIVE = "last_active";
    private static final String PREF_ID_TIME_TRACK = "time_track";

    public static void saveTimeStamp(){
        if(getElapsedTime() > MIN_SAVE_TIME){
            sPreference.edit().putLong(PREF_KEY_LAST_ACTIVE, timeNow()).commit();
            System.out.println("*********Timeout**********");
        }
    }

    public static long getElapsedTime(){
        return timeNow() - sPreference.getLong(PREF_KEY_LAST_ACTIVE,0);
    }

    private static long timeNow(){
        return Calendar.getInstance().getTimeInMillis();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sPreference = getSharedPreferences(PREF_ID_TIME_TRACK,MODE_PRIVATE);
    }
}


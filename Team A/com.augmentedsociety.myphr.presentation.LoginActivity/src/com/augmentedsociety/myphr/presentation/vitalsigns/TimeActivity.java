package com.augmentedsociety.myphr.presentation.vitalsigns;


import android.app.Activity;

public abstract class TimeActivity extends Activity {

    @Override
    public void onUserInteraction() {
        super.onUserInteraction();
        MyApp.saveTimeStamp();
    }

    public long getElapsed(){
        return MyApp.getElapsedTime();
    }

}
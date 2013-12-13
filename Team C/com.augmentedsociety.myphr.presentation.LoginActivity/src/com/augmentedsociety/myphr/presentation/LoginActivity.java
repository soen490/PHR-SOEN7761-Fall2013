package com.augmentedsociety.myphr.presentation;

import com.augmentedsociety.myphr.R;
import com.augmentedsociety.myphr.domain.logs.LogEventType;
import com.augmentedsociety.myphr.persistence.DbRegistry;
import com.augmentedsociety.myphr.presentation.logs.LogEventEmitter;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.View;

public class LoginActivity extends Activity {

    @Override
    public void onCreate(Bundle iSavedInstanceState) {
        super.onCreate(iSavedInstanceState);
        setContentView(R.layout.login);
        DbRegistry.getInstance(this);
     
    }
    
    public void login(View iView) {
    	
    	/* Fires a LogEvent to the LogItemEditor */
			LogEventEmitter.fireLogEvent(this, iView.getContext(), LogEventType.APP_LOGIN);
      Intent myIntent = new Intent(iView.getContext(), DisclaimerActivity.class);
      startActivityForResult(myIntent,0);
      
  
    }
}
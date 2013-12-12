package com.augmentedsociety.myphr.presentation;

import com.augmentedsociety.myphr.R;
import android.annotation.SuppressLint;
import android.os.Bundle;

@SuppressLint("NewApi")
public class MainPageActivity extends MenuActivity {
  @Override
  public void onCreate(Bundle iSavedInstanceState) {
    super.onCreate(iSavedInstanceState);
    setContentView(R.layout.main_page);
   
    int currentapiVersion = android.os.Build.VERSION.SDK_INT;
    if (currentapiVersion >= android.os.Build.VERSION_CODES.FROYO){
    	getActionBar().setDisplayShowHomeEnabled(false);
    }
    
  }
}

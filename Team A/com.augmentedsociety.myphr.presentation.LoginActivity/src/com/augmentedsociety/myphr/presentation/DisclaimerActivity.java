package com.augmentedsociety.myphr.presentation;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.augmentedsociety.myphr.R;
import com.augmentedsociety.myphr.domain.DefaultPageMapper;
import com.augmentedsociety.myphr.domain.MapperException;
import com.augmentedsociety.myphr.domain.logs.LogEventType;
import com.augmentedsociety.myphr.presentation.logs.LogEventEmitter;
import com.augmentedsociety.myphr.presentation.medicalhistory.ViewMedicalHistoryActivity;
import com.augmentedsociety.myphr.presentation.notifications.ViewNotificationsActivity;
import com.augmentedsociety.myphr.presentation.vitalsigns.VitalSignsActivity;

public class DisclaimerActivity extends Activity
{

	// public static String homePage;

	@SuppressLint("NewApi")
	@Override
	public void onCreate(Bundle iSavedInstanceState)
	{
		super.onCreate(iSavedInstanceState);
		setContentView(R.layout.disclaimer);

		int currentapiVersion = android.os.Build.VERSION.SDK_INT;
		if (currentapiVersion >= android.os.Build.VERSION_CODES.HONEYCOMB)
		{
			getActionBar().setDisplayShowHomeEnabled(false);
		}

		// try
		// {
		// //get homepage from database
		// homePage = DefaultPageMapper.get(getApplicationContext());
		// } catch (MapperException e)
		// {
		// //if we fail, show the error toast, and default to "Welcome"
		// Toast.makeText(getApplicationContext(),
		// e.getMessage(),
		// Toast.LENGTH_LONG).show();
		// homePage = "Welcome";
		// }
	}

	public void accept(View iView)
	{
		// go to main page
		/** Fires a LogEvent to the LogItemEditor */
		LogEventEmitter.fireLogEvent(this, iView.getContext(),
				LogEventType.DISCLAIMER_ACCEPT);

		// if(homePage.equals("Welcome")){
		// //Intent myIntent = new Intent(iView.getContext(),
		// MainPageActivity.class);
		// Intent myIntent = new Intent(iView.getContext(),
		// VitalSignsActivity.class);
		// startActivityForResult(myIntent,0);
		// }
		// else if (homePage.equals("Vital Signs")){

//		if (PreparationActivity.firstStart)
//		{
			Intent loginIntent = new Intent(this, SettingsActivity.class);
			startActivity(loginIntent);
//		} else
//		{
//			Intent myIntent = new Intent(iView.getContext(), VitalSignsActivity.class);
//			startActivityForResult(myIntent, 0);
//		}
		// }
		// else if (homePage.equals("Personal Information")){
		// Intent myIntent = new Intent(iView.getContext(),
		// PersonalInformationActivity.class);
		// startActivityForResult(myIntent,0);
		// }
		// else if (homePage.equals("Notifications")){
		// Intent myIntent = new Intent(iView.getContext(),
		// ViewNotificationsActivity.class);
		// startActivityForResult(myIntent,0);
		// }
		// else if (homePage.equals("Settings")){
		// Intent myIntent = new Intent(iView.getContext(), SettingsActivity.class);
		// startActivityForResult(myIntent,0);
		// }
		// else if (homePage.equals("Treatment History")){
		// Intent myIntent = new Intent(iView.getContext(),
		// ViewMedicalHistoryActivity.class);
		// startActivityForResult(myIntent,0);
		// }

	}

	public void decline(View iView)
	{
		// go to previous activity
		/** Fires a LogEvent to the LogItemEditor */
		LogEventEmitter.fireLogEvent(this, iView.getContext(),
				LogEventType.DISCLAIMER_DECLINE);
		Intent intent = new Intent();
		setResult(RESULT_OK, intent);
		finish();
	}

}

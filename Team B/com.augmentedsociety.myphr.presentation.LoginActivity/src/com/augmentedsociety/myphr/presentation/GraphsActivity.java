package com.augmentedsociety.myphr.presentation;

import javax.security.auth.Destroyable;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;

import com.augmentedsociety.myphr.R;
import com.augmentedsociety.myphr.domain.commands.CommandAction;
import com.augmentedsociety.myphr.domain.logs.LogEventType;
import com.augmentedsociety.myphr.presentation.logs.LogEventEmitter;
import com.augmentedsociety.myphr.presentation.vitalsigns.GraphActivity;
import com.augmentedsociety.myphr.presentation.vitalsigns.Measurement;
import com.augmentedsociety.myphr.presentation.vitalsigns.VitalSignsActivity;

public class GraphsActivity extends MenuActivity
{

	final static int WEIGHT = 0;
	final static int O2 = 1;
	final static int TEMPERATURE = 2;
	final static int BLOOD_PRESSURE = 3;
	final static int BLOOD_SUGAR = 4;
	final static int OVERVIEW = 5;
	
	final static String WEIGHT_STRING = "graphBodyWeight";
	final static String O2_STRING = "graphO2Saturation";
	final static String TEMPERATURE_STRING = "graphTemperature";
	final static String BLOOD_PRESSURE_STRING = "graphBloodPressure";
	final static String BLOOD_SUGAR_STRING = "graphBloodSugar";
	
	private final String GRAPHS = "graphs";

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle iSavedInstanceState)
	{
		super.onCreate(iSavedInstanceState);
		setContentView(R.layout.graphs);
		
		int currentapiVersion = android.os.Build.VERSION.SDK_INT;
		if (currentapiVersion >= android.os.Build.VERSION_CODES.HONEYCOMB)
		{
			super.getActionBar().setDisplayShowHomeEnabled(false);
		}
		
		SpeechHelp.getInstance().playTutorial(mAct, GRAPHS);
	}

	public void onButtonPressed(View iView)
	{
		int mCurrentSelection = 0;
		switch (iView.getId())
		{
		case R.id.buttonBloodPressure:
		case	R.id.rowBloodPressure:
			mCurrentSelection = BLOOD_PRESSURE;
			executeCommand(mCurrentSelection, iView);
			break;
		case R.id.buttonWeight:
		case	R.id.rowWeight:
			mCurrentSelection = WEIGHT;
			executeCommand(mCurrentSelection, iView);
			break;
		case R.id.buttonO2Saturation:
		case	R.id.rowO2Saturation:
			mCurrentSelection = O2;
			executeCommand(mCurrentSelection, iView);
			break;
		case R.id.buttonTemperature:
		case	R.id.rowTemperature:
			mCurrentSelection = TEMPERATURE;
			executeCommand(mCurrentSelection, iView);
			break;
		case R.id.buttonBloodSugar:
		case	R.id.rowBloodSugar:
			mCurrentSelection = BLOOD_SUGAR;
			executeCommand(mCurrentSelection, iView);
			break;
		case R.id.buttonLogout:

			/** Fires a LogEvent to the LogItemEditor */
			LogEventEmitter.fireLogEvent(this, getApplicationContext(),
					LogEventType.APP_LOGOUT);

			// remove all other activities off the stack
//			Intent myIntent = new Intent(this, LoginActivity.class);
//			myIntent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
//			startActivity(myIntent);

			// Set indeterminate progress dialog for loading time "UI feedback"
			final ProgressDialog logout_progress = ProgressDialog.show(this, "",
					getString(R.string.logging_out), true);
			logout_progress.setCancelable(true);

			// AsyncTask enables for background operations. In the following, while
			// the graphs load, the user gets "UI feedback" indicating loading time.
			AsyncTask<Void, Void, Boolean> logout_waitForCompletion = new AsyncTask<Void, Void, Boolean>()
			{
				// Progress bar dismissed after 1.5 seconds
				@Override
				protected Boolean doInBackground(Void... params)
				{
					long timeStarted = System.currentTimeMillis();
					while (System.currentTimeMillis() - timeStarted < 1500)
					{
						try
						{
							Thread.sleep(100);
						} catch (InterruptedException e)
						{
						}
					}
					logout_progress.dismiss();
					return null;
				};
			};
//			logout_waitForCompletion.execute(null, null, null);
			finish();
			break;
		}


	}

	private void executeCommand(int mCurrentSelection, View iView)
	{
		CommandAction ca = new CommandAction();
		Measurement.clearDataArray();
		Measurement.initializeDataArray(mCurrentSelection, iView, ca);
		Intent myIntent = new Intent(iView.getContext(), GraphActivity.class);
		String extraString = "";
		switch (mCurrentSelection)
		{
		case 0:
			extraString = WEIGHT_STRING;
			break;
		case 1:
			extraString = O2_STRING;
			break;
		case 2: 
			extraString = TEMPERATURE_STRING;
			break;
		case 3:
			extraString = BLOOD_PRESSURE_STRING;
			break;
		case 4:
			extraString = BLOOD_SUGAR_STRING;
			break;
		default:
			break;
		}
		myIntent.putExtra("measurement", extraString);
		startActivityForResult(myIntent, 0);
	}
	
	/**
	 * Override the Functionality to go back to the overview page in the
	 * measurement screen.
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)
	{
		Intent intent = new Intent(getApplicationContext(), VitalSignsActivity.class);
		startActivity(intent);
		return true;
	}
	
	@Override
	protected void onResume()
	{
		SpeechHelp tut = SpeechHelp.getInstance();
		if (tut.isActive())
			tut.playTutorial(this, GRAPHS);
		super.onResume();
	}
	
	@Override
	public void createHelpDialog(MenuItem item)
	{
		super.createHelpDialog(item);
		SpeechHelp tut = SpeechHelp.getInstance();
		if (tut.isActive())
		{
			tut.playTutorial(this, GRAPHS);
		} else
		{
			tut.stopTutorial();
		}
	}
	
}

package com.augmentedsociety.myphr.presentation.vitalsigns;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.augmentedsociety.myphr.R;
import com.augmentedsociety.myphr.domain.MapperException;
import com.augmentedsociety.myphr.domain.OxygenSaturationMapper;
import com.augmentedsociety.myphr.domain.OxygenSaturationReading;
import com.augmentedsociety.myphr.domain.ReadingSource;
import com.augmentedsociety.myphr.domain.TemperatureMapper;
import com.augmentedsociety.myphr.domain.TemperatureReading;
import com.augmentedsociety.myphr.domain.WeightMapper;
import com.augmentedsociety.myphr.domain.WeightReading;
import com.augmentedsociety.myphr.domain.logs.LogEventType;
import com.augmentedsociety.myphr.presentation.SpeechHelp;
import com.augmentedsociety.myphr.presentation.customviews.SquareImageButton;
import com.augmentedsociety.myphr.presentation.logs.LogEventEmitter;

/**
 * O2 Activity class for user inputs
 * 
 * @author Serge-Antoine
 * 
 */

public class O2Activity extends VitalSignComponent
{
	/**
	 * The following instantiations allow an instance of a controller to take the
	 * arguments generated by this activity in order for the controller itself to
	 * submit the data the the proper mapper via the commands package for
	 * compilation, determined by the VITAL_SIGN string's value
	 */
	private VitalSignsController mController = new VitalSignsController();
	private static final String VITAL_SIGN = "OxygenSaturation";
	private OxygenSaturationReading mBluetoothReading = null;
	private static final int REQUEST_ENABLE_BT = 1;
	private static  boolean savedSuccessful = false;
	private final String O2_SATURATION = "o2Saturation";
	protected boolean isDataValid = false;//fh


	public O2Activity(Activity mAct)
	{
		super(mAct);
		mAct.setTitle("Oxygen Saturation");
		
		EditText t = (EditText) mAct.findViewById(R.id.o2percent_value);
		t.setSelection(t.getText().length());
		
		SpeechHelp tut = SpeechHelp.getInstance();
		if(tut.isActive())
		{
			tut.playTutorial(mAct, O2_SATURATION);
		}
		else
		{
			tut.stopTutorial();
		}
	}

	/**
	 * Handles the "submit"/manual button press; if a Bluetooth reading was
	 * performed, this method will automatically be aware of it and handle it
	 * correctly in a "Bluetooth context entry" for the commands to the mapper
	 * i.e. saved as Bluetooth reading in the DB.
	 */
	public void submit(View iView, Date timeOfReading, boolean lastActivity)
	{
		//fh
  		/**
			 * An object created by bluetooth reading will have BLUETOOTH as a
			 * ReadingSource.
			 */
			if (null != mBluetoothReading
					&& ReadingSource.BLUETOOTH == mBluetoothReading.getMeasurementSource())
			{
				/** Get the view's current context for proper detection of activity launch */
				Context context = iView.getContext();
	
				/** References to text box values */
				TextView t = (TextView) mAct.findViewById(R.id.o2percent_value);
				String tText = t.getText().toString();
				if (!tText.isEmpty())
				{
					float oxygenSaturation = Float.valueOf(t.getText().toString());
	
					try
					{
						/**
						 * Since the OxygenSaturationReading has already been instantiated by
						 * the external BluetoothDevice mapper, it is passed directly to the
						 * OxygenSaturationMapper, without having to go through controller and
						 * command for VitalSignObject instantiation.
						 */
						OxygenSaturationMapper.insert(mBluetoothReading, context);
	
						/** Fires a LogEvent to the LogItemEditor */
						LogEventEmitter.fireLogEvent(this, iView.getContext(),
								LogEventType.VS_OXYGEN_CREATE);
	
						/**
						 * Calls a short text box confirming data save. Should not occur if an
						 * exception should be caught inside mappers
						 */
	//					Toast.makeText(context, context.getString(R.string.confirm_save),
	//							Toast.LENGTH_SHORT).show();
						savedSuccessful = true;
						VitalSignsActivity.hasBeenSaved = true;//fh
						if (lastActivity){
							saveMethod(t);
						}

						
						/**
						 * After data save, text box values are reset and focus is placed to
						 * the first entry of the page list
						 */
						t = (TextView) mAct.findViewById(R.id.o2percent_value);
						t.setText(null);
	
						mAct.findViewById(R.id.o2percent_value).requestFocus(
								(int) oxygenSaturation, null);
					} catch (MapperException e)
					{
						Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
					}
				} else
				{
					savedSuccessful = false;

	//				Toast.makeText(context, context.getString(R.string.prompt_save_again),
	//						Toast.LENGTH_LONG).show();
				}
			} else
			{
				/** Get the view's current context for proper detection of activity launch */
				Context context = iView.getContext();
	
				/** References to text box values */
				TextView t = (TextView) mAct.findViewById(R.id.o2percent_value);
				String tText = t.getText().toString();
				if (!tText.isEmpty())
				{
					float oxygenSaturation = Float.valueOf(t.getText().toString());
	//				Date timeOfReading = getDateValue(R.id.datePickerO2, R.id.timePickerO2);
	
					/**
					 * Controller passes user inputs to right mapper via commands package;
					 * data then processed into database upon successful entry.
					 */
					mController.manipulate(oxygenSaturation, timeOfReading, VITAL_SIGN,
							ReadingSource.KEYED, context);
	
					/** Fires a LogEvent to the LogItemEditor */
					LogEventEmitter.fireLogEvent(this, iView.getContext(),
							LogEventType.VS_OXYGEN_CREATE);
	
					/**
					 * Calls a short text box confirming data save. Should not occur if an
					 * exception should be caught inside mappers
					 */
	//				Toast.makeText(context, context.getString(R.string.confirm_save),
	//						Toast.LENGTH_SHORT).show();
					savedSuccessful = true;
					VitalSignsActivity.hasBeenSaved = true;//fh
					if (lastActivity){
						saveMethod(t);
					}

					
					/**
					 * After data save, text box values are reset and focus is placed to the
					 * first entry of the page list
					 */
					t = (TextView) mAct.findViewById(R.id.o2percent_value);
					t.setText(null);
	
					mAct.findViewById(R.id.o2percent_value).requestFocus(
							(int) oxygenSaturation, null);
				} else
				{
	//				Toast.makeText(context, context.getString(R.string.prompt_save_again),
	//						Toast.LENGTH_LONG).show();
					savedSuccessful = false;
				}
			}
	}
	
	public static boolean getSavedSuccessful()
	{
		return savedSuccessful;
	}

	/**
	 * Handles the "Bluetooth" button press
	 */
	public void bluetooth(View iView)
	{
		msgbox(
				"Bluetooth-enabled oxygen saturation reader feature not yet implemented.",
				"Notice");
	}

	protected void msgbox(String iMessage, String iTitle)
	{
		AlertDialog.Builder dlgAlert = new AlertDialog.Builder(mAct); // mAct: you
																																	// may want to
																																	// replace
																																	// with 'this'
																																	// if you move
																																	// this method
		dlgAlert.setMessage(iMessage);
		dlgAlert.setTitle(iTitle);
		dlgAlert.setPositiveButton("OK", null);
		dlgAlert.setCancelable(true);
		dlgAlert.create().show();
	}

	protected void onActivityResult(int iRequestCode, int iResultCode,
			Intent iData)
	{
		if (iRequestCode == REQUEST_ENABLE_BT)
		{
			if (iResultCode == Activity.RESULT_OK) // mAct: you may want to remove it
																							// if you move this method
			{
				msgbox("Bluetooth has been successfully enabled!", "Success");
				bluetooth(null);
			} else
				msgbox("Please enable Bluetooth to allow communicating to the device.",
						"Message");
		} else
		{
			msgbox("TemperatureActivity: Unknown request code: " + iRequestCode,
					"Error");
		}
	}

	public void finish(View iView)
	{
		// return to previous view
		mAct.finish();
	}

	public void preparePageTransition()
	{
		if (((SquareImageButton) (VitalSignsActivity.VITAL_SIGN_ACTIVITY
				.findViewById(R.id.o2_saturation))).isShown())
			showKeyboard();
		
		setTextFieldFocus(R.id.o2percent_value);
		createNextPageListener(R.id.o2percent_value);
	}
	protected void validateData(View iView){//fh
    //create state
    Context iContext = iView.getContext();
 
		if (this != null){
				try
				{
				// Get today as a Calendar  
					Calendar today = Calendar.getInstance();  
					// Subtract 1 day  
					today.add(Calendar.DATE, -7);  
					// Make an SQL Date out of that  
					Date lastSevenDays = new java.sql.Date(today.getTimeInMillis());
					ArrayList<OxygenSaturationReading> result = OxygenSaturationMapper.findLastSevenDays(lastSevenDays, iContext);
					TextView t = (TextView) mAct.findViewById(R.id.o2percent_value);
			
					float tOxygenSaturation = Float.valueOf(t.getText().toString());
					 

				  if (tOxygenSaturation < 95 || tOxygenSaturation > 100){
				  	isDataValid = false;
				  	return;
				  }
				  
				  float averageOxygen = 0;
				  for (OxygenSaturationReading bp: result){	
				  	averageOxygen += bp.getOxygenSaturation();
				  }
				  if (result.size() ==0){
				  	isDataValid=true;
				  	return;
				  }
				  averageOxygen=averageOxygen/result.size();
				  
				  float diff=0;
				  boolean diffOxygen = false;
				   
				  if (averageOxygen == tOxygenSaturation){
				  	diff=0;
				  } else if (averageOxygen > tOxygenSaturation){
				  	 diff = averageOxygen - tOxygenSaturation;
				  } else if (averageOxygen < tOxygenSaturation){
				  	 diff = tOxygenSaturation-averageOxygen;
				  }
				  if (diff > 5){
				  	diffOxygen = true;
				  }
				  
				  if (diffOxygen){
				  	isDataValid = false;
				  }else {
				  	isDataValid= true;
				  }
				  

				} catch (MapperException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			
		}
		//fh
	}

}

package com.augmentedsociety.myphr.presentation.vitalsigns;

import java.text.DecimalFormat;
import java.util.Date;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.augmentedsociety.myphr.R;
import com.augmentedsociety.myphr.domain.BloodSugarMapper;
import com.augmentedsociety.myphr.domain.BloodSugarReading;
import com.augmentedsociety.myphr.domain.MapperException;
import com.augmentedsociety.myphr.domain.ReadingSource;
import com.augmentedsociety.myphr.domain.logs.LogEventType;
import com.augmentedsociety.myphr.external.ForaD15BloodPressureSugarReader;
import com.augmentedsociety.myphr.presentation.SpeechHelp;
import com.augmentedsociety.myphr.presentation.customviews.SquareImageButton;
import com.augmentedsociety.myphr.presentation.logs.LogEventEmitter;

/**
 * Blood Sugar Activity class for user inputs
 * 
 * @author Serge-Antoine
 *
 */

public class BloodSugarActivity extends VitalSignComponent  
{
	/**The following instantiations allow an instance of a controller to take the arguments generated by
   * this activity in order for the controller itself to submit the data the the proper mapper via the commands package for
   * compilation, determined by the VITAL_SIGN string's value */
  private VitalSignsController mController = new VitalSignsController();
  private static final String VITAL_SIGN = "BloodSugar";
  private BloodSugarReading mBluetoothReading = null;
  private static final int REQUEST_ENABLE_BT = 1;
  private static boolean savedSuccessful = false;
  private final String BLOOD_SUGAR = "bloodSugar";

	public BloodSugarActivity(Activity mAct)
	{
		super(mAct);
		mAct.setTitle("Blood Sugar");
		
		EditText t = (EditText) mAct.findViewById(R.id.blood_sugar_value);
		t.setSelection(t.getText().length());
		
		SpeechHelp tut = SpeechHelp.getInstance();
		if(tut.isActive())
		{
			tut.playTutorial(mAct, BLOOD_SUGAR);
		}
		else
		{
			tut.stopTutorial();
		}
	}

	/**
	 * Handles the "submit"/manual button press; if a Bluetooth reading was performed, this method will automatically be aware of it
	 * and handle it correctly in a "Bluetooth context entry" for the commands to the mapper i.e. saved as Bluetooth reading in the DB.
	 */
	public void submit(View iView, Date timeOfReading) 
	{
		if (null != mBluetoothReading && ReadingSource.BLUETOOTH == mBluetoothReading.getMeasurementSource())
		{
			/**Get the view's current context for proper detection of activity launch*/
			Context context = iView.getContext();
			
			/**References to text box values*/
			TextView t = (TextView) mAct.findViewById(R.id.blood_sugar_value);
			String tText = t.getText().toString();
			if (!tText.isEmpty())
			{
				float bloodSugar = Float.valueOf(t.getText().toString());
				
				try
				{
					/**Since the BloodSugarReading has already been instantiated by the external BluetoothDevice mapper, it is passed directly
					 * to the BloodSugarMapper, without having to go through controller and command for VitalSignObject instantiation.*/
					BloodSugarMapper.insert(mBluetoothReading, context);
				
					/**Fires a LogEvent to the LogItemEditor*/
					LogEventEmitter.fireLogEvent(this, iView.getContext(), LogEventType.VS_BLOODSUGAR_CREATE);
					
					/**Calls a short text box confirming data save. Should not occur if an exception should be caught inside mappers*/
//					Toast.makeText(context, context.getString(R.string.confirm_save), Toast.LENGTH_SHORT).show();
					savedSuccessful = true;
					
					/**After data save, text box values are reset and focus is placed to the first entry of the page list*/
					t = (TextView) mAct.findViewById(R.id.blood_sugar_value);
					t.setText(null);
				
					mAct.findViewById(R.id.blood_sugar_value).requestFocus((int)bloodSugar, null);
				}
				catch (MapperException e)
				{
//					Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
				}
			}
			else
			{
//				Toast.makeText(context, context.getString(R.string.prompt_save_again), Toast.LENGTH_LONG).show();
				savedSuccessful = false;
			}
		}
		else
		{
			/**Get the view's current context for proper detection of activity launch*/
			Context context = iView.getContext();
			
			/**References to text box values*/
			TextView t = (TextView) mAct.findViewById(R.id.blood_sugar_value);
			String tText = t.getText().toString();
			if (!tText.isEmpty())
			{
				float bloodSugar = Float.valueOf(t.getText().toString());
//				Date timeOfReading = getDateValue(R.id.datePickerBS, R.id.timePickerBS);
				
				/**Controller passes user inputs to right mapper via commands package; data then processed into database upon successful entry.*/
				mController.manipulate(bloodSugar, timeOfReading, VITAL_SIGN, ReadingSource.KEYED, context);
			
				/**Fires a LogEvent to the LogItemEditor*/
				LogEventEmitter.fireLogEvent(this, iView.getContext(), LogEventType.VS_BLOODSUGAR_CREATE);
				
				/**Calls a short text box confirming data save. Should not occur if an exception should be caught inside mappers*/
//				Toast.makeText(context, context.getString(R.string.confirm_save), Toast.LENGTH_SHORT).show();
				savedSuccessful = true;
			
				
				/**After data save, text box values are reset and focus is placed to the first entry of the page list*/
				t = (TextView) mAct.findViewById(R.id.blood_sugar_value);
				t.setText(null);
			
				mAct.findViewById(R.id.blood_sugar_value).requestFocus((int)bloodSugar, null);
			}
			else
			{
//				Toast.makeText(context, context.getString(R.string.prompt_save_again), Toast.LENGTH_LONG).show();
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
  	BluetoothAdapter wBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
	  
	  if (wBluetoothAdapter == null) 
	  {
	    Toast.makeText(iView.getContext(), R.string.bluetooth_not_available, Toast.LENGTH_LONG).show();
	    return;
	  }
	  
	  if (!wBluetoothAdapter.isEnabled()) 
	  {
	    Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
	    mAct.startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
	    return;
	  }
 
	  boolean success = false;
	  int attemptsLeft = 5;
	  
	  final ProgressDialog wellness_progress = ProgressDialog.show(iView.getContext(), "", iView.getContext().getString(R.string.bluetooth_request), true);
    wellness_progress.setCancelable(true);
    
    /**
     * Asynchronic dialog box showing progression of reading transfer (note: there seems to be an inevitable lag right before retrieving the
     * readings; the following may or may not appear (user feedback may or may not appear).
     */
    AsyncTask<Void, Void, Boolean> bluetooth_waitForReading = new AsyncTask<Void, Void, Boolean>()
		{
	  	@Override
      protected Boolean doInBackground(Void... params) 
	  	{
	  	//Loading of 3 seconds on average for data gathering from bluetooth thermometer
	  		long timeStarted = System.currentTimeMillis();
	  		while (System.currentTimeMillis() - timeStarted < 3000)
	  		{
	        try 
	        {
	        	Thread.sleep(100);
	        } 
	        catch (InterruptedException e) {}
	  		}
		  	wellness_progress.dismiss();
        return null;
      };
	  };
	  
	  bluetooth_waitForReading.execute(null, null, null);
	  
	  while (!success && attemptsLeft > 0)
	  {
	    try
	    {
	      mBluetoothReading =  ForaD15BloodPressureSugarReader.getLastBloodSugarReading(mAct);
	      success = true;
	    } 
	    catch (Exception e)
	    {
	      --attemptsLeft;
	    }
	  }
	  if (success)
	  {
	  	String wBloodSugarString = (new DecimalFormat("###")).format(mBluetoothReading.getBloodGlucose());
	    
			//Puts the retrieved reading value on the textbox, ready to submit.
	    TextView t1 = (TextView) mAct.findViewById(R.id.blood_sugar_value);
	    t1.setText(wBloodSugarString);
	  }
	  else
	  {
	    msgbox (mAct.getString(R.string.bluetooth_error_5_times), mAct.getString(R.string.error));
	  }
  }
  
  protected void msgbox(String iMessage, String iTitle)
	{
    AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(mAct); //mAct: you may want to replace with 'this' if you move this method                     
    dlgAlert.setMessage(iMessage);
    dlgAlert.setTitle(iTitle);              
    dlgAlert.setPositiveButton("OK", null);
    dlgAlert.setCancelable(true);
    dlgAlert.create().show();
	}
  
  protected void onActivityResult (int iRequestCode, int iResultCode, Intent iData)
	{
	  if (iRequestCode == REQUEST_ENABLE_BT)
	  {
	    if(iResultCode == Activity.RESULT_OK) //mAct: you may want to remove it if you move this method
	    {
	      msgbox(mAct.getString(R.string.bluetooth_success_enabled), mAct.getString(R.string.success) );
	      bluetooth(null);
	    }
	    else
	      msgbox(mAct.getString(R.string.bluetooth_error_not_enabled), mAct.getString(R.string.message));
	  }
	  else
	  {
	    msgbox(mAct.getString(R.string.bloodsugar_error_unknown_id) + iRequestCode, mAct.getString(R.string.error));
	  }
	}

  public void finish(View iView)
  {
  	//return to previous view
  	mAct.finish();
  }
  
	public void preparePageTransition()
	{
		if (((SquareImageButton) (VitalSignsActivity.VITAL_SIGN_ACTIVITY
				.findViewById(R.id.blood_sugar))).isShown())
			showKeyboard();
		
		setTextFieldFocus(R.id.blood_sugar_value);
		createNextPageListener(R.id.blood_sugar_value);

	}
}

package com.augmentedsociety.myphr.presentation.personalinfo;

import java.util.Calendar;
import java.util.Date;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.augmentedsociety.myphr.R;
import com.augmentedsociety.myphr.barcode.PatientInfo;
import com.augmentedsociety.myphr.domain.MapperException;
import com.augmentedsociety.myphr.domain.ProfileMapper;
import com.augmentedsociety.myphr.domain.ProfileReading;
import com.augmentedsociety.myphr.domain.logs.LogEventType;
import com.augmentedsociety.myphr.persistence.PersistenceException;
import com.augmentedsociety.myphr.persistence.ProfileTDG;
import com.augmentedsociety.myphr.presentation.DisclaimerActivity;
import com.augmentedsociety.myphr.presentation.PersonalInformationActivity;
import com.augmentedsociety.myphr.presentation.PreparationActivity;
import com.augmentedsociety.myphr.presentation.logs.LogEventEmitter;

public class PersonalInformationReadingActivity extends PersonalInfoComponent
{
	private final String ADDITIONAL_ZERO = "0";
	private Date tDate = null;
	Activity iAct = null;
	int oldDay, oldMonth, oldYear = 0;
	public PersonalInformationReadingActivity(Activity iAct, View iView)
	
	{
		super(iAct);
		this.iAct = iAct;
		try
		{
			this.mID = ProfileTDG.getCurrentId(iAct.getBaseContext());
			if(this.mID > 0)
				{
				this.exists = true;
				fillExistingView(iView);
				}
		} catch (PersistenceException e)
		{
			e.printStackTrace();
		}
	}
	
	public PersonalInformationReadingActivity(Activity iAct, Long iID)
	{
		super(iAct);
		this.mID =  iID;
		this.exists = true;
	}

	public View fillExistingView(View iView)
	{
		Context context = iView.getContext();
		ProfileReading currentProfile;
		try
		{
			currentProfile = ProfileMapper.getOne(this.mID, context);
			
			TextView first, last, phone, address, email, medicare, insurance, contact1, contact2;
			first = (TextView) iView.findViewById(R.id.first_name);
			last = (TextView) iView.findViewById(R.id.last_name);
			phone = (TextView) iView.findViewById(R.id.home_cell_phone_number);
			address = (TextView) iView.findViewById(R.id.address);
			email = (TextView) iView.findViewById(R.id.email_address);
			medicare = (TextView) iView.findViewById(R.id.medicare_information);
			insurance = (TextView) iView.findViewById(R.id.insurance_information);
			contact1 = (TextView) iView.findViewById(R.id.emergency_contact_1);
			contact2 = (TextView) iView.findViewById(R.id.emergency_contact_2);
			
			first.setText(currentProfile.getFirstName());
			last.setText(currentProfile.getLastName());
			
			if( currentProfile.getPhoneNumber() > 0 )
			{
				phone.setText(String.valueOf(currentProfile.getPhoneNumber()));
			}
			address.setText(currentProfile.getAddress());
			email.setText(currentProfile.getEmail());
			medicare.setText(currentProfile.getMedicare());
			insurance.setText(currentProfile.getInsurance());
			contact1.setText(currentProfile.getEmergencyContact1());
			contact2.setText(currentProfile.getEmergencyContact2());
			
		
			Calendar cal = Calendar.getInstance();
			cal.setTime(currentProfile.getDOB());
			oldYear = cal.get(Calendar.YEAR);
			oldMonth = cal.get(Calendar.MONTH); 
			oldDay = cal.get(Calendar.DAY_OF_MONTH);
//			d.init(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH), null);
	    
		} catch (MapperException e)
		{
			Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
		}
		return iView;
	}
	
  public void submit(View iView)
  {
	  	/**Get the view's current context for proper detection of activity launch*/
	    Context context = iView.getContext();

			/**References to text box values*/
	    TextView first, last, phone, address, email, medicare, insurance, contact1, contact2, note,room,bed;
	    first = (TextView) mAct.findViewById(R.id.first_name);
			last = (TextView) mAct.findViewById(R.id.last_name);
			phone = (TextView) mAct.findViewById(R.id.home_cell_phone_number);
			address = (TextView) mAct.findViewById(R.id.address);
			email = (TextView) mAct.findViewById(R.id.email_address);
			medicare = (TextView) mAct.findViewById(R.id.medicare_information);
			insurance = (TextView) mAct.findViewById(R.id.insurance_information);
			contact1 = (TextView) mAct.findViewById(R.id.emergency_contact_1);
			contact2 = (TextView) mAct.findViewById(R.id.emergency_contact_2);
			note = (TextView) mAct.findViewById(R.id.Notes);
			room = (TextView) mAct.findViewById(R.id.Room_Number);
			bed = (TextView) mAct.findViewById(R.id.Bed_Number);
			/**References to date*/

			String tfirst, tlast, taddress, temail, tmedicare, tinsurance, tcontact1, tcontact2 = "",tnote,troom,tbed;
			long tphone =(long) 0;
			tfirst = first.getText().toString();
			tlast = last.getText().toString();
			if( !phone.getText().toString().equalsIgnoreCase("") )
			{
				tphone = Long.valueOf(phone.getText().toString());
			}
			taddress = address.getText().toString();
			temail = email.getText().toString();
			tmedicare = medicare.getText().toString();
			tinsurance = insurance.getText().toString();
			tcontact1= contact1.getText().toString();
			tcontact2 = contact2.getText().toString();
			tnote = note.getText().toString();
			troom = room.getText().toString();
			tbed = bed.getText().toString();
				try{
				if(exists)
				{
					ProfileMapper.update(new ProfileReading(this.mID,tlast, tfirst, tDate, tphone, taddress,
							temail, tmedicare, tinsurance, tcontact1, tcontact2,tnote,troom,tbed), context);
				}
				else
				{
					
					ProfileReading p = new ProfileReading( this.mID,tlast, tfirst, tDate, tphone, taddress,
							temail, tmedicare, tinsurance, tcontact1, tcontact2,tnote,troom,tbed);
					ProfileMapper.insert(p, context);
				}
				/**Fires a LogEvent to the LogItemEditor*/
				LogEventEmitter.fireLogEvent(this, iView.getContext(), LogEventType.PROFILE_CREATE);
				
				/**Calls a short text box confirming data save. Should not occur if an exception should be caught inside mapper*/
		    Toast.makeText(context, context.getString(R.string.confirm_save), Toast.LENGTH_SHORT).show();
				}
				catch (MapperException e)
				{
					Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
				}
				
				if(PreparationActivity.firstStart)
				{
					Intent settingsIntent  = new Intent(iAct,DisclaimerActivity.class);
					iAct.startActivity(settingsIntent);
				}
				cancel(iView);
  }
  
  @Override
  public void cancel(View iView)
  {
    PersonalInformationActivity wParent = (PersonalInformationActivity) mAct;
    wParent.HandlePersonalProfileButton(iView);
  }

  @Override
  public void dateButtonPressed(View iView)
  {
  	super.dateButtonPressed(iView);
		DatePickerDialog.OnDateSetListener dateCallback = new DatePickerDialog.OnDateSetListener()
		{
			
			@Override
			public void onDateSet(DatePicker view, int year, int monthOfYear,
					int dayOfMonth)
			{
				Button dateButton = (Button) iAct.findViewById(R.id.dateButtonDOB);
				
				Calendar cal = Calendar.getInstance();
				cal.set(year, monthOfYear, dayOfMonth, 0, 0, 0);
				dateButton
				.setText(((dayOfMonth) < 10 ? ADDITIONAL_ZERO + (dayOfMonth) : (dayOfMonth)) + "."
						+ ((monthOfYear + 1) < 10 ? ADDITIONAL_ZERO + (monthOfYear + 1) : (monthOfYear + 1)) + "."
						+ year);
				
				DatePicker d = (DatePicker) view;
		    cal.set(d.getYear(), d.getMonth(), d.getDayOfMonth());

		    tDate = cal.getTime();
				
			}
		};
		
		
		if(oldYear != 0 && oldMonth != 0 && oldDay != 0)
		{
			DatePickerDialog datePickerDialog = new DatePickerDialog(
					iView.getContext(), AlertDialog.THEME_TRADITIONAL, dateCallback, oldYear,
					oldMonth, oldDay);
			datePickerDialog.setCancelable(true);
			datePickerDialog.show();
		}
		else
		{
			Calendar cal = Calendar.getInstance();
			DatePickerDialog datePickerDialog = new DatePickerDialog(
					iView.getContext(), AlertDialog.THEME_TRADITIONAL, dateCallback, cal.get(Calendar.YEAR),
					cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
			datePickerDialog.setCancelable(true);
			datePickerDialog.show();
		}
  }
}

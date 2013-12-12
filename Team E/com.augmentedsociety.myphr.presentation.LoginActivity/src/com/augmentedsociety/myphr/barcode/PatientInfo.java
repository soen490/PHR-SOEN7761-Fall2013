package com.augmentedsociety.myphr.barcode;

//import java.util.Calendar;

import java.util.ArrayList;
import java.util.Date;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.augmentedsociety.myphr.R;
import com.augmentedsociety.myphr.domain.MapperException;
import com.augmentedsociety.myphr.domain.ProfileMapper;
import com.augmentedsociety.myphr.domain.ProfileReading;
import com.augmentedsociety.myphr.domain.logs.LogEventType;
import com.augmentedsociety.myphr.presentation.DisclaimerActivity;
import com.augmentedsociety.myphr.presentation.PreparationActivity;
import com.augmentedsociety.myphr.presentation.logs.LogEventEmitter;
 
public class PatientInfo extends Activity
{
	SearchByRoom SR;
	ScanActivity SA;
	String code;
	Context icontext;
		public static long mID;
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			setContentView(R.layout.patient_file);
			icontext=this;
			if(SR.isItByRoom)
			{	
				assignByRoom(this);
			}
			
			else{
				assign(this);
			}
		
			
		}
		public void submitNote( View iView)
		{
	try
	{		 Context context = iView.getContext();

				ProfileReading iR= ProfileMapper.getOne(mID, icontext);

				EditText note;

				note = (EditText) this.findViewById(R.id.Notes);
				
				String tfirst, tlast, taddress, temail, tmedicare, tinsurance, tcontact1, tcontact2 = "",tnote,troom,tbed;
		
				long tphone = Long.valueOf(iR.getPhoneNumber());
				tfirst = iR.getFirstName().toString();
				tlast = iR.getLastName().toString();
				Date tDate = iR.getDOB();
				taddress = iR.getAddress().toString();
				temail = iR.getEmail().toString();
				tmedicare = iR.getMedicare().toString();
				tinsurance = iR.getInsurance().toString();
				tcontact1= iR.getEmergencyContact1().toString();
				tcontact2 = iR.getEmergencyContact2().toString();
				tnote = note.getText().toString();
				troom = iR.getRoom().toString();
				tbed = iR.getBed().toString();
				
						ProfileMapper.update(new ProfileReading(this.mID,tlast, tfirst, tDate, tphone, taddress,
								temail, tmedicare, tinsurance, tcontact1, tcontact2,tnote,troom,tbed), context);
					
					/**Fires a LogEvent to the LogItemEditor*/
					LogEventEmitter.fireLogEvent(this, iView.getContext(), LogEventType.PROFILE_CREATE);
					
					/**Calls a short text box confirming data save. Should not occur if an exception should be caught inside mapper*/
			    Toast.makeText(context, context.getString(R.string.confirm_save), Toast.LENGTH_SHORT).show();
			
				} 
	catch (MapperException e1)
	{
		// TODO Auto-generated catch block
		e1.printStackTrace();
	}	
					
		}
		public void assign(Context icontext)
		{
		  try
			{
		  	String imedicare=SA.Ireader.toString();
		  	//mID= (long) 1;
			//  Context context= getApplicationContext() ;
		  	
			  ProfileReading currentProfile = ProfileMapper.getOne_BY_Medicare(imedicare, icontext);
			  mID=  currentProfile.getID();
				EditText first, last, medicare, note,room,bed;
				first = (EditText) this.findViewById(R.id.Patient_name);
				last = (EditText) this.findViewById(R.id.Patient_Last);
				note = (EditText) this.findViewById(R.id.Notes);
				medicare = (EditText) this.findViewById(R.id.fileNumber);
				room = (EditText) this.findViewById(R.id.Room_Number);
				bed = (EditText) this.findViewById(R.id.Bed_Number);
				
				first.setText(currentProfile.getFirstName());
				last.setText(currentProfile.getLastName());
				note.setText(currentProfile.getNote());
				medicare.setText(currentProfile.getMedicare());
				room.setText(currentProfile.getRoom());
				bed.setText(currentProfile.getBed());
		  
		  	/*
		  	mID=(long) SA.id;
		  	//mID= (long) 2;
			//  Context context= getApplicationContext() ;
			  ProfileReading currentProfile = ProfileMapper.getOne(mID, icontext);
				
				EditText first, last, medicare, note,room,bed;
				first = (EditText) this.findViewById(R.id.Patient_name);
				last = (EditText) this.findViewById(R.id.Patient_Last);
				note = (EditText) this.findViewById(R.id.Notes);
				medicare = (EditText) this.findViewById(R.id.fileNumber);
				room = (EditText) this.findViewById(R.id.Room_Number);
				bed = (EditText) this.findViewById(R.id.Bed_Number);
				
				first.setText(currentProfile.getFirstName());
				last.setText(currentProfile.getLastName());
				note.setText(currentProfile.getNote());
				medicare.setText(currentProfile.getMedicare());
				room.setText(currentProfile.getRoom());
				bed.setText(currentProfile.getBed());*/
			//	check(currentProfile.getFirstName(),currentProfile.getLastName(),
				//		currentProfile.getMedicare(),currentProfile.getRoom(),currentProfile.getBed());
			} catch (MapperException e)
			{
				Log.i("the problem 1", e.getMessage());
				//Toast.makeText(this, "the problem in", Toast.LENGTH_LONG).show();
				check();
				//Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
			}
		
		}
		public void check()//String first,String last,String medicare,String room,String bed )
		{
			//	if(first =="" & last =="" & medicare=="" & room =="" & bed=="")
				{
					//JOptionPane.showMessageDialog(null, "", "InfoBox: " + "", "");
					/*AlertDialog alt_bld = new AlertDialog.Builder(this).create();     
					alt_bld.setMessage("Wrong Patient's ID");
					alt_bld.setCancelable(false);
					alt_bld.setNegativeButton('A',"OK")//, new OnClickListener() { public void onClick(DialogInterface dialog, int which) { // TODO Auto-generated method stub } });
					alt_bld.show();*/
					AlertDialog.Builder alt_bld = new AlertDialog.Builder(this);
					alt_bld.setMessage("Wrong Patient's ID")
					.setCancelable(false)
					.setPositiveButton("OK", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
					// Action for 'Yes' Button
						openHomePage();
					}
					});
					//.setNegativeButton("No", new DialogInterface.OnClickListener() {
					//public void onClick(DialogInterface dialog, int id) {
//					  Action for 'NO' Button
				//	dialog.cancel();
				//	}
					//}	);
					AlertDialog alert = alt_bld.create();
					// Title for AlertDialog
					alert.setTitle("Warning");
					// Icon for AlertDialog
				//	alert.setIcon(R.drawable.icon);
					alert.show();
					}
			
					
				}
		public void openHomePage()
		{
			Intent intent=new Intent(this,SensorActivity.class);
			startActivity(intent);
		}
		
		public void assignByRoom(Context icontext)
		{
		  try
			{
		  	String iroom=SR.iroom,ibed=SR.ibed;
		  	//mID= (long) 1;
			//  Context context= getApplicationContext() ;
			  ProfileReading currentProfile = ProfileMapper.getOne_BY_ROOM(iroom,ibed, icontext);
			  mID=  currentProfile.getID();
				EditText first, last, medicare, note,room,bed;
				first = (EditText) this.findViewById(R.id.Patient_name);
				last = (EditText) this.findViewById(R.id.Patient_Last);
				note = (EditText) this.findViewById(R.id.Notes);
				medicare = (EditText) this.findViewById(R.id.fileNumber);
				room = (EditText) this.findViewById(R.id.Room_Number);
				bed = (EditText) this.findViewById(R.id.Bed_Number);
				
				first.setText(currentProfile.getFirstName());
				last.setText(currentProfile.getLastName());
				note.setText(currentProfile.getNote());
				medicare.setText(currentProfile.getMedicare());
				room.setText(currentProfile.getRoom());
				bed.setText(currentProfile.getBed());
			//	check(currentProfile.getFirstName(),currentProfile.getLastName(),
				//		currentProfile.getMedicare(),currentProfile.getRoom(),currentProfile.getBed());
				
			} catch (MapperException e)
			{
				Log.i("the problem 1", e.getMessage());
				//Toast.makeText(this, "the problem in", Toast.LENGTH_LONG).show();
				check();
			//	Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
			}
		
		}
		public void onClick(View v) {
			switch (v.getId()) {
			
			case R.id.Next:
				
				Intent intent2=new Intent(this,PreparationActivity.class);
				startActivity(intent2);
				break;
			}
		}
		public void onClick2(View v) {
			Log.d("Scan", "Onlick");
			switch (v.getId()) {
			
			case R.id.SubmitNote:
				submitNote(v);
				break;
			}
		}
}

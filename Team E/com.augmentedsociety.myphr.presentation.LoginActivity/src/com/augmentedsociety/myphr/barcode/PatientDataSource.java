package com.augmentedsociety.myphr.barcode;

import java.util.ArrayList;
import java.util.Date;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.augmentedsociety.myphr.persistence.DbRegistry;
import com.augmentedsociety.myphr.persistence.DefaultPageTDG;
import com.augmentedsociety.myphr.persistence.PersistenceException;
import com.augmentedsociety.myphr.persistence.ProfileTDG;

public class PatientDataSource extends Activity
{
//Context context;
 	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
//context = this;
	assign();
	}
	public  void assign()
	{
		ArrayList<String> reading = new ArrayList<String>(14);

		Long id =  (long) 1;
		String lastName = "Alamri";
		String firstName = "Omar";
	
		Long DOB =(long) 2013;
		Long phoneNumber = (long) 0;
		String address = "";
		String email = "blady@gmail.com";
		String medicare = "1234";
		String insurance = "";
		String emergencyContact1 = "";
		String emergencyContact2 = "";
		String note = "This is a note";
		String room="1";
		String bed="3";
	//	ProfileReading	profile = new ProfileReading(id,lastName, firstName, DOB, phoneNumber, address,
		//		email, medicare, insurance, emergencyContact1, emergencyContact2,note);
	
		reading.add(String.valueOf(id));
		reading.add(lastName.toString());
		reading.add(firstName.toString());
		reading.add(String.valueOf(DOB));
		reading.add(String.valueOf(phoneNumber));
		reading.add(address.toString());
		reading.add(email.toString());
		reading.add(medicare.toString());
		reading.add(insurance.toString());
		reading.add(emergencyContact1.toString());
		reading.add(emergencyContact2.toString());
		reading.add(note.toString());
		reading.add(room.toString());
		reading.add(bed.toString());
		ArrayList<String> reading2 = new ArrayList<String>(14);

		 id =  (long) 2;
		 lastName = "Albeladi";
		 firstName = "Khaled";	
		 address = "";
		 email = "bl@gmail.com";
		 medicare = "5678";
		 insurance = "";
		 emergencyContact1 = "";
		 emergencyContact2 = "";
		 note = "There isn't any note for this patient";
		 room="3";
		  bed="1";
		
		reading2.add(String.valueOf(id));
		reading2.add(lastName.toString());
		reading2.add(firstName.toString());
		reading2.add(String.valueOf(DOB));
		reading2.add(String.valueOf(phoneNumber));
		reading2.add(address.toString());
		reading2.add(email.toString());
		reading2.add(medicare.toString());
		reading2.add(insurance.toString());
		reading2.add(emergencyContact1.toString());
		reading2.add(emergencyContact2.toString());
		reading2.add(note.toString());
		reading2.add(room.toString());
		reading2.add(bed.toString());

		ArrayList<String> reading3 = new ArrayList<String>(14);

		 id =  (long) 3;
		 lastName = "Albeladi";
		 firstName = "Abdulrhman";	
		 address = "";
		 email = "bl@gmail.com";
		 medicare = "123456789";
		 insurance = "";
		 emergencyContact1 = "";
		 emergencyContact2 = "";
		 note = "There isn't any note for this patient";
		 room="1";
		  bed="1";
		
		reading3.add(String.valueOf(id));
		reading3.add(lastName.toString());
		reading3.add(firstName.toString());
		reading3.add(String.valueOf(DOB));
		reading3.add(String.valueOf(phoneNumber));
		reading3.add(address.toString());
		reading3.add(email.toString());
		reading3.add(medicare.toString());
		reading3.add(insurance.toString());
		reading3.add(emergencyContact1.toString());
		reading3.add(emergencyContact2.toString());
		reading3.add(note.toString());
		reading3.add(room.toString());
		reading3.add(bed.toString());
		
		ArrayList<String> reading4 = new ArrayList<String>(14);

		 id =  (long) 4;
		 lastName = "Albeladi";
		 firstName = "Abdulrhman";	
		 address = "";
		 email = "bl@gmail.com";
		 medicare = "987654321";
		 insurance = "";
		 emergencyContact1 = "";
		 emergencyContact2 = "";
		 note = "There isn't any note for this patient";
		 room="2";
		  bed="1";
		
		reading4.add(String.valueOf(id));
		reading4.add(lastName.toString());
		reading4.add(firstName.toString());
		reading4.add(String.valueOf(DOB));
		reading4.add(String.valueOf(phoneNumber));
		reading4.add(address.toString());
		reading4.add(email.toString());
		reading4.add(medicare.toString());
		reading4.add(insurance.toString());
		reading4.add(emergencyContact1.toString());
		reading4.add(emergencyContact2.toString());
		reading4.add(note.toString());
		reading4.add(room.toString());
		reading4.add(bed.toString());
		
		PatientInfo.mID=1;
		try
		{	
			ArrayList<String> resultTable = DefaultPageTDG.select(this);
			// detect first start value
 			if(resultTable.get(0).equals("-1"))
			{
				ProfileTDG.insert(reading, this);
				ProfileTDG.insert(reading2, this);
				ProfileTDG.insert(reading3, this);
				ProfileTDG.insert(reading4, this);
		}
		}catch (PersistenceException e)
		{
			Log.i("the problem 4", e.getMessage());
		
			//e.printStackTrace();
		} 
	
		Intent intent=new Intent(this,SensorActivity.class);
		startActivity(intent);
		finish();
	}
}

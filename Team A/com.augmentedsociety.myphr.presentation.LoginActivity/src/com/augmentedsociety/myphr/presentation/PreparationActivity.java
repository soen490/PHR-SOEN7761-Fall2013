package com.augmentedsociety.myphr.presentation;

import java.util.ArrayList;

import com.augmentedsociety.myphr.domain.DefaultPageMapper;
import com.augmentedsociety.myphr.domain.MapperException;
import com.augmentedsociety.myphr.persistence.DefaultPageTDG;
import com.augmentedsociety.myphr.persistence.PersistenceException;
import com.augmentedsociety.myphr.persistence.ProfileTDG;
import com.augmentedsociety.myphr.presentation.vitalsigns.Measurement;
import com.augmentedsociety.myphr.presentation.vitalsigns.VitalSignsActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.provider.SyncStateContract.Helpers;

public class PreparationActivity extends Activity
{

	MenuActivity mAct;
	final String NOT_DEFINED = "-1";
	final String ALL_OPTIONS_ACTIVE = "31";
	
	public static boolean firstStart = false;
	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		int currentapiVersion = android.os.Build.VERSION.SDK_INT;
		if (currentapiVersion >= android.os.Build.VERSION_CODES.HONEYCOMB)
		{
			getActionBar().setDisplayShowHomeEnabled(false);
		}
		
		try
		{
			ArrayList<String> resultTable = DefaultPageTDG.select(getApplicationContext());
			// detect first start value
			if(resultTable.get(0).equals(NOT_DEFINED))
			{
				// if first start then activate all options = 31 = 1 + 2 + 4 + 8 + 16 
				try
				{
					DefaultPageMapper.update(String.valueOf(ALL_OPTIONS_ACTIVE), getApplicationContext());
				} catch (MapperException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				firstStart = true;
			}
				
		} catch (PersistenceException e1)
		{
			e1.printStackTrace();
		}
		
		if(firstStart)
		{
			Intent loginIntent  = new Intent(this, DisclaimerActivity.class);
			startActivity(loginIntent);
			// activate tutorial on first start
			
//			firstStart = false;
		}
		else
		{
			Intent loginIntent  = new Intent(this, VitalSignsActivity.class);
			startActivity(loginIntent);
		}
		
		finish();
	}
}

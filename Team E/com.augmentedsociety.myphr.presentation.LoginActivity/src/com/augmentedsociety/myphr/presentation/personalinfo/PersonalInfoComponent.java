package com.augmentedsociety.myphr.presentation.personalinfo;

import android.app.Activity;
import android.view.View;

/**
 * Base Personal Information class
 * 
 * @author Rajan Jayakumar
 *
 */

public class PersonalInfoComponent
{  
	protected Activity mAct;
	protected Long mID;
	protected boolean exists = false;
	public PersonalInfoComponent(Activity mAct){
		this.mAct = mAct;
	}
	public PersonalInfoComponent(Activity mAct, Long mID){
		this.mAct = mAct;
		this.mID = mID;
	}
	
	public void setDateTime()
	{
	}
	
	public void submit(View iView){
	}
	
	public void cancel(View iView)
	{}

	public void finish(View iView)
	{
	}
	
	public void dateButtonPressed(View iView)
	{
	}
	
	public void startDateButtonPressed(View iView)
	{
	}
	public void endDateButtonPressed(View iView)
	{
	}
	public void startTimeButtonPressed(View iView)
	{
	}
	public void endTimeButtonPressed(View iView)
	{
	}
	public void dateButtonPressedImm(View iView)
	{
	}
	public void timeButtonPressedImm(View iView)
	{
	}
	
}

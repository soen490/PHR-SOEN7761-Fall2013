package com.augmentedsociety.myphr.domain.commands;

import android.content.Context;
import java.util.Date;

/**
 * VitalSignObject class. Class instance (object) to be passed as the execute 
 * command/action can apply to different types or number of parameters
 * passed.
 * 
 * @author Serge-Antoine Naim
 * 
 */

public class VitalSignObject 
{
	private Context mContext;
	private CommandAction mCommandAction;
	private int mSystolic;
	private int mDiastolic;
	private int mHeartRate;
	private float mBloodSugarOximeterWeightTemp;
	private Date mDate;
	
	/**
   * Two overloaded constructors, general parameters here:
   * 
   * @param mContext 
   *   The context passed from the view/activity where action was  launched from 
   *   user
   * 
   * @param mCommandAction 
   *   The instance of CommandAction which is then used to call the proper vital 
   *   sign reading method for insertion via mappers
   * 
   * @param mSystolic 
   *   The integer variable representing Systolic reading of Blood Pressure 
   *   vital sign
   * 
   * @param mDistolic 
   *   The integer variable representing Diastolic reading of Blood Pressure 
   *   vital sign
   * 
   * @param mHeartRate 
   *   The integer variable representing Heart Rate reading of Blood Pressure 
   *   vital sign
   * 
   * @param mBloodSugarOximeterWeightTemp 
   *   The float present in second version of constructor, which takes in any 
   *   other reading representing a float. It is in another constructor since 
   *   all readings except for Blood Pressure only take in one parameter instead 
   *   of three distinct ones.
   */
	
	public VitalSignObject(CommandAction iCommandAction, int iSystolic, 
			                   int iDiastolic, int iHeartRate, Date iDate, Context iContext)
	{
		this.mCommandAction = iCommandAction;
		this.mSystolic      = iSystolic;
		this.mDiastolic     = iDiastolic;
		this.mHeartRate     = iHeartRate;
		this.mContext       = iContext;
		this.mDate					=	iDate;
	}
	
	public VitalSignObject(CommandAction iCommandAction, 
			                   float iBloodSugarOximeterWeightTemp, Date iDate, Context iContext)
	{
		this.mCommandAction                = iCommandAction;
		this.mBloodSugarOximeterWeightTemp = iBloodSugarOximeterWeightTemp;
		this.mContext                      = iContext;
		this.mDate												 = iDate;
	}
	
	public Context getMContext()
	{
		return mContext;
	}
	public CommandAction getMCommandAction()
	{
		return mCommandAction;
	}
	
	public int getMSystolic()
	{
		return mSystolic;
	}
	
	public int getMDiastolic()
	{
		return mDiastolic;
	}
	
	public int getMHeartRate()
	{
		return mHeartRate;
	}
	
	public float getMBloodSugarOximeterWeightTemp()
	{
		return mBloodSugarOximeterWeightTemp;
	}
	
	public Date getDate()
	{
		return mDate;
	}
}

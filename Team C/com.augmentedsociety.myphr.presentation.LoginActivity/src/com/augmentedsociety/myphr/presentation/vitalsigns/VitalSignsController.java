package com.augmentedsociety.myphr.presentation.vitalsigns;

import android.app.Activity;
import android.content.Context;
import java.util.Date;

import com.augmentedsociety.myphr.domain.ReadingSource;
import com.augmentedsociety.myphr.domain.commands.BloodPressureCommand;
import com.augmentedsociety.myphr.domain.commands.BloodSugarCommand;
import com.augmentedsociety.myphr.domain.commands.CommandAction;
import com.augmentedsociety.myphr.domain.commands.OxygenSaturationCommand;
import com.augmentedsociety.myphr.domain.commands.ReadingInvoker;
import com.augmentedsociety.myphr.domain.commands.TemperatureCommand;
import com.augmentedsociety.myphr.domain.commands.VitalSignObject;
import com.augmentedsociety.myphr.domain.commands.WeightCommand;

/**
 * Vital Signs Controller class, version 1.0
 * 
 * @author Serge-Antoine Naim
 * 
 * The class only takes in passed attributes from the Activity views (method 
 * selected depending on number of attributes passed).
 * 
 * The hashMap will determine, according to a key string defined with the name 
 * of the Activity inside the latter itself, which mapper operations shall take 
 * care of the passed attributes (Blood sugar, Blood pressure, etc)
 */

public class VitalSignsController extends Activity
{ 
  
  private CommandAction mCommandAction = new CommandAction(); 
  
  private ReadingInvoker mReadInvoke = new ReadingInvoker();
  
  private VitalSignObject mVitalSignObject;
  
  /** 
   * Default constructor: Instantiation of the hashMap every time controller initialised in an Activity.
   * @param mCommandAction CommandAction instance variable used to call proper vital sign reading methods
   * @param mReadInvoke ReadingInvoker instance variable for instantiation of hash map for the controller to take care of passing
   * 				the right commands to the right user input from a specific activity trigger.
   * @param mVitalSignObject Vital Sign Object instance variable to store passed data from activity. The object can then pass its own instance
   * 				variables for proper reading input.
   */
  public VitalSignsController()
  {
    mReadInvoke.getMap().put("BloodPressure",    new BloodPressureCommand());
    mReadInvoke.getMap().put("BloodSugar",       new BloodSugarCommand());
    mReadInvoke.getMap().put("OxygenSaturation", new OxygenSaturationCommand());
    mReadInvoke.getMap().put("Weight",           new WeightCommand());
    mReadInvoke.getMap().put("Temperature",      new TemperatureCommand());
  }
  
  public void manipulate(int iPassedValue1, int iPassedValue2, int iPassedValue3, Date iDate, String iVitalSign, ReadingSource iMeasureSource, Context iContext)
  {
  	mVitalSignObject = new VitalSignObject(mCommandAction, iPassedValue1, iPassedValue2, iPassedValue3, iDate, iContext);
  	mReadInvoke.getMap().get(iVitalSign).execute(mVitalSignObject, iMeasureSource);
  }
  
  public void manipulate(float iPassedValue, Date iDate, String iVitalSign, ReadingSource iMeasureSource, Context iContext)
  {
  	mVitalSignObject = new VitalSignObject(mCommandAction, iPassedValue, iDate, iContext);
  	mReadInvoke.getMap().get(iVitalSign).execute(mVitalSignObject, iMeasureSource);
  }
  
}


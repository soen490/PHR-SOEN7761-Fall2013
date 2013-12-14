package com.augmentedsociety.myphr.domain.commands;

import java.util.ArrayList;
import java.util.Date;

import android.content.Context;

import com.augmentedsociety.myphr.domain.BloodPressureMapper;
import com.augmentedsociety.myphr.domain.BloodPressureReading;
import com.augmentedsociety.myphr.domain.BloodSugarMapper;
import com.augmentedsociety.myphr.domain.BloodSugarReading;
import com.augmentedsociety.myphr.domain.MapperException;
import com.augmentedsociety.myphr.domain.OxygenSaturationMapper;
import com.augmentedsociety.myphr.domain.OxygenSaturationReading;
import com.augmentedsociety.myphr.domain.ReadingSource;
import com.augmentedsociety.myphr.domain.TemperatureMapper;
import com.augmentedsociety.myphr.domain.TemperatureReading;
import com.augmentedsociety.myphr.domain.WeightMapper;
import com.augmentedsociety.myphr.domain.WeightReading;

/**
 * CommandAction class
 * 
 * @author Serge-Antoine Naim
 * 
 * This class contains the direct calls to actions/commands needed for vital 
 * sign entries. It is the Receiver class in terms of the command pattern.
 * The context instances come from the view activities; they are needed for 
 * Android applications. Each of these methods represents a vital sign.
 * 
 */

public class CommandAction
{
  public CommandAction()
  {
    
  }
  
  /**
   * Passes parameters from a VitalSignObject going through the 
   * BloodPressureCommand class' execute method. These parameters are then 
   * passed on to a new instance of BloodPressureReading (below) in order to 
   * then be transferred to the BloodPressureMapper for insertion.
   */
  public void readBloodPressure(int iSystolic, int iDiastolic, int iHeartRate, 
  				Date iDate, Context iContext, ReadingSource iMeasureSource)
  {
    try 
    {
      BloodPressureReading r = 
      		new BloodPressureReading(
      				BloodPressureMapper.getNextAvailableId(iContext), 
      				iDate, iMeasureSource, iSystolic, iDiastolic, iHeartRate);
      
      BloodPressureMapper.insert(r, iContext);      
    }
    catch (MapperException e) 
    {
      e.printStackTrace();
    }
  }
  
  public ArrayList<BloodPressureReading> viewBloodPressure( Context iContext )
  {
	  ArrayList<BloodPressureReading> readings; 
	  readings = new ArrayList<BloodPressureReading>();
	  
	  try
	  {
		  readings = BloodPressureMapper.findAll(iContext);
	  }
	  catch( MapperException e )
	  {
		  e.printStackTrace();
	  }
	  return readings;
  }
  
  public ArrayList<BloodPressureReading> viewBloodPressure(Date iLeftBound, 
  		                                                     Date iRightBound, 
  		                                                     Context iContext )
  {
	  ArrayList<BloodPressureReading> readings = new ArrayList<BloodPressureReading>();
	  try
	  {
		  readings = 
		  		BloodPressureMapper.findAllWithinRangeInclusive(iLeftBound, 
		  				                                            iRightBound, iContext);
	  }
	  catch( MapperException e )
	  {
		  e.printStackTrace();
	  }
	  return readings;
  }
  
  /**
   * Passes parameters from a VitalSignObject going through the 
   * BloodSugarCommand class' execute method. These parameters are then passed 
   * on to a new instance of BloodSugarReading (below) in order to then be 
   * transferred to the BloodSugarMapper for insertion.
   */
  public void readBloodSugar(float iBloodGlucose, Date iDate, Context iContext, 
  		                       ReadingSource iMeasureSource)
  {
    try 
    {
      BloodSugarReading r = new BloodSugarReading(
      		                  BloodSugarMapper.getNextAvailableId(iContext), 
      		                  iDate, iMeasureSource, iBloodGlucose);
      
      BloodSugarMapper.insert(r, iContext);      
    }
    catch (MapperException e) 
    {
      e.printStackTrace();
    }
  }
  
  public ArrayList<BloodSugarReading> viewBloodSugar( Context iContext )
  {
	  ArrayList<BloodSugarReading> readings = new ArrayList<BloodSugarReading>();
	  try
	  {
		  readings = BloodSugarMapper.findAll(iContext);
	  }
	  catch( MapperException e )
	  {
		  e.printStackTrace();
	  }
	  return readings;
  }
  
  public ArrayList<BloodSugarReading> viewBloodSugar(Date iLeftBound, 
  		                                               Date iRightBound, 
  		                                               Context iContext)
  {
	  ArrayList<BloodSugarReading> readings = new ArrayList<BloodSugarReading>();
	  try
	  {
		  readings = 
		  		BloodSugarMapper.findAllWithinRangeInclusive(iLeftBound, 
		  		                                             iRightBound, iContext);
	  }
	  catch( MapperException e )
	  {
		  e.printStackTrace();
	  }
	  return readings;
  }
  
  /**
   * Passes parameters from a VitalSignObject going through the 
   * OxygenSaturationCommand class' execute method. These parameters are then 
   * passed on to a new instance of OxygenSaturationReading (below) in order 
   * to then be transferred to the OxygenSaturationMapper for insertion.
   */
  public void readOxygenSaturation(float iOxygenSaturation, Date iDate, Context iContext, 
  		                             ReadingSource iMeasureSource)
  {
    try 
    {
      OxygenSaturationReading r = 
      		new OxygenSaturationReading(
      				OxygenSaturationMapper.getNextAvailableId(iContext), 
      				iDate, iMeasureSource, iOxygenSaturation);
      
      OxygenSaturationMapper.insert(r, iContext);      
    }
    catch (MapperException e) 
    {
      e.printStackTrace();
    }
  }
  
  public ArrayList<OxygenSaturationReading> viewOxygenSaturation( Context iContext )
  {
	  ArrayList<OxygenSaturationReading> readings = new ArrayList<OxygenSaturationReading>();
	  try
	  {
		  readings = OxygenSaturationMapper.findAll(iContext);
	  }
	  catch( MapperException e )
	  {
		  e.printStackTrace();
	  }
	  return readings;
  }
  
  public ArrayList<OxygenSaturationReading> viewOxygenSaturation(
  		                      Date iLeftBound, Date iRightBound,Context iContext )
  {
	  ArrayList<OxygenSaturationReading> readings = 
	  		new ArrayList<OxygenSaturationReading>();
	  
	  try
	  {
		  readings = 
		  		OxygenSaturationMapper.findAllWithinRangeInclusive(iLeftBound, 
		  		                                                   iRightBound, 
		  		                                                   iContext);
	  }
	  catch( MapperException e )
	  {
		  e.printStackTrace();
	  }
	  return readings;
  }
  
  /** 
   * Passes parameters from a VitalSignObject going through the WeightCommand 
   * class' execute method. These parameters are then passed on to a new
   * instance of WeightReading (below) in order to then be transferred to 
   * the WeightMapper for insertion.
   */
  public void readWeight(float iWeight, Date iDate, Context iContext, ReadingSource iMeasureSource)
  {
    try 
    {
      WeightReading r = new WeightReading(
      		WeightMapper.getNextAvailableId(iContext), 
      		iDate, iMeasureSource, iWeight);
      
      WeightMapper.insert(r, iContext);      
    }
    catch (MapperException e) 
    {
      e.printStackTrace();
    }
  }
  public ArrayList<WeightReading> viewWeight( Context iContext )
  {
	  ArrayList<WeightReading> readings = new ArrayList<WeightReading>();
	  try
	  {
		  readings = WeightMapper.findAll(iContext);
	  }
	  catch( MapperException e )
	  {
		  e.printStackTrace();
	  }
	  return readings;
  }
  public ArrayList<WeightReading> viewWeight(Date iLeftBound, 
  		                                       Date iRightBound, Context iContext)
  {
	  ArrayList<WeightReading> readings = new ArrayList<WeightReading>();
	  try
	  {
		  readings = WeightMapper
		  		.findAllWithinRangeInclusive(iLeftBound, iRightBound, iContext);
	  }
	  catch( MapperException e )
	  {
		  e.printStackTrace();
	  }
	  return readings;
  }
  
  /**
   * Passes parameters from a VitalSignObject going through the 
   * TemperatureCommand class' execute method. These parameters are then passed 
   * on to a new instance of TemperatureReading (below) in order to then be 
   * transferred to the TemperatureMapper for insertion.
   */
  public void readTemperature(float iTemperature, Date iDate, Context iContext, 
  		                        ReadingSource iMeasureSource)
  {
	    try 
	    {
	      TemperatureReading r = 
	      		new TemperatureReading(
	      				TemperatureMapper.getNextAvailableId(iContext), 
	      				iDate, iMeasureSource, iTemperature);
	      
	      TemperatureMapper.insert(r, iContext);      
	    }
	    catch (MapperException e) 
	    {
	      e.printStackTrace();
	    }
  }
  
  public ArrayList<TemperatureReading> viewTemperature( Context iContext )
  {
	  ArrayList<TemperatureReading> readings = 
	  		new ArrayList<TemperatureReading>();
	  
	  try
	  {
		  readings = TemperatureMapper.findAll(iContext);
	  }
	  catch( MapperException e )
	  {
		  e.printStackTrace();
	  }
	  
	  return readings;
  }
  
  public ArrayList<TemperatureReading> viewTemperature(Date iLeftBound, 
  		                                                 Date iRightBound, 
  		                                                 Context iContext )
  {
	  ArrayList<TemperatureReading> readings = new ArrayList<TemperatureReading>();
	  try
	  {
		  readings = 
		  		TemperatureMapper.findAllWithinRangeInclusive(iLeftBound, 
		  				                                          iRightBound, iContext);
	  }
	  catch( MapperException e )
	  {
		  e.printStackTrace();
	  }
	  return readings;
  }
}

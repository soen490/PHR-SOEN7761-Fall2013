package com.augmentedsociety.myphr.domain;

import java.util.Date;

/**
 * Specifies a single reading of a blood sugar.
 * 
 * @author Serge-Antoine Naim
 * 
 */

public class BloodSugarReading extends AbstractReading
{
  /**Blood glucose (mg/dL)*/
  private float mBloodGlucose;
  
  /**
   * Constructor for the new Blood Sugar reading
   * 
   * @param id The unique id of the reading for mapping to the database
   * @param dateTaken The date and time when the measurement was taken
   * @param measureSource The way the measuremnt was taken
   * @param bloodGlucose The blood sugar in international units, milligrams per decilitre (mg/dL)
   */
  public BloodSugarReading(long iID, Date iDateTaken, ReadingSource iMeasureSource, float iBloodGlucose)
  {
    super(iID, iDateTaken, iMeasureSource);
    this.mBloodGlucose = iBloodGlucose;
  }
  
  /**
   * Returns the blood sugar in mg/dL.
   * 
   * @return The blood sugar in mg/dL
   */
  public float getBloodGlucose()
  {
    return mBloodGlucose;
  }
  
  /**
   * Sets the blood sugar in mg/dL.
   * 
   * @param bloodGlucose The new blood sugar reading in international units, milligrams per decilitre (mg/dL)
   */
  public void setBloodGlucose(float iBloodGlucose)
  {
    this.mBloodGlucose = iBloodGlucose;
  }
}

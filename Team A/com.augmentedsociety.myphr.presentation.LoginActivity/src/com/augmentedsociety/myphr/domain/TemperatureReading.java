package com.augmentedsociety.myphr.domain;

import java.util.Date;

/**
 * Specifies a single reading of temperature measure.
 * 
 * @author Serge-Antoine Naim
 * 
 */

public class TemperatureReading extends AbstractReading
{
  /**temperature (F)*/
  private float mTemperature;
  
  /**
   * Constructor for the new temperature reading
   * 
   * @param id The unique id of the reading for mapping to the database
   * @param dateTaken The date and time when the measurement was taken
   * @param measureSource The way the measuremnt was taken
   * @param temperature The temperature in Fahrenheit (F)
   */
  public TemperatureReading(long iID, Date iDateTaken, ReadingSource iMeasureSource, float iTemperature)
  {
    super(iID, iDateTaken, iMeasureSource);
    this.mTemperature = iTemperature;
  }
  
  /**
   * Returns the temperature in F.
   * 
   * @return The temperature in F
   */
  public float getTemperature()
  {
    return mTemperature;
  }
  
  /**
   * Sets the temperature in %.
   * 
   * @param temperature The new temperature reading in Fahrenheit (F)
   */
  public void setTemperature(float iTemperature)
  {
    this.mTemperature = iTemperature;
  }
}

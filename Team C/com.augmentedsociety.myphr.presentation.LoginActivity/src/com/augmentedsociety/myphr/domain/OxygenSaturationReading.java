package com.augmentedsociety.myphr.domain;

import java.util.Date;

/**
 * Specifies a single reading of an oxygen saturation level.
 * 
 * @author Serge-Antoine Naim
 * 
 */

public class OxygenSaturationReading extends AbstractReading
{
  /**Oxygen saturation (%)*/
  private float mOxygenSaturation;
  
  /**
   * Constructor for the new oxygen saturation reading
   * 
   * @param id The unique id of the reading for mapping to the database
   * @param dateTaken The date and time when the measurement was taken
   * @param measureSource The way the measuremnt was taken
   * @param oxygenSaturation The blood oxygen saturation in percentage (%)
   */
  public OxygenSaturationReading(long iID, Date iDateTaken, ReadingSource iMeasureSource, float iOxygenSaturation)
  {
    super(iID, iDateTaken, iMeasureSource);
    this.mOxygenSaturation = iOxygenSaturation;
  }
  
  /**
   * Returns the oxygen saturation in %.
   * 
   * @return The oxygen saturation in %
   */
  public float getOxygenSaturation()
  {
    return mOxygenSaturation;
  }
  
  /**
   * Sets the oxygen saturation in %.
   * 
   * @param oxygenSaturation The new blood oxygen saturation reading in percentage (%)
   */
  public void setOxygenSaturation(float iOxygenSaturation)
  {
    this.mOxygenSaturation = iOxygenSaturation;
  }
}

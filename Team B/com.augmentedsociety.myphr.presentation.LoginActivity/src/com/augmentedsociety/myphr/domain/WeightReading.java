package com.augmentedsociety.myphr.domain;

import java.util.Date;

/**
 * Specifies a single reading of weight measure.
 * 
 * @author Serge-Antoine Naim
 * 
 */

public class WeightReading extends AbstractReading
{
  /**Weight (Kg)*/
  private float mWeight;
  
  /**
   * Constructor for the new Weight reading
   * 
   * @param id The unique id of the reading for mapping to the database
   * @param dateTaken The date and time when the measurement was taken
   * @param measureSource The way the measuremnt was taken
   * @param weight The Weight in kilograms (Kg)
   */
  public WeightReading(long iID, Date iDateTaken, ReadingSource iMeasureSource, float iWeight)
  {
    super(iID, iDateTaken, iMeasureSource);
    this.mWeight = iWeight;
  }
  
  /**
   * Returns the Weight in Kg.
   * 
   * @return The Weight in Kg
   */
  public float getWeight()
  {
    return mWeight;
  }
  
  /**
   * Sets the Weight in %.
   * 
   * @param weight The new Weight reading in kilograms (Kg)
   */
  public void setWeight(float iWeight)
  {
    this.mWeight = iWeight;
  }
}

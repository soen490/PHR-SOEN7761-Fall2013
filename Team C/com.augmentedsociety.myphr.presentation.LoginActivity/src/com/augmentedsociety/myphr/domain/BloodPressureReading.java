package com.augmentedsociety.myphr.domain;

import java.util.Date;

/**
 * Specifies a single reading of a blood pressure including the heart rate.
 * 
 * @author Yuri Kitaev
 * 
 */
public class BloodPressureReading extends AbstractReading
{
  // Systolic ("maximum") blood pressure (mmHg).
  private int mSystolic;

  // Diastolic ("minimum") blood pressure (mmHg)
  private int mDiastolic;

  // Heart rate (beats per minute)
  private int mHeartrate;

  /**
   * Constructor for the new Blood Pressure reading
   * 
   * @param iID The unique id of the reading for mapping to the database
   * @param iDateTaken The date and time when the measurement was taken
   * @param iMeasureSource The way the measuremnt was taken
   * @param iSystolic The systolic pressure (mmHg)
   * @param iDiastolic The diastolic pressure (mmHg)
   * @param iHeartrate The heartrate (beats per minute)
   */
  public BloodPressureReading(long iID, Date iDateTaken,
      ReadingSource iMeasureSource, int iSystolic, int iDiastolic, int iHeartrate)
  {
    super(iID, iDateTaken, iMeasureSource);
    this.mSystolic = iSystolic;
    this.mDiastolic = iDiastolic;
    this.mHeartrate = iHeartrate;
  }

  /**
   * Returns the systolic ("maximum") blood pressure in mmHg.
   * 
   * @return The systolic blood pressure in mmHg
   */
  public int getSystolic()
  {
    return mSystolic;
  }

  /**
   * Sets the systolic ("maximum") blood pressure in mmHg.
   * 
   * @param iSystolic The new systolic blood pressure in mmHg
   */
  public void setSystolic(int iSystolic)
  {
    this.mSystolic = iSystolic;
  }

  /**
   * Returns the diastolic ("minimum") blood pressure in mmHg
   * 
   * @return The diastolic ("minimum") blood pressure in mmHg
   */
  public int getDiastolic()
  {
    return mDiastolic;
  }

  /**
   * Sets the diastolic ("minimum") blood pressure in mmHg.
   * 
   * @param iDiastolic The new diastolic blood pressure in mmHg
   */
  public void setDiastolic(int iDiastolic)
  {
    this.mDiastolic = iDiastolic;
  }

  /**
   * Returns the heart rate in beats per minute.
   * 
   * @return The heart rate (bpm)
   */
  public int getHeartrate()
  {
    return mHeartrate;
  }

  /**
   * Sets the heart rate
   * 
   * @param iHeartrate The new heart rate (bpm)
   */
  public void setHeartrate(int iHeartrate)
  {
    this.mHeartrate = iHeartrate;
  }
}

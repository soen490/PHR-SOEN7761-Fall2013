package com.augmentedsociety.myphr.domain;

import java.util.Date;

/**
 * An abstract class representing one wellness reading. Contains attributes
 * common to all readings.
 * 
 * @author Yuri Kitaev
 * 
 */
public abstract class AbstractReading
{
  // The measurement type of reading
	// 0 - BloodPressure
	// 1 - Weight
	// 2 - O2
	// 3 - Temperature
	// 4 - Blood Sugar
  private Long mType;
	
  // The unique mID of the reading
  private Long mID;

  // The date and time when the measurement was taken
  private Date mDateTaken;

  // The way the measurement was taken
  private ReadingSource mMeasurementSource;

  /**
   * The constructor for the abstract reading
   * 
   * @param iID The unique mID of the reading for mapping to the database
   * @param iDateTaken The date and time when the measurement was taken
   * @param iMeasurementSource The way the measurement was taken
   */
  public AbstractReading(Long iID, Date iDateTaken,
      ReadingSource iMeasurementSource)
  {
    super();
    this.mID = iID;
    this.mDateTaken = iDateTaken;
    this.mMeasurementSource = iMeasurementSource;
  }
  
  /**
   * The constructor for the abstract reading
   * 
   * @param iID The unique mID of the reading for mapping to the database
   * @param iDateTaken The date and time when the measurement was taken
   * @param iType to distinguish the different measurements
   */
  public AbstractReading(Long iID, Date iDateTaken, Long iType)
  {
    super();
    this.mDateTaken = iDateTaken;
    this.mID = iID;
    this.mType = iType;
  }
  
  public AbstractReading( Date iDateTaken)
  {
    super();
    this.mDateTaken = iDateTaken;
  }

  /**
   * Returns the date and time when the measure was taken
   * 
   * @return The instant in time when the measure was taken
   */
  public Date getDateTaken()
  {
    return mDateTaken;
  }

  /**
   * Sets the date and time when the measure was taken
   * 
   * @param iDateTaken The instant in time when the measure was taken
   */
  public void setDateTaken(Date iDateTaken)
  {
    this.mDateTaken = iDateTaken;
  }

  /**
   * Gets the way how the measure was taken
   * 
   * @return measurementSource The measure source
   * @note Refer to the MeasureSource enum definitieon for details
   */
  public ReadingSource getMeasurementSource()
  {
    return mMeasurementSource;
  }

  /**
   * Sets the way how the measurement was taken
   * 
   * @param iMeasurementSource The measurement source
   * @note Refer to the ReadingSource enum definitieon for details
   */
  public void setMeasurementSource(ReadingSource iMeasurementSource)
  {
    this.mMeasurementSource = iMeasurementSource;
  }

  /**
   * Gets the unique mID of the reading
   * 
   * @return The unique mID of the reading
   */
  public Long getId()
  {
    return mID;
  }
}

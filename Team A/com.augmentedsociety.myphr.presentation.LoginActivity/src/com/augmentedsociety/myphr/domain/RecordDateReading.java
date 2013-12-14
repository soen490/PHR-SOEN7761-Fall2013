package com.augmentedsociety.myphr.domain;

import java.util.Date;

/**
 * Specifies a single reading of a Date Reading.
 * 
 * @author Johannes Lange
 * 
 */
public class RecordDateReading extends AbstractReading
{

  /**
   * Constructor for the new Date reading
   * 
   * @param iID The unique id of the reading for mapping to the database
   * @param iDateTaken The date and time when the measurement was taken
   * @param iType The type of the measurement
   */
  public RecordDateReading(long iID, Date iDateTaken,
      Long iType)
  {
    super(iID, iDateTaken, iType);
  }
  
  
  /**
   * Constructor for the new Date reading
   * 
   * @param iDateTaken The date and time when the measurement was taken
   */
  public RecordDateReading( Date iDateTaken)
  {
    super(iDateTaken);
  }

}

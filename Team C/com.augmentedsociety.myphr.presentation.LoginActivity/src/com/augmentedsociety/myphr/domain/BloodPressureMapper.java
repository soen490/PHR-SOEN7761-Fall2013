package com.augmentedsociety.myphr.domain;

import java.util.ArrayList;
import java.util.Date;

import android.content.Context;

import com.augmentedsociety.myphr.persistence.BloodPressureTDG;
import com.augmentedsociety.myphr.persistence.PersistenceException;

/**
 * Blood Pressure Reading data mapper
 * 
 * @author Yuri Kitaev
 * 
 */
public class BloodPressureMapper extends ReadingDataMapper
{

  /**
   * Hidden constructor
   */
  private BloodPressureMapper()
  {

  }

  /**
   * Returns all Blood Pressure Reading objects that are in the database.
   * 
   * @param iContext 
   *   The application context
   * 
   * @note 
   *   Recreates only the new objects, since Identity Map is no longer applied: 
   *   the reason for this is because whenever data is fetched, we know its 
   *   usage right away and will not need to keep it in proximity for later 
   *   usage, but rather direct usage
   * 
   * @return 
   *   The ArrayList of all BloodPressureReadings in the database
   * 
   * @throws MapperException 
   *   Thrown when there is either a problem with mapping or if the 
   *   Persistence layer returns an error
   */
  public static ArrayList<BloodPressureReading> findAll(Context iContext) throws MapperException
  {
    try
    {
      ArrayList<ArrayList<String>> resultTable = BloodPressureTDG.selectAll(iContext);
      ArrayList<BloodPressureReading> resultReadings = 
      		new ArrayList<BloodPressureReading>(resultTable.size());
      
      for (int i = 0; i < resultTable.size(); ++i)
      {
        ArrayList<String> values = resultTable.get(i);
        BloodPressureReading r;
        Long id = Long.valueOf(values.get(0));
        Date date = new Date(Long.valueOf(values.get(1))); // Converts from Unix
                                                           // time to Java Date
        int sourceInt = Integer.valueOf(values.get(2));
        ReadingSource source = sourceFromInt(sourceInt);
        int systolic = Integer.valueOf(values.get(3));
        int diastolic = Integer.valueOf(values.get(4));
        int heartrate = Integer.valueOf(values.get(5));

        r = new BloodPressureReading(id, date, source, systolic, diastolic, heartrate);
        resultReadings.add(r);
      }
      return resultReadings;
    } 
    catch (PersistenceException e)
    {
      throw new MapperException(
          "The Mapper failed to obtain the Blood Pressure readings from the persistence layer. The TDG returned the following error: "
              + e.getMessage());
    } 
    catch (Exception e)
    {
      throw new MapperException(
          "The mapper failed to complete an operation for the following unforeseen reason: "
              + e.getMessage());
    }
  }

  /**
   * Returns all Blood Pressure Reading objects that fall within the time range
   * specified (inclusive)
   * 
   * @param iLeftBound 
   *   The Date object specifying the earliest date boundary
   * 
   * @param iRightBound 
   *   The Date object specifying the latest date boundary
   * 
   * @param iContext 
   *   The application context
   * 
   * @note 
   *   Recreates only the new objects, since Identity Map is no longer applied: 
   *   the reason for this is because whenever data is fetched, we know its 
   *   usage right away and will not need to keep it in proximity for later 
   *   usage, but rather direct usage
   * 
   * @return 
   *   The ArrayList of all BloodPressureReadings in the database that fall 
   *   within range
   * 
   * @throws MapperException 
   *   Thrown when there is either a problem with mapping or if the Persistence 
   *   layer returns an error
   */
  public static ArrayList<BloodPressureReading> findAllWithinRangeInclusive(
      Date iLeftBound, Date iRightBound, Context iContext) throws MapperException
  {
    try
    {
      ArrayList<ArrayList<String>> resultTable = BloodPressureTDG
          .selectBetweenDatesInclusive(String.valueOf(iLeftBound.getTime()),
              String.valueOf(iRightBound.getTime()), iContext);
      ArrayList<BloodPressureReading> resultReadings = new ArrayList<BloodPressureReading>(resultTable.size());
      for (int i = 0; i < resultTable.size(); ++i)
      {
        ArrayList<String> values = resultTable.get(i);
        BloodPressureReading r;
        Long id = Long.valueOf(values.get(0));

        Date date = new Date(Long.valueOf(values.get(1))); // Converts from Unix
                                                           // time to Java Date
        int sourceInt = Integer.valueOf(values.get(2));
        ReadingSource source = sourceFromInt(sourceInt);
        int systolic = Integer.valueOf(values.get(3));
        int diastolic = Integer.valueOf(values.get(4));
        int heartrate = Integer.valueOf(values.get(5));

        r = new BloodPressureReading(id, date, source, systolic, diastolic,
            heartrate);

        resultReadings.add(r);
      }
      return resultReadings;
    }
    catch (PersistenceException iE)
    {
      throw new MapperException(
          "The Mapper failed to obtain the Blood Pressure readings from the persistence layer. The TDG returned the following error: "
              + iE.getMessage());
    } 
    catch (Exception iE)
    {
      throw new MapperException(
          "The mapper failed to complete an operation for the following unforeseen reason: "
              + iE.getMessage());
    }
  }

  /**
   * Stores the new BloodPressureReading
   * 
   * @param iReading The BloodPressureReading object
   * @param iContext The application context
   * @throws MapperException Thrown if there is a problem mapping or if the
   *           persistence returns an error
   */
  public static void insert(BloodPressureReading iReading, Context iContext) throws MapperException
  {
    try
    {
      ArrayList<String> values = new ArrayList<String>(10);
      values.add(iReading.getId().toString());
      values.add(String.valueOf(iReading.getDateTaken().getTime()));
      values.add(String.valueOf(intFromSource(iReading.getMeasurementSource())));
      values.add(String.valueOf(iReading.getSystolic()));
      values.add(String.valueOf(iReading.getDiastolic()));
      values.add(String.valueOf(iReading.getHeartrate()));

      BloodPressureTDG.insert(values, iContext);
      BloodPressureTDG.removeRedundantEntries(values, iContext);
    } 
    catch (PersistenceException e)
    {
      throw new MapperException(
          "The mapper failed to complete an operation because the persistence layer returned the following error: "
              + e.getMessage());
    } 
    catch (Exception e)
    {
      throw new MapperException(
          "The mapper failed to complete an operation for the following unforeseen reason: "
              + e.getMessage());
    }
  }

  /**
   * Finds the next ID available for using in Blood Pressure reading
   * initialization
   * 
   * @param iContext The application context
   * @return The next available Id
   * @throws MapperException Thrown if there is a problem mapping or if the
   *           persistence returns an error
   */
  public static Long getNextAvailableId(Context iContext) throws MapperException
  {
    try
    {
      Long id = BloodPressureTDG.getNextAvailableId(iContext);
      return id;
    } 
    catch (PersistenceException e)
    {
      throw new MapperException(
          "The mapper failed to complete an operation because the persistence layer returned the following error: "
              + e.getMessage());
    } 
    catch (Exception e)
    {
      throw new MapperException(
          "The mapper failed to complete an operation for the following unforeseen reason: "
              + e.getMessage());
    }
  }
//-------------------------------------------
public static ArrayList<BloodPressureReading> findLastSevenDays(
      Date iLeftBound, Context iContext) throws MapperException
  {
    try
    {
      ArrayList<ArrayList<String>> resultTable = BloodPressureTDG.selectLastSevenDays(String.valueOf(iLeftBound.getTime()), iContext);
      ArrayList<BloodPressureReading> resultReadings = new ArrayList<BloodPressureReading>(resultTable.size());
      for (int i = 0; i < resultTable.size(); ++i)
      {
        ArrayList<String> values = resultTable.get(i);
        BloodPressureReading r;
        Long id = Long.valueOf(values.get(0));

        Date date = new Date(Long.valueOf(values.get(1))); // Converts from Unix
                                                           // time to Java Date
        int sourceInt = Integer.valueOf(values.get(2));
        ReadingSource source = sourceFromInt(sourceInt);
        int systolic = Integer.valueOf(values.get(3));
        int diastolic = Integer.valueOf(values.get(4));
        int heartrate = Integer.valueOf(values.get(5));

        r = new BloodPressureReading(id, date, source, systolic, diastolic,
            heartrate);

        resultReadings.add(r);
      }
      return resultReadings;
    }
    catch (PersistenceException iE)
    {
      throw new MapperException(
          "The Mapper failed to obtain the Blood Pressure readings from the persistence layer. The TDG returned the following error: "
              + iE.getMessage());
    } 
    catch (Exception iE)
    {
      throw new MapperException(
          "The mapper failed to complete an operation for the following unforeseen reason: "
              + iE.getMessage());
    }
  }
}

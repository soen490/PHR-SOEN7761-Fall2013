package com.augmentedsociety.myphr.domain;

import java.util.ArrayList;
import java.util.Date;

import android.content.Context;

import com.augmentedsociety.myphr.persistence.BloodSugarTDG;
import com.augmentedsociety.myphr.persistence.PersistenceException;

/**
 * Blood Sugar Reading data mapper
 * 
 * @author Serge-Antoine Naim
 * 
 */

public class BloodSugarMapper extends ReadingDataMapper
{
  /**
   * Hidden constructor
   */
  private BloodSugarMapper()
  {

  }
  
  /**
   * Returns all Blood Sugar Reading objects that are in the database.
   * 
   * @param c The application context
   * @note Recreates only the new objects, since Identity Map is no longer applied: the reason for this is because whenever
   *       data is fetched, we know its usage right away and will not need to keep it in proximity for later usage, but rather direct usage
   * @return The ArrayList of all BloodSugarReadings in the database
   * @throws MapperException Thrown when there is either a problem with mapping
   *           or if the Persistence layer returns an error
   */
  public static ArrayList<BloodSugarReading> findAll(Context iC) throws MapperException
  {
    try
    {
      ArrayList<ArrayList<String>> resultTable = BloodSugarTDG.selectAll(iC);
      ArrayList<BloodSugarReading> resultReadings = new ArrayList<BloodSugarReading>(resultTable.size());
      for (int i = 0; i < resultTable.size(); ++i)
      {
        ArrayList<String> values = resultTable.get(i);
        BloodSugarReading r;
        Long id = Long.valueOf(values.get(0));
        Date date = new Date(Long.valueOf(values.get(1))); /** Converts from Unix
                                                            time to Java Date */
        int sourceInt = Integer.valueOf(values.get(2));
        ReadingSource source = sourceFromInt(sourceInt);
        float bloodGlucose = Float.valueOf(values.get(3));

        r = new BloodSugarReading(id, date, source, bloodGlucose);
        resultReadings.add(r);
      }
      return resultReadings;
    } 
    catch (PersistenceException iE)
    {
      throw new MapperException(
          "The Mapper failed to obtain the Blood Sugar readings from the persistence layer. The TDG returned the following error: "
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
   * Returns all Blood Sugar Reading objects that fall within the time range
   * specified (inclusive)
   * 
   * @param leftBound The Date object specifying the earliest date boundary
   * @param rightBound The Date object specifying the latest date boundary
   * @param c The application context
   * @note Recreates only the new objects, since Identity Map is no longer applied: the reason for this is because whenever
   *       data is fetched, we know its usage right away and will not need to keep it in proximity for later usage, but rather direct usage
   * @return The ArrayList of all BloodSugareReadings in the database that
   *         fall within range
   * @throws MapperException Thrown when there is either a problem with mapping
   *           or if the Persistence layer returns an error
   */
  public static ArrayList<BloodSugarReading> findAllWithinRangeInclusive(
      Date iLeftBound, Date iRightBound, Context iC) throws MapperException
  {
    try
    {
      ArrayList<ArrayList<String>> resultTable = BloodSugarTDG
          .selectBetweenDatesInclusive(String.valueOf(iLeftBound.getTime()),
              String.valueOf(iRightBound.getTime()), iC);
      ArrayList<BloodSugarReading> resultReadings = new ArrayList<BloodSugarReading>(resultTable.size());
      for (int i = 0; i < resultTable.size(); ++i)
      {
        ArrayList<String> values = resultTable.get(i);
        BloodSugarReading r;
        Long id = Long.valueOf(values.get(0));

        Date date = new Date(Long.valueOf(values.get(1))); /** Converts from Unix
                                                            time to Java Date */
        int sourceInt = Integer.valueOf(values.get(2));
        ReadingSource source = sourceFromInt(sourceInt);
        float bloodGlucose = Float.valueOf(values.get(3));

        r = new BloodSugarReading(id, date, source, bloodGlucose);

        resultReadings.add(r);
      }
      return resultReadings;
    }
    catch (PersistenceException iE)
    {
      throw new MapperException(
          "The Mapper failed to obtain the Blood Sugar readings from the persistence layer. The TDG returned the following error: "
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
   * Stores the new BloodSugarReading
   * 
   * @param r The BloodSugarReading object
   * @param c The application context
   * @throws MapperException Thrown if there is a problem mapping or if the
   *           persistence returns an error
   */
  public static void insert(BloodSugarReading iR, Context iC) throws MapperException
  {
    try
    {
      ArrayList<String> values = new ArrayList<String>(10);
      values.add(iR.getId().toString());
      values.add(String.valueOf(iR.getDateTaken().getTime()));
      values.add(String.valueOf(intFromSource(iR.getMeasurementSource())));
      values.add(String.valueOf(iR.getBloodGlucose()));

      BloodSugarTDG.insert(values, iC);
      BloodSugarTDG.removeRedundantEntries(values, iC);
    } 
    catch (PersistenceException iE)
    {
      throw new MapperException(
          "The mapper failed to complete an operation because the persistence layer returned the following error: "
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
   * Finds the next ID available for using in Blood Sugar reading
   * initialization
   * 
   * @param c The application context
   * @return The next available Id
   * @throws MapperException Thrown if there is a problem mapping or if the
   *           persistence returns an error
   */
  public static Long getNextAvailableId(Context iC) throws MapperException
  {
    try
    {
      Long id = BloodSugarTDG.getNextAvailableId(iC);
      return id;
    } 
    catch (PersistenceException iE)
    {
      throw new MapperException(
          "The mapper failed to complete an operation because the persistence layer returned the following error: "
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

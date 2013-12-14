package com.augmentedsociety.myphr.domain.logs;

import java.util.ArrayList;
import java.util.Date;

import android.content.Context;

import com.augmentedsociety.myphr.domain.MapperException;
import com.augmentedsociety.myphr.persistence.LogEventTDG;
import com.augmentedsociety.myphr.persistence.PersistenceException;

/**
 * Log Event data mapper.
 * 
 * @author Alexandre Hudon
 * 
 */

public class LogEventMapper 
{  
  /**
   * Returns all LogItems objects that are in the database.
   * 
   * @param c The application context
   * @note 
   *   Recreates only the new objects, since Identity Map is no longer 
   *   applied: the reason for this is because whenever data is fetched, 
   *   we know its usage right away and will not need to keep it in 
   *   proximity for later usage, but rather direct usage
   *       
   * @return The ArrayList of all LogItems in the database
   * @throws MapperException Thrown when there is either a problem with mapping
   *           or if the Persistence layer returns an error
   */
  public static ArrayList<LogItem> findAll(Context iC) throws MapperException
  {
    try
    {
      ArrayList<ArrayList<String>> logTable = LogEventTDG.selectAll(iC);
      ArrayList<LogItem> logs = new ArrayList<LogItem>(logTable.size());
      for (int i = 0; i < logTable.size(); ++i)
      {
        ArrayList<String> values = logTable.get(i);
        LogItem l;
        Long id = Long.valueOf(values.get(0));
        
        /* Converts from Unix time to Java Date */
        Long date = Long.valueOf(values.get(1)); 
        LogEventType type = LogEventType.valueOf(values.get(2));
        String info = values.get(3);
        

        l = new LogItem(id, type, info,date);
        logs.add(l);
      }
      return logs;
    } 
    catch (PersistenceException iE)
    {
      throw new MapperException(
          "The Mapper failed to obtain the Logs from the persistence layer."
          + " The TDG returned the following error: "
          + iE.getMessage());
    } 
    catch (Exception iE)
    {
      throw new MapperException(
          "The mapper failed to complete an operation for the"
          + " following unforeseen reason: "
          + iE.getMessage());
    }
  }

  /**
   * Returns all LogItem objects that fall within the time range
   * specified (inclusive)
   * 
   * @param leftBound The Date object specifying the earliest date boundary
   * @param rightBound The Date object specifying the latest date boundary
   * @param c The application context
   * @note 
   *   Recreates only the new objects, since Identity Map is no longer 
   *   applied: the reason for this is because whenever data is fetched, we 
   *   know its usage right away and will not need to keep it in proximity 
   *   for later usage, but rather direct usage
   *       
   * @return 
   *   The ArrayList of all LogItems in the database that fall within range
   *   
   * @throws 
   *   MapperException Thrown when there is either a problem with mapping or 
   *   if the Persistence layer returns an error
   */
  public static ArrayList<LogItem> findAllWithinRangeInclusive(
      Date iLeftBound, Date iRightBound, Context iC) throws MapperException
  {
    try
    {
      ArrayList<ArrayList<String>> logTable = LogEventTDG
          .selectBetweenDatesInclusive(String.valueOf(iLeftBound.getTime()),
              String.valueOf(iRightBound.getTime()), iC);
      ArrayList<LogItem> logs = new ArrayList<LogItem>(logTable.size());
      for (int i = 0; i < logTable.size(); ++i)
      {
        ArrayList<String> values = logTable.get(i);
        LogItem l;
        Long id = Long.valueOf(values.get(0));
        Long date = Long.valueOf(values.get(1)); /** Converts from Unix
                                                            time to Java Date */
        LogEventType type = LogEventType.valueOf(values.get(2));
        String info = values.get(3);
        

        l = new LogItem(id, type, info,date);
        logs.add(l);
      }
      return logs;
    }
    catch (PersistenceException iE)
    {
      throw new MapperException(
          "The Mapper failed to obtain the logs readings from the persistence layer. The TDG returned the following error: "
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
   * Stores the new Log Item
   * 
   * @param l The Log object
   * @param c The application context
   * @throws MapperException Thrown if there is a problem mapping or if the
   *           persistence returns an error
   */
  public static void insert(LogItem iL, Context iC) throws MapperException
  {
    try
    {
      ArrayList<String> values = new ArrayList<String>(10);
      values.add(iL.getEventID().toString());
      values.add(String.valueOf(iL.getEventDate()));
      values.add(iL.getEventType().toString());
      values.add(iL.getEventInfo());

      LogEventTDG.insert(values, iC);
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
   * Finds the next ID available to use in Log initialization
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
      Long id = LogEventTDG.getNextAvailableId(iC);
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
  
  public static void deleteLogEntries(Context iC) throws MapperException
  {
  	try
  	{
  		LogEventTDG.delete(iC);
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


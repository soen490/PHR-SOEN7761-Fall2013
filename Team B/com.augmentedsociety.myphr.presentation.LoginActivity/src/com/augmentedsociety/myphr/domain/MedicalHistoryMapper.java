package com.augmentedsociety.myphr.domain;

import java.util.ArrayList;
import java.util.Date;

import android.content.Context;

import com.augmentedsociety.myphr.persistence.MedicalHistoryTDG;
import com.augmentedsociety.myphr.persistence.PersistenceException;

/**
 * Create new medical history mapper
 * 
 * @author Roger Makram
 *
 */

public class MedicalHistoryMapper extends ReadingDataMapper
{
	/**
   * Returns all MedicalHistory objects that are in the database.
   * 
   * @param iContext The application context
   * @return The ArrayList of all MedicalHistory in the database
   * @throws MapperException Thrown when there is either a problem with mapping
   *           or if the Persistence layer returns an error
   */
  public static ArrayList<MedicalHistory> findAll(Context iContext) throws MapperException
  {
    try
    {
      ArrayList<ArrayList<String>> wValuesTable = MedicalHistoryTDG.selectAll(iContext);
      ArrayList<MedicalHistory> wStoredMedicalHistory = new ArrayList<MedicalHistory>(wValuesTable.size());
      for (int i = 0; i < wValuesTable.size(); ++i)
      {
        ArrayList<String> values = wValuesTable.get(i);
        MedicalHistory storedMedicalHistory;
        Long id = Long.valueOf(values.get(0));
        Date date = new Date(Long.valueOf(values.get(1)));
        String description = values.get(2);
        String location = values.get(3);

        storedMedicalHistory = new MedicalHistory(id, date, description, location);
        wStoredMedicalHistory.add(storedMedicalHistory);
      }
      return wStoredMedicalHistory;
    } 
    catch (PersistenceException e)
    {
      throw new MapperException(
          "The Mapper failed to obtain the MedicalHistory readings from the persistence layer. The TDG returned the following error: "
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
   * Selects one single MedicalHistory by the specified ID
   * @param iMedicalHistoryId The id of the MedicalHistory to get from the DB
   * @param iContext Application context
   * @return
   * @throws MapperException
   */
  public static MedicalHistory getOne(long iMedicalHistoryId, Context iContext) throws MapperException
  {
    try
    {
      ArrayList<String> fields = MedicalHistoryTDG.select(iMedicalHistoryId, iContext);

      MedicalHistory storedMedicalHistory;
      Long id = Long.valueOf(fields.get(0));
      Date date = new Date(Long.valueOf(fields.get(1)));
      String description = fields.get(2);
      String location = fields.get(3);

      storedMedicalHistory = new MedicalHistory(id, date, description, location);

      return storedMedicalHistory;
    } 
    catch (PersistenceException e)
    {
      throw new MapperException(
          "The Mapper failed to obtain the MedicalHistory readings from the "
          + "persistence layer. The TDG returned the following error: "
          + e.getMessage());
    } 
    catch (Exception e)
    {
      throw new MapperException(
          "The mapper failed to complete an operation for the following "
          + "unforeseen reason: "
          + e.getMessage());
    }
  }

  /**
   * Stores the new MedicalHistory
   * 
   * @param iMedicalHistory The MedicalHistory object
   * @param iContext The application context
   * @throws MapperException Thrown if there is a problem mapping or if the
   *           persistence returns an error
   */
  public static void insert(MedicalHistory iMedicalHistory, Context iContext) throws MapperException
  {
    try
    {
            
      ArrayList<String> values = new ArrayList<String>(20);
      values.add(iMedicalHistory.getID().toString());
      values.add(String.valueOf(iMedicalHistory.getDate().getTime()));
      values.add(String.valueOf(iMedicalHistory.getDescription()));
      values.add(String.valueOf(iMedicalHistory.getLocation()));

      MedicalHistoryTDG.insert(values, iContext);
    } 
    catch (PersistenceException e)
    {
      throw new MapperException(
          "The mapper failed to complete an operation because the persistence "
          + "layer returned the following error: "
          + e.getMessage());
    } 
    catch (Exception e)
    {
      throw new MapperException(
          "The mapper failed to complete an operation for the following "
          + "unforeseen reason: "
          + e.getMessage());
    }
  }
  
  /**
   * Updates an MedicalHistory
   * 
   * @param iMedicalHistory The MedicalHistory object
   * @param iContext The application context
   * @throws MapperException Thrown if there is a problem mapping or if the
   *           persistence returns an error
   */
  public static void update(MedicalHistory iMedicalHistory, Context iContext) throws MapperException
  {
    try
    {
    	ArrayList<String> values = new ArrayList<String>(20);
      values.add(iMedicalHistory.getID().toString());
      values.add(String.valueOf(iMedicalHistory.getDate().getTime()));
      values.add(String.valueOf(iMedicalHistory.getDescription()));
      values.add(String.valueOf(iMedicalHistory.getLocation()));

      int wRowsAffected = MedicalHistoryTDG.update(values, iContext);
      
      if (0 == wRowsAffected)
      {
        throw new MapperException("Failed to update the entry (rows affected = 0); incorrect id?");
      }
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
   * Finds the next ID available for using in an MedicalHistory reading
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
      Long id = MedicalHistoryTDG.getNextAvailableId(iContext);
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
  
  /**
   * Deletes an MedicalHistory
   * 
   * @param iMedicalHistory The MedicalHistory object
   * @param iContext The application context
   * @throws MapperException Thrown if there is a problem mapping or if the
   *           persistence returns an error
   */
  public static void delete(MedicalHistory iMedicalHistory, Context iContext) throws MapperException
  {
    try
    {
      int wRowsAffected = MedicalHistoryTDG.delete(iMedicalHistory.getID(), iContext);
      
      if (0 == wRowsAffected)
      {
        throw new MapperException("Failed to delete the entry (rows affected = 0); incorrect id?");
      }
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
}

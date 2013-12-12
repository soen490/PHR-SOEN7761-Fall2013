package com.augmentedsociety.myphr.domain;

import java.util.ArrayList;
import java.util.Date;

import android.content.Context;

import com.augmentedsociety.myphr.persistence.MedicationsTDG;
import com.augmentedsociety.myphr.persistence.PersistenceException;

/**
 * Create new medication mapper
 * 
 * @author Rajan Jayakumar
 *
 */

public class MedicationMapper extends ReadingDataMapper
{
	/**
   * Returns all Medication objects that are in the database.
   * 
   * @param iContext The application context
   * @return The ArrayList of all Medications in the database
   * @throws MapperException Thrown when there is either a problem with mapping
   *           or if the Persistence layer returns an error
   */
  public static ArrayList<Medication> findAll(Context iContext) throws MapperException
  {
    try
    {
      ArrayList<ArrayList<String>> wValuesTable = MedicationsTDG.selectAll(iContext);
      ArrayList<Medication> wStoredMedications = new ArrayList<Medication>(wValuesTable.size());
      for (int i = 0; i < wValuesTable.size(); ++i)
      {
        ArrayList<String> values = wValuesTable.get(i);
        Medication storedMedication;
        Long id = Long.valueOf(values.get(0));
        String type = values.get(1);
        String name = values.get(2);
        String posology = values.get(3);
        String strength = values.get(4);
        String frequency = values.get(5);
        Date start_date = new Date(Long.valueOf(values.get(6)));
        Date end_date = new Date(Long.valueOf(values.get(7)));
        String reasons = values.get(8);
        String doctor = values.get(9);
        
        storedMedication = new Medication(id, type, name, posology, strength, frequency, start_date, end_date, reasons, doctor);
        wStoredMedications.add(storedMedication);
      }
      return wStoredMedications;
    } 
    catch (PersistenceException e)
    {
      throw new MapperException(
          "The Mapper failed to obtain the Medication readings from the persistence layer. The TDG returned the following error: "
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
   * Selects one single Medication by the specified ID
   * @param iMedicationId The id of the medication to get from the DB
   * @param iContext Application context
   * @return
   * @throws MapperException
   */
  public static Medication getOne(long iMedicationId, Context iContext) throws MapperException
  {
    try
    {
      ArrayList<String> fields = MedicationsTDG.select(iMedicationId, iContext);

      Medication storedMedication;
      Long id = Long.valueOf(fields.get(0));
      String type = fields.get(1);
      String name = fields.get(2);
      String posology = fields.get(3);
      String strength = fields.get(4);
      String frequency = fields.get(5);
      Date start_date = new Date(Long.valueOf(fields.get(6)));
      Date end_date = new Date(Long.valueOf(fields.get(7)));
      String reasons = fields.get(8);
      String doctor = fields.get(9);

      storedMedication = new Medication(id, type, name, posology, strength, frequency, start_date, end_date, reasons, doctor);

      return storedMedication;
    } 
    catch (PersistenceException e)
    {
      throw new MapperException(
          "The Mapper failed to obtain the Medication readings from the "
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
   * Stores the new Medication
   * 
   * @param iMedication The Medication object
   * @param iContext The application context
   * @throws MapperException Thrown if there is a problem mapping or if the
   *           persistence returns an error
   */
  public static void insert(Medication iMedication, Context iContext) throws MapperException
  {
    try
    {
            
      ArrayList<String> values = new ArrayList<String>(20);
      values.add(iMedication.getID().toString());
      values.add(String.valueOf(iMedication.getType()));
      values.add(String.valueOf(iMedication.getName()));
      values.add(String.valueOf(iMedication.getPosology()));
      values.add(String.valueOf(iMedication.getStrength()));
      values.add(String.valueOf(iMedication.getFrequency()));
      values.add(String.valueOf(iMedication.getStartDate().getTime()));
      values.add(String.valueOf(iMedication.getEndDate().getTime()));
      values.add(String.valueOf(iMedication.getReasons()));
      values.add(String.valueOf(iMedication.getDoctor()));

      MedicationsTDG.insert(values, iContext);
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
   * Updates a Medication
   * 
   * @param iMedication The Medication object
   * @param iContext The application context
   * @throws MapperException Thrown if there is a problem mapping or if the
   *           persistence returns an error
   */
  public static void update(Medication iMedication, Context iContext) throws MapperException
  {
    try
    {
    	ArrayList<String> values = new ArrayList<String>(20);
    	values.add(iMedication.getID().toString());
      values.add(String.valueOf(iMedication.getType()));
      values.add(String.valueOf(iMedication.getName()));
      values.add(String.valueOf(iMedication.getPosology()));
      values.add(String.valueOf(iMedication.getStrength()));
      values.add(String.valueOf(iMedication.getFrequency()));
      values.add(String.valueOf(iMedication.getStartDate().getTime()));
      values.add(String.valueOf(iMedication.getEndDate().getTime()));
      values.add(String.valueOf(iMedication.getReasons()));
      values.add(String.valueOf(iMedication.getDoctor()));

      int wRowsAffected = MedicationsTDG.update(values, iContext);
      
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
   * Finds the next ID available for using in a Medication reading
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
      Long id = MedicationsTDG.getNextAvailableId(iContext);
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
   * Deletes a Medication
   * 
   * @param iMedication The Medication object
   * @param iContext The application context
   * @throws MapperException Thrown if there is a problem mapping or if the
   *           persistence returns an error
   */
  public static void delete(Medication iMedication, Context iContext) throws MapperException
  {
    try
    {
      int wRowsAffected = MedicationsTDG.delete(iMedication.getID(), iContext);
      
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

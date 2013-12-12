package com.augmentedsociety.myphr.domain;

import java.util.ArrayList;
import android.content.Context;

import com.augmentedsociety.myphr.persistence.AllergiesTDG;
import com.augmentedsociety.myphr.persistence.PersistenceException;

/**
 * Create new allergy activity
 * 
 * @author Roger Makram
 *
 */

public class AllergyMapper extends ReadingDataMapper
{
	/**
   * Returns all Allergy objects that are in the database.
   * 
   * @param iContext The application context
   * @return The ArrayList of all Allergies in the database
   * @throws MapperException Thrown when there is either a problem with mapping
   *           or if the Persistence layer returns an error
   */
  public static ArrayList<Allergy> findAll(Context iContext) throws MapperException
  {
    try
    {
      ArrayList<ArrayList<String>> wValuesTable = AllergiesTDG.selectAll(iContext);
      ArrayList<Allergy> wStoredAllergies = new ArrayList<Allergy>(wValuesTable.size());
      for (int i = 0; i < wValuesTable.size(); ++i)
      {
        ArrayList<String> values = wValuesTable.get(i);
        Allergy storedAllergy;
        Long id = Long.valueOf(values.get(0));
        String allergy = values.get(1);
        String reaction = values.get(2);
        String severity = values.get(3);

        storedAllergy = new Allergy(id, allergy, reaction, severity);
        wStoredAllergies.add(storedAllergy);
      }
      return wStoredAllergies;
    } 
    catch (PersistenceException e)
    {
      throw new MapperException(
          "The Mapper failed to obtain the Allergy readings from the persistence layer. The TDG returned the following error: "
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
   * Selects one single Allergy by the specified ID
   * @param iAllergyId The id of the allergy to get from the DB
   * @param iContext Application context
   * @return
   * @throws MapperException
   */
  public static Allergy getOne(long iAllergyId, Context iContext) throws MapperException
  {
    try
    {
      ArrayList<String> fields = AllergiesTDG.select(iAllergyId, iContext);

      Allergy storedAllergy;
      Long id = Long.valueOf(fields.get(0));
      String allergy = fields.get(1);
      String reaction = fields.get(2);
      String severity = fields.get(3);

      storedAllergy = 
      		new Allergy(id, allergy, reaction, severity);

      return storedAllergy;
    } 
    catch (PersistenceException e)
    {
      throw new MapperException(
          "The Mapper failed to obtain the Allergy readings from the "
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
   * Stores the new Allergy
   * 
   * @param iAllergy The Allergy object
   * @param iContext The application context
   * @throws MapperException Thrown if there is a problem mapping or if the
   *           persistence returns an error
   */
  public static void insert(Allergy iAllergy, Context iContext) throws MapperException
  {
    try
    {
            
      ArrayList<String> values = new ArrayList<String>(10);
      values.add(iAllergy.getID().toString());
      values.add(String.valueOf(iAllergy.getAllergic()));
      values.add(String.valueOf(iAllergy.getReaction()));
      values.add(String.valueOf(iAllergy.getSeverity()));

      AllergiesTDG.insert(values, iContext);
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
   * Updates a Allergy
   * 
   * @param iAllergy The Allergy object
   * @param iContext The application context
   * @throws MapperException Thrown if there is a problem mapping or if the
   *           persistence returns an error
   */
  public static void update(Allergy iAllergy, Context iContext) throws MapperException
  {
    try
    {
    	ArrayList<String> values = new ArrayList<String>(10);
      values.add(iAllergy.getID().toString());
      values.add(String.valueOf(iAllergy.getAllergic()));
      values.add(String.valueOf(iAllergy.getReaction()));
      values.add(String.valueOf(iAllergy.getSeverity()));

      int wRowsAffected = AllergiesTDG.update(values, iContext);
      
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
   * Finds the next ID available for using in Allergy reading
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
      Long id = AllergiesTDG.getNextAvailableId(iContext);
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
   * Deletes a Allergy
   * 
   * @param iAllergy The Allergy object
   * @param iContext The application context
   * @throws MapperException Thrown if there is a problem mapping or if the
   *           persistence returns an error
   */
  public static void delete(Allergy iAllergy, Context iContext) throws MapperException
  {
    try
    {
      int wRowsAffected = AllergiesTDG.delete(iAllergy.getID(), iContext);
      
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

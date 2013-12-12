package com.augmentedsociety.myphr.domain;

import java.util.ArrayList;
import java.util.Date;

import android.content.Context;

import com.augmentedsociety.myphr.persistence.ImmunizationsTDG;
import com.augmentedsociety.myphr.persistence.PersistenceException;

/**
 * Create new immunization mapper
 * 
 * @author Rajan Jayakumar
 *
 */

public class ImmunizationMapper extends ReadingDataMapper
{
	/**
   * Returns all Immunization objects that are in the database.
   * 
   * @param iContext The application context
   * @return The ArrayList of all Immunizations in the database
   * @throws MapperException Thrown when there is either a problem with mapping
   *           or if the Persistence layer returns an error
   */
  public static ArrayList<Immunization> findAll(Context iContext) throws MapperException
  {
    try
    {
      ArrayList<ArrayList<String>> wValuesTable = ImmunizationsTDG.selectAll(iContext);
      ArrayList<Immunization> wStoredImmunizations = new ArrayList<Immunization>(wValuesTable.size());
      for (int i = 0; i < wValuesTable.size(); ++i)
      {
        ArrayList<String> values = wValuesTable.get(i);
        Immunization storedImmunization;
        Long id = Long.valueOf(values.get(0));
        String type = values.get(1);
        String name = values.get(2);
        String manufacturer = values.get(3);
        String lot_number = values.get(4);
        String route = values.get(5);
        String posology = values.get(6);
        Date date = new Date(Long.valueOf(values.get(7)));
        String location = values.get(8);
        String details = values.get(9);
        String comments = values.get(10);

        storedImmunization = new Immunization(id, type, name, manufacturer, lot_number, route, posology, date, location, details, comments);
        wStoredImmunizations.add(storedImmunization);
      }
      return wStoredImmunizations;
    } 
    catch (PersistenceException e)
    {
      throw new MapperException(
          "The Mapper failed to obtain the Immunization readings from the persistence layer. The TDG returned the following error: "
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
   * Selects one single Immunization by the specified ID
   * @param iImmunizationId The id of the immunization to get from the DB
   * @param iContext Application context
   * @return
   * @throws MapperException
   */
  public static Immunization getOne(long iImmunizationId, Context iContext) throws MapperException
  {
    try
    {
      ArrayList<String> fields = ImmunizationsTDG.select(iImmunizationId, iContext);

      Immunization storedImmunization;
      Long id = Long.valueOf(fields.get(0));
      String type = fields.get(1);
      String name = fields.get(2);
      String manufacturer = fields.get(3);
      String lot_number = fields.get(4);
      String route = fields.get(5);
      String posology = fields.get(6);
      Date date = new Date(Long.valueOf(fields.get(7)));
      String location = fields.get(8);
      String details = fields.get(9);
      String comments = fields.get(10);

      storedImmunization = new Immunization(id, type, name, manufacturer, lot_number, route, posology, date, location, details, comments);

      return storedImmunization;
    } 
    catch (PersistenceException e)
    {
      throw new MapperException(
          "The Mapper failed to obtain the Immunization readings from the "
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
   * Stores the new Immunization
   * 
   * @param iImmunization The Immunization object
   * @param iContext The application context
   * @throws MapperException Thrown if there is a problem mapping or if the
   *           persistence returns an error
   */
  public static void insert(Immunization iImmunization, Context iContext) throws MapperException
  {
    try
    {
            
      ArrayList<String> values = new ArrayList<String>(20);
      values.add(iImmunization.getID().toString());
      values.add(String.valueOf(iImmunization.getType()));
      values.add(String.valueOf(iImmunization.getName()));
      values.add(String.valueOf(iImmunization.getManufacturer()));
      values.add(String.valueOf(iImmunization.getLot_number()));
      values.add(String.valueOf(iImmunization.getRoute()));
      values.add(String.valueOf(iImmunization.getPosology()));
      values.add(String.valueOf(iImmunization.getDate().getTime()));
      values.add(String.valueOf(iImmunization.getLocation()));
      values.add(String.valueOf(iImmunization.getDetails()));
      values.add(String.valueOf(iImmunization.getComments()));

      ImmunizationsTDG.insert(values, iContext);
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
   * Updates an Immunization
   * 
   * @param iImmunization The Immunization object
   * @param iContext The application context
   * @throws MapperException Thrown if there is a problem mapping or if the
   *           persistence returns an error
   */
  public static void update(Immunization iImmunization, Context iContext) throws MapperException
  {
    try
    {
    	ArrayList<String> values = new ArrayList<String>(20);
    	values.add(iImmunization.getID().toString());
      values.add(String.valueOf(iImmunization.getType()));
      values.add(String.valueOf(iImmunization.getName()));
      values.add(String.valueOf(iImmunization.getManufacturer()));
      values.add(String.valueOf(iImmunization.getLot_number()));
      values.add(String.valueOf(iImmunization.getRoute()));
      values.add(String.valueOf(iImmunization.getPosology()));
      values.add(String.valueOf(iImmunization.getDate().getTime()));
      values.add(String.valueOf(iImmunization.getLocation()));
      values.add(String.valueOf(iImmunization.getDetails()));
      values.add(String.valueOf(iImmunization.getComments()));

      int wRowsAffected = ImmunizationsTDG.update(values, iContext);
      
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
   * Finds the next ID available for using in an Immunization reading
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
      Long id = ImmunizationsTDG.getNextAvailableId(iContext);
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
   * Deletes an Immunization
   * 
   * @param iImmunization The Immunization object
   * @param iContext The application context
   * @throws MapperException Thrown if there is a problem mapping or if the
   *           persistence returns an error
   */
  public static void delete(Immunization iImmunization, Context iContext) throws MapperException
  {
    try
    {
      int wRowsAffected = ImmunizationsTDG.delete(iImmunization.getID(), iContext);
      
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

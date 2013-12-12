package com.augmentedsociety.myphr.domain;

import java.util.ArrayList;

import android.content.Context;

import com.augmentedsociety.myphr.persistence.DefaultPageTDG;
import com.augmentedsociety.myphr.persistence.PersistenceException;

/**
 * Create new default page mapper
 * 
 * @author Rajan Jayakumar
 *
 */

public class DefaultPageMapper extends ReadingDataMapper
{
	/**
   * Returns the default page from the database
   */
  public static String get(Context iContext) throws MapperException
  {
    try
    {
      ArrayList<String> fields = DefaultPageTDG.select(iContext);
      return fields.get(0);
    } 
    catch (PersistenceException e)
    {
      throw new MapperException(
          "The Mapper failed to obtain the default page from the "
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
   * Updates the default page
   */
  public static void update(String page, Context iContext) throws MapperException
  {
    try
    {
    	ArrayList<String> values = new ArrayList<String>(2);
    	values.add("1");
      values.add(page);

      int wRowsAffected = DefaultPageTDG.update(values, iContext);
      
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
}

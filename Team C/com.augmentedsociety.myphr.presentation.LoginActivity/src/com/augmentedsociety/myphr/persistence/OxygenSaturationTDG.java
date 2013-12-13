package com.augmentedsociety.myphr.persistence;

import java.util.ArrayList;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * Table Data Gateway for Oxygen Saturation readings
 * 
 * @author Serge-Antoine Naim
 * 
 */

public class OxygenSaturationTDG extends MeasurementTDG
{
	public static final String TABLE = "oxygen_saturation";
	
  private static final String SELECT_ALL = 
  		"SELECT r.id, r.when_taken, r.source, r.oxygen_saturation FROM "
      + TABLE + " as r;";
  
  private static final String SELECT_BETWEEN_DATES_INCLUSIVE = 
  		"SELECT r.id, r.when_taken, r.source, r.oxygen_saturation FROM "
      + TABLE
      + " as r WHERE r.when_taken >= ? AND r.when_taken <= ?;";
  
  private static final String SELECT_LAST_SEVEN_DAYS = 
  		"SELECT r.id, r.when_taken, r.source, r.oxygen_saturation FROM "
      + TABLE
      + " as r WHERE r.when_taken >= ? ;";
  
  private static final String SELECT_MAX_ID = "SELECT max(id) from "
      + TABLE + ";";
  
  private static long mNextId;
  
  /**
   * The next instance variable (boolean) keeps track of giving different id's 
   * for every reading inserted to the corresponding table 
   */
  private static boolean mNextIdSet = false;
  
  /**
   * Selects all Oxygen Saturation readings from the database
   * 
   * @param c The context, usually the application context. Cannot be null.
   * @return The array list of all readings, each reading is another array list
   *         of strings where each element is the value of the columns in their
   *         respective order: id, when_taken, source, oxygen_saturation
   * @throws PersistenceException Thrown if there is any error in the database
   *           or the arguments are incorrect
   */
  
  public static ArrayList<ArrayList<String>> selectAll(Context iC)
      throws PersistenceException
  {
    try
    {
      ArrayList<ArrayList<String>> results = new ArrayList<ArrayList<String>>(100);
      DbRegistry wBpHelper = DbRegistry.getInstance(iC);

      SQLiteDatabase wDatabase = wBpHelper.getReadableDatabase();

      Cursor cursor = wDatabase.rawQuery(SELECT_ALL, null);

      if (cursor.moveToFirst())
      {
        do
        {
          ArrayList<String> reading = new ArrayList<String>(10);

          Long id = cursor.getLong(0);
          Long date = cursor.getLong(1);
          int source = cursor.getInt(2);
          float oxygenSaturation = cursor.getInt(3);

          reading.add(id.toString());
          reading.add(date.toString());
          reading.add(String.valueOf(source));
          reading.add(String.valueOf(oxygenSaturation));

          results.add(reading);
        } 
        while (cursor.moveToNext());
      }

      cursor.close();
      wDatabase.close();
      wBpHelper.close();
      return results;
    } 
    catch (Exception e)
    {
      throw new PersistenceException(e.getMessage());
    }
  }
  
  /**
   * Returns the next available ID
   * 
   * @param c The context, usually the application context. Cannot be null.
   * @return The next id that can be used for inserting a new reading
   * @throws PersistenceException Thrown if there is any error in the database
   *           or the arguments are incorrect
   */
  
  public static long getNextAvailableId(Context iC) throws PersistenceException
  {
    if (!mNextIdSet)
    {
      try
      {
        DbRegistry wBpHelper = DbRegistry.getInstance(iC);
        SQLiteDatabase wDatabase = wBpHelper.getReadableDatabase();

        Cursor cursor = wDatabase.rawQuery(SELECT_MAX_ID, null);
        if (cursor.moveToFirst())
        {
          if (cursor.isNull(0))
          {
            /** No rows in the table */
            mNextIdSet = true;
            mNextId = 1;
          } 
          else
          {
            Long maxId = cursor.getLong(0);
            mNextId = maxId + 1;
            mNextIdSet = true;
          }
        } 
        else
        {
          cursor.close();
          wDatabase.close();
          wBpHelper.close();
          throw new PersistenceException("Failed to compute get the maximum ID from the table.");
        }

        cursor.close();
        wDatabase.close();
        wBpHelper.close();
      } catch (Exception e)
      {
        throw new PersistenceException(e.getMessage());
      }
    }
    return mNextId++;
  }
  
  /**
   * Selects all rows within the specified datetime range (inclusive).
   * 
   * @param leftBound The earliest date, epoch time in milliseconds.
   * @param rightBound The latest date, epoch time in milliseconds.
   * @param c The application context
   * @return The array list of all readings, each reading is another array list
   *         of strings where each element is the value of the columns in their
   *         respective order: id, when_taken, source, oxygen_saturation
   * @throws PersistenceException Thrown if there is any error in the database
   *           or the arguments are incorrect
   */
  
  public static ArrayList<ArrayList<String>> selectBetweenDatesInclusive(
      String iLeftBound, String iRightBound, Context iC)
      throws PersistenceException
  {
    try
    {
      ArrayList<ArrayList<String>> results = new ArrayList<ArrayList<String>>(100);
      DbRegistry wBpHelper = DbRegistry.getInstance(iC);

      SQLiteDatabase wDatabase = wBpHelper.getReadableDatabase();

      String[] args = { iLeftBound, iRightBound };
      Cursor cursor = wDatabase.rawQuery(SELECT_BETWEEN_DATES_INCLUSIVE, args);

      if (cursor.moveToFirst())
      {
        do
        {
          ArrayList<String> reading = new ArrayList<String>(10);

          Long id = cursor.getLong(0);
          Long date = cursor.getLong(1);
          int source = cursor.getInt(2);
          float oxygenSaturation = cursor.getInt(3);

          reading.add(id.toString());
          reading.add(date.toString());
          reading.add(String.valueOf(source));
          reading.add(String.valueOf(oxygenSaturation));

          results.add(reading);
        } while (cursor.moveToNext());
      }

      cursor.close();
      wDatabase.close();
      wBpHelper.close();
      return results;
    } 
    catch (Exception e)
    {
      throw new PersistenceException(e.getMessage());
    }
  }
  
  /**
   * This method insets one single Oxygen Saturation reading into the DB.
   * 
   * @param values The ArrayList of a String exactly matching the columns of in
   *          their respective order: id, when_taken, source, oxygen_saturation
   * @param c The context, usually the application context. Cannot be null.
   * @throws PersistenceException Thrown if there is any error in the database
   *           or the arguments are incorrect
   */
  public static void insert(ArrayList<String> iValues, Context iC) throws PersistenceException
  {
    try
    {
      if (iValues.size() != 4)
        throw new PersistenceException("The array of values must have 4 entries.");
      else if (iC == null)
        throw new NullPointerException("The context you have specified is NULL");

      DbRegistry wHelper = DbRegistry.getInstance(iC);
      SQLiteDatabase wDatabase = wHelper.getWritableDatabase();

      ContentValues wValues = new ContentValues();

      wValues.put("id", Long.valueOf(iValues.get(0)));
      wValues.put("when_taken", iValues.get(1));
      wValues.put("source", iValues.get(2));
      wValues.put("oxygen_saturation", iValues.get(3));

      long wNewRowId = wDatabase.insert(TABLE, null, wValues);

      if (wNewRowId == -1)
        throw new PersistenceException("Insertion to the DB has failed. Id duplicated?");

      wDatabase.close();
      wHelper.close();
    } 
    catch (Exception e)
    {
      throw new PersistenceException(e.getMessage());
    }
  }
  
	public static void removeRedundantEntries(ArrayList<String> iValues, Context iC)
	{
		MeasurementTDG.TABLE = TABLE;
		try
		{
			MeasurementTDG.removeRedundantEntries(iValues, iC, selectAll(iC));
		} catch (PersistenceException e)
		{
			e.printStackTrace();
		}
	}
//----------------------------------------------------
public static ArrayList<ArrayList<String>> selectLastSevenDays(
      String iLeftBound, Context iContext)
      throws PersistenceException
  {
    try
    {
      ArrayList<ArrayList<String>> results = new ArrayList<ArrayList<String>>(100);
      DbRegistry wBpHelper = DbRegistry.getInstance(iContext);;

      SQLiteDatabase wDatabase = wBpHelper.getReadableDatabase();

      String[] args = { iLeftBound };
      Cursor cursor = wDatabase.rawQuery(SELECT_BETWEEN_DATES_INCLUSIVE, args);

      if (cursor.moveToFirst())
      {
      	do
        {
          ArrayList<String> reading = new ArrayList<String>(10);

          Long id = cursor.getLong(0);
          Long date = cursor.getLong(1);
          int source = cursor.getInt(2);
          float oxygenSaturation = cursor.getInt(3);

          reading.add(id.toString());
          reading.add(date.toString());
          reading.add(String.valueOf(source));
          reading.add(String.valueOf(oxygenSaturation));

          results.add(reading);
        } 
        while (cursor.moveToNext());
      }

      cursor.close();
      wDatabase.close();
      wBpHelper.close();
      return results;
    } 
    catch (Exception iE)
    {
      throw new PersistenceException(iE.getMessage());
    }
  }


}

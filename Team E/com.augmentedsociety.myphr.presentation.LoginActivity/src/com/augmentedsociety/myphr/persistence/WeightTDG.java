package com.augmentedsociety.myphr.persistence;

import java.util.ArrayList;

import com.augmentedsociety.myphr.barcode.PatientInfo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.style.SuperscriptSpan;

/**
 * Table Data Gateway for Weight readings
 * 
 * @author Serge-Antoine Naim
 * 
 */

public class WeightTDG extends MeasurementTDG
{
	public static final String TABLE = "weight";
	
  private static final String SELECT_ALL = 
  		"SELECT r.id, r.when_taken, r.source, r.weight FROM "
      + TABLE + " as r WHERE   p_id=?;";
  
  private static final String SELECT_BETWEEN_DATES_INCLUSIVE = 
  		"SELECT r.id, r.when_taken, r.source, r.weight FROM "
      + TABLE
      + " as r WHERE r.when_taken >= ? AND r.when_taken <= ? AND p_id =?;";
  
  private static final String SELECT_MAX_ID = "SELECT max(id) from "
      + TABLE + ";";
  
  private static long mNextId;
  
  /** 
   * The next instance variable (boolean) keeps track of giving different id's 
   * for every reading inserted to the corresponding table 
   */
  private static boolean mNextIdSet = false;
  
  /**
   * Selects all Weight readings from the database
   * 
   * @param c The context, usually the application context. Cannot be null.
   * @return The array list of all readings, each reading is another array list
   *         of strings where each element is the value of the columns in their
   *         respective order: id, when_taken, source, weight
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
      String[] args = { String.valueOf(PatientInfo.mID) };
      Cursor cursor = wDatabase.rawQuery(SELECT_ALL, args);

      if (cursor.moveToFirst())
      {
        do
        {
          ArrayList<String> reading = new ArrayList<String>(10);

          Long id = cursor.getLong(0);
          Long date = cursor.getLong(1);
          int source = cursor.getInt(2);
          float weight = cursor.getInt(3);

          reading.add(id.toString());
          reading.add(date.toString());
          reading.add(String.valueOf(source));
          reading.add(String.valueOf(weight));

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
            /* No rows in the table */
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
   *         respective order: id, when_taken, source, weight
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

      String[] args = { iLeftBound, iRightBound, String.valueOf(PatientInfo.mID) };
      Cursor cursor = wDatabase.rawQuery(SELECT_BETWEEN_DATES_INCLUSIVE, args);

      if (cursor.moveToFirst())
      {
        do
        {
          ArrayList<String> reading = new ArrayList<String>(10);

          Long id = cursor.getLong(0);
          Long date = cursor.getLong(1);
          int source = cursor.getInt(2);
          float weight = cursor.getInt(3);

          reading.add(id.toString());
          reading.add(date.toString());
          reading.add(String.valueOf(source));
          reading.add(String.valueOf(weight));

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
   * This method insets one single Weight reading into the DB.
   * 
   * @param values The ArrayList of a String exactly matching the columns of in
   *          their respective order: id, when_taken, source, weight
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
      wValues.put("weight", iValues.get(3));
      wValues.put("p_id",Long.valueOf(PatientInfo.mID) );

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
  
	public static void removeRedundantEntries(ArrayList<String> iValues,
			Context iC)
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
}

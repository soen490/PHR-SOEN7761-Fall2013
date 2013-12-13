package com.augmentedsociety.myphr.persistence;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
/**
 * Table Data Gateway for Log Items.
 * 
 * @author Alexandre Hudon
 * 
 */
public class LogEventTDG
{
	private static final String TABLE = "log_event";
	
	private static final String SELECT_ALL = 
			"SELECT r.id, r.event_date,  r.event_type, r.event_info FROM "
      + TABLE + " as r;";
  
	private static final String SELECT_BETWEEN_DATES_INCLUSIVE = 
			"SELECT r.id, r.event_date, r.event_type, r.event_info FROM "
      + TABLE
      + " as r WHERE r.event_date >= ? AND r.event_date <= ?;";
  
	private static final String SELECT_MAX_ID = "SELECT max(id) from "
      + TABLE + ";";
  
	
	private static final String DELETE = "delete from "+TABLE+";";
  private static long mNextId;
  
  /** 
   * The next instance variable (boolean) keeps track of giving different id's 
   * for every reading inserted to the corresponding table 
   */
  private static boolean mNextIdSet = false;

  /**
   * Selects all LogItems from the database
   * 
   * @param c The context, usually the application context. Cannot be null.
   * @return The array list of all LogItems, each row is another array list
   *         of strings where each element is the value of the columns in their
   *         respective order: id, date, type, info
   * @throws PersistenceException Thrown if there is any error in the database
   *           or the arguments are incorrect
   */
  public static ArrayList<ArrayList<String>> selectAll(Context iC)
      throws PersistenceException
  {
    try
    {
      ArrayList<ArrayList<String>> results = new ArrayList<ArrayList<String>>();
      DbRegistry wLeHelper = DbRegistry.getInstance(iC);

      SQLiteDatabase wDatabase = wLeHelper.getReadableDatabase();

      Cursor cursor = wDatabase.rawQuery(SELECT_ALL, null);

      if (cursor.moveToFirst())
      {
        do
        {
          ArrayList<String> reading = new ArrayList<String>(10);

          Long id = cursor.getLong(0);
          Long date = cursor.getLong(1);
          String type = cursor.getString(2);
          String info = cursor.getString(3);

          reading.add(id.toString());
          reading.add(date.toString());
          reading.add(type);
          reading.add(info);

          results.add(reading);
        } 
        while (cursor.moveToNext());
      }

      cursor.close();
      wDatabase.close();
      wLeHelper.close();
      return results;
    } 
    catch (Exception iE)
    {
      throw new PersistenceException(iE.getMessage());
    }
  }
  
  /**
   * Returns the next available ID
   * 
   * @param c The context, usually the application context. Cannot be null.
   * @return The next id that can be used for inserting a new log item.
   * @throws PersistenceException Thrown if there is any error in the database
   *           or the arguments are incorrect
   */
  
  public static long getNextAvailableId(Context iC) throws PersistenceException
  {
    if (!mNextIdSet)
    {
      try
      {
      	DbRegistry wLeHelper = DbRegistry.getInstance(iC);
        SQLiteDatabase wDatabase = wLeHelper.getReadableDatabase();

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
          wLeHelper.close();
          throw new PersistenceException("Failed to compute get the maximum ID from the table.");
        }

        cursor.close();
        wDatabase.close();
        wLeHelper.close();
      } catch (Exception iE)
      {
        throw new PersistenceException(iE.getMessage());
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
   * @return The array list of all log items, each log item is another array list
   *         of strings where each element is the value of the columns in their
   *         respective order: id, date, type, info
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
      DbRegistry wLeHelper = DbRegistry.getInstance(iC);

      SQLiteDatabase wDatabase = wLeHelper.getReadableDatabase();

      String[] args = { iLeftBound, iRightBound };
      Cursor cursor = wDatabase.rawQuery(SELECT_BETWEEN_DATES_INCLUSIVE, args);

      if (cursor.moveToFirst())
      {
        do
        {
          ArrayList<String> reading = new ArrayList<String>(10);

          Long id = cursor.getLong(0);
          Long date = cursor.getLong(1);
          String type = cursor.getString(2);
          String info = cursor.getString(3);

          reading.add(id.toString());
          reading.add(date.toString());
          reading.add(type);
          reading.add(info);
         

          results.add(reading);
        } while (cursor.moveToNext());
      }

      cursor.close();
      wDatabase.close();
      wLeHelper.close();
      return results;
    } 
    catch (Exception iE)
    {
      throw new PersistenceException(iE.getMessage());
    }
  }
  
  /**
   * This method inserts one LogItem entry into the DB.
   * 
   * @param values The ArrayList of a String exactly matching the columns of in
   *          their respective order: id, date, type, info
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
      wValues.put("event_date", Long.valueOf(iValues.get(1)));
      wValues.put("event_type", iValues.get(2));
      wValues.put("event_info", iValues.get(3));

      long wNewRowId = wDatabase.insert(TABLE, null, wValues);

      if (wNewRowId == -1)
        throw new PersistenceException("Insertion to the DB has failed. Id duplicated?");

      wDatabase.close();
      wHelper.close();
    } 
    catch (Exception iE)
    {
      throw new PersistenceException(iE.getMessage());
    }
  }
  
  public static void delete(Context iC) throws PersistenceException
  {
  	 DbRegistry wHelper = DbRegistry.getInstance(iC);
     SQLiteDatabase wDatabase = wHelper.getWritableDatabase();
     wDatabase.execSQL(DELETE);
  }
  
}

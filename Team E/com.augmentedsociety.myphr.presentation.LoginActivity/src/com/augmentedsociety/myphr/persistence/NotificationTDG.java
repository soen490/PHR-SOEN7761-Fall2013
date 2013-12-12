package com.augmentedsociety.myphr.persistence;

import java.util.ArrayList;

import com.augmentedsociety.myphr.barcode.PatientInfo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * Table Data Gateway for Notifications
 * 
 * @author Yuri Kitaev
 * 
 */
public class NotificationTDG
{
	private static final String TABLE = "notification";
	
  private static final String SELECT_ALL = 
  		"SELECT r.id, r.first_occurrence, " 
      + "r.recurring, r.repeat_interval, r.enabled, r.title FROM "
      + TABLE + " as r WHERE   p_id=?;";
  
  private static final String SELECT_ONE = 
  		"SELECT r.id, r.first_occurrence, " 
      + "r.recurring, r.repeat_interval, r.enabled, r.title FROM "
      + TABLE + " as r WHERE id=?;";
  
  private static final String SELECT_MAX_ID = "SELECT max(id) from "
      + TABLE + ";";
  
  private static long mNextId;
  
  private static boolean mNextIdSet = false;

  /**
   * Selects all Notifications from the database
   * 
   * @param iContext The context, usually the application context. Cannot be null.
   * @return The array list of all notifications, each row is another array list
   *         of strings where each element is the value of the columns in their
   *         respective order: id, first_occurrence, recurring (0/1), 
   *         repeat_interval, enabled (0/1), title
   * @throws PersistenceException Thrown if there is any error in the database
   *           or the arguments are incorrect
   */
  public static ArrayList<ArrayList<String>> selectAll(Context iContext)
      throws PersistenceException
  {
    try
    {
      ArrayList<ArrayList<String>> results = new ArrayList<ArrayList<String>>(100);
      DbRegistry wBpHelper = DbRegistry.getInstance(iContext);

      SQLiteDatabase wDatabase = wBpHelper.getReadableDatabase();
      String[] args = { String.valueOf(PatientInfo.mID) };
      Cursor cursor = wDatabase.rawQuery(SELECT_ALL, args);

      if (cursor.moveToFirst())
      {
        do
        {
          ArrayList<String> reading = new ArrayList<String>(10);

          Long id = cursor.getLong(0);
          Long firstOccurrence = cursor.getLong(1);
          int recurring = cursor.getInt(2);
          long repeatInterval = cursor.getLong(3);
          int enabled = cursor.getInt(4);
          String title = cursor.getString(5);

          reading.add(id.toString());
          reading.add(firstOccurrence.toString());
          reading.add(String.valueOf(recurring));
          reading.add(String.valueOf(repeatInterval));
          reading.add(String.valueOf(enabled));
          reading.add(title);

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
  
  /**
   * Selects on single row by the specified id
   * @param iNotificationId The notification ID to be searched for
   * @param iContext
   * @return
   * @throws PersistenceException
   */
  public static ArrayList<String> select(long iNotificationId, Context iContext)
      throws PersistenceException
  {
    try
    {
      ArrayList<String> results = new ArrayList<String>(6);
      DbRegistry wBpHelper = DbRegistry.getInstance(iContext);

      SQLiteDatabase wDatabase = wBpHelper.getReadableDatabase();

      String[] args = { String.valueOf(iNotificationId) };
      Cursor cursor = wDatabase.rawQuery(SELECT_ONE, args);

      if (cursor.moveToFirst())
      {
          Long id = cursor.getLong(0);
          Long firstOccurrence = cursor.getLong(1);
          int recurring = cursor.getInt(2);
          long repeatInterval = cursor.getLong(3);
          int enabled = cursor.getInt(4);
          String title = cursor.getString(5);

          results.add(id.toString());
          results.add(firstOccurrence.toString());
          results.add(String.valueOf(recurring));
          results.add(String.valueOf(repeatInterval));
          results.add(String.valueOf(enabled));
          results.add(title);
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

  /**
   * Returns the next available ID
   * 
   * @param iContext The context, usually the application context. Cannot be null.
   * @return The next id that can be used for inserting a new notification
   * @throws PersistenceException Thrown if there is any error in the database
   *           or the arguments are incorrect
   */
  public static long getNextAvailableId(Context iContext) throws PersistenceException
  {
    if (!mNextIdSet)
    {
      try
      {
        DbRegistry wBpHelper = DbRegistry.getInstance(iContext);
        SQLiteDatabase wDatabase = wBpHelper.getReadableDatabase();

        Cursor cursor = wDatabase.rawQuery(SELECT_MAX_ID, null);
        if (cursor.moveToFirst())
        {
          if (cursor.isNull(0))
          {
            // No rows in the table
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
      } catch (Exception iE)
      {
        throw new PersistenceException(iE.getMessage());
      }
    }
    return mNextId++;
  }

  /**
   * This method insets one single Notification into the DB.
   * 
   * @param values The ArrayList of a String exactly matching the columns of in
   *          their respective order: id, first_occurrence, recurring (0/1), repeat_interval, enabled (0/1), title
   * @param iContext The context, usually the application context. Cannot be null.
   * @throws PersistenceException Thrown if there is any error in the database
   *           or the arguments are incorrect
   */
  public static void insert(ArrayList<String> iValues, Context iContext) throws PersistenceException
  {
    try
    {
      if (iValues.size() != 6)
        throw new PersistenceException("The array of values must have 6 entries.");
      else if (iContext == null)
        throw new NullPointerException("The context you have specified is NULL");

      DbRegistry wHelper = DbRegistry.getInstance(iContext);
      SQLiteDatabase wDatabase = wHelper.getWritableDatabase();

      ContentValues wValues = new ContentValues();

      wValues.put("id", Long.valueOf(iValues.get(0)));
      wValues.put("first_occurrence", Long.valueOf(iValues.get(1)));
      wValues.put("recurring", Integer.valueOf(iValues.get(2)));
      wValues.put("repeat_interval", Long.valueOf(iValues.get(3)));
      wValues.put("enabled", Integer.valueOf(iValues.get(4)));
      wValues.put("title", iValues.get(5));
      wValues.put("p_id",Long.valueOf(PatientInfo.mID) );
      
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
  
  /**
   * Updates one single Notification row.
   * 
   * @param values The ArrayList of a String exactly matching the columns of in
   *          their respective order: id, first_occurrence, recurring (0/1), 
   *          repeat_interval, enabled (0/1), title
   * @param iContext The context, usually the application context. Cannot be null.
   * @throws PersistenceException Thrown if there is any error in the database
   *           or the arguments are incorrect
   */
  public static int update(ArrayList<String> iValues, Context iContext) 
  		                                              throws PersistenceException
  {
    try
    {
      if (iValues.size() != 6)
        throw new PersistenceException("The array of values must have 6 entries.");
      else if (iContext == null)
        throw new NullPointerException("The context you have specified is NULL");

      DbRegistry wHelper = DbRegistry.getInstance(iContext);
      SQLiteDatabase wDatabase = wHelper.getWritableDatabase();

      ContentValues wValues = new ContentValues();

      wValues.put("id", Long.valueOf(iValues.get(0)));
      wValues.put("first_occurrence", Long.valueOf(iValues.get(1)));
      wValues.put("recurring", Integer.valueOf(iValues.get(2)));
      wValues.put("repeat_interval", Long.valueOf(iValues.get(3)));
      wValues.put("enabled", Integer.valueOf(iValues.get(4)));
      wValues.put("title", iValues.get(5));
      wValues.put("p_id",Long.valueOf(PatientInfo.mID) );
      
      int wRowsUpdated = 
      		wDatabase.update(TABLE, wValues, "id = ?", new String[]{iValues.get(0)});

      wDatabase.close();
      wHelper.close();
      
      return wRowsUpdated;
    } 
    catch (Exception iE)
    {
      throw new PersistenceException(iE.getMessage());
    }
  }
  
  /**
   * Deletes one  Notification row.
   * 
   * @param iID The id of the notification to delete
   * @param iContext The context, usually the application context. Cannot be null.
   * @throws PersistenceException Thrown if there is any error in the database
   *           or the arguments are incorrect
   */
  public static int delete (long iID, Context iContext) throws PersistenceException
  {
    try
    {
      DbRegistry wHelper = DbRegistry.getInstance(iContext);
      SQLiteDatabase wDatabase = wHelper.getWritableDatabase();

      ContentValues wValues = new ContentValues();

      wValues.put("id", iID);
      
      int wRowsDeleted = wDatabase.delete(TABLE, "id=?", new String[] {String.valueOf(iID)});

      wDatabase.close();
      wHelper.close();
      
      return wRowsDeleted;
    } 
    catch (Exception iE)
    {
      throw new PersistenceException(iE.getMessage());
    }
  }

}


package com.augmentedsociety.myphr.persistence;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * MedicalHistory TDG
 * 
 * @author Roger Makram
 *
 */

public class MedicalHistoryTDG
{
private static final String TABLE = "medicalHistory";
	
  private static final String SELECT_ALL = 
  		"SELECT r.id, r.date, r.description, " 
      + "r.location FROM "
      + TABLE + " as r;";
  
  private static final String SELECT_ONE = 
  		"SELECT r.id, r.date, r.description, " 
  	      + "r.location FROM "
      + TABLE + " as r WHERE id=?;";
  
  private static final String SELECT_MAX_ID = "SELECT max(id) from "
      + TABLE + ";";
  
  private static long mNextId;
  
  private static boolean mNextIdSet = false;

  /**
   * Selects all MedicalHistory from the database
   * 
   * @param iContext The context, usually the application context. Cannot be null.
   * @return The array list of all MedicalHistory, each row is another array list
   *         of strings where each element is the value of the columns in their
   *         respective order:  id, date, description, location
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

      Cursor cursor = wDatabase.rawQuery(SELECT_ALL, null);

      if (cursor.moveToFirst())
      {
        do
        {
          ArrayList<String> reading = new ArrayList<String>(20);

          Long id = cursor.getLong(0);
          Long date = cursor.getLong(1);
          String description = cursor.getString(2);
          String location = cursor.getString(3);
          
          reading.add(id.toString());
          reading.add(date.toString());
          reading.add(description);
          reading.add(location);

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
   * @param iMedicalHistoryId The MedicalHistory ID to be searched for
   * @param iContext
   * @return
   * @throws PersistenceException
   */
  public static ArrayList<String> select(long iMedicalHistoryId, Context iContext)
      throws PersistenceException
  {
    try
    {
      ArrayList<String> results = new ArrayList<String>(20);
      DbRegistry wBpHelper = DbRegistry.getInstance(iContext);

      SQLiteDatabase wDatabase = wBpHelper.getReadableDatabase();

      String[] args = { String.valueOf(iMedicalHistoryId) };
      Cursor cursor = wDatabase.rawQuery(SELECT_ONE, args);

      if (cursor.moveToFirst())
      {
      	Long id = cursor.getLong(0);
        Long date = cursor.getLong(1);
        String description = cursor.getString(2);
        String location = cursor.getString(3);
        
        results.add(id.toString());
        results.add(date.toString());
        results.add(description);
        results.add(location);
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
   * @return The next id that can be used for inserting a new MedicalHistory
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
   * Inserts one single MedicalHistory row.
   * 
   * @param values The ArrayList of a String exactly matching the columns of in
   *        their respective order: id, date, description, location
   * @param iContext The context, usually the application context. Cannot be null.
   * @throws PersistenceException Thrown if there is any error in the database
   *           or the arguments are incorrect
   */
  public static void insert(ArrayList<String> iValues, Context iContext) throws PersistenceException
  {
    try
    {
      if (iValues.size() != 4)
        throw new PersistenceException("The array of values must have 4 entries.");
      else if (iContext == null)
        throw new NullPointerException("The context you have specified is NULL");

      DbRegistry wHelper = DbRegistry.getInstance(iContext);
      SQLiteDatabase wDatabase = wHelper.getWritableDatabase();

      ContentValues wValues = new ContentValues();

      wValues.put("id", Long.valueOf(iValues.get(0)));
      wValues.put("date", Long.valueOf(iValues.get(1)));
      wValues.put("description", iValues.get(2));
      wValues.put("location", iValues.get(3));
      
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
   * Updates one single MedicalHistory row.
   * 
   * @param values The ArrayList of a String exactly matching the columns of in
   *        their respective order: id, date, description, location 
   * @param iContext The context, usually the application context. Cannot be null.
   * @throws PersistenceException Thrown if there is any error in the database
   *           or the arguments are incorrect
   */
  public static int update(ArrayList<String> iValues, Context iContext) 
  		                                              throws PersistenceException
  {
    try
    {
      if (iValues.size() != 4)
        throw new PersistenceException("The array of values must have 4 entries.");
      else if (iContext == null)
        throw new NullPointerException("The context you have specified is NULL");

      DbRegistry wHelper = DbRegistry.getInstance(iContext);
      SQLiteDatabase wDatabase = wHelper.getWritableDatabase();

      ContentValues wValues = new ContentValues();

      wValues.put("id", Long.valueOf(iValues.get(0)));
      wValues.put("date", Long.valueOf(iValues.get(1)));
      wValues.put("description", iValues.get(2));
      wValues.put("location", iValues.get(3));
      
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
   * Deletes one MedicalHistory row.
   * 
   * @param iID The id of the MedicalHistory to delete
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

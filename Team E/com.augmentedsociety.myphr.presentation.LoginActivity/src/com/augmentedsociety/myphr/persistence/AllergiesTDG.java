package com.augmentedsociety.myphr.persistence;

import java.util.ArrayList;

import com.augmentedsociety.myphr.barcode.PatientInfo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * Create new allergy activity
 * 
 * @author Roger Makram
 *
 */

public class AllergiesTDG
{
private static final String TABLE = "allergy";
	
  private static final String SELECT_ALL = 
  		"SELECT r.id, r.allergy, " 
      + "r.reaction, r.severity FROM "
      + TABLE + " as r WHERE   p_id=?;";
  
  private static final String SELECT_ONE = 
  		"SELECT r.id, r.allergy, " 
  	      + "r.reaction, r.severity FROM "
      + TABLE + " as r WHERE id=?;";
  
  private static final String SELECT_MAX_ID = "SELECT max(id) from "
      + TABLE + ";";
  
  private static long mNextId;
  
  private static boolean mNextIdSet = false;

  /**
   * Selects all Allergies from the database
   * 
   * @param iContext The context, usually the application context. Cannot be null.
   * @return The array list of all allergies, each row is another array list
   *         of strings where each element is the value of the columns in their
   *         respective order:  id, allergy, reaction, severity
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
          String allergy = cursor.getString(1);
          String reaction = cursor.getString(2);
          String severity = cursor.getString(3);

          reading.add(id.toString());
          reading.add(allergy);
          reading.add(reaction);
          reading.add(severity);

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
   * Selects one single row by the specified id
   * @param iAllergyId The allergy ID to be searched for
   * @param iContext
   * @return
   * @throws PersistenceException
   */
  public static ArrayList<String> select(long iAllergyId, Context iContext)
      throws PersistenceException
  {
    try
    {
      ArrayList<String> results = new ArrayList<String>(6);
      DbRegistry wBpHelper = DbRegistry.getInstance(iContext);

      SQLiteDatabase wDatabase = wBpHelper.getReadableDatabase();

      String[] args = { String.valueOf(iAllergyId) };
      Cursor cursor = wDatabase.rawQuery(SELECT_ONE, args);

      if (cursor.moveToFirst())
      {
      	Long id = cursor.getLong(0);
        String allergy = cursor.getString(1);
        String reaction = cursor.getString(2);
        String severity = cursor.getString(3);

        results.add(id.toString());
        results.add(allergy);
        results.add(reaction);
        results.add(severity);
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
   * @return The next id that can be used for inserting a new allergy
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
   * Inserts one single Allergy row.
   * 
   * @param values The ArrayList of a String exactly matching the columns of in
   *          their respective order: id, allergy, reaction, severity 
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
      wValues.put("allergy", iValues.get(1));
      wValues.put("reaction", iValues.get(2));
      wValues.put("severity", iValues.get(3));
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
   * Updates one single Allergy row.
   * 
   * @param values The ArrayList of a String exactly matching the columns of in
   *          their respective order: id, allergy, reaction, severity 
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
      wValues.put("allergy", iValues.get(1));
      wValues.put("reaction", iValues.get(2));
      wValues.put("severity", iValues.get(3));
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
   * Deletes one  Allergy row.
   * 
   * @param iID The id of the allergy to delete
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

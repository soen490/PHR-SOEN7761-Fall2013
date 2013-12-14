package com.augmentedsociety.myphr.persistence;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * Immunization TDG
 * 
 * @author Rajan Jayakumar
 *
 */

public class ImmunizationsTDG
{
private static final String TABLE = "immunizations";
	
  private static final String SELECT_ALL = 
  		"SELECT r.id, r.type, r.name, " 
      + "r.manufacturer, r.lot_number, "
      + "r.route, r.posology, r.date, "
      + "r.location, r.details, r.comments FROM "
      + TABLE + " as r;";
  
  private static final String SELECT_ONE = 
  		"SELECT r.id, r.type, r.name, " 
  	  + "r.manufacturer, r.lot_number, "
  	  + "r.route, r.posology, r.date, "
  	  + "r.location, r.details, r.comments FROM "
      + TABLE + " as r WHERE id=?;";
  
  private static final String SELECT_MAX_ID = "SELECT max(id) from "
      + TABLE + ";";
  
  private static long mNextId;
  
  private static boolean mNextIdSet = false;

  /**
   * Selects all Immunizations from the database
   * 
   * @param iContext The context, usually the application context. Cannot be null.
   * @return The array list of all immunizations, each row is another array list
   *         of strings where each element is the value of the columns in their
   *         respective order:  id, type, name, manufacturer, lot number, route
   *         posology, date, location, details, comments
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
          String type = cursor.getString(1);
          String name = cursor.getString(2);
          String manufacturer = cursor.getString(3);
          String lot_number = cursor.getString(4);
          String route = cursor.getString(5);
          String posology = cursor.getString(6);
          Long date = cursor.getLong(7);
          String location = cursor.getString(8);
          String details = cursor.getString(9);
          String comments = cursor.getString(10);
          
          reading.add(id.toString());
          reading.add(type);
          reading.add(name);
          reading.add(manufacturer);
          reading.add(lot_number);
          reading.add(route);
          reading.add(posology);
          reading.add(date.toString());
          reading.add(location);
          reading.add(details);
          reading.add(comments);

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
   * @param iImmunizationId The immunization ID to be searched for
   * @param iContext
   * @return
   * @throws PersistenceException
   */
  public static ArrayList<String> select(long iImmunizationId, Context iContext)
      throws PersistenceException
  {
    try
    {
      ArrayList<String> results = new ArrayList<String>(20);
      DbRegistry wBpHelper = DbRegistry.getInstance(iContext);

      SQLiteDatabase wDatabase = wBpHelper.getReadableDatabase();

      String[] args = { String.valueOf(iImmunizationId) };
      Cursor cursor = wDatabase.rawQuery(SELECT_ONE, args);

      if (cursor.moveToFirst())
      {
      	Long id = cursor.getLong(0);
        String type = cursor.getString(1);
        String name = cursor.getString(2);
        String manufacturer = cursor.getString(3);
        String lot_number = cursor.getString(4);
        String route = cursor.getString(5);
        String posology = cursor.getString(6);
        Long date = cursor.getLong(7);
        String location = cursor.getString(8);
        String details = cursor.getString(9);
        String comments = cursor.getString(10);
        
        results.add(id.toString());
        results.add(type);
        results.add(name);
        results.add(manufacturer);
        results.add(lot_number);
        results.add(route);
        results.add(posology);
        results.add(date.toString());
        results.add(location);
        results.add(details);
        results.add(comments);
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
   * @return The next id that can be used for inserting a new immunization
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
   * Inserts one single Immunization row.
   * 
   * @param values The ArrayList of a String exactly matching the columns of in
   *        their respective order: id, type, name, manufacturer, lot number, route
   *        posology, date, location, details, comments
   * @param iContext The context, usually the application context. Cannot be null.
   * @throws PersistenceException Thrown if there is any error in the database
   *           or the arguments are incorrect
   */
  public static void insert(ArrayList<String> iValues, Context iContext) throws PersistenceException
  {
    try
    {
      if (iValues.size() != 11)
        throw new PersistenceException("The array of values must have 11 entries.");
      else if (iContext == null)
        throw new NullPointerException("The context you have specified is NULL");

      DbRegistry wHelper = DbRegistry.getInstance(iContext);
      SQLiteDatabase wDatabase = wHelper.getWritableDatabase();

      ContentValues wValues = new ContentValues();

      wValues.put("id", Long.valueOf(iValues.get(0)));
      wValues.put("type", iValues.get(1));
      wValues.put("name", iValues.get(2));
      wValues.put("manufacturer", iValues.get(3));
      wValues.put("lot_number", iValues.get(4));
      wValues.put("route", iValues.get(5));
      wValues.put("posology", iValues.get(6));
      wValues.put("date", Long.valueOf(iValues.get(7)));
      wValues.put("location", iValues.get(8));
      wValues.put("details", iValues.get(9));
      wValues.put("comments", iValues.get(10));
      
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
   * Updates one single Immunization row.
   * 
   * @param values The ArrayList of a String exactly matching the columns of in
   *        their respective order: id, type, name, manufacturer, lot number, route
   *        posology, date, location, details, comments 
   * @param iContext The context, usually the application context. Cannot be null.
   * @throws PersistenceException Thrown if there is any error in the database
   *           or the arguments are incorrect
   */
  public static int update(ArrayList<String> iValues, Context iContext) 
  		                                              throws PersistenceException
  {
    try
    {
      if (iValues.size() != 11)
        throw new PersistenceException("The array of values must have 11 entries.");
      else if (iContext == null)
        throw new NullPointerException("The context you have specified is NULL");

      DbRegistry wHelper = DbRegistry.getInstance(iContext);
      SQLiteDatabase wDatabase = wHelper.getWritableDatabase();

      ContentValues wValues = new ContentValues();

      wValues.put("id", Long.valueOf(iValues.get(0)));
      wValues.put("type", iValues.get(1));
      wValues.put("name", iValues.get(2));
      wValues.put("manufacturer", iValues.get(3));
      wValues.put("lot_number", iValues.get(4));
      wValues.put("route", iValues.get(5));
      wValues.put("posology", iValues.get(6));
      wValues.put("date", Long.valueOf(iValues.get(7)));
      wValues.put("location", iValues.get(8));
      wValues.put("details", iValues.get(9));
      wValues.put("comments", iValues.get(10));
      
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
   * Deletes one Immunization row.
   * 
   * @param iID The id of the immunization to delete
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

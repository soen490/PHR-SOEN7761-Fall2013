package com.augmentedsociety.myphr.persistence;

import java.util.ArrayList;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * Table Data Gateway for Blood Pressure readings
 * 
 * @author Yuri Kitaev
 * 
 */
public class BloodPressureTDG extends MeasurementTDG
{
	public static final String TABLE = "blood_pressure";
	
  private static final String SELECT_ALL = 
  		"SELECT r.id, r.when_taken, r.source, r.systolic, " +
  		"r.diastolic, r.heartrate FROM " + TABLE + " as r;";
  
  private static final String SELECT_BETWEEN_DATES_INCLUSIVE = 
  		"SELECT r.id, r.when_taken, r.source, " 
      + "r.systolic, r.diastolic, r.heartrate FROM "
      + TABLE
      + " as r WHERE r.when_taken >= ? AND r.when_taken <= ?;";
  
  private static final String SELECT_MAX_ID = "SELECT max(id) from "
      + TABLE + ";";
  
  
	private static final String KEY_ID = "id";
  private static long mNextId;
  private static boolean mNextIdSet = false;

  /**
   * Selects all Blood Pressure readings from the database
   * 
   * @param iContext The context, usually the application context. Cannot be null.
   * @return The array list of all readings, each reading is another array list
   *         of strings where each element is the value of the columns in their
   *         respective order: id, when_taken, source, systolic, diastolic,
   *         heartrate
   * @throws PersistenceException Thrown if there is any error in the database
   *           or the arguments are incorrect
   */
  public static ArrayList<ArrayList<String>> selectAll(Context iContext)
      throws PersistenceException
  {
    try
    {
      ArrayList<ArrayList<String>> results = new ArrayList<ArrayList<String>>(100);
      DbRegistry wBpHelper =  DbRegistry.getInstance(iContext);

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
          int systolic = cursor.getInt(3);
          int diastolic = cursor.getInt(4);
          int heartrate = cursor.getInt(5);

          reading.add(id.toString());
          reading.add(date.toString());
          reading.add(String.valueOf(source));
          reading.add(String.valueOf(systolic));
          reading.add(String.valueOf(diastolic));
          reading.add(String.valueOf(heartrate));

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
   * Returns the next available ID
   * 
   * @param iContext The context, usually the application context. Cannot be null.
   * @return The next id that can be used for inserting a new reading
   * @throws PersistenceException Thrown if there is any error in the database
   *           or the arguments are incorrect
   */
  public static long getNextAvailableId(Context iContext) throws PersistenceException
  {
    if (!mNextIdSet)
    {
      try
      {
        DbRegistry wBpHelper = DbRegistry.getInstance(iContext);;
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
   * @param iLeftBound The earliest date, epoch time in milliseconds.
   * @param iRightBound The latest date, epoch time in milliseconds.
   * @param iContext The application context
   * @return The array list of all readings, each reading is another array list
   *         of strings where each element is the value of the columns in their
   *         respective order: id, when_taken, source, systolic, diastolic,
   *         heartrate
   * @throws PersistenceException Thrown if there is any error in the database
   *           or the arguments are incorrect
   */
  public static ArrayList<ArrayList<String>> selectBetweenDatesInclusive(
      String iLeftBound, String iRightBound, Context iContext)
      throws PersistenceException
  {
    try
    {
      ArrayList<ArrayList<String>> results = new ArrayList<ArrayList<String>>(100);
      DbRegistry wBpHelper = DbRegistry.getInstance(iContext);;

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
          int systolic = cursor.getInt(3);
          int diastolic = cursor.getInt(4);
          int heartrate = cursor.getInt(5);

          reading.add(id.toString());
          reading.add(date.toString());
          reading.add(String.valueOf(source));
          reading.add(String.valueOf(systolic));
          reading.add(String.valueOf(diastolic));
          reading.add(String.valueOf(heartrate));

          results.add(reading);
        } while (cursor.moveToNext());
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
   * This method insets one single Blood Pressure reading into the DB.
   * 
   * @param iValues The ArrayList of a String exactly matching the columns of in
   *          their respective order: id, when_taken, source, systolic,
   *          diastolic, heartrate
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

      ContentValues wValues = new ContentValues();

      wValues.put("id", Long.valueOf(iValues.get(0)));
      wValues.put("when_taken", iValues.get(1));
      wValues.put("source", iValues.get(2));
      wValues.put("systolic", iValues.get(3));
      wValues.put("diastolic", iValues.get(4));
      wValues.put("heartrate", iValues.get(5));

      long wNewRowId = wHelper.getWritableDatabase()
      		                    .insert(TABLE, null, wValues);

      if (wNewRowId == -1)
        throw new PersistenceException("Insertion to the DB has failed. Id duplicated?");

      wHelper.close();
    } 
    catch (Exception iE)
    {
      throw new PersistenceException(iE.getMessage());
    }
  }

	private static void delete(ArrayList<String> iValues, Context iC)
			throws PersistenceException
	{
		try
		{
			if (iValues.size() != 4)
				throw new PersistenceException(
						"The array of values must have 4 entries");
			else if (iC == null)
				throw new NullPointerException("The context you have specified is NULL");

			DbRegistry wHelper = DbRegistry.getInstance(iC);
			SQLiteDatabase wDatabase = wHelper.getWritableDatabase();



			int test = wDatabase.delete(TABLE, KEY_ID + "=" + String.valueOf(iValues.get(0)),
					null);
			
		} catch (Exception e)
		{
			throw new PersistenceException(e.getMessage());
		}
	}
  
	/**
	 * Deletes all multiple entries for the given ArrayList in the table
	 * 
	 * @param iValues
	 *          which should be checked on redundant entries
	 * @param iC
	 *          Context
	 */
//	public static void removeRedundantEntries(ArrayList<String> iValues,
//			Context iC)
//	{
//		ArrayList<ArrayList<String>> dbResult = null;
//		try
//		{
//			dbResult = selectAll(iC);
//		} catch (PersistenceException e)
//		{
//			e.printStackTrace();
//		}
//		// check if the date of the measurement is inserted in the table yet
//		for (ArrayList<String> entry : dbResult)
//		{
//			if (entry.get(1).compareTo(iValues.get(1)) == 0
//					&& entry.get(0).compareTo(iValues.get(0)) != 0)
//			{
//				// delete the found date by the id
//				try
//				{
//					delete(entry, iC);
//				} catch (PersistenceException e)
//				{
//					e.printStackTrace();
//				}
//			}
//		}
//	}
//  
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
}

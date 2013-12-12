package com.augmentedsociety.myphr.persistence;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * Table Data Gateway for Profile
 * 
 * @author Surbhi Dubey
 * @author Roger Makram
 */
public class ProfileTDG
{
	private static final String TABLE = "user_information";

	private static final String SELECT_ALL = "SELECT r.id, r.lastName, r.firstName, r.DOB, r.phoneNumber, r.address, "
			+ "r.email, r.medicare, r.insurance, "
			+ "r.emergencyContact1, r.emergencyContact2 FROM " + TABLE + " as r;";
	
	private static final String SELECT_ONE = "SELECT r.id, r.lastName, r.firstName, r.DOB, r.phoneNumber, r.address, "
			+ "r.email, r.medicare, r.insurance, "
			+ "r.emergencyContact1, r.emergencyContact2 FROM " + TABLE + " as r WHERE id=?;";
	
	private static final String SELECT_MAX_ID = "SELECT max(id) from "
      + TABLE + ";";
  
  private static long mNextId;
  
  private static boolean mNextIdSet = false;

	/**
	 * Selects all Profile from the database
	 */
	public static ArrayList<ArrayList<String>> selectAll(Context iC)
			throws PersistenceException
	{
		try
		{
			ArrayList<ArrayList<String>> results = new ArrayList<ArrayList<String>>(10);
			DbRegistry wHelper = DbRegistry.getInstance(iC);

			SQLiteDatabase wDatabase = wHelper.getReadableDatabase();

			Cursor cursor = wDatabase.rawQuery(SELECT_ALL, null);

			if (cursor.moveToFirst())
			{
				do
				{
					ArrayList<String> reading = new ArrayList<String>(11);

					Long id = cursor.getLong(0);
					String lastName = cursor.getString(1);
					String firstName = cursor.getString(2);
					Long DOB = cursor.getLong(3);
					Long phoneNumber = cursor.getLong(4);
					String address = cursor.getString(5);
					String email = cursor.getString(6);
					String medicare = cursor.getString(7);
					String insurance = cursor.getString(8);
					String emergencyContact1 = cursor.getString(9);
					String emergencyContact2 = cursor.getString(10);


					reading.add(String.valueOf(id));
					reading.add(lastName.toString());
					reading.add(firstName.toString());
					reading.add(String.valueOf(DOB));
					reading.add(String.valueOf(phoneNumber));
					reading.add(address.toString());
					reading.add(email.toString());
					reading.add(medicare.toString());
					reading.add(insurance.toString());
					reading.add(emergencyContact1.toString());
					reading.add(emergencyContact2.toString());


					results.add(reading);
				} while (cursor.moveToNext());
			}

			cursor.close();
			wDatabase.close();
			wHelper.close();
			return results;
		} catch (Exception iE)
		{
			throw new PersistenceException(iE.getMessage());
		}
	}

	/**
	 * inserts profile inputs into the DB.
	 */
	public static void insert(ArrayList<String> iValues, Context iC)
			throws PersistenceException
	{
		try
		{
			if (iValues.size() != 11)
				throw new PersistenceException(
						"The array of values must have 11 entries.");
			else if (iC == null)
				throw new NullPointerException("The context you have specified is NULL");

			DbRegistry wHelper = DbRegistry.getInstance(iC);
			SQLiteDatabase wDatabase = wHelper.getWritableDatabase();

			ContentValues wValues = new ContentValues();

			wValues.put("id", Long.valueOf(iValues.get(0)));
			wValues.put("lastName", iValues.get(1));
			wValues.put("firstName", iValues.get(2));
			wValues.put("DOB", Long.valueOf(iValues.get(3)));
			wValues.put("phoneNumber", Long.valueOf(iValues.get(4)));
			wValues.put("address", iValues.get(5));
			wValues.put("email", iValues.get(6));
			wValues.put("medicare", iValues.get(7));
			wValues.put("insurance", iValues.get(8));
			wValues.put("emergencyContact1", iValues.get(9));
			wValues.put("emergencyContact2", iValues.get(10));

			long wNewRowId = wDatabase.insert(TABLE, null, wValues);

			if (wNewRowId == -1)
				throw new PersistenceException("Insertion to the DB has failed.");

			wDatabase.close();
			wHelper.close();
			mNextIdSet = true;
		} catch (Exception iE)
		{
			throw new PersistenceException(iE.getMessage());
		}
	}

	/**
	 * Updates profile
	 */
	public static int update(ArrayList<String> iValues, Context iContext)
			throws PersistenceException
	{
		try
		{
			if (iValues.size() != 11)
				throw new PersistenceException(
						"The array of values must have 11 entries.");
			else if (iContext == null)
				throw new NullPointerException("The context you have specified is NULL");

			DbRegistry wHelper = DbRegistry.getInstance(iContext);
			SQLiteDatabase wDatabase = wHelper.getWritableDatabase();

			ContentValues wValues = new ContentValues();

			wValues.put("id", Long.valueOf(iValues.get(0)));
			wValues.put("lastName", iValues.get(1));
			wValues.put("firstName", iValues.get(2));
			wValues.put("DOB", Long.valueOf(iValues.get(3)));
			wValues.put("phoneNumber", Long.valueOf(iValues.get(4)));
			wValues.put("address", iValues.get(5));
			wValues.put("email", iValues.get(6));
			wValues.put("medicare", iValues.get(7));
			wValues.put("insurance", iValues.get(8));
			wValues.put("emergencyContact1", iValues.get(9));
			wValues.put("emergencyContact2", iValues.get(10));

			int wRowsUpdated = wDatabase.update(TABLE, wValues, "id = ?",
					new String[]
					{ iValues.get(0) });

			wDatabase.close();
			wHelper.close();

			return wRowsUpdated;
		} catch (Exception iE)
		{
			throw new PersistenceException(iE.getMessage());
		}
	}

	public static ArrayList<String> select(long iProfileId, Context iContext) throws PersistenceException
	{
		try
		{
			ArrayList<String> reading = new ArrayList<String>(11);
			DbRegistry wHelper = DbRegistry.getInstance(iContext);

			SQLiteDatabase wDatabase = wHelper.getReadableDatabase();

			String[] args = { String.valueOf(iProfileId) };
			Cursor cursor = wDatabase.rawQuery(SELECT_ONE, args);

			if (cursor.moveToFirst())
			{
				do
				{
					

					Long id = cursor.getLong(0);
					String lastName = cursor.getString(1);
					String firstName = cursor.getString(2);
					Long DOB = cursor.getLong(3);
					Long phoneNumber = cursor.getLong(4);
					String address = cursor.getString(5);
					String email = cursor.getString(6);
					String medicare = cursor.getString(7);
					String insurance = cursor.getString(8);
					String emergencyContact1 = cursor.getString(9);
					String emergencyContact2 = cursor.getString(10);


					reading.add(String.valueOf(id));
					reading.add(lastName.toString());
					reading.add(firstName.toString());
					reading.add(String.valueOf(DOB));
					reading.add(String.valueOf(phoneNumber));
					reading.add(address.toString());
					reading.add(email.toString());
					reading.add(medicare.toString());
					reading.add(insurance.toString());
					reading.add(emergencyContact1.toString());
					reading.add(emergencyContact2.toString());
				} while (cursor.moveToNext());
			}

			cursor.close();
			wDatabase.close();
			wHelper.close();
			return reading;
		} catch (Exception iE)
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
            mNextId = maxId;
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
    return mNextId;
  }

	/**
   * Returns the current available ID
   * 
   * @param iContext The context, usually the application context. Cannot be null.
   * @return The next id that can be used for inserting a new immunization
   * @throws PersistenceException Thrown if there is any error in the database
   *           or the arguments are incorrect
   */
  public static long getCurrentId(Context iContext) throws PersistenceException
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
            mNextId = 0;
          } 
          else
          {
            Long maxId = cursor.getLong(0);
            mNextId = maxId;
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
    return mNextId;
  }
  

}

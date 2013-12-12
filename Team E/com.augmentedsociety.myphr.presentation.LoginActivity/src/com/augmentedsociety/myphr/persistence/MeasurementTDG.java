package com.augmentedsociety.myphr.persistence;

import java.util.ArrayList;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public abstract class MeasurementTDG
{

	private static final String KEY_ID = "id";
	public static String TABLE = "";

	private static void delete(ArrayList<String> iValues, Context iC)
			throws PersistenceException
	{
		try
		{
			if ((iValues.size() != 4 && iValues.size() != 6))
				throw new PersistenceException(
						"The array of values must have either 4 or 6 entries");
			else if (iC == null)
				throw new NullPointerException("The context you have specified is NULL");

			DbRegistry wHelper = DbRegistry.getInstance(iC);
			SQLiteDatabase wDatabase = wHelper.getWritableDatabase();

			int test = wDatabase.delete(TABLE,
					KEY_ID + "=" + String.valueOf(iValues.get(0)), null);

		} catch (Exception e)
		{
			throw new PersistenceException(e.getMessage());
		}
	}

	public static ArrayList<ArrayList<String>> selectAll(Context iContext)
			throws PersistenceException
	{
		try
		{

		} catch (Exception iE)
		{
			throw new PersistenceException(iE.getMessage());
		}
		return new ArrayList<ArrayList<String>>();
	}

	/**
	 * Deletes all multiple entries for the given ArrayList in the table
	 * 
	 * @param iValues
	 *          which should be checked on redundant entries
	 * @param iC
	 *          Context
	 */
	public static void removeRedundantEntries(ArrayList<String> iValues,
			Context iC, ArrayList<ArrayList<String>> dbResult)
	{
		// dbResult = selectAll(iC);
		// check if the date of the measurement is inserted in the table yet
		for (ArrayList<String> entry : dbResult)
		{
			if (entry.get(1).compareTo(iValues.get(1)) == 0
					&& entry.get(0).compareTo(iValues.get(0)) != 0)
			{
				// delete the found date by the id
				try
				{
					delete(entry, iC);
				} catch (PersistenceException e)
				{
					e.printStackTrace();
				}
			}
		}
	}
}

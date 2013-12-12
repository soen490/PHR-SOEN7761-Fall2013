package com.augmentedsociety.myphr.persistence;

import java.util.ArrayList;

import com.augmentedsociety.myphr.barcode.PatientInfo;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * Table Data Gateway for the date of all measurements
 * 
 * @author Johannes Lange
 * 
 */
public class RecordDateTDG extends MeasurementTDG
{
	public static final String TABLE_BLOOD_PRESSURE = "blood_pressure";
	public static final String TABLE_BLOOD_SUGAR = "blood_sugar";
	public static final String TABLE_WEIGHT = "weight";
	public static final String TABLE_O2 = "oxygen_saturation";
	public static final String TABLE_TEMPERATURE = "temperature";

	private static final String SELECT_ALL = "SELECT r1.when_taken " + "FROM "
			+ TABLE_BLOOD_PRESSURE + " r1" + " UNION SELECT r2.when_taken " + "FROM "
			+ TABLE_BLOOD_SUGAR + " r2" + " UNION SELECT r3.when_taken " + "FROM "
			+ TABLE_WEIGHT + " r3" + " UNION SELECT r4.when_taken " + "FROM "
			+ TABLE_O2 + " r4" + " UNION SELECT r5.when_taken " + "FROM "
			+ TABLE_TEMPERATURE + " r5 WHERE p_id = ? ;";

	private static final String SELECT_ALL_ID_SOURCE = "SELECT r1.id, r1.when_taken, '1' "
			+ "FROM "
			+ TABLE_BLOOD_PRESSURE
			+ " r1"
			+ " UNION SELECT r2.id, r2.when_taken, '2' "
			+ "FROM "
			+ TABLE_BLOOD_SUGAR
			+ " r2"
			+ " UNION SELECT r3.id, r3.when_taken, '3' "
			+ "FROM "
			+ TABLE_WEIGHT
			+ " r3"
			+ " UNION SELECT r4.id, r4.when_taken, '4' "
			+ "FROM "
			+ TABLE_O2
			+ " r4"
			+ " UNION SELECT r5.id, r5.when_taken, '5' "
			+ "FROM "
			+ TABLE_TEMPERATURE + " r5 WHERE p_id = ? ;";

	/**
	 * Selects all Blood Pressure readings from the database
	 * 
	 * @param iContext
	 *          The context, usually the application context. Cannot be null.
	 * @return The array list of all readings, each reading is another array list
	 *         of strings where each element is the when_taken column in the table
	 * @throws PersistenceException
	 *           Thrown if there is any error in the database or the arguments are
	 *           incorrect
	 */
	public static ArrayList<ArrayList<String>> selectAll(Context iContext)
			throws PersistenceException
	{
		try
		{
			ArrayList<ArrayList<String>> results = new ArrayList<ArrayList<String>>(
					100);
			DbRegistry wBpHelper = DbRegistry.getInstance(iContext);

			SQLiteDatabase wDatabase = wBpHelper.getReadableDatabase();
			 String[] args = { String.valueOf(PatientInfo.mID) };
	      Cursor cursor = wDatabase.rawQuery(SELECT_ALL, args);
			

			if (cursor.moveToFirst())
			{
				do
				{
					ArrayList<String> reading = new ArrayList<String>(10);

					// Long id = cursor.getLong(0);
					Long date = cursor.getLong(0);

					// reading.add(id.toString());
					reading.add(date.toString());

					results.add(reading);
				} while (cursor.moveToNext());
			}

			cursor.close();
			wDatabase.close();
			wBpHelper.close();
			return results;
		} catch (Exception iE)
		{
			throw new PersistenceException(iE.getMessage());
		}
	}

	public static ArrayList<ArrayList<String>> selectAllIDSource(Context iContext)
			throws PersistenceException
	{
		try
		{
			ArrayList<ArrayList<String>> results = new ArrayList<ArrayList<String>>(
					100);
			DbRegistry wBpHelper = DbRegistry.getInstance(iContext);

			SQLiteDatabase wDatabase = wBpHelper.getReadableDatabase();
			String[] args = { String.valueOf(PatientInfo.mID) };
			Cursor cursor = wDatabase.rawQuery(SELECT_ALL_ID_SOURCE, args);

			if (cursor.moveToFirst())
			{
				do
				{
					ArrayList<String> reading = new ArrayList<String>(10);

					Long id = cursor.getLong(0);
					Long date = cursor.getLong(1);
					Long source = cursor.getLong(2);

					reading.add(id.toString());
					reading.add(date.toString());
					reading.add(source.toString());

					results.add(reading);
				} while (cursor.moveToNext());
			}

			cursor.close();
			wDatabase.close();
			wBpHelper.close();
			return results;
		} catch (Exception iE)
		{
			throw new PersistenceException(iE.getMessage());
		}
	}

}

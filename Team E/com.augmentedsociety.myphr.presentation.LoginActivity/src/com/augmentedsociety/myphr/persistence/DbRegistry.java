package com.augmentedsociety.myphr.persistence;
 

import android.content.Context; 
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Responsible for maintaining a connection to the SQLite3 db for use.
 * 
 * @author psyomn
 */
public class DbRegistry extends SQLiteOpenHelper
{
	public static final int DATABASE_VERSION = 27;

	public static final String DATABASE_NAME = "aug_synergy.db";

	private static DbRegistry mInstance = null;

	protected DbRegistry(Context iContext)
	{
		super(iContext, DATABASE_NAME, null, DATABASE_VERSION);
	}

	/**
	 * @param iContext
	 *          The current context in the application
	 * @return The instance of the database
	 */
	public static synchronized DbRegistry getInstance(Context iContext)
	{
		if (null == mInstance)
		{
			mInstance = new DbRegistry(iContext.getApplicationContext());
		}

		return mInstance;
	}

	/** Delegates to the table manager */
	@Override
	public void onCreate(SQLiteDatabase db)
	{
		TableManager.run(db.getVersion(), DATABASE_VERSION, db);
		
	}

	/** Delegates to the table manager */
	/*@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
	{
		TableManager.run(db.getVersion(), DATABASE_VERSION, db);
	}*/
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		String TABLE_PATIENTS="user_information";
		String TABLE1 = "log_event";
		String TABLE2 = "notification";
		String TABLE15 = "allergies";
	  String TABLE14 = "immunizations";
	   String TABLE16 = "medications";
	   String TABLE13 = "medical_conditions";
	   String TABLE12 = "documents";
	   String TABLE3 = "allergy";
	   String TABLE4 = "medicalHistory";
		 String TABLE5 = "allergies";
		 String TABLE6 = "defpage";
		 String TABLE7 = "weight";
		 String TABLE8 = "blood_pressure";
		 String TABLE9 = "blood_sugar";
		 String TABLE10 = "oxygen_saturation";
		 String TABLE11 = "temperature";

		//if(oldVersion != newVersion)
		{
		Log.i("Upgrading database from version ","from"+ oldVersion + " to "
						+ newVersion + ", which will destroy all old data");
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_PATIENTS);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE1);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE2);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE3);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE4);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE5);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE6);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE7);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE8);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE9);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE10);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE11);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE12);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE13);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE14);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE15);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE16);
		
		onCreate(db);
		
		}
		//else
		{
			//TableManager.run(db.getVersion(), DATABASE_VERSION, db);
		}
		
	}
}

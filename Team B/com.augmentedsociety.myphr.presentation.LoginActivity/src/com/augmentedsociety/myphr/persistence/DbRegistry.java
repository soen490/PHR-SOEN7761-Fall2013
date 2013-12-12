package com.augmentedsociety.myphr.persistence;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Responsible for maintaining a connection to the SQLite3 db for use.
 * 
 * @author psyomn
 */
public class DbRegistry extends SQLiteOpenHelper
{
	public static final int DATABASE_VERSION = 12;

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
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
	{
		TableManager.run(db.getVersion(), DATABASE_VERSION, db);
	}
}

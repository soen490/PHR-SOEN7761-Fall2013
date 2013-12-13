package com.augmentedsociety.myphr.persistence.dbcommands;

import android.database.sqlite.SQLiteDatabase;

/**
 * Command to create the notifications table
 * @author psyomn
 * @author ykitaev
 */
public class Command007 implements IDbCommand
{
  private static final String TABLE = "notification";
	
	private static final String TABLE_CREATE = 
			"CREATE TABLE " + TABLE
	      + "  (id BIGINT, first_occurrence BIGINT, recurring INT, "
	      + "repeat_interval BIGINT, enabled INT, title NVARCHAR, "
	      + "PRIMARY KEY(id));";

	private static SQLiteDatabase mDbHandle = null;
	
	public Command007(SQLiteDatabase iDbHandle)
	{
		mDbHandle = iDbHandle;
	}
	
	@Override
	public void execute()
	{
		mDbHandle.execSQL(TABLE_CREATE);
	}

}

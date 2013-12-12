package com.augmentedsociety.myphr.persistence.dbcommands;

import android.database.sqlite.SQLiteDatabase;

/**
 * Command to create the default page table
 * @author Rajan Jayakumar
 */
public class Command012  implements IDbCommand
{
	 private static final String TABLE = "defpage";
		
		private static final String TABLE_CREATE = 
				"CREATE TABLE " + TABLE
		      + "  (id BIGINT, page NVARCHAR, "
		      + "PRIMARY KEY(id));";
		
		private static final String TABLE_INIT = 
				"INSERT INTO " + TABLE
		      + " VALUES (1, \'-1\');";

		private static SQLiteDatabase mDbHandle = null;
		
		public Command012(SQLiteDatabase iDbHandle)
		{
			mDbHandle = iDbHandle;
		}
		
		@Override
		public void execute()
		{
			mDbHandle.execSQL(TABLE_CREATE);
			for(int x=1;x<31;x++)
				mDbHandle.execSQL("INSERT INTO " + TABLE
			      + " VALUES ("+x+", \'-1\');");
				//mDbHandle.execSQL(TABLE_INIT);
		}
}

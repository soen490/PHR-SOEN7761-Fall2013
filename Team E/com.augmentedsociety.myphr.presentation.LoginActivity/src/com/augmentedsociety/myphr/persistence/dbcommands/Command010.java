package com.augmentedsociety.myphr.persistence.dbcommands;

import android.database.sqlite.SQLiteDatabase;

/**
 * Command to create the allergy table
 * @author Roger Makram
 */
public class Command010  implements IDbCommand
{
	 private static final String TABLE = "allergy";
		
		private static final String TABLE_CREATE = 
				"CREATE TABLE " + TABLE
		      + "  (id BIGINT, allergy NVARCHAR, reaction NVARCHAR, "
		      + "severity NVARCHAR, "
		      +"p_id BIGINT, "
		      + "PRIMARY KEY(id), " 
		      + "FOREIGN KEY (p_id) REFERENCES user_information(id) );";

		private static SQLiteDatabase mDbHandle = null;
		
		public Command010(SQLiteDatabase iDbHandle)
		{
			mDbHandle = iDbHandle;
		}
		
		@Override
		public void execute()
		{
			mDbHandle.execSQL(TABLE_CREATE);
		}
}

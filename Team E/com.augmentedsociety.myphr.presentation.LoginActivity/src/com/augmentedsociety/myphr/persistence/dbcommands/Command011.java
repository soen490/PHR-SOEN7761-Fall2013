package com.augmentedsociety.myphr.persistence.dbcommands;

import android.database.sqlite.SQLiteDatabase;

/**
 * Command to create the allergy table
 * @author Roger Makram
 */
public class Command011  implements IDbCommand
{
	 private static final String TABLE = "medicalHistory";
	 private static final String TABLE_TO_DELETE = "allergies";
		
		private static final String TABLE_CREATE = 
				"CREATE TABLE " + TABLE
		      + "  (id BIGINT, date BIGINT, description NVARCHAR, "
		      + "location NVARCHAR, "
		      +"p_id BIGINT, "
		      + "PRIMARY KEY(id), " 
		      + "FOREIGN KEY (p_id) REFERENCES user_information(id) );";
	
		private static final String TABLE_DELETE = 
				"DROP TABLE IF EXISTS " + TABLE_TO_DELETE + ";";

		private static SQLiteDatabase mDbHandle = null;
		
		public Command011(SQLiteDatabase iDbHandle)
		{
			mDbHandle = iDbHandle;
		}
		
		@Override
		public void execute()
		{
			mDbHandle.execSQL(TABLE_CREATE);
			mDbHandle.execSQL(TABLE_DELETE);
		}
}

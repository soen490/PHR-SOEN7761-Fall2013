package com.augmentedsociety.myphr.persistence.dbcommands;

import android.database.sqlite.SQLiteDatabase;

/**
 * Create the documents table.
 * 
 * @author psyomn
 */
public class Command009 implements IDbCommand
{

	private static String TABLE_NAME = "documents";
	
	private static String CREATE_TABLE = 
      "CREATE TABLE " + TABLE_NAME + " ( "
      + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
      + "title varchar(100), "
      + "description varchar(100), "
      + "picture BLOB"
      + " );";
	
	private SQLiteDatabase mDbHandle = null;
	
  public Command009(SQLiteDatabase iDb)
  {
    mDbHandle = iDb;
  }

  @Override
  public void execute()
  {
		mDbHandle.execSQL(CREATE_TABLE);
  }

}

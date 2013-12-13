package com.augmentedsociety.myphr.persistence.dbcommands;

import android.database.sqlite.SQLiteDatabase;

/**
 * Create the logs table 
 * @author psyomn
 *
 */
public class Command006 implements IDbCommand
{
	private static final String TABLE = "log_event";
	
  private static final String CREATE_TABLE_LOG_EVENT = "CREATE TABLE "
    + TABLE
    + "  (id BIGINT, event_date BIGINT, event_type NVARCHAR, "
    + "event_info NVARCHAR, PRIMARY KEY(id));";
  
  private SQLiteDatabase mDbHandle = null;
  
  /**
   * Create the command that will create the create table temperature
   * @param iDbHandle
   */
  public Command006(SQLiteDatabase iDbHandle)
  {
    mDbHandle = iDbHandle;
  }
  
  /**
   * Create the temperature table
   */
  @Override
  public void execute()
  {
    mDbHandle.execSQL(CREATE_TABLE_LOG_EVENT);
  }
  
}

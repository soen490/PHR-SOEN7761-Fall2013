package com.augmentedsociety.myphr.persistence.dbcommands;

import android.database.sqlite.SQLiteDatabase;

/**
 * Create the temperature table
 * @author psyomn
 * @date Tue 22 Jan 2013
 */
public class Command005 implements IDbCommand
{
  /**
   * Create table 'temperature' SQL
   */
  private static final String CREATE_TABLE_TEMPERATURE = 
      "CREATE TABLE " + "temperature"
      + "(id BIGINT, "
      + "when_taken BIGINT,"
      + "source INT, " 
      + "temperature FLOAT, " 
      +"p_id BIGINT, "
      + "PRIMARY KEY(id), " 
      + "FOREIGN KEY (p_id) REFERENCES user_information(id) );";

  private SQLiteDatabase mDbHandle = null;
  
  /**
   * Create the command that will create the create table temperature
   * @param iDbHandle
   */
  public Command005(SQLiteDatabase iDbHandle)
  {
    mDbHandle = iDbHandle;
  }
  
  /**
   * Create the temperature table
   */
  @Override
  public void execute()
  {
    mDbHandle.execSQL(CREATE_TABLE_TEMPERATURE);
  }
  
}

package com.augmentedsociety.myphr.persistence.dbcommands;

import android.database.sqlite.SQLiteDatabase;

/**
 * Create table Oxygen saturation
 * 
 * @author psyomn
 * @date Tue 22 Jan 2013
 * @copyright Team Augmented Society
 */
public class Command004 implements IDbCommand
{
  
  /**
   * SQL for the oxygen saturation table.
   */
  private static final String CREATE_TABLE_OXYGEN_SATURATION = 
      "CREATE TABLE "
      + "oxygen_saturation"
      + "  (id BIGINT, " 
      + "when_taken BIGINT, " 
      + "source INT, " 
      + "oxygen_saturation FLOAT, " 
      +"p_id BIGINT, "
      + "PRIMARY KEY(id), " 
      + "FOREIGN KEY (p_id) REFERENCES user_information(id) );";

  /**
   * the database handle for the object
   */
  private SQLiteDatabase mDbHandle = null;
  
  /**
   * Create the command for the oxygen saturation table
   * @param iDbHandle
   */
  public Command004(SQLiteDatabase iDbHandle)
  {
    mDbHandle = iDbHandle;
  }
  
  /**
   * Create the oxygen saturation table
   */
  @Override
  public void execute()
  {
    mDbHandle.execSQL(CREATE_TABLE_OXYGEN_SATURATION);
  }
}

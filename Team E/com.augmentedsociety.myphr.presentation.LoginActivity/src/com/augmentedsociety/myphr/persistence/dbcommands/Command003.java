package com.augmentedsociety.myphr.persistence.dbcommands;

import android.database.sqlite.SQLiteDatabase;

/**
 * Creates the Blood sugar table
 * 
 * @author psyomn
 * @date Tue 22 Jan
 * @copyright Team Augmented Society
 */
public class Command003 implements IDbCommand
{
  /**
   * SQL for the blood sugar table
   */
  private static final String CREATE_TABLE_BLOOD_SUGAR = 
      "CREATE TABLE "
      + "blood_sugar"
      + "  (id BIGINT, " 
      + "when_taken BIGINT, " 
      + "source INT, " 
      + "blood_glucose FLOAT, " 
      +"p_id BIGINT, "
      + "PRIMARY KEY(id), " 
      + "FOREIGN KEY (p_id) REFERENCES user_information(id) );";

  /**
   * The db handle to execute the sql
   */
  private SQLiteDatabase mDbHandle = null;
  
  /**
   * Constructor to initialize with db handle
   * @param iDbHandle the database handle 
   */
  public Command003(SQLiteDatabase iDbHandle)
  {
    mDbHandle = iDbHandle;
  }
  
  /**
   * Execute the command and create the blood sugar table
   */
  @Override
  public void execute()
  {
    mDbHandle.execSQL(CREATE_TABLE_BLOOD_SUGAR);
  }
}

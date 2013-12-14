package com.augmentedsociety.myphr.persistence.dbcommands;

import android.database.sqlite.SQLiteDatabase;

public class Command002 implements IDbCommand
{

  /**
   * Create blood pressure table
   */
  private static final String CREATE_TABLE_BLOOD_PRESSURE =
    "CREATE TABLE " + "blood_pressure "  
    + " (id BIGINT, " 
    + "when_taken BIGINT," 
    + "source INT,"
    + "systolic INT,"
    + "diastolic INT,"
    + "heartrate INT,"
    + "PRIMARY KEY(id));";

  private SQLiteDatabase mDbHandle = null;
  
  public Command002(SQLiteDatabase iDb)
  {
    mDbHandle = iDb;
  }
  
  /**
   * Create the blood pressure table
   */
  @Override
  public void execute()
  {
    // TODO Auto-generated method stub
    mDbHandle.execSQL(CREATE_TABLE_BLOOD_PRESSURE);
  }
  
}

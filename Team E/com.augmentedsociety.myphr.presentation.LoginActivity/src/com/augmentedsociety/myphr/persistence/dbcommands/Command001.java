package com.augmentedsociety.myphr.persistence.dbcommands;

import android.database.sqlite.SQLiteDatabase;


/**
 * This command creates the weight table
 * 
 * @author psyomn
 */
public class Command001 implements IDbCommand
{

  private SQLiteDatabase mDbHandle = null;
  
  /**
   * Create the weights table
   */
  private static final String CREATE_TABLE_WEIGHT = 
    "CREATE TABLE " + "weight "
    + "(id BIGINT, " 
    + "when_taken BIGINT, " 
    + "source INT, "
    + "weight FLOAT,"
    +"p_id BIGINT, "
    + "PRIMARY KEY(id), " 
    + "FOREIGN KEY (p_id) REFERENCES user_information(id) "
    +");";
  
  /**
   * Create the command with the db handle given
   * @param iDb is the database handle that is passed
   */
  public Command001(SQLiteDatabase iDb)
  {
    mDbHandle = iDb;
  }
  
  /**
   * Execute the alterations to be done to the db.
   */
  @Override
  public void execute()
  {
    mDbHandle.execSQL(CREATE_TABLE_WEIGHT);
  }

}

package com.augmentedsociety.myphr.persistence.dbcommands;
import android.database.sqlite.SQLiteDatabase;

/**
 * These are the tables required by the personal information section. This
 * update command includes the schemas for the following tables: 
 * 
 * - Allergies table
 * - Immunizations table 
 * - Medical Conditions table
 * - Medications table
 * - User information table
 * 
 * @author psyomn
 */
public class Command008 implements IDbCommand
{
  private SQLiteDatabase mDbHandle = null;
  
  /* Table names */
  
  private static final String USER_INFORMATION_TABLE_NAME = "user_information";
  
  private static final String ALLERGIES_TABLE = "allergies";
  
  private static final String IMMUNIZATIONS_TABLE = "immunizations";
  
  private static final String MEDICATIONS_TABLE = "medications";
  
  private static final String MEDICAL_CONDITIONS_TABLE = "medical_conditions";
  
  /* Schemas */
  
  /**
   * This is the schema for the user info (name, surname, phone etc)
   * This is supposed to be cleared, and reinserted on changes (i.e. only one
   * row).
   */
  
  private static final String CREATE_USER_INFORMATION_TABLE = 
      "CREATE TABLE " + USER_INFORMATION_TABLE_NAME + " ("
      + "id BIGINT, "
      + "lastName varchar(150), "
      + "firstName varchar(150), "
      + "DOB BIGINT, "
      + "phoneNumber BIGINT, "
      + "address text, "
      + "email varchar(250), "
      + "medicare text, "
      + "insurance varchar(100), "
      + "emergencyContact1 varchar(200), "
      + "emergencyContact2 varchar(200),"
      + "note text,"
      + "room text,"
      + "bed text,"
      + "PRIMARY KEY (id)"
      +" );";

  /** Create the allergies table */
  private static final String CREATE_ALLERGIES_TABLE = 
      "CREATE TABLE " + ALLERGIES_TABLE + " ( "
      + "allergies text, "
      + "reactions text, "
      + "severity int, "
      +"p_id BIGINT, "
      + "FOREIGN KEY (p_id) REFERENCES user_information(id) "
      +");";
      
  
  /** Create the immunizations table */
  private static final String CREATE_IMMUNIZATIONS_TABLE = 
      "CREATE TABLE " + IMMUNIZATIONS_TABLE + " ( "
      + "id BIGINT, "
      + "type varchar(100), "
      + "name varchar(100), "
      + "manufacturer varchar(100), "
      + "lot_number varchar(100), "
      + "route varchar(100), "
      + "posology varchar(100), "
      + "date BIGINT, "
      + "location text, "
      + "details text, "
      + "comments text, "
      +"p_id BIGINT, "
      + "PRIMARY KEY(id), " 
      + "FOREIGN KEY (p_id) REFERENCES user_information(id) "
      +");";
  
  /** Create table for medications */
  private static final String CREATE_MEDICATIONS_TABLE = 
      "CREATE TABLE " + MEDICATIONS_TABLE + " ( "
      + "id BIGINT, "
      + "type varchar(100), "
      + "name varchar(100), "
      + "posology varchar(100), "
      + "strength varchar(100), "
      + "frequency varchar(100), "
      + "start_date BIGINT, "
      + "end_date BIGINT, "
      + "reasons text, "
      + "doctor varchar(150), "
      +"p_id BIGINT, "
      + "PRIMARY KEY(id), " 
      + "FOREIGN KEY (p_id) REFERENCES user_information(id) "
      +");";
  
  /** Create the table for medical conditions */
  private static final String CREATE_MEDICAL_CONDITIONS_TABLE = 
      "CREATE TABLE " + MEDICAL_CONDITIONS_TABLE + " ( "
      + "name text "
      + ");";
  
  public Command008(SQLiteDatabase iDbHandle)
  {
    mDbHandle = iDbHandle;
  }
  
  /**
   * Creates the tables.
   */
  @Override
  public void execute()
  {
    mDbHandle.execSQL(CREATE_ALLERGIES_TABLE);
    mDbHandle.execSQL(CREATE_IMMUNIZATIONS_TABLE);
    mDbHandle.execSQL(CREATE_MEDICAL_CONDITIONS_TABLE);
    mDbHandle.execSQL(CREATE_MEDICATIONS_TABLE);
    mDbHandle.execSQL(CREATE_USER_INFORMATION_TABLE);
  }

}

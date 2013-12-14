package com.augmentedsociety.myphr.persistence;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

/**
 * TDG for documents
 * 
 * @author psyomn
 */
public class DocumentTDG
{
	private static final String TABLE = "documents";
  private static final String SELECT_ALL = "select * from documents;";
  
  private DocumentTDG(){};
  
  /**
   * 
   * @param args the data of the document
   * @param iContext the current context 
   * @throws PersistenceException 
   */
  public static void insert(ArrayList<String> iValues, Context iContext) throws PersistenceException
  {
  	try
    {
      if (iValues.size() != 3)
        throw new PersistenceException("The array of values must have 4 entries.");
      else if (iContext == null)
        throw new NullPointerException("The context you have specified is NULL");

      DbRegistry wHelper = DbRegistry.getInstance(iContext);
      SQLiteDatabase wDatabase = wHelper.getWritableDatabase();

      ContentValues wValues = new ContentValues();

      wValues.put("title", iValues.get(0));
      wValues.put("description", iValues.get(1));
      wValues.put("picture", iValues.get(2));

      long wNewRowId = wDatabase.insert(TABLE, null, wValues);

      if (wNewRowId == -1)
        throw new PersistenceException("Insertion to the DB has failed. Id duplicated?");

      wDatabase.close();
      wHelper.close();
    } 
    catch (Exception iE)
    {
      throw new PersistenceException(iE.getMessage());
    }
  }
  
}

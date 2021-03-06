package com.augmentedsociety.myphr.persistence;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * Default Page TDG
 * 
 * @author Rajan Jayakumar
 *
 */

public class DefaultPageTDG
{
private static final String TABLE = "defpage";
	
  private static final String SELECT = 
  		"SELECT r.page" 
      + " FROM "
      + TABLE + " as r;";
  
  /**
   * Selects default page from the database
   */
  public static ArrayList<String> select(Context iContext)
      throws PersistenceException
  {
    try
    {
      ArrayList<String> results = new ArrayList<String>(20);
      DbRegistry wBpHelper = DbRegistry.getInstance(iContext);

      SQLiteDatabase wDatabase = wBpHelper.getReadableDatabase();

      Cursor cursor = wDatabase.rawQuery(SELECT, null);

      if (cursor.moveToFirst())
      {
      	String page = cursor.getString(0);
        
        results.add(page);
      }

      cursor.close();
      wDatabase.close();
      wBpHelper.close();
      return results;
    } 
    catch (Exception iE)
    {
      throw new PersistenceException(iE.getMessage());
    }
  }

  
  /**
   * Updates default page in the database
   */
  public static int update(ArrayList<String> iValues, Context iContext) 
  		                                              throws PersistenceException
  {
    try
    {
      if (iValues.size() != 2)
        throw new PersistenceException("The array of values must have 2 entries.");
      else if (iContext == null)
        throw new NullPointerException("The context you have specified is NULL");

      DbRegistry wHelper = DbRegistry.getInstance(iContext);
      SQLiteDatabase wDatabase = wHelper.getWritableDatabase();

      ContentValues wValues = new ContentValues();

      wValues.put("id", Long.valueOf(iValues.get(0)));
      wValues.put("page", iValues.get(1));

      
      int wRowsUpdated = 
      		wDatabase.update(TABLE, wValues, "id = ?", new String[]{iValues.get(0)});

      wDatabase.close();
      wHelper.close();
      
      return wRowsUpdated;
    } 
    catch (Exception iE)
    {
      throw new PersistenceException(iE.getMessage());
    }
  }
  
  
}

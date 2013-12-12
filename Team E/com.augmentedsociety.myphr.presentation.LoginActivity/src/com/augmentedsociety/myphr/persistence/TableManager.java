package com.augmentedsociety.myphr.persistence;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.augmentedsociety.myphr.barcode.PatientDataSource;
import com.augmentedsociety.myphr.barcode.SensorActivity;
import com.augmentedsociety.myphr.persistence.dbcommands.Command001;
import com.augmentedsociety.myphr.persistence.dbcommands.Command002;
import com.augmentedsociety.myphr.persistence.dbcommands.Command003;
import com.augmentedsociety.myphr.persistence.dbcommands.Command004;
import com.augmentedsociety.myphr.persistence.dbcommands.Command005;
import com.augmentedsociety.myphr.persistence.dbcommands.Command006;
import com.augmentedsociety.myphr.persistence.dbcommands.Command007;
import com.augmentedsociety.myphr.persistence.dbcommands.Command008;
import com.augmentedsociety.myphr.persistence.dbcommands.Command009;
import com.augmentedsociety.myphr.persistence.dbcommands.Command010;
import com.augmentedsociety.myphr.persistence.dbcommands.Command011;
import com.augmentedsociety.myphr.persistence.dbcommands.Command012;
import com.augmentedsociety.myphr.persistence.dbcommands.IDbCommand;

/**
 * This is the table manager. This is the implementation of
 * the proof of concept found in the provided architectural 
 * document.  
 * 
 * Android has implemented some of the aspects of the initial
 * design. Hence, the manager in this situation needs to simply
 * identify the calling class / helper, get a version number, 
 * and understand where the current schema stands.
 * 
 * TODO Need to integrate notifications 
 * TODO Need to integrate the new profile tables
 * 
 * @author psyomn
 * @copyright Team Augmented Society
 */
public class TableManager 
{
  /**
   * No instantiation, make this invisible to the programmer. 
   */
  protected TableManager(){}
  
  /**
   * This will run the diagnostics (will check to see if table
   * creation is required, or if table updates are required as 
   * well).
   * 
   * @param iOldVersion    
   *   is the version we want to update from (excluding)
   * 
   * @param iNewVersion    
   *   is the version we want to update to (including)
   */
  public static void run(int iOldVersion, long iToVersion, 
  		                   SQLiteDatabase iDbHandle)
  {
  	List<IDbCommand> wComms = null;
    int i; 
        
    /* Assign the appropriate command list */
    
    wComms = getCommands(iDbHandle);
    
    /* Iterate through commands to execute */
    //iOldVersion
    for (i=0; i < 12; ++i)
    {
      wComms.get(i).execute();
    } 
   }
  
  /* Get Database version-ing commands */
  
  private static List<IDbCommand> getCommands(SQLiteDatabase iDbHandle)
  {
  	ArrayList<IDbCommand> wComms = new ArrayList<IDbCommand>(); 
  	
  	wComms.add(new Command001(iDbHandle));
  	wComms.add(new Command002(iDbHandle));
  	wComms.add(new Command003(iDbHandle));
  	wComms.add(new Command004(iDbHandle));
  	wComms.add(new Command005(iDbHandle));
  	wComms.add(new Command006(iDbHandle));
  	wComms.add(new Command007(iDbHandle));
  	wComms.add(new Command008(iDbHandle));
  	wComms.add(new Command009(iDbHandle));
  	wComms.add(new Command010(iDbHandle));
  	wComms.add(new Command011(iDbHandle));
  	wComms.add(new Command012(iDbHandle));
  	
  	return wComms; 
  }
 
}

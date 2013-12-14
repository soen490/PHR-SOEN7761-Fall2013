package com.augmentedsociety.myphr.domain.commands;

import com.augmentedsociety.myphr.domain.ReadingSource;

/**
 * Command interface for the other concrete vital sign commands; 
 * used by all Commands inheriting from it.
 * 
 * @author Serge-Antoine Naim
 * 
 */

public abstract class Command
{
  public abstract void execute(VitalSignObject iVitalSignObject, 
  		                         ReadingSource iMeasureSource);
}

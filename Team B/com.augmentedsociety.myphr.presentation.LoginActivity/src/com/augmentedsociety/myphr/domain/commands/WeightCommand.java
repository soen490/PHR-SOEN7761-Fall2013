package com.augmentedsociety.myphr.domain.commands;

import com.augmentedsociety.myphr.domain.ReadingSource;

/**
 * WeightCommand class
 * 
 * @author Serge-Antoine Naim
 * 
 * When this class is instantiated and execution method called, it transfers 
 * process to respective method in the CommandAction class. Each of the concrete 
 * commands do the same by having an instance of CommandAction (the receiver) as 
 * an instance variable.
 * 
 */

public class WeightCommand extends Command
{
  public WeightCommand()
  {
    
  }
  
  /**
   * The passed vital sign object formed in the controller by the user input 
   * uses its CommandAction instance to call respectful vital sign reading 
   * method from CommandAction for proper insertion (in right table)
   */
  public void execute(VitalSignObject iVitalSignObject, ReadingSource iMeasureSource)
  {
    iVitalSignObject.getMCommandAction()
                    .readWeight(
                    		iVitalSignObject.getMBloodSugarOximeterWeightTemp(),  iVitalSignObject.getDate(),
                    		iVitalSignObject.getMContext(), iMeasureSource);
  }
}

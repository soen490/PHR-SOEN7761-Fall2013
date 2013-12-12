package com.augmentedsociety.myphr.domain.commands;

import java.util.HashMap;
import java.util.Map;

/**
 * ReadingInvoker class
 * 
 * @author Serge-Antoine Naim
 * 
 */

public class ReadingInvoker
{
  private Map<String, Command> mCommandMap = new HashMap<String, Command>();
  
  /**
   * Empty Default constructor
   * 
   * @param mCommandMap This hashMap is created/instantiated in the controller 
   * class, so that the action triggered by user calls the right command
   * to execute data reading input to the right table.
   */
  
  public ReadingInvoker()
  {
    
  }
  
  public Map<String, Command> getMap()
  {
    return mCommandMap;
  }
}

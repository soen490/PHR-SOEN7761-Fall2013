package com.augmentedsociety.myphr.domain;

/**
 * This enum specifies the possible types of entry of a single wellness measure.
 * 
 * KEYED: entered by hand BLUETOOTH: imported from a bluetooth-enabled
 * measurement device IMPORTED: imported from a compatible health-management
 * application database, for example - VSigns
 * 
 * @author Yuri Kitaev
 * 
 */
public enum ReadingSource
{
  KEYED, BLUETOOTH, IMPORTED, OTHER
}

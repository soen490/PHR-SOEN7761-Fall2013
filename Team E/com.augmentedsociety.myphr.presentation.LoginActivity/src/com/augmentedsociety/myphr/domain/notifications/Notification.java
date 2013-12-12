package com.augmentedsociety.myphr.domain.notifications;

import java.util.Date;

/**
 * Notification domain object; contains only data and notification-domain logic;
 * All managing and Android-specific code is in NotificationManager
 * 
 * @author Yuri Kitaev
 *
 */
public class Notification
{
  private long mID;
  private Date mFirstOccurrence;
  private boolean mRecurring;
  private long mRepeatInterval;
  private boolean mEnabled;
  private String mTitle;
  
  /**
   * General constructor
   * 
   * @param iID 
   *   The unique ID of the notification
   * 
   * @param iFirstOccurrence 
   *   The date and time when the alarm is to go off or the first occurrence 
   *   of the alarm if recurring
   *   
   * @param iRecurring 
   *   True if the alarm is recurring, false otherwise
   *   
   * @param iRepeatInterval 
   *   If recurring, shall be set to a number of milliseconds corresponding 
   *   to the time interval between occurrences
   *   
   * @param iEnabled 
   *   Whether or not the notification should go off
   *   
   * @param iTitle 
   *   The title of the notification to be seen at the status bar
   */
  public Notification(long iID, Date iFirstOccurrence, boolean iRecurring,
      long iRepeatInterval, boolean iEnabled, String iTitle)
  {
    super();
    this.mID = iID;
    this.mFirstOccurrence = iFirstOccurrence;
    this.mRecurring = iRecurring;
    this.mRepeatInterval = iRepeatInterval;
    this.mEnabled = iEnabled;
    this.mTitle = iTitle;
  }
  
  /**
   * Shorthand constructor
   * 
   * @param iID 
   *   The unique ID of the notification
   * 
   * @param iFirstOccurrence 
   *   The date and time when the alarm is to go off or the first occurrence 
   *   of the alarm if recurring
   *   
   * @param iTitle 
   *   The title of the notification to be seen at the status bar
   */
  public Notification(long iID, Date iFirstOccurrence, String iTitle)
  {
    this(iID, iFirstOccurrence, false, 0, true, iTitle);
  }
 
  public boolean isRecurring()
  {
    return mRecurring;
  }

  public void setRecurring(boolean iRecurring)
  {
    this.mRecurring = iRecurring;
  }
  
  public long getId()
  {
    return mID;
  }

  public Date getFirstOccurrence()
  {
    return mFirstOccurrence;
  }

  public void setFirstOccurrence(Date iFirstOccurrence)
  {
    this.mFirstOccurrence = iFirstOccurrence;
  }

  public long getRepeatInterval()
  {
    return mRepeatInterval;
  }

  public void setRepeatInterval(long iRepeatInterval)
  {
    this.mRepeatInterval = iRepeatInterval;
  }

  public boolean isEnabled()
  {
    return mEnabled;
  }

  public void setEnabled(boolean iEnabled)
  {
    this.mEnabled = iEnabled;
  }

  public String getTitle()
  {
    return mTitle;
  }

  public void setTitle(String iTitle)
  {
    this.mTitle = iTitle;
  }

}

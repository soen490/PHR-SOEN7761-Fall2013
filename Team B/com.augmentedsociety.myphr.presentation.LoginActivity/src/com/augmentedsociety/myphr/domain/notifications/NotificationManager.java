package com.augmentedsociety.myphr.domain.notifications;

import java.util.Date;

import com.augmentedsociety.myphr.domain.MapperException;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

/**
 * Notification Manager adds architectural logic to the Notification class, 
 * which only contains domain logic and has no knowledge about how the OS works.
 * 
 * @author Yuri Kitaev
 *
 */
public class NotificationManager
{
  private static final long MINUTE = 60 * 1000; 
  
  public NotificationManager()
  {
  }
  
  /**
   * Sets the one-time or recurring alarm in the OS scheduler if the specified 
   * notification is enabled, or does nothing otherwise. The alarm gets reset 
   * when the device reboots.
   * 
   * @param r 
   *   The notification to set an alarm for
   * 
   * @param c 
   *   The context, usually the application context
   * 
   * @author Yuri Kitaev
   */
  public static void setAlarm(Notification iNotification, Context iContext)
  {
    if (iNotification.isEnabled())
    {
      AlarmManager wAlarmManager = 
      		(AlarmManager) iContext.getSystemService(Context.ALARM_SERVICE);                 
             
      Intent wAlarmHandlerIntent = 
      		new Intent("com.augmentedsociety.myphr.presentation.notifications.AlarmHandler");
      wAlarmHandlerIntent.putExtra("NotificationId", iNotification.getId());                                

      PendingIntent wPendingDisplayIntent = 
      		PendingIntent.getActivity(iContext, 0, wAlarmHandlerIntent, PendingIntent.FLAG_CANCEL_CURRENT);

      
      if (iNotification.isRecurring())
        wAlarmManager.setRepeating(AlarmManager.RTC_WAKEUP,
        		iNotification.getFirstOccurrence().getTime(), 
        		iNotification.getRepeatInterval(), wPendingDisplayIntent);      
      else
        wAlarmManager.set(AlarmManager.RTC_WAKEUP, 
        		iNotification.getFirstOccurrence().getTime(), 
        		wPendingDisplayIntent);
    }
  }
  
  /**
   * Executes code that shall run after the user has seen the notification. 
   * It disables the non-recurring
   * alarms and updates the next occurrence for the recurring notifications
   * 
   * @param iNotification The notification to process
   * @param iContext The application context
   */
  public static void processSeenNotification(Notification iNotification, 
  		                                 Context iContext) throws MapperException
  {
    if (iNotification.isRecurring()
        && iNotification.getRepeatInterval() > 10000)
    {
      // Update the next notification occurrence date 
      long wInterval = iNotification.getRepeatInterval();
      long wNextOccurrence = iNotification.getFirstOccurrence().getTime();
      while (wNextOccurrence < (System.currentTimeMillis() + 5*MINUTE))
      {
        wNextOccurrence += wInterval;        
      }
      iNotification.setFirstOccurrence(new Date(wNextOccurrence));
      NotificationMapper.update(iNotification, iContext);
    }
    else
    {
      // Disable the non-recurring notification
      iNotification.setEnabled(false);
      NotificationMapper.update(iNotification, iContext);
    }     
  }

}

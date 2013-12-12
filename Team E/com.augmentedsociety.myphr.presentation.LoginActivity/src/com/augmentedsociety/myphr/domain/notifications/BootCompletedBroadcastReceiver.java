package com.augmentedsociety.myphr.domain.notifications;

import java.util.ArrayList;

import com.augmentedsociety.myphr.domain.MapperException;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Intercepts the BOOT_COMPLETE system message and activates all enabled 
 * notifications
 * 
 * @author Yuri Kitaev
 *
 */
public class BootCompletedBroadcastReceiver extends BroadcastReceiver
{
  @Override
  public void onReceive(Context iContext, Intent iIntent)
  {
    // When booting, re-enable all notifications
    try
    {
      ArrayList<Notification> wStoredNotifications = NotificationMapper.findAll(iContext);
      for (int i = 0; i < wStoredNotifications.size(); ++i)
      {
        NotificationManager.setAlarm(wStoredNotifications.get(i), iContext);
      }
    } 
    catch (MapperException e)
    {
      return; // No other way to handle an error in this case.
    }
  }
}

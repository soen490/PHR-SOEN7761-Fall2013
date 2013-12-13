package com.augmentedsociety.myphr.presentation.notifications;

import com.augmentedsociety.myphr.R;
import com.augmentedsociety.myphr.domain.MapperException;
import com.augmentedsociety.myphr.domain.notifications.NotificationMapper;
import com.augmentedsociety.myphr.presentation.ToastMessage;

import android.os.Bundle;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.view.Menu;
import android.widget.Toast;

/**
 * The class specifies what is to be done when an alarm goes off. 
 * @author Yuri Kitaev
 * @note Credit: http://mobiforge.com/developing/story/displaying-status-bar-notifications-android
 *       for the base mechanism of passing data (id) and intent identifiers to the 
 *       alarm handler and notification details activity and using the vibration
 *
 */
public class AlarmHandler extends Activity 
{
	private final String NOTIFICATION_ID = "NotificationId";
	
  @SuppressWarnings("deprecation")
  @Override
  public void onCreate(Bundle iSavedInstanceState) 
  {
      super.onCreate(iSavedInstanceState);

      long notificationId = getIntent().getExtras().getLong(NOTIFICATION_ID);
      
      com.augmentedsociety.myphr.domain.notifications.Notification wActiveNotification = null;
      try
      {
        wActiveNotification = NotificationMapper.getOne(notificationId, getApplicationContext());
      } 
      catch (MapperException e1)
      {
      	// The notification has been deleted; finish the activity and return.
      	finish();
      	return;
      }
      catch (Exception e2)
      {
      	// Unforeseen error. Display a message.
      	String genericErrorMessage = getApplicationContext().getResources().getString(R.string.notifications_generic_error);
//        Toast.makeText(AlarmHandler.this, genericErrorMessage,
//            Toast.LENGTH_LONG).show();
        new ToastMessage(AlarmHandler.this, genericErrorMessage, Toast.LENGTH_LONG);
        finish();
        return;
      }
      
      if (null == wActiveNotification)
      {
      	// Unforeseen error. Display a message.
      	String genericErrorMessage = getApplicationContext().getResources().getString(R.string.notifications_generic_error);
//        Toast.makeText(AlarmHandler.this, genericErrorMessage,
//            Toast.LENGTH_LONG).show();
        new ToastMessage(AlarmHandler.this, genericErrorMessage, Toast.LENGTH_LONG);
        finish();
        return;
      }
      
      if (!wActiveNotification.isEnabled())
      {
        // A notification has been disabled in the settings, but the OS still remembers it. 
        // Finish the activity and do nothing else - the icon won't be created
      	finish();
        return;
      } 
      
      Intent i = new Intent("com.augmentedsociety.myphr.presentation.notifications.NotificationDetailsActivity");
      i.putExtra(NOTIFICATION_ID, notificationId);  

      PendingIntent wPendingDisplayIntent = PendingIntent.getActivity(this, 0, i, PendingIntent.FLAG_CANCEL_CURRENT);

      NotificationManager wNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
    
      // Using deprecated constructors to support Android 2.3
      Notification wNewAlarm = new Notification(
          R.drawable.ic_launcher, 
          wActiveNotification.getTitle(),
          System.currentTimeMillis());
      
      CharSequence from = wActiveNotification.getTitle();
      String message = getApplicationContext().getResources().getString(R.string.notifications_reminder_title);
      
      // Using deprecated constructors to support Android 2.3
      wNewAlarm.setLatestEventInfo(this, from, message, wPendingDisplayIntent);

      wNewAlarm.vibrate = new long[] { 100, 300, 100, 500, 100, 700};     
      try
      {
        wNotificationManager.notify((int) notificationId, wNewAlarm);
      }
      catch (Exception e)
      {
        // Just continue; even if we cannot vibrate, our job is done.
      }
      finish();
  }

    @Override
    public boolean onCreateOptionsMenu(Menu iMenu) {
        getMenuInflater().inflate(R.menu.alarm_handler, iMenu);
        return true;
    }
}

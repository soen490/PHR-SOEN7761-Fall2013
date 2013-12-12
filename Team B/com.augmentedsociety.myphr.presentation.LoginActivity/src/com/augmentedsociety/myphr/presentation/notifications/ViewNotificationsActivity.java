package com.augmentedsociety.myphr.presentation.notifications;

import java.util.ArrayList;

import com.augmentedsociety.myphr.R;
import com.augmentedsociety.myphr.domain.MapperException;
import com.augmentedsociety.myphr.domain.logs.LogEventType;
import com.augmentedsociety.myphr.domain.notifications.Notification;
import com.augmentedsociety.myphr.domain.notifications.NotificationMapper;
import com.augmentedsociety.myphr.presentation.MenuActivity;
import com.augmentedsociety.myphr.presentation.ToastMessage;
import com.augmentedsociety.myphr.presentation.SpeechHelp;
import com.augmentedsociety.myphr.presentation.logs.LogEventEmitter;

import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.NotificationManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;
import android.widget.Toast;

/**
 * Lists available notifications and their brief information
 * @author Yuri Kitaev
 *
 */
public class ViewNotificationsActivity 
       extends MenuActivity implements 
               OnItemClickListener
               , OnItemLongClickListener
               , DialogInterface.OnClickListener
{
	public static final int REQ_NOTIF_NEW = 1001;
  private ArrayList<Notification> mAllNotifications;
  private ListView mListView;
  private Notification mCurrent = null;
  private NotificationListItemAdapter mMainAdapter;
  
  private String REMINDER = "reminder";
  
  @SuppressLint("NewApi")
@Override
  public void onCreate(Bundle iSavedInstanceState)
  {
    super.onCreate(iSavedInstanceState);
    setContentView(R.layout.view_notifications);
    
    showAvailableNotifications();
    
		int currentapiVersion = android.os.Build.VERSION.SDK_INT;
		if (currentapiVersion >= android.os.Build.VERSION_CODES.FROYO)
		{
			getActionBar().setDisplayShowHomeEnabled(false);
		}
  }

  private void showAvailableNotifications()
  {
    try
    {
      mAllNotifications = NotificationMapper.findAll(getApplicationContext());
      mListView = (ListView) findViewById(R.id.notifications_available);
      mMainAdapter = new NotificationListItemAdapter(this, mAllNotifications);
      mListView.setAdapter(mMainAdapter);
      mListView.setOnItemClickListener(this);
      mListView.setOnItemLongClickListener(this);
    }
    catch (Exception e)
    {
//      Toast.makeText(ViewNotificationsActivity.this,
//          e.getMessage(),
//          Toast.LENGTH_LONG).show();
    	new ToastMessage(ViewNotificationsActivity.this, e.getMessage(), Toast.LENGTH_LONG);
    }
  }
  
  @Override
  public void onResume()
  {
		SpeechHelp tut = SpeechHelp.getInstance();
		if (tut.isActive())
			tut.playTutorial(this, REMINDER);
    super.onResume();
    showAvailableNotifications();
  }
  
  /**
   * Navigates to the New Notification form
   * @param view
   */
  public void newNotification(View iView)
  {
    Intent myIntent = new Intent(this, NewNotificationActivity.class);
    startActivityForResult(myIntent, REQ_NOTIF_NEW);    
  }
  
  /**
   * Handles what's going to happen when you tap on a notification brief
   * Right now, it toggles the 'enabled' status
   */
  public void onItemClick (AdapterView<?> iAdapter, View iV, int iPosition, long iID)
  {
    Object o = mListView.getItemAtPosition(iPosition);
    
    Notification selectedNotification = (Notification) o;
    
    selectedNotification.setEnabled(!selectedNotification.isEnabled());
    
    // Save the status in the database
    try
    {
      NotificationMapper.update(selectedNotification, getApplicationContext());
      /**Fires a LogEvent to the LogItemEditor*/
			LogEventEmitter.fireLogEvent(this, getApplicationContext(), LogEventType.NOTIF_EDIT);
    } catch (MapperException e)
    {
//      Toast.makeText(ViewNotificationsActivity.this,
//          e.getMessage(),
//          Toast.LENGTH_LONG).show();
    	new ToastMessage(ViewNotificationsActivity.this, e.getMessage(), Toast.LENGTH_LONG);
    }
    
    // Update the list on the screen
    mListView.invalidateViews();
    
    // Update the status of the notification in the OS
    if(selectedNotification.isEnabled())
    {
      com.augmentedsociety.myphr.domain.notifications.NotificationManager.setAlarm(selectedNotification, getApplicationContext());
    }
    else
    {
      NotificationManager notificationManager = (NotificationManager) 
          getSystemService(NOTIFICATION_SERVICE);
      int notifId = (int)selectedNotification.getId();
      notificationManager.cancel(notifId); 
    }
  }

  /**
   * Handles deleting a notification
   */
  public boolean onItemLongClick(AdapterView<?> iAdapter, View iView, int iPosition, long iID)
  {
    Object o = mListView.getItemAtPosition(iPosition);
    mCurrent = (Notification) o;
    
    String deleteQuestion = iView.getContext().getResources().getString(R.string.notifications_delete_question);
    String yes = iView.getContext().getResources().getString(R.string.button_yes);
    String no = iView.getContext().getResources().getString(R.string.button_no);
    
    AlertDialog.Builder builder = new AlertDialog.Builder(this);
    builder.setMessage(deleteQuestion + " " + mCurrent.getTitle() + "?").setPositiveButton(yes, this)
        .setNegativeButton(no, this).show();

    return false;
  }

  /**
   * Handles Yes/No selection for deleting dialog
   * @param dialog
   * @param which
   */
  public void onClick(DialogInterface iDialog, int iWhich)
  {
    switch (iWhich)
    {
    case DialogInterface.BUTTON_POSITIVE:
      try
      {
      	String notificationDeletedMessage = getApplicationContext().getResources().getString(R.string.notification_deleted);
        NotificationMapper.delete(mCurrent, getApplicationContext());
        /**Fires a LogEvent to the LogItemEditor*/
  			LogEventEmitter.fireLogEvent(this, getApplicationContext(), LogEventType.NOTIF_DELETE);
        mAllNotifications = NotificationMapper.findAll(getApplicationContext());

        
        try
        {
          mAllNotifications = NotificationMapper.findAll(getApplicationContext());
          mListView = (ListView) findViewById(R.id.notifications_available);
          mMainAdapter = new NotificationListItemAdapter(this, mAllNotifications);
          mListView.setAdapter(mMainAdapter);
          mListView.setOnItemClickListener(this);
          mListView.setOnItemLongClickListener(this);
        }
        catch (Exception e)
        {
//          Toast.makeText(ViewNotificationsActivity.this,
//              e.getMessage(),
//              Toast.LENGTH_LONG).show();
        	new ToastMessage(ViewNotificationsActivity.this, e.getMessage(), Toast.LENGTH_LONG);
        }
        
//        Toast.makeText(ViewNotificationsActivity.this,
//        		notificationDeletedMessage,
//            Toast.LENGTH_LONG).show();
//        mMainAdapter.notifyDataSetChanged();
//        mListView.invalidateViews();
        new ToastMessage(ViewNotificationsActivity.this, notificationDeletedMessage, Toast.LENGTH_LONG);
        
      } catch (MapperException e)
      {
//        Toast.makeText(ViewNotificationsActivity.this,
//            e.getMessage(),
//            Toast.LENGTH_LONG).show();
        new ToastMessage(ViewNotificationsActivity.this, e.getMessage(), Toast.LENGTH_LONG);
      }
        
        break;

    case DialogInterface.BUTTON_NEGATIVE:
        mCurrent.setEnabled(!mCurrent.isEnabled());
        break;
    }

  }
  
	@Override
	public void createHelpDialog(MenuItem item)
	{
		super.createHelpDialog(item);
		SpeechHelp tut = SpeechHelp.getInstance();
		if (tut.isActive())
		{
			tut.playTutorial(this, REMINDER);
		} else
		{
			tut.stopTutorial();
		}
	}
  
}

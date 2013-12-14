package com.augmentedsociety.myphr.presentation.notifications;

import com.augmentedsociety.myphr.R;
import com.augmentedsociety.myphr.domain.notifications.Notification;
import com.augmentedsociety.myphr.domain.notifications.NotificationMapper;
import com.augmentedsociety.myphr.presentation.ToastMessage;
import com.augmentedsociety.myphr.presentation.SpeechHelp;
import com.augmentedsociety.myphr.presentation.vitalsigns.VitalSignsActivity;

import android.os.Bundle;
import android.app.Activity;
import android.app.NotificationManager;
import android.content.Intent;
import android.view.KeyEvent;
import android.view.Menu;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Presents detailed information about the notification that was shown
 * 
 * @author Yuri Kitaev
 * 
 */
public class NotificationDetailsActivity extends Activity
{

	private final String NOTIFICATION_ID = "NotificationId";

	@Override
	public void onCreate(Bundle iSavedInstanceState)
	{
		super.onCreate(iSavedInstanceState);
		setContentView(R.layout.notification_details);

		// Remove the notification from the status bar
		NotificationManager wNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		int id = (int) getIntent().getExtras().getLong(NOTIFICATION_ID);
		wNotificationManager.cancel(id);

		// Reaction to the user having seen the notification
		Notification wSeenNotification = null;
		try
		{
			wSeenNotification = NotificationMapper
					.getOne(id, getApplicationContext());
			com.augmentedsociety.myphr.domain.notifications.NotificationManager
					.processSeenNotification(wSeenNotification, getApplicationContext());

			// Show the text on the screen
			TextView textBox = (TextView) (TextView) findViewById(R.id.alarm_text);
			textBox.setText(wSeenNotification.getTitle());
		} catch (Exception e)
		{
			String detailsDeletedErrorMessage = getApplicationContext()
					.getResources().getString(
							R.string.notifications_error_details_deleted);
			// Toast.makeText(NotificationDetailsActivity.this,
			// detailsDeletedErrorMessage,
			// Toast.LENGTH_LONG).show();
			new ToastMessage(NotificationDetailsActivity.this,
					detailsDeletedErrorMessage, Toast.LENGTH_LONG);
			finish();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu iMenu)
	{
		getMenuInflater().inflate(R.menu.notification_details, iMenu);
		return true;
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)
	{
		Intent intent = new Intent(getApplicationContext(),
				VitalSignsActivity.class);
		startActivity(intent);
		return true;
	}

}

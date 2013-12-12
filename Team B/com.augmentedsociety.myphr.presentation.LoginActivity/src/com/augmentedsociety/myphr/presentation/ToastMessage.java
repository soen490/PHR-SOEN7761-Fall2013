package com.augmentedsociety.myphr.presentation;

import android.app.Activity;
import android.content.Context;
import android.view.Gravity;
import android.widget.Toast;

import com.augmentedsociety.myphr.presentation.notifications.NewNotificationActivity;

public class ToastMessage
{

	public ToastMessage(Activity activity, CharSequence notificationSavedMessage,
			int length)
	{

		Toast toast_notification = Toast.makeText(activity,
				notificationSavedMessage, length);
		toast_notification.setGravity(Gravity.CENTER, 0, 0);
		toast_notification.show();
	}

	public ToastMessage(Context context, CharSequence notificationSavedMessage,
			int length)
	{

		Toast toast_notification = Toast.makeText(context,
				notificationSavedMessage, length);
		toast_notification.setGravity(Gravity.CENTER, 0, 0);
		toast_notification.show();
	}

}

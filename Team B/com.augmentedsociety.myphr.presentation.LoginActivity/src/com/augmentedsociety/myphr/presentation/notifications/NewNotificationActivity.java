package com.augmentedsociety.myphr.presentation.notifications;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import com.augmentedsociety.myphr.R;
import com.augmentedsociety.myphr.domain.MapperException;
import com.augmentedsociety.myphr.domain.logs.LogEventType;
import com.augmentedsociety.myphr.domain.notifications.Notification;
import com.augmentedsociety.myphr.domain.notifications.NotificationManager;
import com.augmentedsociety.myphr.domain.notifications.NotificationMapper;
import com.augmentedsociety.myphr.presentation.ToastMessage;
import com.augmentedsociety.myphr.presentation.logs.LogEventEmitter;
import com.augmentedsociety.myphr.presentation.vitalsigns.VitalSignsActivity;

import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

/**
 * Activity responsible for creating new notifications
 * 
 * @author Yuri Kitaev
 * 
 */
@SuppressLint("NewApi")
public class NewNotificationActivity extends Activity implements
		OnItemSelectedListener
{
	private long mRepeatInterval = 0;
	private static final long HOUR = 60 * 60 * 1000;
	private static final long DAY = 24 * HOUR;
	private static final String ADDITIONAL_ZERO = "0";
	
	long mIntervals[] =
	{ 0, HOUR / 2, HOUR, 3 * HOUR, 6 * HOUR, 12 * HOUR, DAY, 2 * DAY, 7 * DAY,
			30 * DAY };
	Activity iAct;
	int oldHour, oldMinute, oldDay, oldMonth, oldYear;
	Calendar cal;

	// private TimePicker mTimePicker;
	// private DatePicker mDatePicker;

	@Override
	public void onCreate(Bundle iSavedInstanceState)
	{
		super.onCreate(iSavedInstanceState);
		setContentView(R.layout.new_notification);

		iAct = this;

		Spinner spinner = (Spinner) findViewById(R.id.repeat_spinner);
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
				R.array.notif_repeat_options, android.R.layout.simple_spinner_item);
		adapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(adapter);
		spinner.setOnItemSelectedListener(this);
		TextView titleText = (TextView) findViewById(R.id.notif_title);
		titleText.setText("");

		Date wNow = new Date(System.currentTimeMillis());
		Calendar wCalendar = GregorianCalendar.getInstance();
		wCalendar.setTime(wNow);
		// mTimePicker = (TimePicker) findViewById(R.id.notifTimePicker);
		// mDatePicker = (DatePicker) findViewById(R.id.notifDatePicker);

		int currentapiVersion = android.os.Build.VERSION.SDK_INT;
		if (currentapiVersion >= android.os.Build.VERSION_CODES.FROYO)
		{
			getActionBar().setDisplayShowHomeEnabled(false);
		}

		setDate(null, wCalendar.get(Calendar.YEAR), wCalendar.get(Calendar.MONTH),
				wCalendar.get(Calendar.DAY_OF_MONTH));
		setTime(wCalendar.get(Calendar.HOUR_OF_DAY),wCalendar.get(Calendar.MINUTE));
	}

	/**
	 * Actually creates the new notification
	 * 
	 * @param view
	 */
	public void doCreateNew(View iView)
	{
		if (cal == null)
		{
			new ToastMessage(this, getString(R.string.requirement_time_and_date), 1);
			return;
		}
		String title = "";
		TextView titleText = null;
		try
		{
			titleText = (TextView) findViewById(R.id.notif_title);
			title = (String) titleText.getText().toString();
		} catch (Exception e)
		{
			Toast toast_notification = Toast.makeText(NewNotificationActivity.this,
					e.getMessage(), Toast.LENGTH_LONG);// .show();
			toast_notification.setGravity(Gravity.CENTER, 0, 0);
			toast_notification.show();
			return;
		}

		// Validate
		if (title.length() == 0)
		{
			String pleaseSpecifyNameMessage = getApplicationContext().getResources()
					.getString(R.string.notification_please_specify_name);
			Toast toastNotification = Toast.makeText(NewNotificationActivity.this,
					pleaseSpecifyNameMessage, Toast.LENGTH_LONG);// .show();
			toastNotification.setGravity(Gravity.CENTER, 0, 0);
			toastNotification.show();
			return;
		}

		// Compute the notification date and time in milliseconds
		// Calendar calendar = Calendar.getInstance();

		// calendar.set(Calendar.YEAR, mDatePicker.getYear());
		// calendar.set(Calendar.MONTH, mDatePicker.getMonth());
		// calendar.set(Calendar.DAY_OF_MONTH, mDatePicker.getDayOfMonth());
		// calendar.set(Calendar.HOUR_OF_DAY, mTimePicker.getCurrentHour());
		// calendar.set(Calendar.MINUTE, mTimePicker.getCurrentMinute());
		// calendar.set(Calendar.SECOND, 0);

		long id;
		try
		{
			// Prepare the data
			id = NotificationMapper.getNextAvailableId(getApplicationContext());
			boolean isRecurring = (mRepeatInterval > 0);
			Notification r = new Notification(id, cal.getTime(), isRecurring,
					mRepeatInterval, true, title);

			// Store the notification in the DB
			NotificationMapper.insert(r, getApplicationContext());

			/** Fires a LogEvent to the LogItemEditor */
			LogEventEmitter.fireLogEvent(this, iView.getContext(),
					LogEventType.NOTIF_CREATE);

			// Set the alarm in the OS
			NotificationManager.setAlarm(r, getApplicationContext());

			// Tell the user that the operation is successful
			String notificationSavedMessage = getApplicationContext().getResources()
					.getString(R.string.notification_saved);
			Toast toast_notification = Toast.makeText(NewNotificationActivity.this,
					notificationSavedMessage, Toast.LENGTH_LONG);// .show();
			toast_notification.setGravity(Gravity.CENTER, 0, 0);
			toast_notification.show();
			// Reset the screen
			titleText.setText("");

			// Navigate back the the list of the alarms
			Intent returnIntent = new Intent();
			if (getParent() == null)
			{
				setResult(Activity.RESULT_OK, returnIntent);
			} else
			{
				getParent().setResult(Activity.RESULT_OK, returnIntent);
			}
			finish();

			return;

		} catch (MapperException e)
		{
			Toast toast_notification = Toast.makeText(NewNotificationActivity.this,
					e.getMessage(), Toast.LENGTH_LONG);// .show();
			toast_notification.setGravity(Gravity.CENTER, 0, 0);
			toast_notification.show();
		}
	}

	// @Override
	// public boolean onCreateOptionsMenu(Menu iMenu)
	// {
	// getMenuInflater().inflate(R.menu.new_notification, iMenu);
	// return true;
	// }

	public void onItemSelected(AdapterView<?> iAdapter, View iView, int iPos,
			long iID)
	{
		mRepeatInterval = mIntervals[iPos];
	}

	public void onNothingSelected(AdapterView<?> iView)
	{
		mRepeatInterval = 0;
	}

	private void setDate(View view, int year, int monthOfYear, int dayOfMonth)
	{
		oldYear = year;
		oldMonth = monthOfYear;
		oldDay = dayOfMonth;

		Button dateButton = (Button) iAct.findViewById(R.id.dateButtonReminder);

		Calendar cal = Calendar.getInstance();
		cal.set(year, monthOfYear, dayOfMonth, oldHour, oldMinute, 0);
		dateButton
				.setText(((dayOfMonth) < 10 ? ADDITIONAL_ZERO + (dayOfMonth) : (dayOfMonth))
						+ "."
						+ ((monthOfYear + 1) < 10 ? ADDITIONAL_ZERO + (monthOfYear + 1)
								: (monthOfYear + 1)) + "." + year);

		if(view != null)
		{
			DatePicker d = (DatePicker) view;
			cal.set(d.getYear(), d.getMonth(), d.getDayOfMonth());
		}

		NewNotificationActivity.this.cal = cal;
	}

	public void dateButtonPressed(View iView)
	{
		DatePickerDialog.OnDateSetListener dateCallback = new DatePickerDialog.OnDateSetListener()
		{

			@Override
			public void onDateSet(DatePicker view, int year, int monthOfYear,
					int dayOfMonth)
			{

				setDate(view, year, monthOfYear, dayOfMonth);

			}
		};

		if (oldYear != 0 && oldMonth != 0 && oldDay != 0)
		{
			DatePickerDialog datePickerDialog = new DatePickerDialog(
					iView.getContext(), AlertDialog.THEME_TRADITIONAL, dateCallback,
					oldYear, oldMonth, oldDay);
			datePickerDialog.setCancelable(true);
			datePickerDialog.show();
		} else
		{
			Calendar cal = Calendar.getInstance();
			DatePickerDialog datePickerDialog = new DatePickerDialog(
					iView.getContext(), AlertDialog.THEME_TRADITIONAL, dateCallback,
					cal.get(Calendar.YEAR), cal.get(Calendar.MONTH),
					cal.get(Calendar.DAY_OF_MONTH));
			datePickerDialog.setCancelable(true);
			datePickerDialog.show();
		}
	}
	
	private void setTime( int hourOfDay, int minute)
	{
		Button timeButton = (Button) findViewById(R.id.timeButtonReminder);

		oldMinute = minute;
		oldHour = hourOfDay;

		timeButton.setText(((hourOfDay) < 10 ? ADDITIONAL_ZERO + (hourOfDay) : (hourOfDay))
				+ ":" + (minute < 10 ? ADDITIONAL_ZERO + minute : minute));

		Calendar cal = Calendar.getInstance();
		cal.set(oldYear, oldMonth, oldDay, hourOfDay, minute, 0);

		NewNotificationActivity.this.cal = cal;
	}

	public void timeButtonPressed(View iView)
	{
		TimePickerDialog.OnTimeSetListener timeCallback = new TimePickerDialog.OnTimeSetListener()
		{

			@Override
			public void onTimeSet(TimePicker view, int hourOfDay, int minute)
			{
				setTime(hourOfDay, minute);
			}
		};

		if (oldMinute != 0 && oldHour != 0)
		{
			TimePickerDialog datePickerDialog = new TimePickerDialog(
					iView.getContext(), AlertDialog.THEME_TRADITIONAL, timeCallback,
					oldHour, oldMinute, true);
			datePickerDialog.setCancelable(true);
			datePickerDialog.show();
		} else
		{
			Calendar cal = Calendar.getInstance();
			TimePickerDialog datePickerDialog = new TimePickerDialog(
					iView.getContext(), AlertDialog.THEME_TRADITIONAL, timeCallback,
					cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), true);
			datePickerDialog.setCancelable(true);
			datePickerDialog.show();
		}
	}

	public void doCancel(View iView)
	{
		Intent returnCancel = new Intent();
		setResult(Activity.RESULT_CANCELED, returnCancel);
		finish();
	}

}

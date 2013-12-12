package com.augmentedsociety.myphr.presentation.medicalhistory;

import java.util.Calendar;
import java.util.Date;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.Time;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.augmentedsociety.myphr.R;
import com.augmentedsociety.myphr.domain.Allergy;
import com.augmentedsociety.myphr.domain.AllergyMapper;
import com.augmentedsociety.myphr.domain.MapperException;
import com.augmentedsociety.myphr.domain.MedicalHistory;
import com.augmentedsociety.myphr.domain.MedicalHistoryMapper;
import com.augmentedsociety.myphr.domain.logs.LogEventType;
import com.augmentedsociety.myphr.presentation.ToastMessage;
import com.augmentedsociety.myphr.presentation.logs.LogEventEmitter;

/**
 * Activity responsible for creating new medical history
 * 
 * @author Roger Makram
 * 
 */
@SuppressLint("NewApi")
public class NewMedicalHistoryActivity extends Activity implements
		OnItemSelectedListener
{
	Activity iAct;
	Date tDate;
	Time tTime;
	Long mId;
	int oldMonth, oldDay, oldYear, oldHourOfDay, oldMinute;

	final String ID = "ID";
	final String ADDITIONAL_ZERO = "0";

	@Override
	public void onCreate(Bundle iSavedInstanceState)
	{
		super.onCreate(iSavedInstanceState);
		setContentView(R.layout.new_medical_history);

		this.iAct = this;

		TextView description = (TextView) findViewById(R.id.medHist_description);
		TextView location = (TextView) findViewById(R.id.location_title);
		description.setText("");
		location.setText("");

		DatePicker d = (DatePicker) this.findViewById(R.id.medHistDatePicker);
		TimePicker time = (TimePicker) this.findViewById(R.id.medHistTimePicker);
		Time now = new Time();
		now.setToNow();
		d.updateDate(now.year, now.month, now.monthDay);
		time.setCurrentHour(now.hour);
		time.setCurrentMinute(now.minute);

		int currentapiVersion = android.os.Build.VERSION.SDK_INT;
		if (currentapiVersion >= android.os.Build.VERSION_CODES.FROYO)
		{
			getActionBar().setDisplayShowHomeEnabled(false);
		}
		String id = getIntent().getStringExtra(ID);
		if (id != null)
		{
			int iId = Integer.valueOf(id);
			fillExistingView(iId);
		}

		Calendar cal = Calendar.getInstance();
		tDate = cal.getTime();
		Button dateButton = (Button) findViewById(R.id.dateButtonMedicalHistory);

		dateButton.setText(((cal.get(Calendar.DAY_OF_MONTH)) < 10 ? ADDITIONAL_ZERO
				+ (cal.get(Calendar.DAY_OF_MONTH)) : (cal.get(Calendar.DAY_OF_MONTH)))
				+ "."
				+ ((cal.get(Calendar.MONTH) < 10) ? ADDITIONAL_ZERO
						+ (cal.get(Calendar.MONTH) + 1) : (cal.get(Calendar.MONTH) + 1))
				+ "." + cal.get(Calendar.YEAR));

		Button timeButton = (Button) findViewById(R.id.timeButtonMedicalHistory);
		timeButton.setText(((cal.get(Calendar.HOUR_OF_DAY)) < 10 ? ADDITIONAL_ZERO
				+ (cal.get(Calendar.HOUR_OF_DAY)) : (cal.get(Calendar.HOUR_OF_DAY)))
				+ ":"
				+ (cal.get(Calendar.MINUTE) < 10 ? ADDITIONAL_ZERO
						+ cal.get(Calendar.MINUTE) : cal.get(Calendar.MINUTE)));
	}

	/**
	 * Actually creates the new medical history
	 * 
	 * @param view
	 */
	public void doCreateNew(View iView)
	{
		/** Get the view's current context for proper detection of activity launch */
		Context context = iView.getContext();

		/** References to text box values */
		TextView description, location;
		description = (TextView) this.findViewById(R.id.medHist_description);
		location = (TextView) this.findViewById(R.id.location_title);

		/** References to date */
		// Calendar cal = Calendar.getInstance();
		// DatePicker d = (DatePicker) this.findViewById(R.id.medHistDatePicker);
		// TimePicker time = (TimePicker) this.findViewById(R.id.medHistTimePicker);
		// cal.set(d.getYear(), d.getMonth(), d.getDayOfMonth(),
		// time.getCurrentHour(), time.getCurrentMinute(), 0);

		// Calendar cal = Calendar.getInstance();
		// oldYear = cal.get(Calendar.YEAR);
		// oldMonth = cal.get(Calendar.MONTH);
		// oldDay = cal.get(Calendar.DAY_OF_MONTH);
		// cal.set(Calendar.DAY_OF_MONTH, oldHourOfDay);
		// cal.set(Calendar.MONTH, oldMonth);
		// cal.set(Calendar.YEAR, oldYear);
		// cal.set(Calendar.MINUTE, oldMinute);
		// cal.set(Calendar.HOUR_OF_DAY, oldHourOfDay);
		//
		// tDate = cal.getTime();

		String tDescription, tLocation = "";
		tDescription = description.getText().toString();
		tLocation = location.getText().toString();

		if (!tDescription.isEmpty() && !tLocation.isEmpty())
		{
			try
			{
				MedicalHistoryMapper.insert(
						new MedicalHistory(
								MedicalHistoryMapper.getNextAvailableId(context), tDate,
								tDescription, tLocation), context);

				/** Fires a LogEvent to the LogItemEditor */
				LogEventEmitter.fireLogEvent(this, iView.getContext(),
						LogEventType.MEDICAL_HISTORY_CREATE);

				/**
				 * Calls a short text box confirming data save. Should not occur if an
				 * exception should be caught inside mapper
				 */
				new ToastMessage(context, context.getString(R.string.confirm_save),
						Toast.LENGTH_SHORT);

				// Toast.makeText(context, context.getString(R.string.confirm_save),
				// Toast.LENGTH_SHORT).show();
				Intent myIntent = new Intent(this, ViewMedicalHistoryActivity.class);
				startActivity(myIntent);
			} catch (MapperException e)
			{
				new ToastMessage(context, e.getMessage(), Toast.LENGTH_LONG);
				// Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
			}

			/**
			 * After data save, text box values are reset and focus is placed to the
			 * first entry of the page list
			 */
			description.setText(null);
			location.setText(null);

			// Time now = new Time();
			// now.setToNow();
			// d.updateDate(now.year, now.month, now.monthDay);
			// time.setCurrentHour(now.hour);
			// time.setCurrentMinute(now.minute);

			description.requestFocus();
		} else
		{
			// Request fill medical history fields
			new ToastMessage(context,
					context.getString(R.string.fill_medical_history_info),
					Toast.LENGTH_SHORT);
			// Toast.makeText(context,
			// context.getString(R.string.fill_medical_history_info),
			// Toast.LENGTH_SHORT).show();
		}
	}

	public void handleCancel(View iView)
	{
		Intent myIntent = new Intent(this, ViewMedicalHistoryActivity.class);
		startActivity(myIntent);
	}

	@Override
	public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3)
	{
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0)
	{
	}

	public void dateButtonPressed(View iView)
	{
		DatePickerDialog.OnDateSetListener dateCallback = new DatePickerDialog.OnDateSetListener()
		{

			@Override
			public void onDateSet(DatePicker view, int year, int monthOfYear,
					int dayOfMonth)
			{
				Button dateButton = (Button) iAct
						.findViewById(R.id.dateButtonMedicalHistory);

				oldYear = year;
				oldMonth = monthOfYear;
				oldDay = dayOfMonth;

				Calendar cal = Calendar.getInstance();
				cal.set(year, monthOfYear, dayOfMonth, oldHourOfDay, oldMinute, 0);
				dateButton.setText(((dayOfMonth) < 10 ? ADDITIONAL_ZERO + (dayOfMonth)
						: (dayOfMonth))
						+ "."
						+ ((monthOfYear + 1) < 10 ? ADDITIONAL_ZERO + (monthOfYear + 1)
								: (monthOfYear + 1)) + "." + year);

				DatePicker d = (DatePicker) view;
				cal.set(d.getYear(), d.getMonth(), d.getDayOfMonth());

				tDate = cal.getTime();

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

	public void fillExistingView(long id)
	{
		if (id == 0)
			return;
		Context context = iAct.getApplicationContext();
		try
		{
			MedicalHistory medicalHistory = MedicalHistoryMapper.getOne(id, context);
			EditText desc = (EditText) findViewById(R.id.medHist_description);
			desc.setText(medicalHistory.getDescription());
			EditText location = (EditText) findViewById(R.id.location_title);
			location.setText(medicalHistory.getLocation());
			Button dateButton = (Button) findViewById(R.id.dateButtonMedicalHistory);
			Date iDate = medicalHistory.getDate();
			Calendar cal = Calendar.getInstance();
			cal.setTime(iDate);
			tDate = iDate;

			dateButton
					.setText(((cal.get(Calendar.DAY_OF_MONTH)) < 10 ? ADDITIONAL_ZERO
							+ (cal.get(Calendar.DAY_OF_MONTH)) : (cal
							.get(Calendar.DAY_OF_MONTH)))
							+ "."
							+ ((cal.get(Calendar.MONTH) < 10) ? ADDITIONAL_ZERO
									+ (cal.get(Calendar.MONTH) + 1)
									: (cal.get(Calendar.MONTH) + 1))
							+ "."
							+ cal.get(Calendar.YEAR));

			Button timeButton = (Button) findViewById(R.id.timeButtonMedicalHistory);
			timeButton
					.setText(((cal.get(Calendar.HOUR_OF_DAY)) < 10 ? ADDITIONAL_ZERO
							+ (cal.get(Calendar.HOUR_OF_DAY)) : (cal
							.get(Calendar.HOUR_OF_DAY)))
							+ ":"
							+ (cal.get(Calendar.MINUTE) < 10 ? ADDITIONAL_ZERO
									+ cal.get(Calendar.MINUTE) : cal.get(Calendar.MINUTE)));

		} catch (MapperException e)
		{
			Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
		}
	}

	public void timeButtonPressed(View iView)
	{

		TimePickerDialog.OnTimeSetListener timeCallback = new TimePickerDialog.OnTimeSetListener()
		{

			@Override
			public void onTimeSet(TimePicker view, int hourOfDay, int minute)
			{
				Button timeButton = (Button) findViewById(R.id.timeButtonMedicalHistory);

				oldMinute = minute;
				oldHourOfDay = hourOfDay;

				timeButton.setText(((hourOfDay) < 10 ? ADDITIONAL_ZERO + (hourOfDay)
						: (hourOfDay))
						+ ":"
						+ (minute < 10 ? ADDITIONAL_ZERO + minute : minute));

				Calendar cal = Calendar.getInstance();
				cal.set(oldYear, oldMonth, oldDay, hourOfDay, minute, 0);

				tDate = cal.getTime();
			}
		};

		if (oldMinute != 0 && oldHourOfDay != 0)
		{
			TimePickerDialog datePickerDialog = new TimePickerDialog(
					iView.getContext(), AlertDialog.THEME_TRADITIONAL, timeCallback,
					oldHourOfDay, oldMinute, true);
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
}

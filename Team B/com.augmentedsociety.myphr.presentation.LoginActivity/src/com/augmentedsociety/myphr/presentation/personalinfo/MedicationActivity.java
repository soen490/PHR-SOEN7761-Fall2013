package com.augmentedsociety.myphr.presentation.personalinfo;

import java.util.Calendar;
import java.util.Date;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.text.format.Time;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.augmentedsociety.myphr.R;
import com.augmentedsociety.myphr.domain.MapperException;
import com.augmentedsociety.myphr.domain.Medication;
import com.augmentedsociety.myphr.domain.MedicationMapper;
import com.augmentedsociety.myphr.domain.logs.LogEventType;
import com.augmentedsociety.myphr.presentation.PersonalInformationActivity;
import com.augmentedsociety.myphr.presentation.logs.LogEventEmitter;
import com.augmentedsociety.myphr.presentation.notifications.NewNotificationActivity;

/**
 * Create new medication activity
 * 
 * @author Rajan Jayakumar
 * 
 */

public class MedicationActivity extends PersonalInfoComponent
{
	private final String ADDITIONAL_ZERO = "0";

	Date tStartDate, tEndDate;
	int startOldMonth, startOldDay, startOldYear;
	int endOldMonth, endOldDay, endOldYear;
	int startOldHour, startOldMinute;
	int endOldHour, endOldMinute;

	public MedicationActivity(Activity iAct)
	{
		super(iAct);

		Calendar sCal = Calendar.getInstance();
		Calendar eCal = Calendar.getInstance();
		startOldMonth = sCal.get(Calendar.MONTH);
		startOldDay = sCal.get(Calendar.DAY_OF_MONTH);
		startOldYear = sCal.get(Calendar.YEAR);
		startOldMinute = sCal.get(Calendar.MINUTE);
		startOldHour = sCal.get(Calendar.HOUR_OF_DAY);
		endOldMonth = eCal.get(Calendar.MONTH);
		endOldDay = eCal.get(Calendar.DAY_OF_MONTH);
		endOldYear = eCal.get(Calendar.YEAR);
		endOldMinute = eCal.get(Calendar.MINUTE);
		endOldHour = eCal.get(Calendar.HOUR_OF_DAY);

		Button sDateButton = (Button) mAct
				.findViewById(R.id.startDateButtonMedication);
		sDateButton.setText(((startOldDay) < 10 ? ADDITIONAL_ZERO + (startOldDay)
				: (startOldDay))
				+ "."
				+ ((startOldMonth + 1) < 10 ? ADDITIONAL_ZERO + (startOldMonth + 1)
						: (startOldMonth + 1)) + "." + startOldYear);
		Button eDateButton = (Button) mAct
				.findViewById(R.id.endDateButtonMedication);
		eDateButton.setText(((endOldDay) < 10 ? ADDITIONAL_ZERO + (endOldDay)
				: (endOldDay))
				+ "."
				+ ((endOldMonth + 1) < 10 ? ADDITIONAL_ZERO + (endOldMonth + 1)
						: (endOldMonth + 1)) + "." + endOldYear);

		Button sTimeButton = (Button) mAct
				.findViewById(R.id.startTimeButtonMedication);
		sTimeButton.setText(((startOldHour) < 10 ? ADDITIONAL_ZERO + (startOldHour)
				: (startOldHour))
				+ ":"
				+ (startOldMinute < 10 ? ADDITIONAL_ZERO + startOldMinute
						: startOldMinute));

		Button eTimeButton = (Button) mAct
				.findViewById(R.id.endTimeButtonMedication);
		eTimeButton.setText(((endOldHour) < 10 ? ADDITIONAL_ZERO + (endOldHour)
				: (endOldHour))
				+ ":"
				+ (endOldMinute < 10 ? ADDITIONAL_ZERO + endOldMinute : endOldMinute));
	}

	public MedicationActivity(Activity iAct, long iID)
	{
		super(iAct);
		this.mID = iID;
		this.exists = true;
	}

	public View fillExistingView(View iView)
	{
		Context context = iView.getContext();
		Medication currentMedication;
		try
		{
			currentMedication = MedicationMapper.getOne(mID, context);

			/** References to text box values */
			TextView type, name, dose, concentration, schedule, reason, prescription;
			type = (TextView) mAct.findViewById(R.id.medication_type);
			name = (TextView) mAct.findViewById(R.id.medication_name);
			dose = (TextView) mAct.findViewById(R.id.medication_dosage);
			concentration = (TextView) mAct
					.findViewById(R.id.medication_concentration);
			schedule = (TextView) mAct.findViewById(R.id.medication_frequency);
			reason = (TextView) mAct.findViewById(R.id.medication_reason);
			prescription = (TextView) mAct.findViewById(R.id.medication_prescriber);

			/** Set text box values */
			type.setText(currentMedication.getType());
			name.setText(currentMedication.getName());
			dose.setText(currentMedication.getPosology());
			concentration.setText(currentMedication.getStrength());
			schedule.setText(currentMedication.getFrequency());
			reason.setText(currentMedication.getReasons());
			prescription.setText(currentMedication.getDoctor());

			/** Set date/time picker values */
			Calendar calStart = Calendar.getInstance();
			calStart.setTime(currentMedication.getStartDate());
			DatePicker dStart = (DatePicker) iView
					.findViewById(R.id.datePickerMedStart);
			dStart.init(calStart.get(Calendar.YEAR), calStart.get(Calendar.MONTH),
					calStart.get(Calendar.DAY_OF_MONTH), null);
			TimePicker timeStart = (TimePicker) iView
					.findViewById(R.id.timePickerMedStart);
			timeStart.setCurrentHour(calStart.get(Calendar.HOUR_OF_DAY));
			timeStart.setCurrentMinute(calStart.get(Calendar.MINUTE));

			// Calendar calEnd = Calendar.getInstance();
			// calEnd.setTime(currentMedication.getEndDate());
			// DatePicker dEnd = (DatePicker)
			// iView.findViewById(R.id.datePickerMedEnd);
			// dEnd.init(calEnd.get(Calendar.YEAR), calEnd.get(Calendar.MONTH),
			// calEnd.get(Calendar.DAY_OF_MONTH), null);
			// TimePicker timeEnd = (TimePicker)
			// iView.findViewById(R.id.timePickerMedEnd);
			// timeEnd.setCurrentHour(calEnd.get(Calendar.HOUR_OF_DAY));
			// timeEnd.setCurrentMinute(calEnd.get(Calendar.MINUTE));

			Calendar sCal = Calendar.getInstance();
			sCal.setTime(currentMedication.getStartDate());
			Calendar eCal = Calendar.getInstance();
			eCal.setTime(currentMedication.getEndDate());
			startOldMonth = sCal.get(Calendar.MONTH);
			startOldDay = sCal.get(Calendar.DAY_OF_MONTH);
			startOldYear = sCal.get(Calendar.YEAR);
			startOldMinute = sCal.get(Calendar.MINUTE);
			startOldHour = sCal.get(Calendar.HOUR_OF_DAY);
			endOldMonth = eCal.get(Calendar.MONTH);
			endOldDay = eCal.get(Calendar.DAY_OF_MONTH);
			endOldYear = eCal.get(Calendar.YEAR);
			endOldMinute = eCal.get(Calendar.MINUTE);
			endOldHour = eCal.get(Calendar.HOUR_OF_DAY);

			Button sDateButton = (Button) mAct
					.findViewById(R.id.startDateButtonMedication);
			sDateButton.setText(((startOldDay) < 10 ? ADDITIONAL_ZERO + (startOldDay)
					: (startOldDay))
					+ "."
					+ ((startOldMonth + 1) < 10 ? ADDITIONAL_ZERO + (startOldMonth + 1)
							: (startOldMonth + 1)) + "." + startOldYear);
			Button eDateButton = (Button) mAct
					.findViewById(R.id.endDateButtonMedication);
			eDateButton.setText(((endOldDay) < 10 ? ADDITIONAL_ZERO + (endOldDay)
					: (endOldDay))
					+ "."
					+ ((endOldMonth + 1) < 10 ? ADDITIONAL_ZERO + (endOldMonth + 1)
							: (endOldMonth + 1)) + "." + endOldYear);

			Button sTimeButton = (Button) mAct
					.findViewById(R.id.startTimeButtonMedication);
			sTimeButton.setText(((startOldHour) < 10 ? ADDITIONAL_ZERO
					+ (startOldHour) : (startOldHour))
					+ ":"
					+ (startOldMinute < 10 ? ADDITIONAL_ZERO + startOldMinute
							: startOldMinute));

			Button eTimeButton = (Button) mAct
					.findViewById(R.id.endTimeButtonMedication);
			eTimeButton
					.setText(((endOldHour) < 10 ? ADDITIONAL_ZERO + (endOldHour)
							: (endOldHour))
							+ ":"
							+ (endOldMinute < 10 ? ADDITIONAL_ZERO + endOldMinute
									: endOldMinute));

		} catch (MapperException e)
		{
			Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
		}
		return iView;
	}

	public void submit(View iView)
	{
		/** Get the view's current context for proper detection of activity launch */
		Context context = iView.getContext();

		/** References to text box values */
		TextView type, name, dose, concentration, schedule, reason, prescription;
		type = (TextView) mAct.findViewById(R.id.medication_type);
		name = (TextView) mAct.findViewById(R.id.medication_name);
		dose = (TextView) mAct.findViewById(R.id.medication_dosage);
		concentration = (TextView) mAct.findViewById(R.id.medication_concentration);
		schedule = (TextView) mAct.findViewById(R.id.medication_frequency);
		reason = (TextView) mAct.findViewById(R.id.medication_reason);
		prescription = (TextView) mAct.findViewById(R.id.medication_prescriber);

		/** References to start/end dates */
		Calendar cal = Calendar.getInstance();
		DatePicker dS = (DatePicker) mAct.findViewById(R.id.datePickerMedStart);
		TimePicker timeS = (TimePicker) mAct.findViewById(R.id.timePickerMedStart);
		cal.set(dS.getYear(), dS.getMonth(), dS.getDayOfMonth(),
				timeS.getCurrentHour(), timeS.getCurrentMinute(), 0);

		// Date tStartDate = cal.getTime();

		DatePicker dE = (DatePicker) mAct.findViewById(R.id.datePickerMedEnd);
		TimePicker timeE = (TimePicker) mAct.findViewById(R.id.timePickerMedEnd);
		cal.set(dE.getYear(), dE.getMonth(), dE.getDayOfMonth(),
				timeE.getCurrentHour(), timeE.getCurrentMinute(), 0);

		// Date tEndDate = cal.getTime();

		String tType, tName, tDose, tConcentration, tSchedule, tReason, tPrescription;
		tType = type.getText().toString();
		tName = name.getText().toString();
		tDose = dose.getText().toString();
		tConcentration = concentration.getText().toString();
		tSchedule = schedule.getText().toString();
		tReason = reason.getText().toString();
		tPrescription = prescription.getText().toString();

		if (!tType.isEmpty() && !tName.isEmpty() && !tDose.isEmpty())
		{
			try
			{
				if (exists)
				{
					MedicationMapper.update(new Medication(mID, tType, tName, tDose,
							tConcentration, tSchedule, tStartDate, tEndDate, tReason,
							tPrescription), context);
				} else
				{
					MedicationMapper.insert(
							new Medication(MedicationMapper.getNextAvailableId(context),
									tType, tName, tDose, tConcentration, tSchedule, tStartDate,
									tEndDate, tReason, tPrescription), context);
				}
				/** Fires a LogEvent to the LogItemEditor */
				LogEventEmitter.fireLogEvent(this, iView.getContext(),
						LogEventType.MEDICATION_CREATE);

				/**
				 * Calls a short text box confirming data save. Should not occur if an
				 * exception should be caught inside mapper
				 */
				Toast.makeText(context, context.getString(R.string.confirm_save),
						Toast.LENGTH_SHORT).show();
			} catch (MapperException e)
			{
				Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
			}

			/**
			 * After data save, text box values are reset and focus is placed to the
			 * first entry of the page list
			 */
			type.setText(null);
			name.setText(null);
			dose.setText(null);
			concentration.setText(null);
			schedule.setText(null);
			reason.setText(null);
			prescription.setText(null);

			Time now = new Time();
			now.setToNow();
			dS.updateDate(now.year, now.month, now.monthDay);
			dE.updateDate(now.year, now.month, now.monthDay);
			timeS.setCurrentHour(now.hour);
			timeS.setCurrentMinute(now.minute);
			timeE.setCurrentHour(now.hour);
			timeE.setCurrentMinute(now.minute);

			type.requestFocus();
		} else
		{
			// Request fill medication fields
			Toast.makeText(context, context.getString(R.string.fill_med),
					Toast.LENGTH_SHORT).show();
		}
		cancel(iView);
	}

	@Override
	public void cancel(View iView)
	{
		PersonalInformationActivity wParent = (PersonalInformationActivity) mAct;
		wParent.HandleMedicationsButton(iView);
	}

	public void startDateButtonPressed(View iView)
	{
		super.dateButtonPressed(iView);
		DatePickerDialog.OnDateSetListener dateCallback = new DatePickerDialog.OnDateSetListener()
		{

			@Override
			public void onDateSet(DatePicker view, int year, int monthOfYear,
					int dayOfMonth)
			{
				Button dateButton = (Button) mAct
						.findViewById(R.id.startDateButtonMedication);

				startOldDay = dayOfMonth;
				startOldMonth = monthOfYear;
				startOldYear = year;

				Calendar cal = Calendar.getInstance();
				cal.set(year, monthOfYear, dayOfMonth, 0, 0, 0);
				dateButton.setText(((dayOfMonth) < 10 ? ADDITIONAL_ZERO + (dayOfMonth)
						: (dayOfMonth))
						+ "."
						+ ((monthOfYear + 1) < 10 ? ADDITIONAL_ZERO + (monthOfYear + 1)
								: (monthOfYear + 1)) + "." + year);

				DatePicker d = (DatePicker) view;
				cal.set(d.getYear(), d.getMonth(), d.getDayOfMonth());

				tStartDate = cal.getTime();

			}
		};

		if (startOldYear != 0 && startOldMonth != 0 && startOldDay != 0)
		{
			DatePickerDialog datePickerDialog = new DatePickerDialog(
					iView.getContext(), AlertDialog.THEME_TRADITIONAL, dateCallback,
					startOldYear, startOldMonth, startOldDay);
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

	public void endDateButtonPressed(View iView)
	{
		super.dateButtonPressed(iView);
		DatePickerDialog.OnDateSetListener dateCallback = new DatePickerDialog.OnDateSetListener()
		{

			@Override
			public void onDateSet(DatePicker view, int year, int monthOfYear,
					int dayOfMonth)
			{
				Button dateButton = (Button) mAct
						.findViewById(R.id.endDateButtonMedication);

				endOldDay = dayOfMonth;
				endOldYear = year;
				endOldMonth = monthOfYear;

				Calendar cal = Calendar.getInstance();
				cal.set(year, monthOfYear, dayOfMonth, 0, 0, 0);
				dateButton.setText(((dayOfMonth) < 10 ? ADDITIONAL_ZERO + (dayOfMonth)
						: (dayOfMonth))
						+ "."
						+ ((monthOfYear + 1) < 10 ? ADDITIONAL_ZERO + (monthOfYear + 1)
								: (monthOfYear + 1)) + "." + year);

				DatePicker d = (DatePicker) view;
				cal.set(d.getYear(), d.getMonth(), d.getDayOfMonth());

				tEndDate = cal.getTime();

			}
		};

		if (endOldYear != 0 && endOldMonth != 0 && endOldDay != 0)
		{
			DatePickerDialog datePickerDialog = new DatePickerDialog(
					iView.getContext(), AlertDialog.THEME_TRADITIONAL, dateCallback,
					endOldYear, endOldMonth, endOldDay);
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

	public void startTimeButtonPressed(View iView)
	{

		TimePickerDialog.OnTimeSetListener timeCallback = new TimePickerDialog.OnTimeSetListener()
		{
			@Override
			public void onTimeSet(TimePicker view, int hourOfDay, int minute)
			{
				Button timeButton = (Button) mAct
						.findViewById(R.id.startTimeButtonMedication);

				startOldMinute = minute;
				startOldHour = hourOfDay;

				timeButton.setText(((hourOfDay) < 10 ? ADDITIONAL_ZERO + (hourOfDay) : (hourOfDay))
						+ ":" + (minute < 10 ? ADDITIONAL_ZERO + minute : minute));

				Calendar cal = Calendar.getInstance();
				cal.set(startOldYear, startOldMonth, startOldDay, hourOfDay, minute, 0);

				tStartDate = cal.getTime();
			}
		};

		if (startOldMinute != 0 && startOldHour != 0)
		{
			TimePickerDialog datePickerDialog = new TimePickerDialog(
					iView.getContext(), AlertDialog.THEME_TRADITIONAL, timeCallback,
					startOldHour, startOldMinute, true);
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

	public void endTimeButtonPressed(View iView)
	{
		TimePickerDialog.OnTimeSetListener timeCallback = new TimePickerDialog.OnTimeSetListener()
		{
			@Override
			public void onTimeSet(TimePicker view, int hourOfDay, int minute)
			{
				Button timeButton = (Button) mAct
						.findViewById(R.id.endTimeButtonMedication);

				endOldMinute = minute;
				endOldHour = hourOfDay;

				timeButton.setText(((hourOfDay) < 10 ? ADDITIONAL_ZERO + (hourOfDay) : (hourOfDay))
						+ ":" + (minute < 10 ? ADDITIONAL_ZERO + minute : minute));

				Calendar cal = Calendar.getInstance();
				cal.set(endOldYear, endOldMonth, endOldDay, hourOfDay, minute, 0);

				tEndDate = cal.getTime();
			}
		};

		if (endOldMinute != 0 && endOldHour != 0)
		{
			TimePickerDialog datePickerDialog = new TimePickerDialog(
					iView.getContext(), AlertDialog.THEME_TRADITIONAL, timeCallback,
					endOldHour, endOldMinute, true);
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
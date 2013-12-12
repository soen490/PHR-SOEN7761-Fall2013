package com.augmentedsociety.myphr.presentation.personalinfo;

import java.util.Calendar;
import java.util.Date;

import com.augmentedsociety.myphr.R;

import com.augmentedsociety.myphr.presentation.PersonalInformationActivity;
import com.augmentedsociety.myphr.presentation.logs.LogEventEmitter;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.text.format.Time;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;

import android.widget.Toast;

import com.augmentedsociety.myphr.domain.Allergy;
import com.augmentedsociety.myphr.domain.AllergyMapper;
import com.augmentedsociety.myphr.domain.ImmunizationMapper;
import com.augmentedsociety.myphr.domain.Immunization;
import com.augmentedsociety.myphr.domain.MapperException;
import com.augmentedsociety.myphr.domain.logs.LogEventType;

/**
 * Create new immunization activity
 * 
 * @author Rajan Jayakumar
 * 
 */

public class ImmunizationActivity extends PersonalInfoComponent
{
	int oldMinute, oldHour, oldYear, oldMonth, oldDay;
	Date tDate;
	private final String ADDITIONAL_ZERO = "0";

	public ImmunizationActivity(Activity iAct)
	{
		super(iAct);
		
		Calendar sCal = Calendar.getInstance();
		oldMonth = sCal.get(Calendar.MONTH);
		oldDay = sCal.get(Calendar.DAY_OF_MONTH);
		oldYear = sCal.get(Calendar.YEAR);
		oldMinute = sCal.get(Calendar.MINUTE);
		oldHour = sCal.get(Calendar.HOUR_OF_DAY);

		Button sDateButton = (Button) mAct.findViewById(R.id.dateButtonImm);
		sDateButton.setText(((oldDay) < 10 ? ADDITIONAL_ZERO + (oldDay) : (oldDay)) + "."
				+ ((oldMonth + 1) < 10 ? ADDITIONAL_ZERO + (oldMonth + 1) : (oldMonth + 1)) + "."
				+ oldYear);

		Button sTimeButton = (Button) mAct
				.findViewById(R.id.timeButtonImm);
		sTimeButton.setText(((oldHour) < 10 ? ADDITIONAL_ZERO + (oldHour) : (oldHour)) + ":"
				+ (oldMinute < 10 ? ADDITIONAL_ZERO + oldMinute : oldMinute));
	}

	public ImmunizationActivity(Activity iAct, long iID)
	{
		super(iAct);
		this.mID = iID;
		this.exists = true;
		
	}

	public View fillExistingView(View iView)
	{
		Context context = iView.getContext();
		Immunization currentImmunization;
		try
		{
			currentImmunization = ImmunizationMapper.getOne(mID, context);

			TextView type, name, mfg, lot, dose, location, followup, comments;
			type = (TextView) iView.findViewById(R.id.immunization_type);
			name = (TextView) iView.findViewById(R.id.immunization_name);
			mfg = (TextView) iView.findViewById(R.id.immunization_mfg);
			lot = (TextView) iView.findViewById(R.id.immunization_lot);
			dose = (TextView) iView.findViewById(R.id.immunization_dosage);
			location = (TextView) iView.findViewById(R.id.immunization_loc);
			followup = (TextView) iView.findViewById(R.id.immunization_followUp);
			comments = (TextView) iView.findViewById(R.id.immunization_comments);

			type.setText(currentImmunization.getType());
			name.setText(currentImmunization.getName());
			mfg.setText(currentImmunization.getManufacturer());
			lot.setText(currentImmunization.getLot_number());
			dose.setText(currentImmunization.getPosology());
			location.setText(currentImmunization.getLocation());
			followup.setText(currentImmunization.getDetails());
			comments.setText(currentImmunization.getComments());

			RadioButton rRoute;
			rRoute = (RadioButton) iView.findViewById(R.id.oral_set);
			if (currentImmunization.getRoute().equals(rRoute.getText()))
			{
				rRoute.setChecked(true);
			}
			rRoute = (RadioButton) iView.findViewById(R.id.inject_set);
			if (currentImmunization.getRoute().equals(rRoute.getText()))
			{
				rRoute.setChecked(true);
			}

			// Calendar cal = Calendar.getInstance();
			// cal.setTime(currentImmunization.getDate());
			// DatePicker d = (DatePicker) iView.findViewById(R.id.datePickerImm);
			// d.init(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH),
			// cal.get(Calendar.DAY_OF_MONTH), null);
			// TimePicker time = (TimePicker) iView.findViewById(R.id.timePickerImm);
			// time.setCurrentHour(cal.get(Calendar.HOUR_OF_DAY));
			// time.setCurrentMinute(cal.get(Calendar.MINUTE));

			Calendar sCal = Calendar.getInstance();
			sCal.setTime(currentImmunization.getDate());
			oldMonth = sCal.get(Calendar.MONTH);
			oldDay = sCal.get(Calendar.DAY_OF_MONTH);
			oldYear = sCal.get(Calendar.YEAR);
			oldMinute = sCal.get(Calendar.MINUTE);
			oldHour = sCal.get(Calendar.HOUR_OF_DAY);

			Button sDateButton = (Button) mAct.findViewById(R.id.dateButtonImm);
			sDateButton.setText(((oldDay) < 10 ? ADDITIONAL_ZERO + (oldDay) : (oldDay)) + "."
					+ ((oldMonth + 1) < 10 ? ADDITIONAL_ZERO + (oldMonth + 1) : (oldMonth + 1)) + "."
					+ oldYear);

			Button sTimeButton = (Button) mAct
					.findViewById(R.id.timeButtonImm);
			sTimeButton.setText(((oldHour) < 10 ? ADDITIONAL_ZERO + (oldHour) : (oldHour)) + ":"
					+ (oldMinute < 10 ? ADDITIONAL_ZERO + oldMinute : oldMinute));

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
		TextView type, name, mfg, lot, dose, location, followup, comments;
		type = (TextView) mAct.findViewById(R.id.immunization_type);
		name = (TextView) mAct.findViewById(R.id.immunization_name);
		mfg = (TextView) mAct.findViewById(R.id.immunization_mfg);
		lot = (TextView) mAct.findViewById(R.id.immunization_lot);
		dose = (TextView) mAct.findViewById(R.id.immunization_dosage);
		location = (TextView) mAct.findViewById(R.id.immunization_loc);
		followup = (TextView) mAct.findViewById(R.id.immunization_followUp);
		comments = (TextView) mAct.findViewById(R.id.immunization_comments);

		/** References to date */
		// Calendar cal = Calendar.getInstance();
		// DatePicker d = (DatePicker) mAct.findViewById(R.id.datePickerImm);
		// TimePicker time = (TimePicker) mAct.findViewById(R.id.timePickerImm);
		// cal.set(d.getYear(), d.getMonth(), d.getDayOfMonth(),
		// time.getCurrentHour(), time.getCurrentMinute(), 0);

		// Date tDate = cal.getTime();

		/** References to Radio Button Group */
		RadioGroup route;
		route = (RadioGroup) mAct.findViewById(R.id.radioGroupImm);

		String tType, tName, tMfg, tLot, tDose, tLocation, tFollowup, tComments, tRoute = "";
		tType = type.getText().toString();
		tName = name.getText().toString();
		tMfg = mfg.getText().toString();
		tLot = lot.getText().toString();
		tDose = dose.getText().toString();
		tLocation = location.getText().toString();
		tFollowup = followup.getText().toString();
		tComments = comments.getText().toString();

		RadioButton rRoute;
		rRoute = (RadioButton) mAct.findViewById(route.getCheckedRadioButtonId());
		if (rRoute != null)
		{
			tRoute = rRoute.getText().toString();
		}

		if (!tType.isEmpty() && !tName.isEmpty() && !tRoute.isEmpty())
		{
			try
			{
				if (exists)
				{
					ImmunizationMapper.update(new Immunization(mID, tType, tName, tMfg,
							tLot, tRoute, tDose, tDate, tLocation, tFollowup, tComments),
							context);
				} else
				{
					ImmunizationMapper.insert(
							new Immunization(ImmunizationMapper.getNextAvailableId(context),
									tType, tName, tMfg, tLot, tRoute, tDose, tDate, tLocation,
									tFollowup, tComments), context);
				}
				/** Fires a LogEvent to the LogItemEditor */
				LogEventEmitter.fireLogEvent(this, iView.getContext(),
						LogEventType.IMMUNIZATION_CREATE);

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
			mfg.setText(null);
			lot.setText(null);
			dose.setText(null);
			location.setText(null);
			followup.setText(null);
			comments.setText(null);

			// Time now = new Time();
			// now.setToNow();
			// d.updateDate(now.year, now.month, now.monthDay);
			// time.setCurrentHour(now.hour);
			// time.setCurrentMinute(now.minute);

			route.clearCheck();

			type.requestFocus();
		} else
		{
			// Request fill immunization fields
			Toast.makeText(context, context.getString(R.string.fill_imm),
					Toast.LENGTH_SHORT).show();
		}
		cancel(iView);
	}

	@Override
	public void cancel(View iView)
	{
		PersonalInformationActivity wParent = (PersonalInformationActivity) mAct;
		wParent.HandleImmunizationsButton(iView);
	}

	public void dateButtonPressedImm(View iView)
	{
		super.dateButtonPressed(iView);
		DatePickerDialog.OnDateSetListener dateCallback = new DatePickerDialog.OnDateSetListener()
		{

			@Override
			public void onDateSet(DatePicker view, int year, int monthOfYear,
					int dayOfMonth)
			{
				Button dateButton = (Button) mAct.findViewById(R.id.dateButtonImm);

				oldDay = dayOfMonth;
				oldYear = year;
				oldMonth = monthOfYear;

				Calendar cal = Calendar.getInstance();
				cal.set(year, monthOfYear, dayOfMonth, 0, 0, 0);
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

	public void timeButtonPressedImm(View iView)
	{
		TimePickerDialog.OnTimeSetListener timeCallback = new TimePickerDialog.OnTimeSetListener()
		{
			@Override
			public void onTimeSet(TimePicker view, int hourOfDay, int minute)
			{
				Button timeButton = (Button) mAct.findViewById(R.id.timeButtonImm);

				oldMinute = minute;
				oldHour = hourOfDay;

				timeButton.setText(((hourOfDay) < 10 ? ADDITIONAL_ZERO + (hourOfDay) : (hourOfDay))
						+ ":" + (minute < 10 ? ADDITIONAL_ZERO + minute : minute));

				Calendar cal = Calendar.getInstance();
				cal.set(oldYear, oldMonth, oldDay, hourOfDay, minute, 0);

				tDate = cal.getTime();
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
}
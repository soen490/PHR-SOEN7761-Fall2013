package com.augmentedsociety.myphr.presentation.vitalsigns;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.ListIterator;
import java.util.Map;
import java.util.TreeMap;
import java.util.Vector;

import android.R.drawable;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.TimePicker;

import com.augmentedsociety.myphr.R;
import com.augmentedsociety.myphr.domain.AbstractReading;
import com.augmentedsociety.myphr.domain.BloodPressureMapper;
import com.augmentedsociety.myphr.domain.BloodPressureReading;
import com.augmentedsociety.myphr.domain.BloodSugarMapper;
import com.augmentedsociety.myphr.domain.BloodSugarReading;
import com.augmentedsociety.myphr.domain.DefaultPageMapper;
import com.augmentedsociety.myphr.domain.MapperException;
import com.augmentedsociety.myphr.domain.OxygenSaturationMapper;
import com.augmentedsociety.myphr.domain.OxygenSaturationReading;
import com.augmentedsociety.myphr.domain.RecordDateMapper;
import com.augmentedsociety.myphr.domain.RecordDateReading;
import com.augmentedsociety.myphr.domain.TemperatureMapper;
import com.augmentedsociety.myphr.domain.TemperatureReading;
import com.augmentedsociety.myphr.domain.WeightMapper;
import com.augmentedsociety.myphr.domain.WeightReading;
import com.augmentedsociety.myphr.domain.commands.CommandAction;
import com.augmentedsociety.myphr.presentation.MenuActivity;
import com.augmentedsociety.myphr.presentation.PreparationActivity;
import com.augmentedsociety.myphr.presentation.SpeechHelp;
import com.augmentedsociety.myphr.presentation.customviews.SquareImageButton;


/**
 * Activity which handles everything to do with vital signs
 * 
 * @author Dipesh
 */
public class VitalSignsActivity extends MenuActivity
{

	final static int WEIGHT = 0;
	final static int O2 = 1;
	final static int TEMPERATURE = 2;
	final static int BLOOD_PRESSURE_HR = 3;
	final static int BLOOD_SUGAR = 4;
	final static int OVERVIEW = 5;
	final static int BLOOD_PRESSURE_SYS = 6;
	public final static int BLOOD_PRESSURE_DIA = 7;

	private final String OVERVIEW_HELP = "overview";
	private final String WEIGHT_HELP = "bodyWeight";
	private final String TEMPERATURE_HELP = "temperature";
	private final String O2_SATURATION_HELP = "o2Saturation";
	private final String BLOOD_SUGAR_HELP = "bloodSugar";
	private final String BLOOD_PRESSURE_HELP = "bloodPressure";

	static Map<Integer, SquareImageButton> SQI_BUTTON = new TreeMap<Integer, SquareImageButton>();

	private int mCurrentSelection;
	private VitalSignComponent mSubActivity;
	private boolean inMeasurementOne;
	static public boolean nextButtonPressed;
	static public boolean doneButtonPressed;

	public static Activity VITAL_SIGN_ACTIVITY;

	private int mMonth;
	private int mDay;
	private int mYear;
	private int mHour;
	private int mMinute;
	private int mSecond;
	private Date mDate;

	// private HashMap<String, String> mRecordedDates;

	private Date leftDate;
	private Date rightDate;

	boolean mIgnoreTimeSet = false;

	// global because it must be cancelable by press
	// cancel date dialog
	private TimePickerDialog timePickerDialog;

	// the last created acitivities
	private WeightActivity mLastWeightAct;
	private BloodPressureActivity mLastBloodPressureAct;
	private BloodSugarActivity mLastBloodSugarAct;
	private O2Activity mLastO2Act;
	private TemperatureActivity mLastTemperatureAct;

	// Arraylist with the recorded data
	ArrayList<? extends AbstractReading> bloodPressureArray;
	ArrayList<WeightReading> weightArray;
	ArrayList<OxygenSaturationReading> oxygenSatArray;
	ArrayList<TemperatureReading> tempArray;
	ArrayList<BloodSugarReading> bloodSugarArray;

	ArrayList<RecordDateReading> mMeasurements;

	ArrayList<String> monthName;

	BloodPressureReading leftBloodPressureValue = null;
	BloodPressureReading rightBloodPressureValue = null;
	WeightReading leftWeightValue = null;
	WeightReading rightWeightValue = null;
	OxygenSaturationReading leftOxygenSaturationValue = null;
	OxygenSaturationReading rightOxygenSaturationValue = null;
	TemperatureReading leftTemperatureValue = null;
	TemperatureReading rightTemperatureValue = null;
	BloodSugarReading leftBloodSugarValue = null;
	BloodSugarReading rightBloodSugarValue = null;

	EditText heartRate;
	EditText diastolic;
	EditText systolic;
	EditText weight;
	EditText o2percent;
	EditText temperature;
	EditText bloodSugar;

	EditText heartRateO;
	EditText diastolicO;
	EditText systolicO;
	EditText weightO;
	EditText o2percentO;
	EditText temperatureO;
	EditText bloodSugarO;

	@SuppressLint("NewApi")
	@Override
	public void onCreate(Bundle iSavedInstanceState)
	{

		super.onCreate(iSavedInstanceState);
		setContentView(R.layout.vital_signs);
		findViewById(R.id.vital_signs_welcome_page).setVisibility(View.VISIBLE);
		stopedCounting();
		instantiateEditTextVariables();
		fillSquareImageButtonMap();
		fillMonthName();

		VITAL_SIGN_ACTIVITY = this;
		mCurrentSelection = OVERVIEW;

		createTimeStamp();
		findAllMeasurementValues(getApplicationContext());
		refreshRecordedData();

		changeDateText(mYear, mMonth, mDay);
		changeTimeText(mHour, mMinute);

		int currentapiVersion = android.os.Build.VERSION.SDK_INT;
		if (currentapiVersion >= android.os.Build.VERSION_CODES.HONEYCOMB)
		{
			getActionBar().setDisplayShowHomeEnabled(false);
		}

		if (PreparationActivity.firstStart)
			SpeechHelp.getInstance().playTutorial(this, OVERVIEW_HELP);

		// dirty method for finding out if the keyboard is hidden (screen has
		// changed). Because there is no working built-in function for this task.
		final View activityRootView = findViewById(R.id.vitalSignsActivity);
		activityRootView.getViewTreeObserver().addOnGlobalLayoutListener(
				new ViewTreeObserver.OnGlobalLayoutListener()
				{
					@Override
					public void onGlobalLayout()
					{
						// if next button was pressed
						// dont go back to the overview
						if (nextButtonPressed || doneButtonPressed)
						{
							nextButtonPressed = false;
							doneButtonPressed = false;
							return;
						}
						// Galaxy Nexus no keyboard: heightDiff =146
						// activev keyboard: heightDiff =590
						int heightDiff = activityRootView.getRootView().getHeight()
								- activityRootView.getHeight();
						if (100 < heightDiff && heightDiff < 300)
						{ // if more than 100 pixels, its probably a keyboard...

							Log.d("DEBUG", "KEYBOARD HIDDEN");
							switch (mCurrentSelection)
							{
							case OVERVIEW:
								// reset the measurement indicator
								// when going to the overview
								// trySwitchToOverview();
								inMeasurementOne = false;
								break;
							case BLOOD_PRESSURE_HR:
								trySwitchToOverview();
								break;
							case BLOOD_PRESSURE_DIA:
								trySwitchToOverview();
								break;
							case BLOOD_PRESSURE_SYS:
								trySwitchToOverview();
								break;
							case WEIGHT:
								trySwitchToOverview();
								break;
							case TEMPERATURE:
								trySwitchToOverview();
								break;
							case O2:
								trySwitchToOverview();
								break;
							case BLOOD_SUGAR:
								trySwitchToOverview();
								break;
							default:
								inMeasurementOne = false;
							}
						} else if (heightDiff > 300 && mCurrentSelection == OVERVIEW)
						{
							trySwitchToOverview();
						}
					}
				});
	}

	/**
	 * This function will be called twice within the keyboard disappearing event.
	 * It necessary to indicate that the keyboard has changed
	 */
	private void trySwitchToOverview()
	{
		// Every switch to a measurement will trigger
		// one change event.
		// That's why it is necessary to use one variable
		// for remembering the state
		if (inMeasurementOne)
		{
			inMeasurementOne = false;
			handleOverView(null);
			setTitle("Record Measurement");
			this.hl_timeout.removeCallbacks(DoOnTimeOut);
			return;
		} else
		{

			inMeasurementOne = true;
		}
	}

	@Override
	protected void onStart()
	{
		super.onStart();
		this.hl_timeout.removeCallbacks(DoOnTimeOut);
		setMeasurementProfileOptions();
	}

	/**
	 * Adds the selected measurement profiles which were set in the measurement
	 * profile options.
	 */
	private void setMeasurementProfileOptions()
	{
		ArrayList<SquareImageButton> imgBtns = new ArrayList<SquareImageButton>();
		imgBtns.add((SquareImageButton) findViewById(R.id.blood_pressure));
		imgBtns.add((SquareImageButton) findViewById(R.id.body_weight));
		imgBtns.add((SquareImageButton) findViewById(R.id.o2_saturation));
		imgBtns.add((SquareImageButton) findViewById(R.id.body_temperature));
		imgBtns.add((SquareImageButton) findViewById(R.id.blood_sugar));
		String measurements = "0";
		try
		{
			measurements = DefaultPageMapper.get(getApplicationContext());
		} catch (MapperException e)
		{
			e.printStackTrace();
		}
		int i = 1;
		for (SquareImageButton btn : imgBtns)
		{
			if ((Integer.valueOf(measurements) & i) == i)
			{
				btn.setVisibility(View.VISIBLE);
				showField(btn);
			} else
			{
				btn.setVisibility(View.INVISIBLE);
				hideField(btn);
			}
			i = (i * 2); // shift one bit left
		}
	}

	private void showField(SquareImageButton btn)
	{
		changeFieldVisibility(btn, View.VISIBLE);
	}

	private void hideField(SquareImageButton btn)
	{
		changeFieldVisibility(btn, View.GONE);
	}

	/**
	 * Change the visibility of the text fields which are linked to the
	 * SquareImageButton
	 * 
	 * @param btn
	 *          - corresponding text fields to this button will be affected
	 * @param visibility
	 *          - change the visibility to this state
	 */
	private void changeFieldVisibility(SquareImageButton btn, int visibility)
	{
		switch (btn.getId())
		{
		case R.id.blood_pressure:
			((EditText) findViewById(R.id.blood_pressure_diastolic_valueO))
					.setVisibility(visibility);
			((EditText) findViewById(R.id.blood_pressure_systolic_valueO))
					.setVisibility(visibility);
			((EditText) findViewById(R.id.blood_pressure_heart_rate_valueO))
					.setVisibility(visibility);

			((TextView) findViewById(R.id.txtBPM)).setVisibility(visibility);
			((TextView) findViewById(R.id.txtBPMUnit)).setVisibility(visibility);

			((TextView) findViewById(R.id.txtSys)).setVisibility(visibility);
			((TextView) findViewById(R.id.txtSysUnit)).setVisibility(visibility);

			((TextView) findViewById(R.id.txtDia)).setVisibility(visibility);
			((TextView) findViewById(R.id.txtDiaUnit)).setVisibility(visibility);

			break;

		case R.id.body_weight:
			((EditText) findViewById(R.id.weight_valueO)).setVisibility(visibility);
			((TextView) findViewById(R.id.txtWeight)).setVisibility(visibility);
			((TextView) findViewById(R.id.txtWeightUnit)).setVisibility(visibility);
			break;
		case R.id.o2_saturation:
			((EditText) findViewById(R.id.o2percent_valueO))
					.setVisibility(visibility);
			((TextView) findViewById(R.id.txtO2)).setVisibility(visibility);
			((TextView) findViewById(R.id.txtO2Unit)).setVisibility(visibility);
			break;
		case R.id.body_temperature:
			((EditText) findViewById(R.id.temperature_valueO))
					.setVisibility(visibility);
			((TextView) findViewById(R.id.txtTemp)).setVisibility(visibility);
			((TextView) findViewById(R.id.txtTempUnit)).setVisibility(visibility);
			break;
		case R.id.blood_sugar:
			((EditText) findViewById(R.id.blood_sugar_valueO))
					.setVisibility(visibility);
			((TextView) findViewById(R.id.txtBlood)).setVisibility(visibility);
			((TextView) findViewById(R.id.txtBloodUnit)).setVisibility(visibility);

			break;
		default:
			break;
		}

	}

	private void instantiateEditTextVariables()
	{
		heartRate = (EditText) findViewById(R.id.blood_pressure_heart_rate_value);
		diastolic = (EditText) findViewById(R.id.blood_pressure_diastolic_value);
		systolic = (EditText) findViewById(R.id.blood_pressure_systolic_value);
		weight = (EditText) findViewById(R.id.weight_value);
		o2percent = (EditText) findViewById(R.id.o2percent_value);
		temperature = (EditText) findViewById(R.id.temperature_value);
		bloodSugar = (EditText) findViewById(R.id.blood_sugar_value);

		heartRateO = (EditText) findViewById(R.id.blood_pressure_heart_rate_valueO);
		diastolicO = (EditText) findViewById(R.id.blood_pressure_diastolic_valueO);
		systolicO = (EditText) findViewById(R.id.blood_pressure_systolic_valueO);
		weightO = (EditText) findViewById(R.id.weight_valueO);
		o2percentO = (EditText) findViewById(R.id.o2percent_valueO);
		temperatureO = (EditText) findViewById(R.id.temperature_valueO);
		bloodSugarO = (EditText) findViewById(R.id.blood_sugar_valueO);
	}

	private void fillSquareImageButtonMap()
	{
		SQI_BUTTON.put(BLOOD_PRESSURE_HR,
				(SquareImageButton) findViewById(R.id.blood_pressure));
		SQI_BUTTON.put(BLOOD_PRESSURE_SYS,
				(SquareImageButton) findViewById(R.id.blood_pressure));
		SQI_BUTTON.put(BLOOD_PRESSURE_DIA,
				(SquareImageButton) findViewById(R.id.blood_pressure));
		SQI_BUTTON.put(BLOOD_SUGAR,
				(SquareImageButton) findViewById(R.id.blood_sugar));
		SQI_BUTTON.put(TEMPERATURE,
				(SquareImageButton) findViewById(R.id.body_temperature));
		SQI_BUTTON.put(WEIGHT, (SquareImageButton) findViewById(R.id.body_weight));
		SQI_BUTTON.put(O2, (SquareImageButton) findViewById(R.id.o2_saturation));
	}

	private void fillMonthName()
	{
		monthName = new ArrayList<String>();
		monthName.add("Jan");
		monthName.add("Feb");
		monthName.add("Mar");
		monthName.add("Apr");
		monthName.add("May");
		monthName.add("Jun");
		monthName.add("Jul");
		monthName.add("Aug");
		monthName.add("Sep");
		monthName.add("Oct");
		monthName.add("Nov");
		monthName.add("Dec");
	}

	public int getCurrentSelection()
	{
		return mCurrentSelection;
	}

	public void createTimeStamp()
	{
		applyCurrentTime();
	}

	/**
	 * Set the time variables on this class with the current time and date
	 */
	private void applyCurrentTime()
	{
		Calendar cal;
		cal = new GregorianCalendar();
		mMonth = cal.get(Calendar.MONTH); // the month starts with zero
		mDay = cal.get(Calendar.DAY_OF_MONTH);
		mYear = cal.get(Calendar.YEAR);
		mHour = cal.get(Calendar.HOUR_OF_DAY);
		mMinute = cal.get(Calendar.MINUTE);
		mSecond = cal.get(Calendar.SECOND);
		mDate = cal.getTime();
	}

	/**
	 * Set the time for the global time variable
	 * 
	 * @param second
	 * @param minute
	 * @param hourOfDay
	 * @param day
	 * @param month
	 * @param year
	 */
	public void applyCustomTime(int second, int minute, int hourOfDay, int day,
			int month, int year)
	{
		Calendar cal;
		cal = new GregorianCalendar(year, month, day, hourOfDay, minute, second);
		mMonth = month; // the month starts with zero
		mDay = day;
		mYear = year;
		mHour = hourOfDay;
		mMinute = minute;
		mSecond = second;

		mDate = cal.getTime();

	}

	/**
	 * Update the closest past and future date depending on the mDate value.
	 */
	public void refreshRecordedData()
	{
		ImageView datePlus = (ImageView) findViewById(R.id.datePlus);
		ImageView dateMinus = (ImageView) findViewById(R.id.dateMinus);

		fillInLeftAndRightMeasurements();

		if (leftBloodPressureValue != null || leftWeightValue != null
				|| leftOxygenSaturationValue != null || leftTemperatureValue != null
				|| leftBloodSugarValue != null)
		{
			dateMinus.setVisibility(View.VISIBLE);
		} else
		{
			dateMinus.setVisibility(View.INVISIBLE);
		}
		if (rightBloodPressureValue != null || rightWeightValue != null
				|| rightOxygenSaturationValue != null || rightTemperatureValue != null
				|| rightBloodSugarValue != null)
		{
			datePlus.setVisibility(View.VISIBLE);
			datePlus.setImageResource(R.drawable.arrow_right_small);
		} else
		{
			datePlus.setVisibility(View.INVISIBLE);
		}
	}

	/**
	 * 
	 * @return ArrayList of the distinct dates
	 * @throws MapperException
	 */
	public ArrayList<RecordDateReading> getDistinctDates() throws MapperException
	{
		ArrayList<RecordDateReading> result = RecordDateMapper.findAll(this);
		ListIterator<RecordDateReading> iter = result.listIterator();

		ArrayList<RecordDateReading> resultArray = new ArrayList<RecordDateReading>();

		while (iter.hasNext())
		{
			RecordDateReading rec = iter.next();
			resultArray.add(rec);
		}
		return resultArray;
	}

	/**
	 * 
	 * @return ArrayList of the dates with ID and type
	 * @throws MapperException
	 */
	public ArrayList<RecordDateReading> getDates() throws MapperException
	{
		ArrayList<RecordDateReading> result = RecordDateMapper.findAll(this);
		ListIterator<RecordDateReading> iter = result.listIterator();

		ArrayList<RecordDateReading> resultArray = new ArrayList<RecordDateReading>();

		while (iter.hasNext())
		{
			RecordDateReading rec = iter.next();
			resultArray.add(rec);
		}
		return resultArray;
	}

	/**
	 * Try to find the closest future and historical record next to the current
	 * date mDate only for the activated measurements.
	 * 
	 */
	public void fillInLeftAndRightMeasurements()
	{
		leftDate = null;
		rightDate = null;
		if (mMeasurements == null)
			mMeasurements = getMeasurements();

		try
		{
			int measurements = Integer.valueOf(DefaultPageMapper
					.get(getApplicationContext()));
			if ((measurements & 1) == 1) // Blood Pressure
			{
				// sets the previous and next value for the Blood Pressure if available
				Vector<? extends AbstractReading> bloodPressureVec = getLeftAndRightAbstractReading(bloodPressureArray);
				leftBloodPressureValue = (BloodPressureReading) bloodPressureVec.get(0);
				rightBloodPressureValue = (BloodPressureReading) bloodPressureVec
						.get(1);
				leftDate = null;
				rightDate = null;
			}
			if ((measurements & 2) == 2)
			{
				Vector<? extends AbstractReading> weightVec = getLeftAndRightAbstractReading(weightArray);
				leftWeightValue = (WeightReading) weightVec.get(0);
				rightWeightValue = (WeightReading) weightVec.get(1);
				// sets the previous and next value for the oxygen saturation if
				// available
				leftDate = null;
				rightDate = null;
			}
			if ((measurements & 4) == 4)
			{
				Vector<? extends AbstractReading> oxySatVec = getLeftAndRightAbstractReading(oxygenSatArray);
				leftOxygenSaturationValue = (OxygenSaturationReading) oxySatVec.get(0);
				rightOxygenSaturationValue = (OxygenSaturationReading) oxySatVec.get(1);
				// sets the previous and next value for the temperature if available
				leftDate = null;
				rightDate = null;
			}
			if ((measurements & 8) == 8)
			{
				Vector<? extends AbstractReading> tempVec = getLeftAndRightAbstractReading(tempArray);
				leftTemperatureValue = (TemperatureReading) tempVec.get(0);
				rightTemperatureValue = (TemperatureReading) tempVec.get(1);
				// sets the previous and next value for the blood sugar if available
				leftDate = null;
				rightDate = null;
			}
			if ((measurements & 16) == 16)
			{
				Vector<? extends AbstractReading> bloodSugarVec = getLeftAndRightAbstractReading(bloodSugarArray);
				leftBloodSugarValue = (BloodSugarReading) bloodSugarVec.get(0);
				rightBloodSugarValue = (BloodSugarReading) bloodSugarVec.get(1);
			}
		} catch (MapperException e)
		{
		}

		setClosestDate();
	}

	/**
	 * Try to find the closest historical date to the current selected date
	 */
	private void setClosestDate()
	{
		Date leftClosestDate = null;
		Date rightClosestDate = null;

		ArrayList<AbstractReading> leftMeasurement = new ArrayList<AbstractReading>();
		ArrayList<AbstractReading> rightMeasurement = new ArrayList<AbstractReading>();
		leftMeasurement.add(leftBloodPressureValue);
		leftMeasurement.add(leftWeightValue);
		leftMeasurement.add(leftOxygenSaturationValue);
		leftMeasurement.add(leftTemperatureValue);
		leftMeasurement.add(leftBloodSugarValue);
		rightMeasurement.add(rightBloodPressureValue);
		rightMeasurement.add(rightWeightValue);
		rightMeasurement.add(rightOxygenSaturationValue);
		rightMeasurement.add(rightTemperatureValue);
		rightMeasurement.add(rightBloodSugarValue);
		for (AbstractReading reading : leftMeasurement)
		{
			if (leftClosestDate == null && reading != null)
				leftClosestDate = reading.getDateTaken();
			if (leftClosestDate != null && reading != null)
			{
				if (leftClosestDate.before(reading.getDateTaken()))
					leftClosestDate = reading.getDateTaken();
			}
		}
		for (AbstractReading reading : rightMeasurement)
		{
			if (rightClosestDate == null && reading != null)
				rightClosestDate = reading.getDateTaken();
			if (rightClosestDate != null && reading != null)
			{
				if (rightClosestDate.after(reading.getDateTaken()))
					rightClosestDate = reading.getDateTaken();
			}
		}

		// change the date so that the closest date before and after
		// the current selected date is always there
		leftDate = leftClosestDate;
		rightDate = rightClosestDate;
		if (leftBloodPressureValue != null
				&& !leftBloodPressureValue.getDateTaken().equals(leftDate))
			leftBloodPressureValue = null;
		if (leftWeightValue != null
				&& !leftWeightValue.getDateTaken().equals(leftDate))
			leftWeightValue = null;
		if (leftOxygenSaturationValue != null
				&& !leftOxygenSaturationValue.getDateTaken().equals(leftDate))
			leftOxygenSaturationValue = null;
		if (leftTemperatureValue != null
				&& !leftTemperatureValue.getDateTaken().equals(leftDate))
			leftTemperatureValue = null;
		if (leftBloodSugarValue != null
				&& !leftBloodSugarValue.getDateTaken().equals(leftDate))
			leftBloodSugarValue = null;
		if (rightBloodPressureValue != null
				&& !rightBloodPressureValue.getDateTaken().equals(rightDate))
			rightBloodPressureValue = null;
		if (rightWeightValue != null
				&& !rightWeightValue.getDateTaken().equals(rightDate))
			rightWeightValue = null;
		if (rightOxygenSaturationValue != null
				&& !rightOxygenSaturationValue.getDateTaken().equals(rightDate))
			rightOxygenSaturationValue = null;
		if (rightTemperatureValue != null
				&& !rightTemperatureValue.getDateTaken().equals(rightDate))
			rightTemperatureValue = null;
		if (rightBloodSugarValue != null
				&& !rightBloodSugarValue.getDateTaken().equals(rightDate))
			rightBloodSugarValue = null;
	}

	private void findAllMeasurementValues(Context context)
	{
		try
		{
			bloodPressureArray = BloodPressureMapper.findAll(context);
			weightArray = WeightMapper.findAll(context);
			oxygenSatArray = OxygenSaturationMapper.findAll(context);
			tempArray = TemperatureMapper.findAll(context);
			bloodSugarArray = BloodSugarMapper.findAll(context);
		} catch (MapperException e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * Tries to find the measurement value for the given date in the database
	 * 
	 * @param context
	 * @param date
	 */
	public ArrayList<ArrayList<? extends AbstractReading>> findMeasurementValue(
			Context iContext, Date date)
	{
		ArrayList<ArrayList<? extends AbstractReading>> result = new ArrayList<ArrayList<? extends AbstractReading>>();
		try
		{
			result.add(BloodPressureMapper.findAllWithinRangeInclusive(date, date,
					iContext));
			result
					.add(WeightMapper.findAllWithinRangeInclusive(date, date, iContext));
			result.add(OxygenSaturationMapper.findAllWithinRangeInclusive(date, date,
					iContext));
			result.add(TemperatureMapper.findAllWithinRangeInclusive(date, date,
					iContext));
			result.add(BloodSugarMapper.findAllWithinRangeInclusive(date, date,
					iContext));

		} catch (MapperException e)
		{
			e.printStackTrace();
		}
		return result;
	}

	public Date setTextEditValues(BloodPressureReading rBP, WeightReading rW,
			OxygenSaturationReading rO, TemperatureReading rT, BloodSugarReading rBS)
	{
		heartRateO.setText(rBP != null ? rBP.getHeartrate() == 0 ? "" : String
				.valueOf(rBP.getHeartrate()) : "");
		systolicO.setText(rBP != null ? rBP.getSystolic() == 0 ? "" : String
				.valueOf(rBP.getSystolic()) : "");
		diastolicO.setText(rBP != null ? rBP.getDiastolic() == 0 ? "" : String
				.valueOf(rBP.getDiastolic()) : "");
		weightO.setText(rW != null ? Float.toString(rW.getWeight()) : "");
		o2percentO.setText(rO != null ? Float.toString(rO.getOxygenSaturation())
				: "");
		temperatureO.setText(rT != null ? Float.toString(rT.getTemperature()) : "");
		bloodSugarO.setText(rBS != null ? Float.toString(rBS.getBloodGlucose())
				: "");

		heartRate.setText(rBP != null ? rBP.getHeartrate() == 0 ? "" : String
				.valueOf(rBP.getHeartrate()) : "");
		systolic.setText(rBP != null ? rBP.getSystolic() == 0 ? "" : String
				.valueOf(rBP.getSystolic()) : "");
		diastolic.setText(rBP != null ? rBP.getDiastolic() == 0 ? "" : String
				.valueOf(rBP.getDiastolic()) : "");
		weight.setText(rW != null ? Float.toString(rW.getWeight()) : "");
		o2percent.setText(rO != null ? Float.toString(rO.getOxygenSaturation())
				: "");
		temperature.setText(rT != null ? Float.toString(rT.getTemperature()) : "");
		bloodSugar
				.setText(rBS != null ? Float.toString(rBS.getBloodGlucose()) : "");

		Date dateTaken = null;
		if (rBP != null)
			dateTaken = rBP.getDateTaken();
		else if (rW != null)
			dateTaken = rW.getDateTaken();
		else if (rO != null)
			dateTaken = rO.getDateTaken();
		else if (rT != null)
			dateTaken = rT.getDateTaken();
		else if (rBS != null)
			dateTaken = rBS.getDateTaken();

		Button newRecord = (Button) findViewById(R.id.newRecordButton);
		newRecord.setVisibility(View.VISIBLE);

		return dateTaken;
	}

	public void setMeasurementArray(ArrayList<RecordDateReading> iMeasurements)
	{
		mMeasurements = iMeasurements;
	}

	public void setDate(Date date)
	{
		mDate = date;
	}

	/**
	 * Method returns the index of the given date in the mMeasurements array.
	 * 
	 * @param date
	 * @return int : -1 if error, otherwise array index
	 */
	public int getDateIndex(Date date)
	{
		int i;
		boolean found = false;
		for (i = 0; i < mMeasurements.size(); i++)
		{
			long recDate = mMeasurements.get(i).getDateTaken().getTime();
			long cmpDate = date.getTime();
			if (recDate == cmpDate)
			{
				found = true;
				break;
			}
		}
		return found ? i : -1;
	}

	/**
	 * Iterate through the given ArrayList and returns a Vector of the closest
	 * element (earlier and later) to the current Date mDate
	 */
	private Vector<? extends AbstractReading> getLeftAndRightAbstractReading(
			ArrayList<? extends AbstractReading> reading)
	{
		AbstractReading leftReadingValue = null;
		AbstractReading rightReadingValue = null;
		for (AbstractReading value : reading)
		{
			Date dateTaken = value.getDateTaken();
			dateTaken.setTime((dateTaken.getTime() / 1000L) * 1000L); // cut off the
																																// milliseconds
			// if one date is missing the direction will be ignored
			// the first found value decides if there will be
			// a left and a right direction
			if (leftDate == null && rightDate == null)
			{
				if (dateTaken.compareTo(mDate) < 0)
				// dateTaken is before mDate
				{
					if (leftReadingValue == null)
					{
						leftReadingValue = value;
					} else if (value.getDateTaken()
							.after(leftReadingValue.getDateTaken()))
					{
						leftReadingValue = value;
					}
				} else if (dateTaken.compareTo(mDate) > 0)
				// dateTaken is after mDate
				{
					if (rightReadingValue == null)
					{
						rightReadingValue = value;
					} else if (value.getDateTaken().before(
							rightReadingValue.getDateTaken()))
					{
						rightReadingValue = value;
					}
				}
			} else
			{
				if (leftDate != null && dateTaken.compareTo(leftDate) == 0)
				{
					leftReadingValue = value;
				}
				if (rightDate != null && dateTaken.compareTo(rightDate) == 0)
				{
					rightReadingValue = value;
				}
			}
		}

		// set the left and right date
		if (leftDate == null && rightDate == null)
		{
			if (leftReadingValue != null)
				leftDate = leftReadingValue.getDateTaken();
			if (rightReadingValue != null)
				rightDate = rightReadingValue.getDateTaken();
		}

		Vector<AbstractReading> resultVector = new Vector<AbstractReading>();
		resultVector.add(leftReadingValue);
		resultVector.add(rightReadingValue);
		return resultVector;
	}

	public void handleNewEntry(View iView)
	{
		disableCells();
		switch (mCurrentSelection)
		{
		case WEIGHT:
			findViewById(R.id.body_weight_cell).setVisibility(View.VISIBLE);
			mSubActivity = new WeightActivity(this);
			mLastWeightAct = (WeightActivity) mSubActivity;
			break;
		case TEMPERATURE:
			findViewById(R.id.temperature_cell).setVisibility(View.VISIBLE);
			mSubActivity = new TemperatureActivity(this);
			mLastTemperatureAct = (TemperatureActivity) mSubActivity;
			break;
		case BLOOD_PRESSURE_HR:
			findViewById(R.id.blood_pressure_cell).setVisibility(View.VISIBLE);
			mSubActivity = new BloodPressureActivity(this);
			mLastBloodPressureAct = (BloodPressureActivity) mSubActivity;
			mLastBloodPressureAct.setHeartRateFocused();
			break;
		case BLOOD_PRESSURE_SYS:
			findViewById(R.id.blood_pressure_cell).setVisibility(View.VISIBLE);
			mSubActivity = new BloodPressureActivity(this);
			mLastBloodPressureAct = (BloodPressureActivity) mSubActivity;
			mLastBloodPressureAct.setSystolicFocused();
			break;
		case BLOOD_PRESSURE_DIA:
			findViewById(R.id.blood_pressure_cell).setVisibility(View.VISIBLE);
			mSubActivity = new BloodPressureActivity(this);
			mLastBloodPressureAct = (BloodPressureActivity) mSubActivity;
			mLastBloodPressureAct.setDiastolicFocused();
			break;
		case BLOOD_SUGAR:
			findViewById(R.id.blood_sugar_cell).setVisibility(View.VISIBLE);
			mSubActivity = new BloodSugarActivity(this);
			mLastBloodSugarAct = (BloodSugarActivity) mSubActivity;
			break;
		case O2:
			findViewById(R.id.o2_saturation_cell).setVisibility(View.VISIBLE);
			mSubActivity = new O2Activity(this);
			mLastO2Act = (O2Activity) mSubActivity;
			break;
		case OVERVIEW:
			findViewById(R.id.vital_signs_welcome_page).setVisibility(View.VISIBLE);
			loadHealthValues();
			break;
		default:
			break;
		}
		// the keyboard has to appear when the subActivity is clicked
		if (mSubActivity != null && mCurrentSelection != OVERVIEW)
			mSubActivity.preparePageTransition();
	}

	/**
	 * Handles the "Bluetooth" button press
	 */
	public void bluetooth(View iView)
	{
		mSubActivity.bluetooth(iView);
	}

	public void finish(View iView)
	{
		disableCells();
	}

	public void handleGraph(View iView)
	{
		CommandAction ca = new CommandAction();
		Measurement.clearDataArray();
		Measurement.initializeDataArray(mCurrentSelection, iView, ca);
		Intent myIntent = new Intent(iView.getContext(), GraphActivity.class);
		startActivityForResult(myIntent, 0);
	}

	@SuppressLint("NewApi")
	private void handleClick(View iView)
	{
		// disableRestButtons();
		adaptMeasurementIconStyle(iView);
		handleNewEntry(iView);
	}

	/**
	 * Disable the rest of the buttons (when another has been touched).
	 */
	private void disableCells()
	{
		ArrayList<View> wViews = new ArrayList<View>();
		wViews.add(findViewById(R.id.blood_pressure_cell));
		wViews.add(findViewById(R.id.body_weight_cell));
		wViews.add(findViewById(R.id.o2_saturation_cell));
		wViews.add(findViewById(R.id.temperature_cell));
		wViews.add(findViewById(R.id.blood_sugar_cell));
		wViews.add(findViewById(R.id.vital_signs_welcome_page));

		for (View v : wViews)
		{
			v.setVisibility(View.GONE);
		}
	}

	private void disableRestButtons()
	{
		disableCells();
		ArrayList<ImageButton> wButtons = new ArrayList<ImageButton>();

		wButtons.add((ImageButton) findViewById(R.id.blood_pressure));
		wButtons.add((ImageButton) findViewById(R.id.body_weight));
		wButtons.add((ImageButton) findViewById(R.id.o2_saturation));
		wButtons.add((ImageButton) findViewById(R.id.body_temperature));
		wButtons.add((ImageButton) findViewById(R.id.blood_sugar));

		for (ImageButton butt : wButtons)
		{
			butt.setBackgroundColor(Color.DKGRAY);
		}
	}

	@SuppressLint("NewApi")
	private void enableRestButtons()
	{
		disableCells();
		ArrayList<ImageButton> wButtons = new ArrayList<ImageButton>();

		wButtons.add((ImageButton) findViewById(R.id.blood_pressure));
		wButtons.add((ImageButton) findViewById(R.id.body_weight));
		wButtons.add((ImageButton) findViewById(R.id.o2_saturation));
		wButtons.add((ImageButton) findViewById(R.id.body_temperature));
		wButtons.add((ImageButton) findViewById(R.id.blood_sugar));

		for (ImageButton butt : wButtons)
		{
			int sdk = android.os.Build.VERSION.SDK_INT;
			if (sdk < android.os.Build.VERSION_CODES.JELLY_BEAN)
			{
				butt.setBackgroundDrawable(getResources().getDrawable(
						drawable.btn_default));
			} else
			{
				butt.setBackground(getResources().getDrawable(drawable.btn_default));
			}
		}
	}

	protected void handleOverView(View iView)
	{
		mCurrentSelection = OVERVIEW;
		handleClick(iView);
		Button newRecord = (Button) findViewById(R.id.newRecordButton);
		newRecord.setVisibility(View.VISIBLE);

		SpeechHelp tut = SpeechHelp.getInstance();
		if (tut.isActive())
		{
			tut.playTutorial(this, OVERVIEW_HELP);
		} else
		{
			tut.stopTutorial();
		}
	}

	public void handleButtonPressed(View iView)
	{
		retrieveOverviewValues();
		if (iView.getId() == R.id.blood_pressure)
		{
			mCurrentSelection = BLOOD_PRESSURE_HR;
			handleClick(iView);
		}
		if (iView.getId() == R.id.body_temperature)
		{
			mCurrentSelection = TEMPERATURE;
			handleClick(iView);
		}
		if (iView.getId() == R.id.blood_sugar)
		{
			mCurrentSelection = BLOOD_SUGAR;
			handleClick(iView);
		}
		if (iView.getId() == R.id.o2_saturation)
		{
			mCurrentSelection = O2;
			handleClick(iView);
		}

		if (iView.getId() == R.id.body_weight)
		{
			mCurrentSelection = WEIGHT;
			handleClick(iView);

		}

		if (iView.getId() == R.id.blood_pressure_heart_rate_valueO
				|| iView.getId() == R.id.blood_pressure_systolic_valueO
				|| iView.getId() == R.id.blood_pressure_diastolic_valueO
				|| iView.getId() == R.id.weight_valueO
				|| iView.getId() == R.id.o2percent_valueO
				|| iView.getId() == R.id.temperature_valueO
				|| iView.getId() == R.id.blood_sugar_valueO)
		{
			handleEditTextClicked(iView);

		}
		if (iView.getId() == R.id.dateMinus || iView.getId() == R.id.datePlus)
		{
			Date dateTaken = null;
			// boolean minusPressed = false;
			if (iView.getId() == R.id.dateMinus
					&& (leftBloodPressureValue != null || leftWeightValue != null
							|| leftOxygenSaturationValue != null
							|| leftTemperatureValue != null || leftBloodSugarValue != null))
			{
				dateTaken = handleMinusPressed(iView);
				// minusPressed = true;
			}
			if (iView.getId() == R.id.datePlus
					&& (rightBloodPressureValue != null || rightWeightValue != null
							|| rightOxygenSaturationValue != null
							|| rightTemperatureValue != null || rightBloodSugarValue != null))
			{
				dateTaken = handlePlusPressed(iView);
				// minusPressed = false;
			} else if (iView.getId() == R.id.datePlus)
			{
				handleNewDatePressed(iView);
			}
			Calendar cal = new GregorianCalendar();
			if (dateTaken != null)
				cal.setTime(dateTaken);

			TableRow dateRow = (TableRow) findViewById(R.id.rowDate);
			TableRow timeRow = (TableRow) findViewById(R.id.rowTime);
			dateRow.setBackgroundColor(0x4000ff00);
			timeRow.setBackgroundColor(0x4000ff00);

			applyCustomTime(cal.get(Calendar.SECOND), cal.get(Calendar.MINUTE),
					cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.DAY_OF_MONTH),
					cal.get(Calendar.MONTH), cal.get(Calendar.YEAR));
			changeDateText(mYear, mMonth, mDay);
			changeTimeText(mHour, mMinute);

			// ensure the left and right date are the closest possible ones
			// setClosestDate(minusPressed);
			refreshRecordedData(); // refresh
		}
		if (iView.getId() == R.id.listButton)
		{
			Intent listIntent = new Intent(this, RecordList.class);
			int dateNr = 0;
			ListIterator<RecordDateReading> iter = null;
			mMeasurements = getMeasurements();

			iter = mMeasurements.listIterator();

			while (iter != null && iter.hasNext())
			{
				RecordDateReading rec = iter.next();
				listIntent.putExtra("" + dateNr, rec.getDateTaken().getTime());
				dateNr++;
			}
			setMeasurementArray(mMeasurements);

			listIntent = listIntent.putExtra("dateCount", dateNr);
			startActivity(listIntent);
		}
		// if (iView.getId() == R.id.dateButton)
		// {
		// handleDateButtonPressed(iView);
		// }
		// if (iView.getId() == R.id.menuButton)
		// {
		// handleMenuButtonPressed(iView);
		// }
		if (iView.getId() == R.id.newRecordButton)
		{
			handleNewDatePressed(iView);
		}
	}

	private ArrayList<RecordDateReading> getMeasurements()
	{
		try
		{
			return RecordDateMapper.findAllDistinct(this);

			// handleTimeButtonPressed(iView);
		} catch (MapperException e)
		{
			e.printStackTrace();
		}
		return null;
	}

	public void handleEditTextClicked(View iView)
	{
		if (iView.getId() == R.id.blood_pressure_heart_rate_valueO)
		{
			mCurrentSelection = BLOOD_PRESSURE_HR;
		}
		if (iView.getId() == R.id.blood_pressure_systolic_valueO)
		{
			mCurrentSelection = BLOOD_PRESSURE_SYS;
		}
		if (iView.getId() == R.id.blood_pressure_diastolic_valueO)
		{
			mCurrentSelection = BLOOD_PRESSURE_DIA;
		}
		if (iView.getId() == R.id.weight_valueO)
		{
			mCurrentSelection = WEIGHT;
		}
		if (iView.getId() == R.id.o2percent_valueO)
		{
			mCurrentSelection = O2;
		}
		if (iView.getId() == R.id.temperature_valueO)
		{
			mCurrentSelection = TEMPERATURE;
		}
		if (iView.getId() == R.id.blood_sugar_valueO)
		{
			mCurrentSelection = BLOOD_SUGAR;
		}

		adaptMeasurementIconStyle(iView);

		handleNewEntry(iView);
	}

	private Date handleMinusPressed(View iView)
	{

		return setTextEditValues(leftBloodPressureValue, leftWeightValue,
				leftOxygenSaturationValue, leftTemperatureValue, leftBloodSugarValue);
	}

	private Date handlePlusPressed(View iView)
	{
		return setTextEditValues(rightBloodPressureValue, rightWeightValue,
				rightOxygenSaturationValue, rightTemperatureValue, rightBloodSugarValue);
	}

	public void handleNewDatePressed(View iView)
	{
		handleTimeButtonPressed(iView);
		handleDateButtonPressed(iView);
	}

	/**
	 * Save all the current Data
	 */
	public void saveCurrentData()
	{
		View iView = (View) findViewById(R.id.menuButton);
		if (mLastBloodPressureAct != null)
			mLastBloodPressureAct.submit(iView, mDate);
		if (mLastWeightAct != null)
			mLastWeightAct.submit(iView, mDate);
		if (mLastO2Act != null)
			mLastO2Act.submit(iView, mDate);
		if (mLastTemperatureAct != null)
			mLastTemperatureAct.submit(iView, mDate);
		if (mLastBloodSugarAct != null)
			mLastBloodSugarAct.submit(iView, mDate);
		findAllMeasurementValues(getApplicationContext());
		this.hl_timeout.removeCallbacks(DoOnTimeOut);
	}

	private void handleTimeButtonPressed(View iView)
	{
		TimePickerDialog.OnTimeSetListener timeCallback = new TimePickerDialog.OnTimeSetListener()
		{

			@Override
			public void onTimeSet(TimePicker view, int hourOfDay, int minutePicker)
			{
				if (mIgnoreTimeSet)
					return;
				changeTimeText(hourOfDay, minutePicker);
				refreshRecordedData();
				deleteEditTextContent();
				Button newRecord = (Button) findViewById(R.id.newRecordButton);
				newRecord.setVisibility(View.INVISIBLE);
			}
		};

		Calendar calCurr;
		calCurr = new GregorianCalendar();

		timePickerDialog = new TimePickerDialog(iView.getContext(),
				AlertDialog.THEME_TRADITIONAL, timeCallback,
				calCurr.get(Calendar.HOUR_OF_DAY), calCurr.get(Calendar.MINUTE), false);
		timePickerDialog.setButton(DialogInterface.BUTTON_POSITIVE, "OK",
				new DialogInterface.OnClickListener()
				{
					public void onClick(DialogInterface dialog, int which)
					{
						mIgnoreTimeSet = false;
						// only manually invoke OnTimeSetListener (through the dialog) on
						// pre-ICS devices
						if (Build.VERSION.SDK_INT < Build.VERSION_CODES.ICE_CREAM_SANDWICH)
							((VitalSignsActivity) VitalSignsActivity.VITAL_SIGN_ACTIVITY).timePickerDialog
									.onClick(dialog, which);
					}
				});

		timePickerDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel",
				new DialogInterface.OnClickListener()
				{
					public void onClick(DialogInterface dialog, int which)
					{
						mIgnoreTimeSet = true;
						dialog.cancel();
					}
				});

		timePickerDialog.setCancelable(true);
		timePickerDialog.cancel();
		timePickerDialog.setOnCancelListener(new DialogInterface.OnCancelListener()
		{
			@Override
			public void onCancel(DialogInterface dialog)
			{
				mIgnoreTimeSet = true;
			}
		});
		timePickerDialog.show();
	}

	@SuppressLint("NewApi")
	private void handleDateButtonPressed(View iView)
	{

		Calendar calCurr;
		calCurr = new GregorianCalendar();

		final DatePickerDialog datePickerDialog = new DatePickerDialog(
				iView.getContext(), AlertDialog.THEME_TRADITIONAL, null,
				calCurr.get(Calendar.YEAR), calCurr.get(Calendar.MONTH),
				calCurr.get(Calendar.DAY_OF_MONTH));
		datePickerDialog.setButton(DialogInterface.BUTTON_POSITIVE, "OK",
				new DialogInterface.OnClickListener()
				{
					@Override
					public void onClick(DialogInterface dialog, int which)
					{
						// this is necessary because the callback function will be called
						// first
						// and after that the click event ( Android Issue 34833)
						DatePicker datePicker = datePickerDialog.getDatePicker();
						changeDateText(datePicker.getYear(), datePicker.getMonth(),
								datePicker.getDayOfMonth());
						refreshRecordedData();
						deleteEditTextContent();
						Button newRecord = (Button) findViewById(R.id.newRecordButton);
						newRecord.setVisibility(View.INVISIBLE);
						TableRow rTime = (TableRow) findViewById(R.id.rowDate);
						TableRow rDate = (TableRow) findViewById(R.id.rowTime);
						rTime.setBackgroundColor(0x60ff0000);
						rDate.setBackgroundColor(0x60ff0000);
					}
				});
		datePickerDialog.setButton(DialogInterface.BUTTON_NEGATIVE,
				getString(R.string.cancel), new DialogInterface.OnClickListener()
				{
					public void onClick(DialogInterface dialog, int which)
					{
						if (which == DialogInterface.BUTTON_NEGATIVE)
						{
							dialog.dismiss();
							if (timePickerDialog != null)
								timePickerDialog.cancel();
							Button newRecord = (Button) findViewById(R.id.newRecordButton);
							newRecord.setVisibility(View.VISIBLE);
							// dateCanceled = true;
							// onBackPressed();
						}
					}
				});
		datePickerDialog.show();
	}

	public void changeTimeText(int hourOfDay, int minutePicker)
	{
		TextView timeText = (TextView) findViewById(R.id.timeTextEdit);

		applyCustomTime(mSecond, minutePicker, hourOfDay, mDay, mMonth, mYear);
		int hourAMPM = mHour % 12 == 0 ? 12 : hourOfDay % 12;
		timeText.setText(((hourAMPM < 10) ? ("0" + hourAMPM) : (hourAMPM)) + ":"
				+ ((mMinute < 10) ? ("0" + mMinute) : (mMinute)) + " "
				+ (hourOfDay < 12 ? "AM" : "PM"));
	}

	public void changeDateText(int yearPicker, int monthOfYear, int dayOfMonth)
	{
		TextView dateText = (TextView) findViewById(R.id.dateTextEdit);

		applyCustomTime(mSecond, mMinute, mHour, dayOfMonth, monthOfYear,
				yearPicker);
		// the month starts with zero
		dateText.setText(((mDay) < 10 ? "0" + (mDay) : (mDay))
				+ ". "
				+ ((mMonth + 1) < 10 ? (monthName.get(mMonth))
						: (monthName.get(mMonth))) + " " + mYear);
	}

	/**
	 * copy the values from each measurement text field to the text fields of the
	 * overview
	 */
	private void loadHealthValues()
	{
		heartRateO.setText(heartRate.getText().toString() == "0" ? "" : heartRate
				.getText().toString());
		diastolicO.setText(diastolic.getText().toString() == "0" ? "" : diastolic
				.getText().toString());
		systolicO.setText(systolic.getText().toString() == "0" ? "" : systolic
				.getText().toString());
		weightO.setText(weight.getText().toString());
		o2percentO.setText(o2percent.getText().toString());
		temperatureO.setText(temperature.getText().toString());
		bloodSugarO.setText(bloodSugar.getText().toString());
		this.hl_timeout.removeCallbacks(DoOnTimeOut);
	}

	private void retrieveOverviewValues()
	{
		heartRate.setText(heartRateO.getText().toString() == "0" ? "" : heartRateO
				.getText().toString());
		diastolic.setText(diastolicO.getText().toString() == "0" ? "" : diastolicO
				.getText().toString());
		systolic.setText(systolicO.getText().toString() == "0" ? "" : systolicO
				.getText().toString());
		weight.setText(weightO.getText().toString());
		o2percent.setText(o2percentO.getText().toString());
		temperature.setText(temperatureO.getText().toString());
		bloodSugar.setText(bloodSugarO.getText().toString());
		this.hl_timeout.removeCallbacks(DoOnTimeOut);
	}

	@SuppressLint("NewApi")
	private void adaptMeasurementIconStyle(View iView)
	{

		// clicking on EditText artificially sets the the corresponding
		// mCurrentSelection value
		// and sets the background
		disableRestButtons();
		if (iView instanceof EditText)
		{
			View affectedMeasurementOption = SQI_BUTTON.get(mCurrentSelection);
			iView = affectedMeasurementOption;
		}

		if (iView == null || mCurrentSelection == OVERVIEW)
		{
			enableRestButtons();
			return;
		}

		int sdk = android.os.Build.VERSION.SDK_INT;
		if (sdk < android.os.Build.VERSION_CODES.JELLY_BEAN)
		{
			iView.setBackgroundDrawable(getResources().getDrawable(
					drawable.btn_default));
		} else
		{
			iView.setBackground(getResources().getDrawable(drawable.btn_default));
		}
	}

	private void deleteEditTextContent()
	{
		heartRateO.setText("");
		diastolicO.setText("");
		systolicO.setText("");
		weightO.setText("");
		o2percentO.setText("");
		temperatureO.setText("");
		bloodSugarO.setText("");

		heartRate.setText("");
		diastolic.setText("");
		systolic.setText("");
		weight.setText("");
		o2percent.setText("");
		temperature.setText("");
		bloodSugar.setText("");
		this.hl_timeout.removeCallbacks(DoOnTimeOut);
	}

	/**
	 * Override the Functionality to go back to the overview page in the
	 * measurement screen.
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)
	{
		if (keyCode == KeyEvent.KEYCODE_BACK)
		{
			if (mCurrentSelection == OVERVIEW)
			{
				finish();
				return true;
			} else
			{
				handleOverView(null);
				setTitle("Record Measurement");
				return true;

			}
		} else
			return false;
	}

	@Override
	protected void onResume()
	{
		SpeechHelp tut = SpeechHelp.getInstance();
		if (tut.isActive())
		{
			tut.playTutorial(this, OVERVIEW_HELP);
		} else
		{
			tut.stopTutorial();
		}
		super.onResume();
	}

	@Override
	public void createHelpDialog(MenuItem item)
	{
		super.createHelpDialog(item);
		SpeechHelp tut = SpeechHelp.getInstance();
		if (tut.isActive())
		{
			switch (mCurrentSelection)
			{
			case WEIGHT:
				tut.playTutorial(this, WEIGHT_HELP);
				break;
			case BLOOD_SUGAR:
				tut.playTutorial(this, BLOOD_SUGAR_HELP);
				break;
			case BLOOD_PRESSURE_HR:
			case BLOOD_PRESSURE_DIA:
			case BLOOD_PRESSURE_SYS:
				tut.playTutorial(this, BLOOD_PRESSURE_HELP);
				break;
			case TEMPERATURE:
				tut.playTutorial(this, TEMPERATURE_HELP);
				break;
			case O2:
				tut.playTutorial(this, O2_SATURATION_HELP);
				break;
			case OVERVIEW:
				tut.playTutorial(this, OVERVIEW_HELP);
				break;
			default:
				break;
			}
		} else
		{
			tut.stopTutorial();
		}
	}

	private Runnable DoOnTimeOut = new Runnable() 
	{
	    public void run() 
	    {
	        // Do something Here
	    	warning();
	    	
	    }
	};
	Handler hl_timeout = new Handler();

	public void onUserInteraction()
	{
	    super.onUserInteraction();
	    //Remove any previous callback
	    hl_timeout.removeCallbacks(getDoOnTimeOut());
	    System.out.println("Re-calculating!");
	    hl_timeout.postDelayed(getDoOnTimeOut(), 10000);

	}
	public void warning(){
		new AlertDialog.Builder(VitalSignsActivity.this)

		.setTitle("Timeout")
		.setMessage("You wanna keep the previous data?")
		.setNegativeButton("No", new DialogInterface.OnClickListener()
		{
			@Override
			public void onClick(DialogInterface dialog, int which)
			{

				/*VitalSignsActivity.this.heartRate.setText("");
				VitalSignsActivity.this.diastolic.setText("");
				VitalSignsActivity.this.systolic.setText("");
				VitalSignsActivity.this.weight.setText("");
				VitalSignsActivity.this.o2percent.setText("");
				VitalSignsActivity.this.temperature.setText("");
				VitalSignsActivity.this.bloodSugar.setText("");
				VitalSignsActivity.this.heartRateO.setText("");
				VitalSignsActivity.this.diastolicO.setText("");
				VitalSignsActivity.this.systolicO.setText("");
				VitalSignsActivity.this.weightO.setText("");
				VitalSignsActivity.this.o2percentO.setText("");
				VitalSignsActivity.this.temperatureO.setText("");
				VitalSignsActivity.this.bloodSugarO.setText("");*/
				/*VitalSignsActivity.this.heartRate.setFocusable(true);
				VitalSignsActivity.this.heartRate.requestFocus();*/
				
				
				findViewById(R.id.vital_signs_welcome_page).setVisibility(View.VISIBLE);
				VitalSignsActivity.this.heartRate.setText("");
				VitalSignsActivity.this.diastolic.setText("");
				VitalSignsActivity.this.systolic.setText("");
				VitalSignsActivity.this.weight.setText("");
				VitalSignsActivity.this.o2percent.setText("");
				VitalSignsActivity.this.temperature.setText("");
				VitalSignsActivity.this.bloodSugar.setText("");
				VitalSignsActivity.this.heartRateO.setText("");
				VitalSignsActivity.this.diastolicO.setText("");
				VitalSignsActivity.this.systolicO.setText("");
				VitalSignsActivity.this.weightO.setText("");
				VitalSignsActivity.this.o2percentO.setText("");
				VitalSignsActivity.this.temperatureO.setText("");
				VitalSignsActivity.this.bloodSugarO.setText("");
				VitalSignsActivity.this.heartRate.setFocusable(true);
				VitalSignsActivity.this.heartRate.requestFocus();
				
		}}).setPositiveButton("Yes", new DialogInterface.OnClickListener()

		{
			public void onClick(DialogInterface dialog, int whichButton)
			{
				getCurrentFocus().setBackgroundColor(Color.GREEN);

				
			}
		}).show();
		
	}

	public Runnable getDoOnTimeOut()
	{
		return DoOnTimeOut;
	}

	public void setDoOnTimeOut(Runnable doOnTimeOut)
	{
		DoOnTimeOut = doOnTimeOut;
	}
	public void stopedCounting(){
		
		this.hl_timeout.removeCallbacks(DoOnTimeOut);
	}
}

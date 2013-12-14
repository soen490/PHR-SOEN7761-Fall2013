package com.augmentedsociety.myphr.presentation.vitalsigns;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.TreeMap;

import android.app.Activity;
import android.content.Context;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.TimePicker;

import com.augmentedsociety.myphr.R;
import com.augmentedsociety.myphr.presentation.ToastMessage;
import com.augmentedsociety.myphr.presentation.SpeechHelp;
import com.augmentedsociety.myphr.presentation.customviews.SquareImageButton;

public class VitalSignComponent implements VitalSignsComponentInterface
{
	static Map<Integer, Integer> NEXT_EDITTEXT = new TreeMap<Integer, Integer>(); // next
																																								// edit
																																								// text
																																								// field
																																								// in
																																								// the
																																								// overview
	static Map<Integer, Integer> EDITTEXT_MEASUREMENT = new TreeMap<Integer, Integer>(); // corrosponding
																																												// measurement
																																												// button
																																												// to
																																												// text
																																												// field
																																												// in
																																												// overview
	static Map<Integer, Integer> EDITTEXT_TRANSLATE = new TreeMap<Integer, Integer>(); // translate
																																											// the
																																											// the
																																											// edittext
																																											// in
																																											// the
																																											// overview
																																											// to
																																											// edittext
																																											// in
																																											// sub
																																											// measurement
	static Map<Integer, Integer> SUBMEASUREMENT_OVERVIEW = new TreeMap<Integer, Integer>();
	protected Activity mAct;
	private int fieldNr;
	
	private final String VALUES_SAVED = "valuesSaved";

	public VitalSignComponent(Activity mAct)
	{
		this.mAct = mAct;

		fillEditTextMap();

	}

	private void fillEditTextMap()
	{
		NEXT_EDITTEXT.put(R.id.blood_pressure_diastolic_value, R.id.weight_valueO);
		NEXT_EDITTEXT.put(R.id.weight_value, R.id.o2percent_valueO);
		NEXT_EDITTEXT.put(R.id.o2percent_value, R.id.temperature_valueO);
		NEXT_EDITTEXT.put(R.id.temperature_value, R.id.blood_sugar_valueO);
		NEXT_EDITTEXT.put(R.id.blood_sugar_value, R.id.vital_signs_welcome_page);

		EDITTEXT_MEASUREMENT.put(R.id.blood_pressure_diastolic_valueO,
				R.id.blood_pressure);
		EDITTEXT_MEASUREMENT.put(R.id.weight_valueO, R.id.body_weight);
		EDITTEXT_MEASUREMENT.put(R.id.o2percent_valueO, R.id.o2_saturation);
		EDITTEXT_MEASUREMENT.put(R.id.temperature_valueO, R.id.body_temperature);
		EDITTEXT_MEASUREMENT.put(R.id.blood_sugar_valueO, R.id.blood_sugar);

		EDITTEXT_TRANSLATE.put(R.id.blood_pressure_diastolic_valueO,
				R.id.blood_pressure_diastolic_value);
		EDITTEXT_TRANSLATE.put(R.id.weight_valueO, R.id.weight_value);
		EDITTEXT_TRANSLATE.put(R.id.o2percent_valueO, R.id.o2percent_value);
		EDITTEXT_TRANSLATE.put(R.id.temperature_valueO, R.id.temperature_value);
		EDITTEXT_TRANSLATE.put(R.id.blood_sugar_valueO, R.id.blood_sugar_value);

		SUBMEASUREMENT_OVERVIEW.put(R.id.blood_pressure_diastolic_value,
				R.id.blood_pressure);
		SUBMEASUREMENT_OVERVIEW.put(R.id.weight_value, R.id.body_weight);
		SUBMEASUREMENT_OVERVIEW.put(R.id.o2percent_value, R.id.o2_saturation);
		SUBMEASUREMENT_OVERVIEW.put(R.id.temperature_value, R.id.body_temperature);
		SUBMEASUREMENT_OVERVIEW.put(R.id.blood_sugar_value, R.id.blood_sugar);
	}

	protected Date getDateValue(int datePicker, int timePicker)
	{
		Calendar cal = Calendar.getInstance();
		DatePicker d = (DatePicker) mAct.findViewById(datePicker);
		TimePicker time = (TimePicker) mAct.findViewById(timePicker);
		cal.set(d.getYear(), d.getMonth(), d.getDayOfMonth(),
				time.getCurrentHour(), time.getCurrentMinute(), 0);
		return cal.getTime();
	}

	public void setDateTime()
	{
	}

	public void submit(View iView, Date timeOfReading)
	{
	}

	public void bluetooth(View iView)
	{
	}

	public void finish(View iView)
	{
	}

	public void preparePageTransition()
	{
	}

	protected void showKeyboard()
	{
		InputMethodManager imm = (InputMethodManager) mAct
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,
				InputMethodManager.HIDE_IMPLICIT_ONLY);
	}

	/**
	 * sets the focus to the textField given fieldNr
	 * 
	 * @param fieldNr
	 */
	protected void setTextFieldFocus(int fieldNr)
	{
		EditText currentField = (EditText) this.mAct.findViewById(fieldNr);
		currentField.requestFocus();
	}

	/**
	 * request focus for current text field and create trigger for the next button
	 * on the virtual keyboard
	 * 
	 * @param field
	 */
	protected void createNextPageListener(int fieldNr)
	{
		this.fieldNr = fieldNr;
		EditText currentField = (EditText) this.mAct.findViewById(fieldNr);

//		String txt = currentField.getText().toString();
//		boolean erg = !txt.equals("");
		int nextEditText = NEXT_EDITTEXT.get(fieldNr);
		String nxtText = "";
		String currentText = "";
		if (nextEditText != R.id.vital_signs_welcome_page && nextEditText != 0)
		{
			nextEditText = EDITTEXT_TRANSLATE.get(nextEditText);
			EditText nxtEdit = (EditText) mAct.findViewById(nextEditText);
			nxtText = nxtEdit.getText().toString();
			currentText = currentField.getText().toString();
		}

		if (isLastMeasurement(fieldNr) || currentText.length() != 0
				|| nxtText.length() != 0)
		{
			// the last visible measurement shall have a done button on the keyboard
			currentField.setImeOptions(EditorInfo.IME_ACTION_DONE);
		} else
		{
			// all others shall have the next button
			currentField.setImeOptions(EditorInfo.IME_ACTION_NEXT);
		}

		currentField.setOnEditorActionListener(new NewActionListener());

	}

	private boolean isLastMeasurement(int fieldNr)
	{
		ArrayList<Integer> mButtons = new ArrayList<Integer>();

		mButtons.add(R.id.blood_sugar);
		mButtons.add(R.id.body_temperature);
		mButtons.add(R.id.o2_saturation);
		mButtons.add(R.id.body_weight);
		mButtons.add(R.id.blood_pressure);

		for (Integer btnNr : mButtons)
		{

			SquareImageButton sqiBtn = (SquareImageButton) VitalSignsActivity.VITAL_SIGN_ACTIVITY
					.findViewById(btnNr);
			if (sqiBtn.isShown())
			{
				if (SUBMEASUREMENT_OVERVIEW.get(fieldNr).compareTo(btnNr) == 0)
				{
					// found last visible measurement
					return true;
				} else
				{
					// if first found entry don't belong to the field nr the field is not
					// the last entry
					break;
				}

			}
		}
		// no visible entry found
		return false;
	}

	public class NewActionListener implements OnEditorActionListener
	{

		public boolean onEditorAction(TextView v, int actionId, KeyEvent event)
		{
			// check if the next field is an active measurement

			int nextFieldNr;// = NEXT_EDITTEXT.get(fieldNr);
			// while (!activeMeasurement(nextFieldNr))
			// {
			// nextFieldNr = NEXT_EDITTEXT.get(EDITTEXT_TRANSLATE.get(nextFieldNr));
			// if (nextFieldNr == R.id.vital_signs_welcome_page)
			// {
			// break;
			// }
			// }
			nextFieldNr = getNextEditTextField(fieldNr);

			VitalSignsActivity vitalSignActivity = (VitalSignsActivity) VitalSignsActivity.VITAL_SIGN_ACTIVITY;
			if (vitalSignActivity != null)
			{
				if (nextFieldNr != R.id.vital_signs_welcome_page
						&& actionId == EditorInfo.IME_ACTION_NEXT)
				{
					vitalSignActivity.handleEditTextClicked((EditText) vitalSignActivity
							.findViewById(nextFieldNr));
					VitalSignsActivity.nextButtonPressed = true;
					
				} else if (nextFieldNr == R.id.vital_signs_welcome_page
						|| actionId == EditorInfo.IME_ACTION_DONE)
				{
					// when the overview is reached directly call the method for the
					// overview
					mAct.setTitle("Record Measurement");

					VitalSignsActivity.nextButtonPressed = true;
					vitalSignActivity.handleOverView(null);
					
					// check if a least one measurement value was entered
					EditText editBPM = (EditText) mAct
							.findViewById(R.id.blood_pressure_heart_rate_value);
					EditText editSys = (EditText) mAct
							.findViewById(R.id.blood_pressure_diastolic_value);
					EditText editDia = (EditText) mAct
							.findViewById(R.id.blood_pressure_systolic_value);
					EditText editWeight = (EditText) mAct.findViewById(R.id.weight_value);
					EditText editO2 = (EditText) mAct.findViewById(R.id.o2percent_value);
					EditText editTemp = (EditText) mAct
							.findViewById(R.id.temperature_value);
					EditText editBloodSugar = (EditText) mAct
							.findViewById(R.id.blood_sugar_value);
					
					if ((editBPM != null && !editBPM.getText().toString().equals(""))
							|| (editSys != null && !editSys.getText().toString().equals(""))
							|| (editDia != null && !editDia.getText().toString().equals(""))
							|| (editWeight != null && !editWeight.getText().toString().equals(""))
							|| (editO2 != null && !editO2.getText().toString().equals(""))
							|| (editTemp != null && !editTemp.getText().toString().equals(""))
							|| (editBloodSugar != null && !editBloodSugar.getText().toString().equals("")))
					{ 
					((VitalSignsActivity) VitalSignsActivity.VITAL_SIGN_ACTIVITY)
							.saveCurrentData();

						new ToastMessage(mAct, " The Values are saved.", 1);
						InputMethodManager imm = (InputMethodManager) mAct
								.getSystemService(Context.INPUT_METHOD_SERVICE);
						imm.hideSoftInputFromWindow(v.getWindowToken(), 0);

						TableRow rTime = (TableRow) VitalSignsActivity.VITAL_SIGN_ACTIVITY
								.findViewById(R.id.rowDate);
						TableRow rDate = (TableRow) VitalSignsActivity.VITAL_SIGN_ACTIVITY
								.findViewById(R.id.rowTime);
						rTime.setBackgroundColor(0x4000ff00);
						rDate.setBackgroundColor(0x4000ff00);
						
//						Tutorial.getInstance().stopTutorial();
						SpeechHelp.getInstance().playTutorial(mAct, VALUES_SAVED);
					}
				}
			}
			return false;
		}

	}

	private int getNextEditTextField(int fieldNr)
	{
		int nextField = NEXT_EDITTEXT.get(fieldNr);
		while (nextField != R.id.vital_signs_welcome_page)
		{
			SquareImageButton sqiBtn = (SquareImageButton) VitalSignsActivity.VITAL_SIGN_ACTIVITY
					.findViewById(EDITTEXT_MEASUREMENT.get(nextField));
			if (sqiBtn.isShown())
			{
				break;
			} else
			{
				nextField = NEXT_EDITTEXT.get(EDITTEXT_TRANSLATE.get(nextField));
			}
		}
		return nextField;

	}

//	private boolean activeMeasurement(int fieldNr)
//	{
//		int measurementNr = EDITTEXT_MEASUREMENT.get(fieldNr);
//		if (((SquareImageButton) VitalSignsActivity.VITAL_SIGN_ACTIVITY
//				.findViewById(measurementNr)).isShown())
//		{
//			return true;
//		} else
//		{
//			return false;
//		}
//	}
}

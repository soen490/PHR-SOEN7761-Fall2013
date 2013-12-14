package com.augmentedsociety.myphr.presentation;

import java.util.ArrayList;
import java.util.Calendar;

import android.R.drawable;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;

import com.augmentedsociety.myphr.R;
import com.augmentedsociety.myphr.domain.logs.LogEventType;
import com.augmentedsociety.myphr.presentation.allergies.ViewAllergiesActivity;
import com.augmentedsociety.myphr.presentation.documents.DocumentsActivity;
import com.augmentedsociety.myphr.presentation.immunizations.ViewImmunizationActivity;
import com.augmentedsociety.myphr.presentation.logs.LogEventEmitter;
import com.augmentedsociety.myphr.presentation.medications.ViewMedicationActivity;
import com.augmentedsociety.myphr.presentation.personalinfo.AllergyActivity;
import com.augmentedsociety.myphr.presentation.personalinfo.ImmunizationActivity;
import com.augmentedsociety.myphr.presentation.personalinfo.MedicationActivity;
import com.augmentedsociety.myphr.presentation.personalinfo.PersonalInfoComponent;
import com.augmentedsociety.myphr.presentation.personalinfo.PersonalInformationReadingActivity;
import com.augmentedsociety.myphr.presentation.vitalsigns.VitalSignsActivity;

/**
 * Common personal Information activity handler
 * 
 * @author Dipesh Patel
 * @author Rajan Jayakumar
 */

public class PersonalInformationActivity extends MenuActivity
{

	private PersonalInfoComponent infoSection;
	private String PERSONAL_INFO = "personalInformation";

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{

		super.onCreate(savedInstanceState);
		setContentView(R.layout.personal_information);
		View v = findViewById(R.id.personal_information_welcome_page);
		v.setVisibility(View.VISIBLE);
		setTitle(getResources().getString(R.string.personal_iformation_welcome_header));
		if (PreparationActivity.firstStart)
		{
			HandlePersonalProfileButton(findViewById(R.id.personal_profile_button));
			showOnlyPersonalProfile();

		}

		int currentapiVersion = android.os.Build.VERSION.SDK_INT;
		if (currentapiVersion >= android.os.Build.VERSION_CODES.FROYO)
		{
			super.getActionBar().setDisplayShowHomeEnabled(false);
		}
	}

	public void showOnlyPersonalProfile()
	{

		View v = findViewById(R.id.personal_profile_cell);
		v.setVisibility(View.VISIBLE);

		ArrayList<ImageButton> wPanelButtons = new ArrayList<ImageButton>();

		wPanelButtons.add((ImageButton) findViewById(R.id.personal_profile_button));
		wPanelButtons.add((ImageButton) findViewById(R.id.allergies_button));
		wPanelButtons.add((ImageButton) findViewById(R.id.immunizations_button));
		wPanelButtons.add((ImageButton) findViewById(R.id.medications_button));
		wPanelButtons.add((ImageButton) findViewById(R.id.documents_button));
		wPanelButtons
				.add((ImageButton) findViewById(R.id.medical_conditions_button));

		for (ImageButton button : wPanelButtons)
		{
			button.setVisibility(View.GONE);
		}
	}

	public void submit(View iView)
	{
		// Fires a LogEvent to the LogItemEditor
		LogEventEmitter.fireLogEvent(this, iView.getContext(),
				LogEventType.PROFILE_EDIT);
		// nullpointer because infosecition must be set
		infoSection.submit(iView);

		if (PreparationActivity.firstStart)
		{
			Intent measurementOptionIntent = new Intent(this, SettingsActivity.class);
			startActivity(measurementOptionIntent);
		}
	}

	public void clear(View iView)
	{
		// Fires a LogEvent to the LogItemEditor
		LogEventEmitter.fireLogEvent(this, iView.getContext(),
				LogEventType.PROFILE_DELETE);
		infoSection.cancel(iView);
	}

	public void HandlePersonalProfileButton(View iView)
	{
		disableRestButtons(iView);
		View v = findViewById(R.id.personal_profile_cell);
		v.setVisibility(View.VISIBLE);
		infoSection = new PersonalInformationReadingActivity(this, v);
	}

	public void HandleAllergiesButton(View iView)
	{
		disableRestButtons(iView);
		View v = findViewById(R.id.allergies_cell);
		v.setVisibility(View.VISIBLE);
		infoSection = new ViewAllergiesActivity(this);
	}

	public void HandleNewAllergiesButton(View iView)
	{
		disableRestButtons(iView);
		View v = findViewById(R.id.new_allergy_cell);
		v.setVisibility(View.VISIBLE);
		infoSection = new AllergyActivity(this);
	}

	public void HandleExistingAllergy(View iView, long allergyID)
	{
		disableRestButtons(iView);
		infoSection = new AllergyActivity(this, allergyID);
		View v = ((AllergyActivity) infoSection)
				.fillExistingView(findViewById(R.id.new_allergy_cell));
		v.setVisibility(View.VISIBLE);
	}

	public void HandleImmunizationsButton(View iView)
	{
		disableRestButtons(iView);
		View v = findViewById(R.id.immunizations_cell);
		v.setVisibility(View.VISIBLE);
		infoSection = new ViewImmunizationActivity(this);
	}

	public void HandleNewImmunizationsButton(View iView)
	{
		disableRestButtons(iView);
		View v = findViewById(R.id.new_immunizations_cell);
		v.setVisibility(View.VISIBLE);
		infoSection = new ImmunizationActivity(this);
	}

	public void HandleExistingImmunization(View iView, long immunizationID)
	{
		disableRestButtons(iView);
		infoSection = new ImmunizationActivity(this, immunizationID);
		View v = ((ImmunizationActivity) infoSection)
				.fillExistingView(findViewById(R.id.new_immunizations_cell));
		v.setVisibility(View.VISIBLE);
	}

	public void HandleMedicationsButton(View iView)
	{
		disableRestButtons(iView);
		View v = findViewById(R.id.medications_cell);
		v.setVisibility(View.VISIBLE);
		infoSection = new ViewMedicationActivity(this);
	}

	public void HandleNewMedicationsButton(View iView)
	{
		disableRestButtons(iView);
		View v = findViewById(R.id.new_medications_cell);
		v.setVisibility(View.VISIBLE);
		infoSection = new MedicationActivity(this);
	}

	public void HandleExistingMedication(View iView, long medicationID)
	{
		disableRestButtons(iView);
		infoSection = new MedicationActivity(this, medicationID);
		View v = ((MedicationActivity) infoSection)
				.fillExistingView(findViewById(R.id.new_medications_cell));
		v.setVisibility(View.VISIBLE);
	}

	public void HandleDocumentsButton(View iView)
	{
		disableRestButtons(iView);
		Intent intent = new Intent(this, DocumentsActivity.class);
		startActivity(intent);
	}

	public void HandleMedicalConditionsButton(View iView)
	{
		disableRestButtons(iView);
		View v = findViewById(R.id.medical_conditions_cell);
		v.setVisibility(View.VISIBLE);
	}

	public void dateButtonPressed(View iView)
	{
		infoSection.dateButtonPressed(iView);
	}

	public void startDateButtonPressed(View iView)
	{
		infoSection.startDateButtonPressed(iView);
	}

	public void endDateButtonPressed(View iView)
	{
		infoSection.endDateButtonPressed(iView);
	}

	public void startTimeButtonPressed(View iView)
	{
		infoSection.startTimeButtonPressed(iView);
	}

	public void endTimeButtonPressed(View iView)
	{
		infoSection.endTimeButtonPressed(iView);
	}

	public void dateButtonPressedImm(View iView)
	{
		infoSection.dateButtonPressedImm(iView);
	}

	public void timeButtonPressedImm(View iView)
	{
		infoSection.timeButtonPressedImm(iView);
	}

	public void handleSave(View iView)
	{
	}

	public void handleCancel(View iView)
	{
		infoSection.cancel(iView);
	}

	/**
	 * Disable the rest of the buttons (when another has been touched). and
	 * disables the view on the right for the other options
	 */
	@SuppressLint("NewApi")
	private void disableRestButtons(View iView)
	{
		// also change the visibility of each cell to gone
		ArrayList<View> wViews = new ArrayList<View>();
		wViews.add(findViewById(R.id.personal_profile_cell));
		wViews.add(findViewById(R.id.allergies_cell));
		wViews.add(findViewById(R.id.new_allergy_cell));
		wViews.add(findViewById(R.id.immunizations_cell));
		wViews.add(findViewById(R.id.new_immunizations_cell));
		wViews.add(findViewById(R.id.medications_cell));
		wViews.add(findViewById(R.id.new_medications_cell));
		wViews.add(findViewById(R.id.documents_cell));
		wViews.add(findViewById(R.id.medical_conditions_cell));
		wViews.add(findViewById(R.id.personal_information_welcome_page));
		for (View v : wViews)
		{
			v.setVisibility(View.GONE);
		}

		ArrayList<ImageButton> wPanelButtons = new ArrayList<ImageButton>();

		wPanelButtons.add((ImageButton) findViewById(R.id.personal_profile_button));
		wPanelButtons.add((ImageButton) findViewById(R.id.allergies_button));
		wPanelButtons.add((ImageButton) findViewById(R.id.immunizations_button));
		wPanelButtons.add((ImageButton) findViewById(R.id.medications_button));
		wPanelButtons.add((ImageButton) findViewById(R.id.documents_button));
		wPanelButtons
				.add((ImageButton) findViewById(R.id.medical_conditions_button));

		// Only change highlight when another panel button is pressed.
		// If a secondary button is pressed (eg. Cancel), do nothing
		if (wPanelButtons.contains(iView))
		{
			int sdk = android.os.Build.VERSION.SDK_INT;
			for (ImageButton button : wPanelButtons)
			{
				if (sdk < android.os.Build.VERSION_CODES.JELLY_BEAN)
				{
					button.setBackgroundDrawable(getResources().getDrawable(
							drawable.btn_default));
				} else
				{
					button.setBackgroundColor(Color.DKGRAY);
				}
			}
			iView.setBackground(getResources().getDrawable(drawable.btn_default));
			// iView.setBackgroundColor(Color.GRAY);
		}
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)
	{
		Intent intent = new Intent(getApplicationContext(), VitalSignsActivity.class);
		startActivity(intent);
		return true;
	}

	@Override
	protected void onResume()
	{
		SpeechHelp tut = SpeechHelp.getInstance();
		if (tut.isActive())
			tut.playTutorial(this, PERSONAL_INFO);
		super.onResume();
	}	
	
	@Override
	public void createHelpDialog(MenuItem item)
	{
		super.createHelpDialog(item);
		SpeechHelp tut = SpeechHelp.getInstance();
		if (tut.isActive())
		{
			tut.playTutorial(this, PERSONAL_INFO);
		} else
		{
			tut.stopTutorial();
		}
	}
}
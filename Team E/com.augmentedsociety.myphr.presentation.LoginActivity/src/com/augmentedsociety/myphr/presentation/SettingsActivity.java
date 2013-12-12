package com.augmentedsociety.myphr.presentation;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import android.R.drawable;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.inputmethodservice.Keyboard.Key;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.augmentedsociety.myphr.R;
import com.augmentedsociety.myphr.domain.DefaultPageMapper;
import com.augmentedsociety.myphr.domain.MapperException;
import com.augmentedsociety.myphr.domain.logs.LogEventMapper;
import com.augmentedsociety.myphr.domain.logs.LogItem;
import com.augmentedsociety.myphr.presentation.vitalsigns.VitalSignsActivity;

public class SettingsActivity extends MenuActivity
{
	Context context;
	final String NO_OPTIONS_SELECTED = "0";
	final String MEASUREMENT_PROFILES = "measurementProfiles";

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{

		super.onCreate(savedInstanceState);
		setContentView(R.layout.settings);
		// View v = findViewById(R.id.settings_welcome_page);
		// v.setVisibility(View.VISIBLE);
		context = getApplicationContext();
		updateCheckBoxes();

		int currentapiVersion = android.os.Build.VERSION.SDK_INT;
		if (currentapiVersion >= android.os.Build.VERSION_CODES.FROYO)
		{
			getActionBar().setDisplayShowHomeEnabled(false);
		}

		if (PreparationActivity.firstStart)
		{
			SpeechHelp tut = SpeechHelp.getInstance();
			tut.setActive(true, this);
			tut.playTutorial(this, MEASUREMENT_PROFILES);
		}

		setTitle(R.string.titleSelectMeasurement);

		View measurementCell = (View) findViewById(R.id.measurement_profile_cell);
		measurementCell.setVisibility(View.VISIBLE);
		showOnlyMeasurementProfile();

		LinearLayout btnLayout = (LinearLayout) findViewById(R.id.buttonLayout);
		// Button nextButton = new Button(context);
		// nextButton.setText(R.string.next);
		// nextButton.setOnClickListener(new NextButtonListener());
		// nextButton.setTextColor(Color.BLACK);
		// nextButton.set
		// btnLayout.addView(nextButton);

		// else
		// {
		// setTitle(R.string.settings);
		// }
	}

	public void nextButtonClicked(View v)
	{
		PreparationActivity.firstStart = false;
		Intent measurementOptionIntent = new Intent(context,
				VitalSignsActivity.class);
		startActivity(measurementOptionIntent);

		// restore the standard settings page
		setTitle(R.string.settings);
		// showEveryOption();
		// findViewById(R.id.settings_welcome_page).setVisibility(View.VISIBLE);
		// findViewById(R.id.measurement_profile_cell).setVisibility(View.INVISIBLE);
		// v.setVisibility(View.GONE);

		SpeechHelp.getInstance().stopTutorial();
	}

	private void showOnlyMeasurementProfile()
	{

		ArrayList<ImageButton> wPanelButtons = new ArrayList<ImageButton>();

		wPanelButtons
				.add((ImageButton) findViewById(R.id.set_measurement_profiles_button));
		wPanelButtons.add((ImageButton) findViewById(R.id.languages_button));
		wPanelButtons.add((ImageButton) findViewById(R.id.change_password_button));
		wPanelButtons.add((ImageButton) findViewById(R.id.view_tutorial_button));
		wPanelButtons.add((ImageButton) findViewById(R.id.diagnostics_button));

		for (ImageButton button : wPanelButtons)
		{
			button.setVisibility(View.GONE);
		}
	}

	private void showEveryOption()
	{

		ArrayList<ImageButton> wPanelButtons = new ArrayList<ImageButton>();

		((ImageButton) findViewById(R.id.set_measurement_profiles_button))
				.setVisibility(View.VISIBLE);
		((ImageButton) findViewById(R.id.diagnostics_button))
				.setVisibility(View.VISIBLE);
		((ImageButton) findViewById(R.id.change_password_button))
				.setVisibility(View.INVISIBLE);
		((ImageButton) findViewById(R.id.languages_button))
				.setVisibility(View.INVISIBLE);
		((ImageButton) findViewById(R.id.view_tutorial_button))
				.setVisibility(View.INVISIBLE);

	}

	private void updateCheckBoxes()
	{
		ArrayList<CheckBox> msrCheckBox = new ArrayList<CheckBox>();
		msrCheckBox.add((CheckBox) findViewById(R.id.checkBoxBloodPressure));
		msrCheckBox.add((CheckBox) findViewById(R.id.checkBoxWeight));
		msrCheckBox.add((CheckBox) findViewById(R.id.checkBoxO2));
		msrCheckBox.add((CheckBox) findViewById(R.id.checkBoxTempature));
		msrCheckBox.add((CheckBox) findViewById(R.id.checkBoxBlood_sugar));
		String checkedBoxes = NO_OPTIONS_SELECTED;
		try
		{
			checkedBoxes = DefaultPageMapper.get(context);
		} catch (MapperException e)
		{
			e.printStackTrace();
		}
		int i = 1;
		for (CheckBox chk : msrCheckBox)
		{
			if ((Integer.valueOf(checkedBoxes) & i) == i)
			{
				chk.setChecked(true);
			}
			i = (i * 2); // shift one bit left
		}
	}

	public void handleLanguagesButton(View iView)
	{
		disableRestButtons(iView);
		// open the languages page in the Android OS settings
		startActivityForResult(new Intent(
				android.provider.Settings.ACTION_LOCALE_SETTINGS), 0);

	}

	public void handlePasswordChangeButton(View iView)
	{
		disableRestButtons(iView);
		View v = findViewById(R.id.change_password_cell);
		v.setVisibility(View.VISIBLE);

	}

	public void handleMeasurementProfiles(View iView)
	{
		disableRestButtons(iView);

		View v = findViewById(R.id.measurement_profile_cell);
		v.setVisibility(View.VISIBLE);

		setTitle("Select Measurements");
	}

	public void handleViewTutorialButton(View iView)
	{
		disableRestButtons(iView);
	}

	public void handleDiagnosticsButton(View iView)
	{
		setTitle(R.string.logged_events);
		disableRestButtons(iView);

		View v = findViewById(R.id.diagnostics_cell);
		TableLayout table = (TableLayout) findViewById(R.id.logTable2);

		table.removeAllViews();

		loadLogItems(v, table);

		v.setVisibility(View.VISIBLE);
	}

	public void rowPressed(View view)
	{
		switch (view.getId())
		{
		case R.id.heartRow:
			CheckBox chkBP = (CheckBox) findViewById(R.id.checkBoxBloodPressure);
			chkBP.setChecked(!(chkBP.isChecked())); // inverse the current state
			break;
		case R.id.o2Row:
			CheckBox chkO2 = (CheckBox) findViewById(R.id.checkBoxO2);
			chkO2.setChecked(!(chkO2.isChecked()));
			break;
		case R.id.diabetesRow:
			CheckBox chkBS = (CheckBox) findViewById(R.id.checkBoxBlood_sugar);
			chkBS.setChecked(!(chkBS.isChecked()));
			break;
		case R.id.weightRow:
			CheckBox chkWeight = (CheckBox) findViewById(R.id.checkBoxWeight);
			chkWeight.setChecked(!(chkWeight.isChecked()));
			break;
		case R.id.temperatureRow:
			CheckBox chkTemperature = (CheckBox) findViewById(R.id.checkBoxTempature);
			chkTemperature.setChecked(!(chkTemperature.isChecked()));
			break;
		}
		checkboxPressed(view);
	}

	public void iconPressed(View view)
	{
		switch (view.getId())
		{
		case R.id.heartIcon:
			CheckBox chkBP = (CheckBox) findViewById(R.id.checkBoxBloodPressure);
			chkBP.setChecked(!(chkBP.isChecked())); // inverse the current state
			break;
		case R.id.handIcon:
			CheckBox chkO2 = (CheckBox) findViewById(R.id.checkBoxO2);
			chkO2.setChecked(!(chkO2.isChecked()));
			break;
		case R.id.diabetesIcon:
			CheckBox chkBS = (CheckBox) findViewById(R.id.checkBoxBlood_sugar);
			chkBS.setChecked(!(chkBS.isChecked()));
			break;
		case R.id.weightIcon:
			CheckBox chkWeight = (CheckBox) findViewById(R.id.checkBoxWeight);
			chkWeight.setChecked(!(chkWeight.isChecked()));
			break;
		case R.id.thermometerIcon:
			CheckBox chkTemperature = (CheckBox) findViewById(R.id.checkBoxTempature);
			chkTemperature.setChecked(!(chkTemperature.isChecked()));
			break;
		}
		checkboxPressed(view);
	}

	public void checkboxPressed(View iView)
	{
		ArrayList<CheckBox> msrCheckBox = new ArrayList<CheckBox>();
		msrCheckBox.add((CheckBox) findViewById(R.id.checkBoxBloodPressure));
		msrCheckBox.add((CheckBox) findViewById(R.id.checkBoxWeight));
		msrCheckBox.add((CheckBox) findViewById(R.id.checkBoxO2));
		msrCheckBox.add((CheckBox) findViewById(R.id.checkBoxTempature));
		msrCheckBox.add((CheckBox) findViewById(R.id.checkBoxBlood_sugar));

		int checked = 0;
		int i = 1;
		for (CheckBox chk : msrCheckBox)
		{
			if (chk.isChecked())
			{
				checked ^= i; // like unix permission style
			}
			i = (i * 2); // shift one bit left
		}

		// Toast.makeText(context, String.valueOf(checked), 10).show();

		try
		{
			DefaultPageMapper.update(String.valueOf(checked), context);
		} catch (MapperException e)
		{
			Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
		}
	}

	public void clearLogs(View iView)
	{

		try
		{
			LogEventMapper.deleteLogEntries(iView.getContext());
		} catch (MapperException e)
		{
			e.printStackTrace();
		}
		View v = findViewById(R.id.diagnostics_cell);
		TableLayout table = (TableLayout) v.findViewById(R.id.logTable2);

		table.removeAllViews();
		loadMainRow(table);
	}

	@SuppressLint("SimpleDateFormat")
	public void loadLogItems(View iView, TableLayout table)
	{

		loadMainRow(table);
		DateFormat df = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss");
		ArrayList<LogItem> mArrayLogItems;
		try
		{
			mArrayLogItems = LogEventMapper.findAll(iView.getContext());

			for (int i = 0; i < mArrayLogItems.size(); i++)
			{

				final TableRow row = new TableRow(this);

				TextView hLabel1 = new TextView(this);
				java.util.Date time = new java.util.Date((long) mArrayLogItems.get(i)
						.getEventDate());
				String date = df.format(time);
				hLabel1.setText(date);
				hLabel1.setTextSize(12);
				hLabel1.setPadding(3, 3, 3, 3);

				row.addView(hLabel1, new TableRow.LayoutParams());

				TextView hLabel2 = new TextView(this);
				hLabel2.setText(mArrayLogItems.get(i).getEventInfo());
				hLabel2.setTextSize(12);
				hLabel2.setGravity(Gravity.RIGHT);
				hLabel2.setPadding(3, 3, 3, 3);

				row.addView(hLabel2, new TableRow.LayoutParams());

				table.addView(row, new TableLayout.LayoutParams());
			}
		} catch (MapperException e)
		{
			e.printStackTrace();
		}

	}

	public void loadMainRow(TableLayout table)
	{
		TableRow row = new TableRow(this);

		TextView hLabel1 = new TextView(this);

		hLabel1.setText(R.string.date_time);
		hLabel1.setTextSize(12);
		hLabel1.setPadding(3, 3, 3, 3);
		hLabel1.setTextAppearance(getApplicationContext(), R.style.boldText);
		hLabel1.setBackgroundColor(Color.BLUE);
		hLabel1.setTextColor(Color.WHITE);
		hLabel1.setGravity(Gravity.CENTER);
		row.addView(hLabel1, new TableRow.LayoutParams());

		TextView hLabel2 = new TextView(this);
		hLabel2.setText(R.string.events);
		hLabel2.setTextSize(12);
		hLabel2.setPadding(3, 3, 3, 3);
		hLabel2.setTextAppearance(getApplicationContext(), R.style.boldText);
		hLabel2.setBackgroundColor(Color.BLUE);
		hLabel2.setTextColor(Color.WHITE);
		hLabel2.setGravity(Gravity.CENTER);
		row.addView(hLabel2, new TableRow.LayoutParams());

		table.addView(row, new TableLayout.LayoutParams());

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
		wViews.add(findViewById(R.id.languages_cell));
		wViews.add(findViewById(R.id.measurement_profile_cell));
		wViews.add(findViewById(R.id.change_password_cell));
		wViews.add(findViewById(R.id.diagnostics_cell));
		wViews.add(findViewById(R.id.view_tutorial_cell));
		wViews.add(findViewById(R.id.settings_welcome_page));

		for (View v : wViews)
		{
			v.setVisibility(View.GONE);
		}

		ArrayList<ImageButton> wButtons = new ArrayList<ImageButton>();

		wButtons.add((ImageButton) findViewById(R.id.languages_button));
		wButtons.add((ImageButton) findViewById(R.id.change_password_button));
		wButtons
				.add((ImageButton) findViewById(R.id.set_measurement_profiles_button));
		wButtons.add((ImageButton) findViewById(R.id.view_tutorial_button));
		wButtons.add((ImageButton) findViewById(R.id.diagnostics_button));

		for (ImageButton butt : wButtons)
		{
			butt.setBackgroundColor(Color.DKGRAY);
		}
		iView.setBackground(getResources().getDrawable(drawable.btn_default));
	}

	// name should change for these two methods, these are for the change password
	// option
	public void handleCancel(View iView)
	{
		super.onBackPressed();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)
	{
		// couldn't find the keycode for the keyboard down button
		if (keyCode != KeyEvent.KEYCODE_VOLUME_DOWN
				&& keyCode != KeyEvent.KEYCODE_VOLUME_UP
				&& keyCode != KeyEvent.KEYCODE_VOLUME_MUTE 
				&& keyCode != KeyEvent.KEYCODE_MENU)
		{
			Intent intent = new Intent(getApplicationContext(),
					VitalSignsActivity.class);
			startActivity(intent);
			return true;
		} else
			return false;
	}

	@Override
	protected void onResume()
	{
		SpeechHelp tut = SpeechHelp.getInstance();
		if (tut.isActive())
			tut.playTutorial(this, MEASUREMENT_PROFILES);
		super.onResume();
	}

	@Override
	public void createHelpDialog(MenuItem item)
	{
		super.createHelpDialog(item);
		SpeechHelp tut = SpeechHelp.getInstance();
		if (tut.isActive())
		{
			tut.playTutorial(this, MEASUREMENT_PROFILES);
		} else
		{
			tut.stopTutorial();
		}
	}
}

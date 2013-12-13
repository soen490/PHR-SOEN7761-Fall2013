package com.augmentedsociety.myphr.presentation.vitalsigns;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

import android.annotation.SuppressLint;
import android.app.ListActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TableRow;

import com.augmentedsociety.myphr.R;
import com.augmentedsociety.myphr.domain.AbstractReading;
import com.augmentedsociety.myphr.domain.BloodPressureReading;
import com.augmentedsociety.myphr.domain.BloodSugarReading;
import com.augmentedsociety.myphr.domain.OxygenSaturationReading;
import com.augmentedsociety.myphr.domain.TemperatureReading;
import com.augmentedsociety.myphr.domain.WeightReading;

public class RecordList extends ListActivity
{
	private Long[] valuesLong;
	private String[] values;
	private ArrayList<String> monthName;

	Calendar cal;

	BloodPressureReading rBP;
	WeightReading rW;
	OxygenSaturationReading rO;
	TemperatureReading rT;
	BloodSugarReading rBS;

	int nrOfDates;

	@SuppressLint("NewApi")
	public void onCreate(Bundle dates)
	{
		super.onCreate(dates);

		int currentapiVersion = android.os.Build.VERSION.SDK_INT;
		if (currentapiVersion >= android.os.Build.VERSION_CODES.HONEYCOMB)
		{
			getActionBar().setDisplayShowHomeEnabled(false);
		}
		
		fillMonthName();
		
		cal = GregorianCalendar.getInstance();
		setTitle("Select a Recorded Measurement");
		// int nrOfDates = dates.getInt("dateCount");
		nrOfDates = (Integer) getIntent().getExtras().get("dateCount");
		if (nrOfDates <= 0)
			return;

		valuesLong = new Long[nrOfDates];
		values = new String[nrOfDates];

		for (int i = 0; i < nrOfDates; i++)
		{
			valuesLong[i] = (Long) getIntent().getExtras().get("" + i);
			cal.setTimeInMillis(valuesLong[i]);
			// reverse the list
			values[(nrOfDates - 1) - i] = String.valueOf((cal
					.get(Calendar.DAY_OF_MONTH) < 10 ? "0"
					+ cal.get(Calendar.DAY_OF_MONTH) : cal.get(Calendar.DAY_OF_MONTH))
					+ ". "
					+ ( monthName.get(cal.get(Calendar.MONTH)))
					+ " "
					+ cal.get(Calendar.YEAR)
					+ " - "
					+ (cal.get(Calendar.HOUR_OF_DAY) < 10 ? "0"
							+ cal.get(Calendar.HOUR_OF_DAY) : cal.get(Calendar.HOUR_OF_DAY))
					+ ":"
					+ ((cal.get(Calendar.MINUTE) < 10) ? "0" + cal.get(Calendar.MINUTE)
							: cal.get(Calendar.MINUTE)));
		}

		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, values);
		setListAdapter(adapter);
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
	
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id)
	{
		super.onListItemClick(l, v, position, id);

		if (cal != null)
		{
			// select the row and then the right cal for this row

			cal = GregorianCalendar.getInstance();
			cal.setTimeInMillis(valuesLong[nrOfDates - 1 - position]);

			// String date = cal.get(Calendar.DAY_OF_MONTH);
			// String time = dateText.substring(13, 18);
			VitalSignsActivity vitalSigns = ((VitalSignsActivity) VitalSignsActivity.VITAL_SIGN_ACTIVITY);

			int day = Integer.valueOf(cal.get(Calendar.DAY_OF_MONTH));
			int month = Integer.valueOf(cal.get(Calendar.MONTH));
			int year = Integer.valueOf(cal.get(Calendar.YEAR));

			int hour = Integer.valueOf(cal.get(Calendar.HOUR));
			int minute = Integer.valueOf(cal.get(Calendar.MINUTE));
			int second = Integer.valueOf(cal.get(Calendar.SECOND));

			vitalSigns.applyCustomTime(second, minute, hour, day, month, year);
			vitalSigns.changeDateText(year, month, day);
			vitalSigns.changeTimeText(hour, minute);
			ArrayList<ArrayList<? extends AbstractReading>> result = vitalSigns
					.findMeasurementValue(vitalSigns, cal.getTime());
			vitalSigns.setDate(cal.getTime());

			// cast the results of the measurements
			if (result != null && result.get(0).size() > 0)
			{
				rBP = (BloodPressureReading) result.get(0).get(0);
			}
			if (result != null && result.get(1).size() > 0)
			{
				rW = (WeightReading) result.get(1).get(0);
			}
			if (result != null && result.get(2).size() > 0)
			{
				rO = (OxygenSaturationReading) result.get(2)
						.get(0);
			}
			if (result != null && result.get(3).size() > 0)
			{
				rT = (TemperatureReading) result.get(3).get(0);
			}
			if (result != null && result.get(4).size() > 0)
			{
				rBS = (BloodSugarReading) result.get(4).get(0);
			}

			vitalSigns.setTextEditValues(rBP, rW, rO, rT, rBS);

			TableRow dateRow = (TableRow)VitalSignsActivity.VITAL_SIGN_ACTIVITY.findViewById(R.id.rowDate);
			TableRow timeRow = (TableRow)VitalSignsActivity.VITAL_SIGN_ACTIVITY.findViewById(R.id.rowTime);
			dateRow.setBackgroundColor(0x4000ff00);
			timeRow.setBackgroundColor(0x4000ff00);
			
			// ensure the left and right date are the closest possible ones
			// setClosestDate(minusPressed);
			vitalSigns.refreshRecordedData(); // refresh
		}

		finish();
	}
}
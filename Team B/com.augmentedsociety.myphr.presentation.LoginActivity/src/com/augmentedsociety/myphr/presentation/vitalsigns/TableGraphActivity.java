package com.augmentedsociety.myphr.presentation.vitalsigns;

import java.sql.Date;
import java.text.DateFormat;
import java.util.List;

import android.R.drawable;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.augmentedsociety.myphr.R;
import com.augmentedsociety.myphr.presentation.MenuActivity;

public class TableGraphActivity extends MenuActivity
{
	private String mMeasurement;
	private int rowNr = 0;
	@SuppressLint("NewApi")
	@Override
  public void onCreate(Bundle iSavedInstanceState) 
	{
	  super.onCreate(iSavedInstanceState);
	  setContentView(R.layout.table_view);
	  TableLayout table = (TableLayout)findViewById(R.id.table);
	  displayValuesFromArray(table);
	  setTitle(getResources().getString(R.string.table_view));
		mMeasurement = getIntent().getExtras().getString("measurement");
	  
		int currentapiVersion = android.os.Build.VERSION.SDK_INT;
		if (currentapiVersion >= android.os.Build.VERSION_CODES.FROYO)
		{
			getActionBar().setDisplayShowHomeEnabled(false);
		}
	}
	/* Displaying the data points and the table headers in the table */
	private void displayValuesFromArray(TableLayout iTable )
	{
		iTable.removeAllViews();
		List<String> values = Measurement.getLabels();
		Context context = iTable.getContext();
		if(values.size() == 1)
			this.appendRow(iTable, context.getString(R.string.date), values.get(0), context.getString(R.string.visible), "", "", "");
		else if(values.size() == 3)
			this.appendRow(iTable, context.getString(R.string.date), values.get(0), values.get(1), values.get(2), context.getString(R.string.visible), "");
		
		rowNr = 0;
		for(String key : Measurement.getDataArray().keySet())
		{

			values = Measurement.getPoints(key);
			if(key.equals("label"))
			{
				continue;
			}
			else
			{
				++rowNr;
				Date timestamp = new Date((long) Float.parseFloat(key));
				String visible = Measurement.isVisible(values) ? context.getString(R.string.no) : context.getString(R.string.yes);
				if(values.size() == 2)
					this.appendRow(iTable, DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.SHORT).format(timestamp), values.get(1), visible, "", "" , key );
				else
				{
					this.appendRow(iTable, DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.SHORT).format(timestamp), values.get(1), values.get(2), values.get(3), visible, key);
				}
			}
		}
	}
//create own function for append TableRow
  private void appendRow(final TableLayout iTable, String iValue1, String iValue2, String iValue3, String iValue4, String iValue5, final String iHiddenKey) {
       final TableRow row = new TableRow(this);
       
       
       TextView hLabel1 = new TextView(this);
       hLabel1.setText(iValue1);
       hLabel1.setTextSize(14);
       hLabel1.setPadding(3, 3, 3, 3);

       TextView hLabel2 = new TextView(this);
       hLabel2.setText(iValue2);
       hLabel2.setTextSize(14);
       hLabel2.setGravity(Gravity.CENTER | Gravity.TOP);
       hLabel2.setPadding(25, 3, 3, 3);
       
       TextView hLabel3 = new TextView(this);
       hLabel3.setText(iValue3);
       hLabel3.setTextSize(14);
       hLabel3.setPadding(3, 3, 3, 3);
       hLabel3.setGravity(Gravity.CENTER | Gravity.TOP);

       TextView hLabel4 = new TextView(this);
       hLabel4.setText(iValue4);
       hLabel4.setTextSize(14);
       hLabel4.setPadding(3, 3, 15, 3);
       hLabel4.setGravity(Gravity.CENTER | Gravity.TOP);
       
       TextView hLabel5 = new TextView(this);
       hLabel5.setText(iValue5);
       hLabel5.setTextSize(14);
       hLabel5.setPadding(3, 3, 3, 3);
       hLabel5.setGravity(Gravity.CENTER | Gravity.TOP);
       
       if(iHiddenKey.equals(""))
       {
      	 hLabel1.setTextSize(15);
      	 hLabel1.setTextAppearance(getApplicationContext(), R.style.boldText);
      	 hLabel1.setBackgroundColor(Color.BLUE);
      	 hLabel1.setTextColor(Color.WHITE);
      	 if(hLabel2 != null && hLabel2.getText().toString().equalsIgnoreCase("Systolic (mmHg)"))
      		 hLabel2.setText("Sys");
      	 hLabel2.setTextSize(15);
      	 hLabel2.setTextAppearance(getApplicationContext(), R.style.boldText);
      	 hLabel2.setBackgroundColor(Color.BLUE);
      	 hLabel2.setTextColor(Color.WHITE);
      	 if(hLabel3 != null && hLabel3.getText().toString().equalsIgnoreCase("Diastolic (mmHg)"))
      		 hLabel3.setText("Dia");
      	 else if(hLabel3 != null && hLabel3.getText().toString().equalsIgnoreCase("Visible"))
      		 hLabel3.setText("Hide");
      	 hLabel3.setTextSize(15);
      	 hLabel3.setTextAppearance(getApplicationContext(), R.style.boldText);
      	 hLabel3.setBackgroundColor(Color.BLUE);
      	 hLabel3.setTextColor(Color.WHITE);
      	 hLabel3.setGravity(Gravity.CENTER | Gravity.TOP);
      	 if(hLabel4 != null && hLabel4.getText().toString().equalsIgnoreCase("Heart rate (bpm)"))
      		 hLabel4.setText("BPM");
      	 hLabel4.setTextSize(15);
      	 hLabel4.setTextAppearance(getApplicationContext(), R.style.boldText);
      	 hLabel4.setBackgroundColor(Color.BLUE);
      	 hLabel4.setTextColor(Color.WHITE);
      	 if(hLabel5 != null && hLabel5.getText().toString().equalsIgnoreCase("Visible"))
      		 hLabel5.setText("Hide");
      	 hLabel5.setTextSize(15);
      	 hLabel5.setTextAppearance(getApplicationContext(), R.style.boldText);
      	 hLabel5.setBackgroundColor(Color.BLUE);
      	 hLabel5.setTextColor(Color.WHITE);
       }
       
       // set every second row grey
       if(rowNr%2 ==0 && !iHiddenKey.equals(""))
       {
      	 hLabel1.setBackgroundColor(Color.GRAY);
      	 hLabel1.setTextColor(Color.WHITE);
      	 hLabel2.setBackgroundColor(Color.GRAY);
      	 hLabel2.setTextColor(Color.WHITE);
      	 hLabel3.setBackgroundColor(Color.GRAY);
      	 hLabel3.setTextColor(Color.WHITE);
      	 hLabel4.setBackgroundColor(Color.GRAY);
      	 hLabel4.setTextColor(Color.WHITE);
      	 hLabel5.setBackgroundColor(Color.GRAY);
      	 hLabel5.setTextColor(Color.WHITE);
       }
       
       row.addView(hLabel1, new TableRow.LayoutParams(1));
       row.addView(hLabel2, new TableRow.LayoutParams());
       row.addView(hLabel3, new TableRow.LayoutParams());
       row.addView(hLabel4, new TableRow.LayoutParams());
       row.addView(hLabel5, new TableRow.LayoutParams());

       /* Adding a listener to each row so we could update the visibility of the points */
       if(!iHiddenKey.equals(""))
       {
	       row.setOnClickListener(new OnClickListener()
	       {
						@Override
						public void onClick(View v)
						{
							Measurement.toggleVisibility(iHiddenKey);
							displayValuesFromArray(iTable);
						}
				 });
	       row.setBackgroundResource(drawable.list_selector_background);
       }
       
       iTable.addView(row, new TableLayout.LayoutParams());
   }
  
  public void handleGraph(View iView)
	{
		Intent myIntent = new Intent(iView.getContext(), GraphActivity.class);
		myIntent.putExtra("measurement", mMeasurement);
		startActivityForResult(myIntent, 0);
		finish();
	}
  @Override
	public void onResume()
	{
	   super.onResume();
	   this.onCreate(null);
	}
  @Override
	public void onBackPressed() {
  	buttonPressed((View) findViewById(R.id.backToGraph));
	}
  
  public void buttonPressed(View iView)
  {
  	if(iView.getId() == R.id.backToGraph)
  	{
  		Intent myIntent = new Intent(iView.getContext(), GraphActivity.class);
  		myIntent.putExtra("measurement", mMeasurement);
  		startActivityForResult(myIntent, 0);
  		finish();
  	}
  }
  
	@Override
	public void createHelpDialog(MenuItem item)
	{
	}
}

package com.augmentedsociety.myphr.presentation.logs;

import android.app.Activity;
import android.util.Log;
import android.widget.TableLayout;

import com.augmentedsociety.myphr.R;

public class LogActivity extends Activity
{
	TableLayout table;

	public void onCreate()
	{
		table = (TableLayout) findViewById(R.id.logTable2);
		Log.e("DERP", table.toString());
	}

}

package com.augmentedsociety.myphr.presentation.vitalsigns;

import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;

import com.augmentedsociety.myphr.R;
import com.augmentedsociety.myphr.presentation.GraphsActivity;
import com.augmentedsociety.myphr.presentation.MenuActivity;
import com.augmentedsociety.myphr.presentation.SpeechHelp;

/**
 * Graph Activity class for VS data graphical representation
 * 
 * @authors Roger Makram and Serge-Antoine Naim
 * 
 */

@SuppressLint(
{ "SetJavaScriptEnabled", "NewApi" })
public class GraphActivity extends MenuActivity
{

	private String mMeasurement;

	@Override
	public void onCreate(Bundle iSavedInstanceState)
	{
		super.onCreate(iSavedInstanceState);
		setContentView(R.layout.graph_and_table_view);
		makePage();
		setTitle(getResources().getString(R.string.graph_view));
		mMeasurement = getIntent().getExtras().getString("measurement");

		// Set indeterminate progress dialog for loading time "UI feedback"
		final ProgressDialog graphLoad_progress = ProgressDialog.show(
				GraphActivity.this, "", getString(R.string.loading), true);
		graphLoad_progress.setCancelable(true);

		// AsyncTask enables for background operations. In the following, while the
		// graphs load, the user gets "UI feedback" indicating loading time.
		AsyncTask<Void, Void, Boolean> waitForCompletion = new AsyncTask<Void, Void, Boolean>()
		{
			// Progress bar dismissed after 2 seconds
			@Override
			protected Boolean doInBackground(Void... params)
			{
				long timeStarted = System.currentTimeMillis();
				while (System.currentTimeMillis() - timeStarted < 2000)
				{
					try
					{
						Thread.sleep(100);
					} catch (InterruptedException e)
					{
					}
				}
				graphLoad_progress.dismiss();
				return null;
			};
		};
		waitForCompletion.execute(null, null, null);

		int currentapiVersion = android.os.Build.VERSION.SDK_INT;
		if (currentapiVersion >= android.os.Build.VERSION_CODES.FROYO)
		{
			getActionBar().setDisplayShowHomeEnabled(false);
		}
	}

	public void handleTableView(View iView)
	{
		Intent myIntent = new Intent(iView.getContext(), TableGraphActivity.class);
		myIntent.putExtra("measurement", mMeasurement);
		startActivityForResult(myIntent, 0);
		finish();
	}

	@Override
	public void onResume()
	{
		SpeechHelp tut = SpeechHelp.getInstance();
		if (tut.isActive())
			tut.playTutorial(this, mMeasurement);
		super.onResume();
		this.onCreate(null);
	}

	/* Display logic centralized to be used when creating page and resuming it */
	private void makePage()
	{
		WebView webView = null;
		webView = (WebView) findViewById(R.id.graph_and_table_view);
		webView.getSettings().setJavaScriptEnabled(true);
		String htmlString = "";
		AssetManager assetManager = getAssets();
		InputStream ims = null;
		try
		{
			ims = assetManager.open("graph_and_table_view.html");
			htmlString = new Scanner(ims, "UTF-8").useDelimiter("EOF").next();
		} catch (IOException e)
		{
			e.printStackTrace();
		}
		htmlString = Measurement.getHtmlStringFromData(htmlString);
		webView.loadDataWithBaseURL(
				"file:///android_asset/graph_and_table_view.html", htmlString,
				"text/html", null, null);
	}

	public void handleBackButton(View iView)
	{
		Intent myIntent = new Intent(iView.getContext(), GraphsActivity.class);
		startActivityForResult(myIntent, 0);
		finish();
	}

	@Override
	public void createHelpDialog(MenuItem item)
	{
		super.createHelpDialog(item);
		SpeechHelp tut = SpeechHelp.getInstance();
		if (tut.isActive())
		{
			tut.playTutorial(this, mMeasurement);
		} else
		{
			tut.stopTutorial();
		}

	}

}

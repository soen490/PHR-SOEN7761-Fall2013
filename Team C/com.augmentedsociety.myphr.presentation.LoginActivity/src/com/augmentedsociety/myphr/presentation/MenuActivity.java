package com.augmentedsociety.myphr.presentation;

import com.augmentedsociety.myphr.R;
import com.augmentedsociety.myphr.domain.logs.LogEventType;
import com.augmentedsociety.myphr.presentation.logs.LogEventEmitter;
import com.augmentedsociety.myphr.presentation.medicalhistory.ViewMedicalHistoryActivity;
import com.augmentedsociety.myphr.presentation.notifications.ViewNotificationsActivity;
import com.augmentedsociety.myphr.presentation.vitalsigns.VitalSignsActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

/**
 * Menu Activity class for sub-menu item selection
 * 
 * @authors The "unknown coder" and Serge-Antoine Naim
 * 
 */

@SuppressLint("NewApi")
public class MenuActivity extends Activity
{
	protected MenuActivity mAct;
	Menu mMenu;
	MenuItem iItem = null;
	Context mContext = null;
	private final int REPEAT_ID = 2349; // a random value

	protected void onCreate(Bundle iSavedInstanceState)
	{
		super.onCreate(iSavedInstanceState);
		mAct = this;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu iMenu)
	{
		mMenu = iMenu;
		if (PreparationActivity.firstStart || SpeechHelp.getInstance().isActive())
		{
			getMenuInflater().inflate(R.menu.main_menu_help_active, mMenu);
			PreparationActivity.firstStart = false;
		} else
		{
			getMenuInflater().inflate(R.menu.main_menu, mMenu);
		}

		return true;
	}

	@Override
	protected void onResume()
	{
		if (mMenu != null)
		{
			if (PreparationActivity.firstStart || SpeechHelp.getInstance().isActive())
			{
				mMenu.getItem(0).setIcon(R.drawable.ic_menu_help_active);
			} else
			{
				mMenu.getItem(0).setIcon(R.drawable.ic_menu_help);
			}
		}

		super.onResume();
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu)
	{
		mMenu = menu;
		final BitmapDrawable bitmapActive = (BitmapDrawable) (getResources()
				.getDrawable(R.drawable.ic_menu_help_active));
		final BitmapDrawable bitmapItem = (BitmapDrawable) (menu.getItem(0)
				.getIcon());

		if (bitmapActive.getBitmap().equals(bitmapItem.getBitmap()))
		{
			if (menu.findItem(REPEAT_ID) == null)
				menu.add(Menu.NONE, REPEAT_ID, Menu.NONE, "Repeat");
		} else
		{
			menu.removeItem(REPEAT_ID);
		}

		return super.onPrepareOptionsMenu(menu);
	}

	public Menu getMenu()
	{
		return mMenu;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem iItem)
	{
		Intent myIntent = null;

		switch (iItem.getItemId())
		{
		case R.id.menu_wellness:

			myIntent = new Intent(this, VitalSignsActivity.class);
			// if an activity is already open it'll be brought to the front
			myIntent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(myIntent);

			// Set indeterminate progress dialog for loading time "UI feedback"
			final ProgressDialog wellness_progress = ProgressDialog.show(
					MenuActivity.this, "", getString(R.string.loading), true);
			wellness_progress.setCancelable(true);

			// AsyncTask enables for background operations. In the following, while
			// the graphs load, the user gets "UI feedback" indicating loading time.
			AsyncTask<Void, Void, Boolean> wellness_waitForCompletion = new AsyncTask<Void, Void, Boolean>()
			{
				// Progress bar dismissed after 1.5 seconds
				@Override
				protected Boolean doInBackground(Void... params)
				{
					long timeStarted = System.currentTimeMillis();
					while (System.currentTimeMillis() - timeStarted < 1500)
					{
						try
						{
							Thread.sleep(100);
						} catch (InterruptedException e)
						{
						}
					}
					wellness_progress.dismiss();
					return null;
				};
			};
			wellness_waitForCompletion.execute(null, null, null);

			break;
		case R.id.menu_graphs:
			myIntent = new Intent(this, GraphsActivity.class);
			// if an activity is already open it'll be brought to the front
			myIntent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(myIntent);
			break;

		case R.id.menu_personal_info:

			myIntent = new Intent(this, PersonalInformationActivity.class);
			myIntent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(myIntent);

			// Set indeterminate progress dialog for loading time "UI feedback"
			final ProgressDialog wPersonalInformationProgress = ProgressDialog.show(
					MenuActivity.this, "", getString(R.string.loading), true);
			wPersonalInformationProgress.setCancelable(true);

			// AsyncTask enables for background operations. In the following, while
			// the graphs load, the user gets "UI feedback" indicating loading time.
			AsyncTask<Void, Void, Boolean> personal_waitForCompletion = new AsyncTask<Void, Void, Boolean>()
			{
				// Progress bar dismissed after 1.5 seconds
				@Override
				protected Boolean doInBackground(Void... params)
				{
					long timeStarted = System.currentTimeMillis();
					while (System.currentTimeMillis() - timeStarted < 1500)
					{
						try
						{
							Thread.sleep(100);
						} catch (InterruptedException e)
						{
						}
					}
					wPersonalInformationProgress.dismiss();
					return null;
				};
			};
			personal_waitForCompletion.execute(null, null, null);

			break;

		case R.id.menu_notifications:

			myIntent = new Intent(this, ViewNotificationsActivity.class);
			myIntent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(myIntent);

			// Set indeterminate progress dialog for loading time "UI feedback"
			final ProgressDialog notification_progress = ProgressDialog.show(
					MenuActivity.this, "", getString(R.string.loading), true);
			notification_progress.setCancelable(true);

			// AsyncTask enables for background operations. In the following, while
			// the graphs load, the user gets "UI feedback" indicating loading time.
			AsyncTask<Void, Void, Boolean> notification_waitForCompletion = new AsyncTask<Void, Void, Boolean>()
			{
				// Progress bar dismissed after 1.5 seconds
				@Override
				protected Boolean doInBackground(Void... params)
				{
					long timeStarted = System.currentTimeMillis();
					while (System.currentTimeMillis() - timeStarted < 1500)
					{
						try
						{
							Thread.sleep(100);
						} catch (InterruptedException e)
						{
						}
					}
					notification_progress.dismiss();
					return null;
				};
			};
			notification_waitForCompletion.execute(null, null, null);

			break;

		case R.id.menu_settings:

			myIntent = new Intent(this, SettingsActivity.class);
			myIntent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(myIntent);

			// Set indeterminate progress dialog for loading time "UI feedback"
			final ProgressDialog settings_progress = ProgressDialog.show(
					MenuActivity.this, "", getString(R.string.loading), true);
			settings_progress.setCancelable(true);

			// AsyncTask enables for background operations. In the following, while
			// the graphs load, the user gets "UI feedback" indicating loading time.
			AsyncTask<Void, Void, Boolean> settings_waitForCompletion = new AsyncTask<Void, Void, Boolean>()
			{
				// Progress bar dismissed after 1.5 seconds
				@Override
				protected Boolean doInBackground(Void... params)
				{
					long timeStarted = System.currentTimeMillis();
					while (System.currentTimeMillis() - timeStarted < 1500)
					{
						try
						{
							Thread.sleep(100);
						} catch (InterruptedException e)
						{
						}
					}
					settings_progress.dismiss();
					return null;
				};
			};
			settings_waitForCompletion.execute(null, null, null);

			break;

		case R.id.menu_info_tips:

			myIntent = new Intent(this, ViewMedicalHistoryActivity.class);
			myIntent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(myIntent);

			// Set indeterminate progress dialog for loading time "UI feedback"
			final ProgressDialog info_progress = ProgressDialog.show(
					MenuActivity.this, "", getString(R.string.loading), true);
			info_progress.setCancelable(true);

			// AsyncTask enables for background operations. In the following, while
			// the graphs load, the user gets "UI feedback" indicating loading time.
			AsyncTask<Void, Void, Boolean> info_waitForCompletion = new AsyncTask<Void, Void, Boolean>()
			{
				// Progress bar dismissed after 1.5 seconds
				@Override
				protected Boolean doInBackground(Void... params)
				{
					long timeStarted = System.currentTimeMillis();
					while (System.currentTimeMillis() - timeStarted < 1500)
					{
						try
						{
							Thread.sleep(100);
						} catch (InterruptedException e)
						{
						}
					}
					info_progress.dismiss();
					return null;
				};
			};
			info_waitForCompletion.execute(null, null, null);

			break;
		case R.id.menu_help:
			createHelpDialog(iItem);
			break;
		case REPEAT_ID:
			// SpeechHelp.getInstance().stopTutorial();
			// SpeechHelp.getInstance().setActive(false, mAct);
			SpeechHelp.getInstance().playTutorial(mAct,
					SpeechHelp.getInstance().getLastPlayedSpeech());
			break;
		default:
			break;
		}

		return super.onOptionsItemSelected(iItem);
	}

	protected void setContext(Context context)
	{
		mContext = context;
	}

	@SuppressLint("NewApi")
	public void createHelpDialog(MenuItem item)
	{
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
		iItem = item;
		final BitmapDrawable bitmapActive = (BitmapDrawable) (getResources()
				.getDrawable(R.drawable.ic_menu_help_active));
		final BitmapDrawable bitmapItem = (BitmapDrawable) (iItem.getIcon());

		// set title
		alertDialogBuilder.setTitle(bitmapActive.getBitmap().equals(
				bitmapItem.getBitmap()) ? "Help Deactivated" : "Help Activated");

		// set dialog message
		if (bitmapActive.getBitmap().equals(bitmapItem.getBitmap()))
		{
			alertDialogBuilder
					.setMessage( "For activating click on the question mark again.")
					.setCancelable(false)
					.setNeutralButton(getString(R.string.ok),
							new DialogInterface.OnClickListener()
							{
								public void onClick(DialogInterface dialog, int id)
								{
									// if this button is clicked, close
									// current activity
									mMenu.removeItem(REPEAT_ID);
								}
							});
		} else
		{
			alertDialogBuilder
					.setMessage("")
					.setCancelable(false)
					.setNeutralButton(getString(R.string.ok),
							new DialogInterface.OnClickListener()
							{
								public void onClick(DialogInterface dialog, int id)
								{
								}
							});
		}
		if (bitmapActive.getBitmap().equals(bitmapItem.getBitmap()))
		{
			iItem.setIcon(R.drawable.ic_menu_help);
			// stopTutorial(mMediaPlayer);
			// mMediaPlayer.pause();
			SpeechHelp.getInstance().stopTutorial();
			SpeechHelp.getInstance().setActive(false, mAct);
			SpeechHelp.getInstance().disableTutIcon(mAct);

		} else
		{
			iItem.setIcon(R.drawable.ic_menu_help_active);
			SpeechHelp.getInstance().setActive(true, mAct);
			SpeechHelp.getInstance().enableTutIcon(mAct);
			int currentapiVersion = android.os.Build.VERSION.SDK_INT;
			if (currentapiVersion >= android.os.Build.VERSION_CODES.HONEYCOMB)
			{
				invalidateOptionsMenu();
			}
			// mMediaPlayer = playTutorial();
		}

		// create alert dialog
		AlertDialog alertDialog = alertDialogBuilder.create();

		// show it
		alertDialog.show();
	}
}

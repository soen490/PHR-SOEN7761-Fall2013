package com.augmentedsociety.myphr.presentation.medicalhistory;

import java.util.ArrayList;

import com.augmentedsociety.myphr.R;
import com.augmentedsociety.myphr.domain.Immunization;
import com.augmentedsociety.myphr.domain.MedicalHistoryMapper;
import com.augmentedsociety.myphr.domain.MapperException;
import com.augmentedsociety.myphr.domain.logs.LogEventType;
import com.augmentedsociety.myphr.domain.MedicalHistory;
import com.augmentedsociety.myphr.presentation.MainPageActivity;
import com.augmentedsociety.myphr.presentation.MenuActivity;
import com.augmentedsociety.myphr.presentation.PersonalInformationActivity;
import com.augmentedsociety.myphr.presentation.ToastMessage;
import com.augmentedsociety.myphr.presentation.logs.LogEventEmitter;
import com.augmentedsociety.myphr.presentation.notifications.NewNotificationActivity;
import com.augmentedsociety.myphr.presentation.personalinfo.AllergyActivity;
import com.augmentedsociety.myphr.presentation.personalinfo.PersonalInfoComponent;
import com.augmentedsociety.myphr.presentation.vitalsigns.VitalSignsActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.wifi.WpsInfo;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;
import android.widget.Toast;

/**
 * Lists medical histories and their brief information
 * 
 * @author Roger Makram
 * 
 */
public class ViewMedicalHistoryActivity extends MenuActivity implements
		OnItemClickListener, OnItemLongClickListener,
		DialogInterface.OnClickListener
{
	public static final int REQ_MEDHIST_NEW = 6969;
	private final String INTENT_ID = "ID";
	
	private ArrayList<MedicalHistory> mAllMedicalHistorysArrayList;
	private ListView mListView;
	private MedicalHistory mCurrent = null;
	private MedicalHistoryListItemAdapter mMainAdapter;
	
	@SuppressLint("NewApi")
	@Override
	public void onCreate(Bundle iSavedInstanceState)
	{
		super.onCreate(iSavedInstanceState);
		setContentView(R.layout.activity_info_tips);

		showMedicalHistorys();

		int currentapiVersion = android.os.Build.VERSION.SDK_INT;
		if (currentapiVersion >= android.os.Build.VERSION_CODES.FROYO)
		{
			getActionBar().setDisplayShowHomeEnabled(false);
		}
	}

	private void showMedicalHistorys()
	{
		try
		{
			mAllMedicalHistorysArrayList = MedicalHistoryMapper.findAll(this
					.getApplicationContext());
			mListView = (ListView) this.findViewById(R.id.medical_history_available);
			mMainAdapter = new MedicalHistoryListItemAdapter(this,
					mAllMedicalHistorysArrayList);
			mListView.setAdapter(mMainAdapter);
			mListView.setOnItemClickListener(this);
			mListView.setOnItemLongClickListener(this);
		} catch (Exception e)
		{
			new ToastMessage(ViewMedicalHistoryActivity.this, e.getMessage(),
					Toast.LENGTH_LONG);
			// Toast.makeText(ViewMedicalHistoryActivity.this,
			// e.getMessage(),
			// Toast.LENGTH_LONG).show();
		}
	}

	@Override
	public void onResume()
	{
		super.onResume();
		showMedicalHistorys();
	}

	/**
	 * Navigates to the New Medical History form
	 * 
	 * @param view
	 */
	public void newMedicalHistory(View iView)
	{
		Intent myIntent = new Intent(this, NewMedicalHistoryActivity.class);
		startActivityForResult(myIntent, REQ_MEDHIST_NEW);
	}

	/**
	 * Navigates back to the main menu
	 * 
	 * @param view
	 */
	public void handleCancel(View iView)
	{
		Intent myIntent = new Intent(this, MainPageActivity.class);
		startActivity(myIntent);
	}

	public void handleExistingMedicalHistory(View iView, long iID)
	{
		// infoSection = new AllergyActivity(this, allergyID);
		// View v = ((AllergyActivity) infoSection)
		// .fillExistingView(findViewById(R.id.new_allergy_cell));
		// v.setVisibility(View.VISIBLE);

		Intent myIntent = new Intent(this, NewMedicalHistoryActivity.class);
		myIntent.putExtra(INTENT_ID, String.valueOf(iID));
		startActivityForResult(myIntent, REQ_MEDHIST_NEW);	
	}

	/**
	 * Handles deleting an MedicalHistory
	 */
	public boolean onItemLongClick(AdapterView<?> iAdapter, View iView,
			int iPosition, long iID)
	{
		Object o = mListView.getItemAtPosition(iPosition);
		mCurrent = (MedicalHistory) o;

		String deleteQuestion = iView.getContext().getResources()
				.getString(R.string.medical_history_delete);
		String yes = iView.getContext().getResources()
				.getString(R.string.button_yes);
		String no = iView.getContext().getResources().getString(R.string.button_no);

		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder
				.setMessage(deleteQuestion + " \"" + mCurrent.getDescription() + "\"?")
				.setPositiveButton(yes, this).setNegativeButton(no, this).show();

		return false;
	}

	/**
	 * Handles Yes/No selection for deleting dialog
	 * 
	 * @param dialog
	 * @param which
	 */
	@Override
	public void onClick(DialogInterface iDialog, int iWhich)
	{
		switch (iWhich)
		{
		case DialogInterface.BUTTON_POSITIVE:
			try
			{
				String MedicalHistoryDeletedMessage = this.getApplicationContext()
						.getResources()
						.getString(R.string.medical_history_has_been_deleted)
						+ mCurrent.getDescription();
				MedicalHistoryMapper.delete(mCurrent, this.getApplicationContext());

				mAllMedicalHistorysArrayList = MedicalHistoryMapper.findAll(this
						.getApplicationContext());

				new ToastMessage(this, MedicalHistoryDeletedMessage, Toast.LENGTH_LONG);
				// Toast.makeText(this,
				// MedicalHistoryDeletedMessage,
				// Toast.LENGTH_LONG).show();
				mMainAdapter.notifyDataSetChanged();
				mListView.invalidateViews();

			} catch (MapperException e)
			{
				new ToastMessage(this, e.getMessage(), Toast.LENGTH_LONG);
				// Toast.makeText(this,
				// e.getMessage(),
				// Toast.LENGTH_LONG).show();
			}

			break;

		case DialogInterface.BUTTON_NEGATIVE:
			// Do nothing
			break;
		}

	}

	@Override
	public void onItemClick(AdapterView<?> iAdapter, View iView, int iPosition,
			long iID)
	{
		Object item = mListView.getItemAtPosition(iPosition);
		mCurrent = (MedicalHistory) item;
		handleExistingMedicalHistory(iView, mCurrent.getID());
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)
	{
		Intent intent = new Intent(getApplicationContext(), VitalSignsActivity.class);
		startActivity(intent);
		return true;
	}

	@Override
	public void createHelpDialog(MenuItem item)
	{
	}
	
}

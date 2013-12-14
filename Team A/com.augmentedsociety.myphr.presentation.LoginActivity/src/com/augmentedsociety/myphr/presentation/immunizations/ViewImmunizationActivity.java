package com.augmentedsociety.myphr.presentation.immunizations;

import java.util.ArrayList;
import java.util.Calendar;

import com.augmentedsociety.myphr.R;
import com.augmentedsociety.myphr.domain.Allergy;
import com.augmentedsociety.myphr.domain.ImmunizationMapper;
import com.augmentedsociety.myphr.domain.MapperException;
import com.augmentedsociety.myphr.domain.logs.LogEventType;
import com.augmentedsociety.myphr.domain.Immunization;
import com.augmentedsociety.myphr.presentation.MenuActivity;
import com.augmentedsociety.myphr.presentation.PersonalInformationActivity;
import com.augmentedsociety.myphr.presentation.ToastMessage;
import com.augmentedsociety.myphr.presentation.logs.LogEventEmitter;
import com.augmentedsociety.myphr.presentation.personalinfo.PersonalInfoComponent;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TimePicker;
import android.widget.Toast;

/**
 * Lists immunizations and their brief information
 * 
 * @author Rajan Jayakumar
 * 
 */
public class ViewImmunizationActivity extends PersonalInfoComponent implements
		OnItemClickListener, OnItemLongClickListener,
		DialogInterface.OnClickListener
{
	// public static final int REQ_ALLERGY_NEW = 8001;
	private ArrayList<Immunization> mAllImmunizationsArrayList;
	private ListView mListView;
	private Immunization mCurrent = null;
	private ImmunizationListItemAdapter mMainAdapter;

	public ViewImmunizationActivity(Activity iActivity)
	{
		super(iActivity);
		showImmunizations();
		mAct.setTitle(mAct.getResources().getString(R.string.immunization));
	}

	private void showImmunizations()
	{
		try
		{
			mAllImmunizationsArrayList = ImmunizationMapper.findAll(mAct
					.getApplicationContext());
			mListView = (ListView) mAct.findViewById(R.id.immunizations_available);
			mMainAdapter = new ImmunizationListItemAdapter(mAct,
					mAllImmunizationsArrayList);
			mListView.setAdapter(mMainAdapter);
			mListView.setOnItemClickListener(this);
			mListView.setOnItemLongClickListener(this);
		} catch (Exception e)
		{
			new ToastMessage(mAct,e.getMessage(),Toast.LENGTH_LONG);
		}
	}

	/**
	 * Create a new immunization handler
	 * 
	 * @param view
	 */
	@Override
	public void submit(View iView)
	{
		PersonalInformationActivity wParent = (PersonalInformationActivity) mAct;
		wParent.HandleNewImmunizationsButton(iView);
	}

	/**
	 * Handles deleting an immunization
	 */
	public boolean onItemLongClick(AdapterView<?> iAdapter, View iView,
			int iPosition, long iID)
	{
		Object o = mListView.getItemAtPosition(iPosition);
		mCurrent = (Immunization) o;

		String deleteQuestion = iView.getContext().getResources()
				.getString(R.string.immunization_delete);
		String yes = iView.getContext().getResources()
				.getString(R.string.button_yes);
		String no = iView.getContext().getResources().getString(R.string.button_no);

		AlertDialog.Builder builder = new AlertDialog.Builder(mAct);
		builder.setMessage(deleteQuestion + " \"" + mCurrent.getName() + "\"?")
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
				String immunizationDeletedMessage = mAct.getApplicationContext()
						.getResources().getString(R.string.immunization_has_been_deleted);
				ImmunizationMapper.delete(mCurrent, mAct.getApplicationContext());

				mAllImmunizationsArrayList = ImmunizationMapper.findAll(mAct
						.getApplicationContext());

				new ToastMessage(mAct,immunizationDeletedMessage,Toast.LENGTH_LONG);
				mMainAdapter.notifyDataSetChanged();
				mListView.invalidateViews();

			} catch (MapperException e)
			{
				new ToastMessage(mAct,e.getMessage(),Toast.LENGTH_LONG);
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
		Object o = mListView.getItemAtPosition(iPosition);
		mCurrent = (Immunization) o;
		PersonalInformationActivity wParent = (PersonalInformationActivity) mAct;
		wParent.HandleExistingImmunization(iView, mCurrent.getID());
	}

}

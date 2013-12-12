package com.augmentedsociety.myphr.presentation.medications;

import java.util.ArrayList;

import com.augmentedsociety.myphr.R;
import com.augmentedsociety.myphr.domain.Allergy;
import com.augmentedsociety.myphr.domain.MedicationMapper;
import com.augmentedsociety.myphr.domain.MapperException;
import com.augmentedsociety.myphr.domain.logs.LogEventType;
import com.augmentedsociety.myphr.domain.Medication;
import com.augmentedsociety.myphr.presentation.MenuActivity;
import com.augmentedsociety.myphr.presentation.PersonalInformationActivity;
import com.augmentedsociety.myphr.presentation.ToastMessage;
import com.augmentedsociety.myphr.presentation.logs.LogEventEmitter;
import com.augmentedsociety.myphr.presentation.personalinfo.PersonalInfoComponent;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;
import android.widget.Toast;

/**
 * Lists medications and their brief information
 * @author Rajan Jayakumar
 *
 */
public class ViewMedicationActivity 
       extends PersonalInfoComponent implements 
               OnItemClickListener
               , OnItemLongClickListener
               , DialogInterface.OnClickListener
{
	//public static final int REQ_ALLERGY_NEW = 8001;
  private ArrayList<Medication> mAllMedicationsArrayList;
  private ListView mListView;
  private Medication mCurrent = null;
  private MedicationListItemAdapter mMainAdapter;
  
  public ViewMedicationActivity(Activity iActivity)
  {
  	super(iActivity);
  	showMedications();
  	mAct.setTitle(mAct.getResources().getString(R.string.medication));
  }

  private void showMedications()
  {
    try
    {
      mAllMedicationsArrayList = MedicationMapper.findAll(mAct.getApplicationContext());
      mListView = (ListView) mAct.findViewById(R.id.medications_available);
      mMainAdapter = new MedicationListItemAdapter(mAct, mAllMedicationsArrayList);
      mListView.setAdapter(mMainAdapter);
      mListView.setOnItemClickListener(this);
      mListView.setOnItemLongClickListener(this);
    }
    catch (Exception e)
    {
    	new ToastMessage(mAct,e.getMessage(),Toast.LENGTH_LONG);
    }
  }
 
  
  /**
   * Create a new medication handler
   * @param view
   */
  @Override
  public void submit(View iView)
  {
    PersonalInformationActivity wParent = (PersonalInformationActivity) mAct;
    wParent.HandleNewMedicationsButton(iView);
  }

  /**
   * Handles deleting an immunization
   */
  public boolean onItemLongClick(AdapterView<?> iAdapter, View iView, int iPosition, long iID)
  {
    Object o = mListView.getItemAtPosition(iPosition);
    mCurrent = (Medication) o;
    
    String deleteQuestion = iView.getContext().getResources().getString(R.string.medication_delete);
    String yes = iView.getContext().getResources().getString(R.string.button_yes);
    String no = iView.getContext().getResources().getString(R.string.button_no);
    
    AlertDialog.Builder builder = new AlertDialog.Builder(mAct);
    builder.setMessage(deleteQuestion + " \"" + mCurrent.getName() + "\"?").setPositiveButton(yes, this)
        .setNegativeButton(no, this).show();

    return false;
  }

  /**
   * Handles Yes/No selection for deleting dialog
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
      	String medicationDeletedMessage = mAct.getApplicationContext().getResources().getString(R.string.medication_has_been_deleted);
        MedicationMapper.delete(mCurrent, mAct.getApplicationContext());

        mAllMedicationsArrayList = MedicationMapper.findAll(mAct.getApplicationContext());

        new ToastMessage(mAct,medicationDeletedMessage,Toast.LENGTH_LONG);
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
	public void onItemClick(AdapterView<?> iAdapter, View iView, int iPosition, long iID)
	{
		Object o = mListView.getItemAtPosition(iPosition);
		mCurrent = (Medication) o;
		PersonalInformationActivity wParent = (PersonalInformationActivity) mAct;
    wParent.HandleExistingMedication(iView, mCurrent.getID());
	}
}

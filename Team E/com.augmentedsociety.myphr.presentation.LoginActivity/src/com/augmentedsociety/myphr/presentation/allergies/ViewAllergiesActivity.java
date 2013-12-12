package com.augmentedsociety.myphr.presentation.allergies;

import java.util.ArrayList;

import com.augmentedsociety.myphr.R;
import com.augmentedsociety.myphr.domain.AllergyMapper;
import com.augmentedsociety.myphr.domain.MapperException;
import com.augmentedsociety.myphr.domain.logs.LogEventType;
import com.augmentedsociety.myphr.domain.Allergy;
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
 * Lists allergies and their brief information
 * @author Yuri Kitaev
 *
 */
public class ViewAllergiesActivity 
       extends PersonalInfoComponent implements 
               OnItemClickListener
               , OnItemLongClickListener
               , DialogInterface.OnClickListener
{
	public static final int REQ_ALLERGY_NEW = 8001;
  private ArrayList<Allergy> mAllAllergiesArrayList;
  private ListView mListView;
  private Allergy mCurrent = null;
  private AllergyListItemAdapter mMainAdapter;
  
  public ViewAllergiesActivity(Activity iActivity)
  {
  	super(iActivity);
  	showAllergies();
  	mAct.setTitle(mAct.getResources().getString(R.string.allergies));
  }

  private void showAllergies()
  {
    try
    {
      mAllAllergiesArrayList = AllergyMapper.findAll(mAct.getApplicationContext());
      mListView = (ListView) mAct.findViewById(R.id.allergies_available);
      mMainAdapter = new AllergyListItemAdapter(mAct, mAllAllergiesArrayList);
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
   * Navigates to the New Notification form
   * @param view
   */
  @Override
  public void submit(View iView)
  {
    PersonalInformationActivity wParent = (PersonalInformationActivity) mAct;
    wParent.HandleNewAllergiesButton(iView);
  }

  /**
   * Handles deleting a notification
   */
  public boolean onItemLongClick(AdapterView<?> iAdapter, View iView, int iPosition, long iID)
  {
    Object o = mListView.getItemAtPosition(iPosition);
    mCurrent = (Allergy) o;
    
    String deleteQuestion = iView.getContext().getResources().getString(R.string.allergy_delete);
    String yes = iView.getContext().getResources().getString(R.string.button_yes);
    String no = iView.getContext().getResources().getString(R.string.button_no);
    
    AlertDialog.Builder builder = new AlertDialog.Builder(mAct);
    builder.setMessage(deleteQuestion + " " + mCurrent.getAllergic() + "?").setPositiveButton(yes, this)
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
      	String allergyDeletedMessage = mAct.getApplicationContext().getResources().getString(R.string.allergy_has_been_deleted);
        AllergyMapper.delete(mCurrent, mAct.getApplicationContext());

        mAllAllergiesArrayList = AllergyMapper.findAll(mAct.getApplicationContext());

        new ToastMessage(mAct,allergyDeletedMessage,Toast.LENGTH_LONG);
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
		mCurrent = (Allergy) o;
		PersonalInformationActivity wParent = (PersonalInformationActivity) mAct;
    wParent.HandleExistingAllergy(iView, mCurrent.getID());
	}
}

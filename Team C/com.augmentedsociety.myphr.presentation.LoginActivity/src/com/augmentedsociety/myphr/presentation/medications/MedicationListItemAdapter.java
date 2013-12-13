package com.augmentedsociety.myphr.presentation.medications;

import java.util.ArrayList;

import com.augmentedsociety.myphr.R;
import com.augmentedsociety.myphr.domain.Medication;
import com.augmentedsociety.myphr.domain.MedicationMapper;
import com.augmentedsociety.myphr.domain.MapperException;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Notification List Item Adapter Transforms the data into a visible list item
 * that can be displayed on the screen
 * 
 * @author Yuri Kitaev
 * @author Rajan Jayakumar
 * @note Credit:
 *       http://www.javasrilankansupport.com/2012/05/android-listview-example
 *       -with-image-and.html for the fundamental organizational ideas
 * 
 */
public class MedicationListItemAdapter extends BaseAdapter
{
  private ArrayList<Medication> mMedications;
  private LayoutInflater mLayoutInflater;
  private Context mContext;

  public MedicationListItemAdapter(Context iContext, ArrayList<Medication> iResults)
  {
    mMedications = iResults;
    mLayoutInflater = LayoutInflater.from(iContext);
    mContext = iContext;
  }
  
  @Override 
  public void notifyDataSetChanged()
  {
    try
    {
      mMedications=MedicationMapper.findAll(mContext);
    } 
    catch (MapperException e)
    {
      
    }
    super.notifyDataSetChanged();
  }

  public int getCount()
  {
    return mMedications.size();
  }

  public Object getItem(int position)
  {
    return mMedications.get(position);
  }

  public long getItemId(int position)
  {
    return position;
  }

  /**
   * Actual view generation
   * @author Yuri Kitaev
   * @author Rajan Jayakumar
   */
  public View getView(int iPosition, View iConvertView, ViewGroup iParent)
  {
    MedicationListItemViewHolder wListItemHolder;
    if (iConvertView == null)
    {
      iConvertView = mLayoutInflater.inflate(R.layout.medication_list_item,
          null);
      wListItemHolder = new MedicationListItemViewHolder();
      wListItemHolder.mTextFieldMedicationName = (TextView) iConvertView.findViewById(R.id.medicationToListItemTextField);
      wListItemHolder.mTextFieldMedicationType= (TextView) iConvertView
          .findViewById(R.id.medicationTypeListItemTextField);
      wListItemHolder.mTextFieldMedicationPosology = (TextView) iConvertView
          .findViewById(R.id.medicationPosologyListItemTextField);
      wListItemHolder.mTextFieldMedicationStrength = (TextView) iConvertView
          .findViewById(R.id.medicationStrengthListItemTextField);
      wListItemHolder.mTextFieldMedicationFrequency = (TextView) iConvertView
          .findViewById(R.id.medicationFrequencyListItemTextField);
      wListItemHolder.mTextFieldMedicationStartDate = (TextView) iConvertView
          .findViewById(R.id.medicationStartDateListItemTextField);
      wListItemHolder.mTextFieldMedicationEndDate = (TextView) iConvertView
          .findViewById(R.id.medicationEndDateListItemTextField);
      wListItemHolder.mTextFieldMedicationReasons = (TextView) iConvertView
          .findViewById(R.id.medicationReasonsListItemTextField);
      wListItemHolder.mTextFieldMedicationDoctor = (TextView) iConvertView
          .findViewById(R.id.medicationDoctorListItemTextField);

      iConvertView.setTag(wListItemHolder);
    } else
    {
      wListItemHolder = (MedicationListItemViewHolder) iConvertView.getTag();
    }

    // Set title
    wListItemHolder.mTextFieldMedicationName.setText(mMedications.get(iPosition).getName());
  
    wListItemHolder.mTextFieldMedicationType.setText(mContext.getString(R.string.type)+": "+mMedications.get(iPosition).getType());
    
    wListItemHolder.mTextFieldMedicationPosology.setText(mContext.getString(R.string.posology)+": "+mMedications.get(iPosition).getPosology());
    
    wListItemHolder.mTextFieldMedicationStrength.setText(mContext.getString(R.string.strength)+": "+mMedications.get(iPosition).getStrength());

    wListItemHolder.mTextFieldMedicationFrequency.setText(mContext.getString(R.string.frequency)+": "+mMedications.get(iPosition).getFrequency());

    wListItemHolder.mTextFieldMedicationStartDate.setText(mContext.getString(R.string.start_date)+": "+mMedications.get(iPosition).getStartDateString());
    
    wListItemHolder.mTextFieldMedicationEndDate.setText(mContext.getString(R.string.end_date)+": "+mMedications.get(iPosition).getEndDateString());

    wListItemHolder.mTextFieldMedicationReasons.setText(mContext.getString(R.string.reason)+": "+mMedications.get(iPosition).getReasons());

    wListItemHolder.mTextFieldMedicationDoctor.setText(mContext.getString(R.string.doctor)+": "+mMedications.get(iPosition).getDoctor());
    
    return iConvertView;
  }
}
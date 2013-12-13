package com.augmentedsociety.myphr.presentation.medicalhistory;

import java.util.ArrayList;

import com.augmentedsociety.myphr.R;
import com.augmentedsociety.myphr.domain.MedicalHistory;
import com.augmentedsociety.myphr.domain.MedicalHistoryMapper;
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
 * Medical History List Item Adapter Transforms the data into a visible list item
 * that can be displayed on the screen
 * 
 * @author Yuri Kitaev
 * @author Roger Makram
 * @note Credit:
 *       http://www.javasrilankansupport.com/2012/05/android-listview-example
 *       -with-image-and.html for the fundamental organizational ideas
 * 
 */
public class MedicalHistoryListItemAdapter extends BaseAdapter
{
  private ArrayList<MedicalHistory> mMedicalHistorys;
  private LayoutInflater mLayoutInflater;
  private Context mContext;

  public MedicalHistoryListItemAdapter(Context iContext, ArrayList<MedicalHistory> iResults)
  {
    mMedicalHistorys = iResults;
    mLayoutInflater = LayoutInflater.from(iContext);
    mContext = iContext;
  }
  
  @Override 
  public void notifyDataSetChanged()
  {
    try
    {
      mMedicalHistorys=MedicalHistoryMapper.findAll(mContext);
    } 
    catch (MapperException e)
    {
      
    }
    super.notifyDataSetChanged();
  }

  public int getCount()
  {
    return mMedicalHistorys.size();
  }

  public Object getItem(int position)
  {
    return mMedicalHistorys.get(position);
  }

  public long getItemId(int position)
  {
    return position;
  }

  /**
   * Actual view generation
   * @author Yuri Kitaev
   * @author Roger Makram
   */
  public View getView(int iPosition, View iConvertView, ViewGroup iParent)
  {
    MedicalHistoryListItemViewHolder wListItemHolder;
    if (iConvertView == null)
    {
      iConvertView = mLayoutInflater.inflate(R.layout.medical_history_details,
          null);
      wListItemHolder = new MedicalHistoryListItemViewHolder();
      wListItemHolder.mTextFieldMedicalHistoryDate = (TextView) iConvertView.findViewById(R.id.medicalHistoryDateListItemTextField);
      wListItemHolder.mTextFieldMedicalHistoryDescription= (TextView) iConvertView
          .findViewById(R.id.medicalHistoryDescriptionListItemTextField);
      wListItemHolder.mTextFieldMedicalHistoryLocation = (TextView) iConvertView
          .findViewById(R.id.medicalHistoryLocationListItemTextField);

      iConvertView.setTag(wListItemHolder);
    } else
    {
      wListItemHolder = (MedicalHistoryListItemViewHolder) iConvertView.getTag();
    }

    // Set title
    wListItemHolder.mTextFieldMedicalHistoryDate.setText(mMedicalHistorys.get(iPosition).getDateString());
  
    wListItemHolder.mTextFieldMedicalHistoryDescription.setText(mContext.getString(R.string.details)+": "+mMedicalHistorys.get(iPosition).getDescription());
    
    wListItemHolder.mTextFieldMedicalHistoryLocation.setText(mContext.getString(R.string.location)+": "+mMedicalHistorys.get(iPosition).getLocation());
    
    return iConvertView;
  }
}
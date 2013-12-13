package com.augmentedsociety.myphr.presentation.immunizations;

import java.util.ArrayList;

import com.augmentedsociety.myphr.R;
import com.augmentedsociety.myphr.domain.Immunization;
import com.augmentedsociety.myphr.domain.ImmunizationMapper;
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
public class ImmunizationListItemAdapter extends BaseAdapter
{
  private ArrayList<Immunization> mImmunizations;
  private LayoutInflater mLayoutInflater;
  private Context mContext;

  public ImmunizationListItemAdapter(Context iContext, ArrayList<Immunization> iResults)
  {
    mImmunizations = iResults;
    mLayoutInflater = LayoutInflater.from(iContext);
    mContext = iContext;
  }
  
  @Override 
  public void notifyDataSetChanged()
  {
    try
    {
      mImmunizations=ImmunizationMapper.findAll(mContext);
    } 
    catch (MapperException e)
    {
      
    }
    super.notifyDataSetChanged();
  }

  public int getCount()
  {
    return mImmunizations.size();
  }

  public Object getItem(int position)
  {
    return mImmunizations.get(position);
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
    ImmunizationListItemViewHolder wListItemHolder;
    if (iConvertView == null)
    {
      iConvertView = mLayoutInflater.inflate(R.layout.immunization_list_item,
          null);
      wListItemHolder = new ImmunizationListItemViewHolder();
      wListItemHolder.mTextFieldImmunizationName = (TextView) iConvertView.findViewById(R.id.immunizationToListItemTextField);
      wListItemHolder.mTextFieldImmunizationType= (TextView) iConvertView
          .findViewById(R.id.immunizationTypeListItemTextField);
      wListItemHolder.mTextFieldImmunizationRoute = (TextView) iConvertView
          .findViewById(R.id.immunizationRouteListItemTextField);
      wListItemHolder.mTextFieldImmunizationManufacturer = (TextView) iConvertView
          .findViewById(R.id.immunizationManufacturerListItemTextField);
      wListItemHolder.mTextFieldImmunizationLot = (TextView) iConvertView
          .findViewById(R.id.immunizationLotListItemTextField);
      wListItemHolder.mTextFieldImmunizationPosology = (TextView) iConvertView
          .findViewById(R.id.immunizationPosologyListItemTextField);
      wListItemHolder.mTextFieldImmunizationDate = (TextView) iConvertView
          .findViewById(R.id.immunizationDateListItemTextField);
      wListItemHolder.mTextFieldImmunizationLocation = (TextView) iConvertView
          .findViewById(R.id.immunizationLocationListItemTextField);
      wListItemHolder.mTextFieldImmunizationDetails = (TextView) iConvertView
          .findViewById(R.id.immunizationDetailsListItemTextField);
      wListItemHolder.mTextFieldImmunizationComments = (TextView) iConvertView
          .findViewById(R.id.immunizationCommentsListItemTextField);

      iConvertView.setTag(wListItemHolder);
    } else
    {
      wListItemHolder = (ImmunizationListItemViewHolder) iConvertView.getTag();
    }

    // Set title
    wListItemHolder.mTextFieldImmunizationName.setText(mImmunizations.get(iPosition).getName());
  
    wListItemHolder.mTextFieldImmunizationType.setText(mContext.getString(R.string.type)+": "+mImmunizations.get(iPosition).getType());
    
    wListItemHolder.mTextFieldImmunizationRoute.setText(mContext.getString(R.string.immunization_route)+": "+mImmunizations.get(iPosition).getRoute());
    
    wListItemHolder.mTextFieldImmunizationManufacturer.setText(mContext.getString(R.string.manufacturer)+": "+mImmunizations.get(iPosition).getManufacturer());

    wListItemHolder.mTextFieldImmunizationLot.setText(mContext.getString(R.string.lot)+": "+mImmunizations.get(iPosition).getLot_number());

    wListItemHolder.mTextFieldImmunizationPosology.setText(mContext.getString(R.string.dosage)+": "+mImmunizations.get(iPosition).getPosology());
    
    wListItemHolder.mTextFieldImmunizationDate.setText(mImmunizations.get(iPosition).getDateString());

    wListItemHolder.mTextFieldImmunizationLocation.setText(mContext.getString(R.string.location)+": "+mImmunizations.get(iPosition).getLocation());

    wListItemHolder.mTextFieldImmunizationDetails.setText(mContext.getString(R.string.details)+": "+mImmunizations.get(iPosition).getDetails());

    wListItemHolder.mTextFieldImmunizationComments.setText(mContext.getString(R.string.comments)+": "+mImmunizations.get(iPosition).getComments());
    
    return iConvertView;
  }
}
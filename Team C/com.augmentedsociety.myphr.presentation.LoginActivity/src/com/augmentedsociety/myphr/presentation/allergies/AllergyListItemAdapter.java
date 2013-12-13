package com.augmentedsociety.myphr.presentation.allergies;

import java.util.ArrayList;

import com.augmentedsociety.myphr.R;
import com.augmentedsociety.myphr.domain.Allergy;
import com.augmentedsociety.myphr.domain.AllergyMapper;
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
 * @note Credit:
 *       http://www.javasrilankansupport.com/2012/05/android-listview-example
 *       -with-image-and.html for the fundamental organizational ideas
 * 
 */
public class AllergyListItemAdapter extends BaseAdapter
{
  private ArrayList<Allergy> mAllergies;
  private LayoutInflater mLayoutInflater;
  private Context mContext;

  public AllergyListItemAdapter(Context iContext, ArrayList<Allergy> iResults)
  {
    mAllergies = iResults;
    mLayoutInflater = LayoutInflater.from(iContext);
    mContext = iContext;
  }
  
  @Override 
  public void notifyDataSetChanged()
  {
    try
    {
      mAllergies=AllergyMapper.findAll(mContext);
    } 
    catch (MapperException e)
    {
      
    }
    super.notifyDataSetChanged();
  }

  public int getCount()
  {
    return mAllergies.size();
  }

  public Object getItem(int position)
  {
    return mAllergies.get(position);
  }

  public long getItemId(int position)
  {
    return position;
  }

  /**
   * Actual view generation
   * @author Yuri Kitaev
   */
  public View getView(int iPosition, View iConvertView, ViewGroup iParent)
  {
    AllergyListItemViewHolder wListItemHolder;
    if (iConvertView == null)
    {
      iConvertView = mLayoutInflater.inflate(R.layout.allergy_list_item,
          null);
      wListItemHolder = new AllergyListItemViewHolder();
      wListItemHolder.mTextFieldAllergicTo = (TextView) iConvertView.findViewById(R.id.allergicToListItemTextField);
      wListItemHolder.mTextFieldAllergicReaction = (TextView) iConvertView
          .findViewById(R.id.allergicReactionListItemTextField);
      wListItemHolder.mTextFieldSeverity = (TextView) iConvertView
          .findViewById(R.id.allergySeverityListItemTextField);   

      iConvertView.setTag(wListItemHolder);
    } else
    {
      wListItemHolder = (AllergyListItemViewHolder) iConvertView.getTag();
    }

    // Set title
    wListItemHolder.mTextFieldAllergicTo.setText(mAllergies.get(iPosition).getAllergic());
  
    wListItemHolder.mTextFieldAllergicReaction.setText(mAllergies.get(iPosition).getReaction());
   
    String wSeverity = mAllergies.get(iPosition).getSeverity();
    if (mContext.getString(R.string.high).equalsIgnoreCase(wSeverity))
    {
    	wListItemHolder.mTextFieldSeverity.setTextColor(Color.rgb(150, 0, 0));
    	wListItemHolder.mTextFieldSeverity.setText(mContext.getString(R.string.high));
    }
    else if (mContext.getString(R.string.medium).equalsIgnoreCase(wSeverity))
    {
    	wListItemHolder.mTextFieldSeverity.setTextColor(Color.rgb(150, 150, 0));
    	wListItemHolder.mTextFieldSeverity.setText(mContext.getString(R.string.medium));
    }
    else if (mContext.getString(R.string.low).equalsIgnoreCase(wSeverity))
    {
    	wListItemHolder.mTextFieldSeverity.setTextColor(Color.rgb(0, 150, 0));
    	wListItemHolder.mTextFieldSeverity.setText(mContext.getString(R.string.low));
    }
    else
    {
    	wListItemHolder.mTextFieldSeverity.setTextColor(Color.rgb(0, 0, 0));
    	wListItemHolder.mTextFieldSeverity.setText("---");
    }
    
    
    
    return iConvertView;
  }
}
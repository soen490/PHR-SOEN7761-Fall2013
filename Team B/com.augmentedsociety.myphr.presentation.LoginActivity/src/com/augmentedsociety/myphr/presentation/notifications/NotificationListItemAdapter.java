package com.augmentedsociety.myphr.presentation.notifications;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import com.augmentedsociety.myphr.R;
import com.augmentedsociety.myphr.domain.MapperException;
import com.augmentedsociety.myphr.domain.notifications.Notification;
import com.augmentedsociety.myphr.domain.notifications.NotificationMapper;

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
public class NotificationListItemAdapter extends BaseAdapter
{
  private static final long HOUR = 60 * 60 * 1000;
  private static final long DAY = 24 * HOUR;
  private static SimpleDateFormat mFirstOccurrenceDateFormatter = new SimpleDateFormat("yyyy.MM.dd 'at' HH:mm");
  private static Drawable mNotificationIconDrawable = null;
  private static Drawable mNotificationEnabledDrawable = null;
  private static Drawable mNotificationDisabledDrawable = null;
  private ArrayList<Notification> mNotifications;
  private LayoutInflater mLayoutInflater;
  private Context mContext;

  public NotificationListItemAdapter(Context iC, ArrayList<Notification> iResults)
  {
    mNotifications = iResults;
    mLayoutInflater = LayoutInflater.from(iC);
    mContext = iC;
  }
  
  @Override 
  public void notifyDataSetChanged()
  {
    try
    {
      mNotifications=NotificationMapper.findAll(mContext);
    } 
    catch (MapperException e)
    {
      
    }
    super.notifyDataSetChanged();
  }

  public int getCount()
  {
    return mNotifications.size();
  }

  public Object getItem(int position)
  {
    return mNotifications.get(position);
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
    NotificationListItemViewHolder wListItemHolder;
    if (iConvertView == null)
    {
      iConvertView = mLayoutInflater.inflate(R.layout.notification_list_item,
          null);
      wListItemHolder = new NotificationListItemViewHolder();
      wListItemHolder.mTextFieldTitle = (TextView) iConvertView.findViewById(R.id.title);
      wListItemHolder.mTextFieldFirstOccurrence = (TextView) iConvertView
          .findViewById(R.id.time);
      wListItemHolder.mTextFieldPeriodicity = (TextView) iConvertView
          .findViewById(R.id.periodicity);
      wListItemHolder.mTextFieldEnabledState = (TextView) iConvertView.findViewById(R.id.enabled);
      wListItemHolder.mItemImage = (ImageView) iConvertView.findViewById(R.id.icon);
    

      iConvertView.setTag(wListItemHolder);
    } else
    {
      wListItemHolder = (NotificationListItemViewHolder) iConvertView.getTag();
    }

    // Set title
    wListItemHolder.mTextFieldTitle.setText(mNotifications.get(iPosition).getTitle());
    
    // Set first occurrence date/time
    Date wFirstOccurrenceDate = mNotifications.get(iPosition).getFirstOccurrence();
    String wFirstOccurrenceDateStringFormatted = mFirstOccurrenceDateFormatter.format(wFirstOccurrenceDate);
    
    wListItemHolder.mTextFieldFirstOccurrence.setText(wFirstOccurrenceDateStringFormatted);
    
    // Cache button images
    if (null == mNotificationIconDrawable)
    {
    	mNotificationIconDrawable = iConvertView.getResources().getDrawable(R.drawable.notification);
    }
    if (null == mNotificationEnabledDrawable)
    {
    	mNotificationEnabledDrawable =  iConvertView.getResources().getDrawable(R.drawable.on_button_green);  	
    }
    if (null == mNotificationDisabledDrawable)
    {
    	mNotificationDisabledDrawable =  iConvertView.getResources().getDrawable(R.drawable.off_button_gray);  	
    }
    
    // Show the on/off status
    String statusOn = iConvertView.getContext().getResources().getString(R.string.notifications_status_on);
    String statusOff = iConvertView.getContext().getResources().getString(R.string.notifications_status_off);
    wListItemHolder.mTextFieldEnabledState.setText((mNotifications.get(iPosition).isEnabled() ? statusOn
        : statusOff));
    wListItemHolder.mItemImage.setImageDrawable(mNotifications.get(iPosition).isEnabled() ? mNotificationEnabledDrawable : mNotificationDisabledDrawable);
    
    // Set the color for ON/OFF values - dark green for ON, dark red for OFF
    if (mNotifications.get(iPosition).isEnabled())
      wListItemHolder.mTextFieldEnabledState.setTextColor(Color.rgb(0, 150, 0));
    else
      wListItemHolder.mTextFieldEnabledState.setTextColor(Color.rgb(150, 0, 0));

    // Show the repeat interval
    long repeatInterval = mNotifications.get(iPosition).getRepeatInterval();
    String repeatsEvery = iConvertView.getContext().getResources().getString(R.string.notifications_repeats_every);
    String onlyOnce = iConvertView.getContext().getResources().getString(R.string.notifications_interval_only_once);
    String minutes = iConvertView.getContext().getResources().getString(R.string.notifications_interval_minutes);
    String hours = iConvertView.getContext().getResources().getString(R.string.notifications_interval_hours);
    String days = iConvertView.getContext().getResources().getString(R.string.notifications_interval_days);
    String weeks = iConvertView.getContext().getResources().getString(R.string.notifications_interval_weeks);
    String months = iConvertView.getContext().getResources().getString(R.string.notifications_interval_months);
    if (repeatInterval == 0)
      wListItemHolder.mTextFieldPeriodicity.setText(onlyOnce);
    else if (repeatInterval < HOUR)
      wListItemHolder.mTextFieldPeriodicity.setText(repeatsEvery + " " + repeatInterval
          / (HOUR/60) + " " + minutes);
    else if (repeatInterval < DAY)
      wListItemHolder.mTextFieldPeriodicity.setText(repeatsEvery + " " + repeatInterval
          / HOUR + " " + hours);
    else if (repeatInterval < 7 * DAY)
      wListItemHolder.mTextFieldPeriodicity.setText(repeatsEvery + " " + repeatInterval
          / DAY + " " + days);
    else if (repeatInterval < 29 * DAY)
      wListItemHolder.mTextFieldPeriodicity.setText(repeatsEvery + " " + repeatInterval
          / (7 * DAY) + " " + weeks);
    else
      wListItemHolder.mTextFieldPeriodicity.setText(repeatsEvery + " " + repeatInterval
          / (30 * DAY) + " " + months);

    return iConvertView;
  }
}
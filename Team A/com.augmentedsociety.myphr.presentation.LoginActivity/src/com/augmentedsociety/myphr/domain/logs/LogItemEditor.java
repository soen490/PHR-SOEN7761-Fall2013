package com.augmentedsociety.myphr.domain.logs;

/**
 * LogItemEditor receives LogEvents and populates a LogItem reflecting 
 * this event to be stored in the database.
 * 
 * @author Alexandre Hudon
 *
 */

import android.content.Context;

import com.augmentedsociety.myphr.R;
import com.augmentedsociety.myphr.domain.MapperException;

public class LogItemEditor implements LogEventListener
{

	private LogEvent mLogEvent;
	private LogItem mLogItem;
	private Context mContext;
	
  /**
   * General Constructor
   * @param iLogEvent The instance of the LogEvent.
   * @param iC The current application context.
   */
	public void LogReceived(LogEvent iLogEvent, Context iC)
	{
		mContext=iC;
		mLogEvent=iLogEvent;
		mLogItem = CreateLogItem();
		StoreLogItem();
	}

	/** 
	 * @return
	 *   Returns a LogItem
	 */
	private LogItem CreateLogItem()
	{
		try
		{
			LogItem newLogItem = 
					new LogItem(mLogEvent.getEventID(),mLogEvent.getEventType(),
							        GenerateEventInfo(mLogEvent.getEventType()),
							        mLogEvent.getEventDate());
			
			return newLogItem;
		}
		catch (Exception e)
		{
			//Failed to create LogItem.
			return null;
		}
		
	}
	
	/**
	 * Insert the LogItem in the Database.
	 */
	private void StoreLogItem()
	{
		try
		{
			LogEventMapper.insert(mLogItem, mContext);
		}
		catch(MapperException e)
		{
			//Failed to insert the log item in the database.
		}
	}
	
	/**
	 * @param iLET
	 * @return
	 *   Returns the event information based on the EventType.
	 */
	private String GenerateEventInfo(LogEventType iLET)
	{
		switch(iLET)
		{
			case VS_TEMP_CREATE:
				return mContext.getString(R.string.temp_desc);
			case VS_BLOODPRESSURE_CREATE:
				return mContext.getString(R.string.bp_desc);
			case VS_OXYGEN_CREATE:
				return mContext.getString(R.string.oxy_desc);
			case 	VS_BLOODSUGAR_CREATE:
				return mContext.getString(R.string.bs_desc);
			case 	VS_WEIGHT_CREATE:
				return mContext.getString(R.string.weight_desc);
			case 	NOTIF_CREATE:
				return mContext.getString(R.string.notif_create);
			case 	NOTIF_EDIT:
				return mContext.getString(R.string.notif_edit);
			case 	NOTIF_DELETE:
				return mContext.getString(R.string.notif_delete);
			case 	PROFILE_EDIT:
				return mContext.getString(R.string.profile_edit);
			case 	PROFILE_DELETE:
				return mContext.getString(R.string.profile_delete);
			case 	SETTINGS_LANG_EDIT:
				return mContext.getString(R.string.setting_lang_edit);
			case SETTING_APP_EDIT:
				return mContext.getString(R.string.app_set);
			case APP_LOGIN:
				return mContext.getString(R.string.app_login);
			case APP_LOGOUT:
				return mContext.getString(R.string.app_logout);
			case DISCLAIMER_ACCEPT:
				return mContext.getString(R.string.disc_accept);
			case 	DISCLAIMER_DECLINE:
				return mContext.getString(R.string.disc_reject);
			default:
			return mContext.getString(R.string.gen_eventtype_error);
		}
	
		
	}
	
}

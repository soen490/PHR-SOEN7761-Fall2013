package com.augmentedsociety.myphr.domain.logs;

/**
 * LogItem class. Encapsulates information derived from a LogEvent.
 * 
 * @author Alexandre Hudon
 *
 */

public class LogItem
{

	private String mEventInfo;
	private LogEventType mEventType;
	private Long mID;
	private Long mDate;
	
	 /**
  * General Constructor
  * @param iID The unique ID of the event.
  * @param iEventType The LogEventType of the LogItem.
  * @param iInfo Information related to the LogItem. Created by the LogItemEditor.
  * @param iCurrentDate The data at which the LogItem took place.
  */
	public LogItem(Long iID, LogEventType iEventType, String iInfo, Long iDate)
	{
		mEventType=iEventType;
		mDate=iDate;
		mID = iID;
		mEventInfo=iInfo;
	}
	
	/**
	 * @return
	 *   Returns the Event Info
	 */
	public String getEventInfo()
	{
		return mEventInfo;
	}
	
	/** 
	 * @return
	 *   Returns the Event Type
	 */
	public LogEventType getEventType()
	{
		return mEventType;
	}
	
	/** 
	 * @return
	 *   Returns the Event unique ID
	 */
	public Long getEventID()
	{
		return mID;
	}
	
	/**
	 * @return
	 *   Returns the date at which the event took place
	 */
	public Long getEventDate()
	{
		return mDate;
	}
	
}

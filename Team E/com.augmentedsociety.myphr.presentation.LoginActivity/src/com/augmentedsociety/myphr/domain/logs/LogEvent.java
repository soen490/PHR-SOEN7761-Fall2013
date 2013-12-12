package com.augmentedsociety.myphr.domain.logs;

import java.util.EventObject;
import java.util.Date;

/**
 * Encapsulates log-related event.
 * 
 * @author Alexandre Hudon
 *
 */

public class LogEvent extends EventObject
{

	private static final long serialVersionUID = -5888560365720252377L;
	
	private LogEventType mEventType;
	private Long mID;
	private Long mDate;
	
	 /**
   * General Constructor
   * @param source The instance responsible for the event.
   * @param iID The unique ID of the event.
   * @param iEventType The LogEventType of the LogEvent.
   * @param iCurrentDate The data at which the event is taking place.
   */
	public LogEvent(Object source, Long iID, LogEventType iEventType, Date iCurrentDate )
	{
		super(source);
		mEventType=iEventType;
		mDate=iCurrentDate.getTime();
		mID = iID;
	}
	
	//Returns log event type
	
	public LogEventType getEventType()
	{
		return mEventType;
	}
	
	//Returns log event unique ID
	
	public Long getEventID()
	{
		return mID;
	}
	
	//Returns log event date
	public Long getEventDate()
	{
		return mDate;
	}
}

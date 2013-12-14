package com.augmentedsociety.myphr.presentation.logs;


/**
 * LogEventEmitter fires LogEvent to LogItemEditor.
 * 
 * @author Alexandre Hudon
 *
 */

import java.util.Date;

import com.augmentedsociety.myphr.domain.MapperException;
import com.augmentedsociety.myphr.domain.logs.LogEvent;
import com.augmentedsociety.myphr.domain.logs.LogEventMapper;
import com.augmentedsociety.myphr.domain.logs.LogEventType;
import com.augmentedsociety.myphr.domain.logs.LogItemEditor;

import android.content.Context;

public class LogEventEmitter
{
	private static LogItemEditor mLogItemEditor = new LogItemEditor();
	
	 /**
  * FireLogEvent
  * @param iSource The instance responsible for the event.
  * @param iC The context of the application.
  * @param iLET The LogEventType of the LogEvent.
  */
	public static synchronized void fireLogEvent(Object iSource, Context iC, LogEventType iLET)
	{
		Date timeOfEvent = new Date(System.currentTimeMillis());		
		try
		{
			LogEvent logEvent = new LogEvent(iSource, LogEventMapper.getNextAvailableId(iC), iLET, timeOfEvent);
			mLogItemEditor.LogReceived(logEvent, iC);
		} 
		catch (MapperException e)
		{
			e.printStackTrace();
		}
	}
}

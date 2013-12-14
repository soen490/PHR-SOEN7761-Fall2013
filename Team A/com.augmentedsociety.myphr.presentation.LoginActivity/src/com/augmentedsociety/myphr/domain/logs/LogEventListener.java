package com.augmentedsociety.myphr.domain.logs;

import android.content.Context;

/**
 * LogEventListener Interface, enforces implementation of Log Received 
 * triggered by fireLogEvent.
 * 
 * @author Alexandre Hudon
 *
 */

public interface LogEventListener
{
  /**
   * LogReceived
   * @param iLogEvent The instance of the LogEvent taking place.
   * @param iC The current context.
   */
	 public void LogReceived(LogEvent iLogEvent, Context iC);
}

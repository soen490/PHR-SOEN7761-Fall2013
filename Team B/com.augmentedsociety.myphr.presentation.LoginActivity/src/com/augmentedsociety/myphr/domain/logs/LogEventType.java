package com.augmentedsociety.myphr.domain.logs;

/**
 * Enum containing the different possible types of events.
 * 
 * @author Alexandre Hudon
 *
 */
public enum LogEventType
{
	/* Vital Signs */
	VS_TEMP_CREATE, 
	VS_BLOODPRESSURE_CREATE, 
	VS_OXYGEN_CREATE, 
	VS_BLOODSUGAR_CREATE, 
	VS_WEIGHT_CREATE,
	
	/* Notifications */
	NOTIF_CREATE,   NOTIF_EDIT, 
	NOTIF_DELETE,   PROFILE_EDIT, 
	
	PROFILE_DELETE,     SETTINGS_LANG_EDIT, 
	SETTING_APP_EDIT,   APP_LOGIN, 
	APP_LOGOUT,         DISCLAIMER_ACCEPT, 
	DISCLAIMER_DECLINE, PROFILE_CREATE,
	
	/* Personal Info */
	ALLERGY_CREATE,	MEDICATION_CREATE,
	IMMUNIZATION_CREATE, MEDICAL_HISTORY_CREATE;
}
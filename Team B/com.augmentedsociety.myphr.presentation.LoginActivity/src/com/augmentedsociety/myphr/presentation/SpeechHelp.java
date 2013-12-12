package com.augmentedsociety.myphr.presentation;

import java.util.HashMap;

import android.app.Activity;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.util.Log;
import android.view.MenuItem;

import com.augmentedsociety.myphr.R;

public class SpeechHelp
{
	static boolean mActive = false;
	private static SpeechHelp instance = null;
	private MenuActivity mAct;
	private MediaPlayer mMediaPlayer;
	private String lastPlayedSpeech;
	
	int HELP_INDEX = 0;
	
	private HashMap<String, Integer> speech;

	// Singleton
	public static SpeechHelp getInstance()
	{
		if (instance == null)
		{
			instance = new SpeechHelp();
			//initSpeech();
		}
		return instance;
	}
	
	private void initSpeech(MenuActivity menu)
	{
		speech = new HashMap<String, Integer>();
		speech.put("overview", R.raw.overview_1);
		speech.put("bloodPressure", R.raw.blood_pressure_2);
		speech.put("bodyWeight", R.raw.body_weight_3);
		speech.put("o2Saturation",R.raw.o2_saturation_4);
		speech.put("temperature", R.raw.temperature_5);
		speech.put("bloodSugar", R.raw.blood_sugar_6);
		speech.put("valuesSaved", R.raw.values_saved_7);
		speech.put("measurementProfiles", R.raw.measurement_profiles_8_old);
		speech.put("reminder", R.raw.reminders_9);
		speech.put("personalInformation", R.raw.personal_information_10);
		speech.put("graphs", R.raw.graphs_11);
		speech.put("graphBloodPressure",R.raw.graph_blood_pressure_12_1);
		speech.put("graphBloodSugar",R.raw.graph_blood_sugar_12_2);
		speech.put("graphBodyWeight",R.raw.graph_body_weight12_3);
		speech.put("graphO2Saturation", R.raw.graph_o2_saturation_12_4);
		speech.put("graphTemperature", R.raw.graph_temperature_12_5);
	}

	public boolean isActive()
	{
		return mActive;
	}

	public void enableTutIcon(MenuActivity menuActivity)
	{
		MenuItem item = menuActivity.getMenu().getItem(HELP_INDEX);
		item.setIcon(menuActivity.getResources()
				.getDrawable(R.drawable.ic_menu_help_active));
	}
	
	public void disableTutIcon(MenuActivity menuActivity)
	{
		MenuItem item = menuActivity.getMenu().getItem(HELP_INDEX);
		item.setIcon(menuActivity.getResources()
				.getDrawable(R.drawable.ic_menu_help));
	}
	
	public void setActive(boolean active, MenuActivity menuActivity)
	{
		if(menuActivity == null)
			initSpeech(menuActivity);
		mActive = active;
		mAct = menuActivity;
	}

	public void updateMenuIcon()
	{
		if(mActive)
			enableTutIcon(mAct);
		else
			disableTutIcon(mAct);
	}
	
	public MediaPlayer playTutorial(Activity iAct, String speechName)
	{
		lastPlayedSpeech = speechName;
		if(iAct != null)
			initSpeech((MenuActivity)iAct);
		if(!isActive())
			return null;
		if(mMediaPlayer != null)
				mMediaPlayer.release();
		mMediaPlayer = MediaPlayer.create(iAct, speech.get(speechName));

		
		mMediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener()
		{
			
			@Override
			public boolean onError(MediaPlayer mp, int what, int extra)
			{
				Log.d("Error", "Error "+what+" extra "+extra);
				return false;
			}
		});
		
//	  try
//		{
//			mMediaPlayer.prepare();
//		} catch (IllegalStateException e)
//		{
//			e.printStackTrace();
//		} catch (IOException e)
//		{
//			e.printStackTrace();
//		}
	  
	  
		if (mMediaPlayer.isPlaying())
		{
			mMediaPlayer.stop();
			mMediaPlayer.release();
		}
		// MediaPlayer mediaPlayer = new MediaPlayer();
		mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

		mMediaPlayer.setOnCompletionListener(new OnCompletionListener()
		{

			@Override
			public void onCompletion(MediaPlayer mp)
			{
//				mp.release();
			}

		});
		mMediaPlayer.start();
		return mMediaPlayer;
	}

	public void stopTutorial()
	{
		if(!isActive())
			return;
		if (mMediaPlayer != null && mMediaPlayer.isPlaying())
		{
			mMediaPlayer.stop();
			mMediaPlayer.release();
		}
	}
	
	public String getLastPlayedSpeech()
	{
		return lastPlayedSpeech;
	}
	

}

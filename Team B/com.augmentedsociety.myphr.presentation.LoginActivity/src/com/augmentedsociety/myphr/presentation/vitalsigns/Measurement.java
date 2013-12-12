package com.augmentedsociety.myphr.presentation.vitalsigns;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import com.augmentedsociety.myphr.R;
import android.view.View;
import android.content.Context;
import com.augmentedsociety.myphr.domain.BloodPressureReading;
import com.augmentedsociety.myphr.domain.BloodSugarReading;
import com.augmentedsociety.myphr.domain.OxygenSaturationReading;
import com.augmentedsociety.myphr.domain.TemperatureReading;
import com.augmentedsociety.myphr.domain.WeightReading;
import com.augmentedsociety.myphr.domain.commands.CommandAction;

public class Measurement
{
	private static Hashtable<String,List<String>> mDataArray;
	private static String mGraphTitle;
	
	public static void initializeDataArray(int iCurrentSelection, View iView, CommandAction iCA)
	{
		if(mDataArray == null)
			mDataArray = new Hashtable<String,List<String>>();
		else
			mDataArray.clear();
		Context context = iView.getContext();
		switch(iCurrentSelection)
		{
			case VitalSignsActivity.WEIGHT:
				ArrayList<WeightReading> wReadings = iCA.viewWeight(iView.getContext());
				addLabel(R.string.weight_label, context );
				setTitle( R.string.weight_graph_title, context );
				for( WeightReading wr : wReadings)
				{
					addPoint(String.valueOf(wr.getDateTaken().getTime()), String.valueOf(wr.getWeight()));
				}
				break;
			case VitalSignsActivity.TEMPERATURE:
				ArrayList<TemperatureReading> tReadings = iCA.viewTemperature(iView.getContext());
				addLabel(R.string.temperature_label, context);
				setTitle( R.string.temperature_graph_title, context );
				for( TemperatureReading wr : tReadings)
				{
					addPoint(String.valueOf(wr.getDateTaken().getTime()), String.valueOf(wr.getTemperature()));
				}
				break;
			case VitalSignsActivity.BLOOD_PRESSURE_HR:
				ArrayList<BloodPressureReading> BPReadings = iCA.viewBloodPressure(iView.getContext());
				addLabel(R.string.systolic_label, context);
				addLabel(R.string.diastolic_label, context);
				addLabel(R.string.heart_rate_label, context);
				setTitle( R.string.blood_pressure_graph_title, context );
				for( BloodPressureReading wr : BPReadings)
				{
					addPoint(String.valueOf(wr.getDateTaken().getTime()), String.valueOf(wr.getSystolic()));
					addPoint(String.valueOf(wr.getDateTaken().getTime()), String.valueOf(wr.getDiastolic()));
					addPoint(String.valueOf(wr.getDateTaken().getTime()), String.valueOf(wr.getHeartrate()));
				}
				break;
			case VitalSignsActivity.BLOOD_SUGAR:
				ArrayList<BloodSugarReading> readings = iCA.viewBloodSugar(iView.getContext());
				addLabel(R.string.glucose_label, context);
				setTitle( R.string.glucose_graph_title, context );
				for( BloodSugarReading wr : readings)
				{
					addPoint(String.valueOf(wr.getDateTaken().getTime()), String.valueOf(wr.getBloodGlucose()));
				}
				break;
			case VitalSignsActivity.O2:
				ArrayList<OxygenSaturationReading> OSReadings = iCA.viewOxygenSaturation(iView.getContext());
				addLabel(R.string.oxygen_label, context);
				setTitle( R.string.oxygen_graph_title, context );
				for( OxygenSaturationReading wr : OSReadings)
				{
					addPoint(String.valueOf(wr.getDateTaken().getTime()), String.valueOf(wr.getOxygenSaturation()));
				}
				break;
			default: break;
		}
	}
	
	public static void setDataArray(Hashtable<String,List<String>> iDataArray)
	{
		mDataArray = iDataArray;
	}
	public static Hashtable<String,List<String>> getDataArray()
	{
		return mDataArray;
	}
	public static List<String> getLabels()
	{
		List<String> retVal = mDataArray.get("label");
		return retVal;
	}
	public static void addLabel( int iValue, Context iContext )
	{
		addLabel( iContext.getString( iValue ) );
	}
	public static void addLabel(String iValue)
	{
		if(mDataArray.containsKey("label"))
		{
			mDataArray.get("label").add(iValue);
		}
		else
		{
			List<String> newList = new ArrayList<String>();
			newList.add(iValue);
			mDataArray.put("label", newList);
		}
	}
	public static void addPoint(String iKey, String iValue)
	{
		if(mDataArray.containsKey(iKey))
		{
			mDataArray.get(iKey).add(iValue);
		}
		else
		{
			List<String> newList = new ArrayList<String>();
			newList.add(""); // toggle "" => enabled, "1" => disabled
			newList.add(iValue);
			mDataArray.put(iKey, newList);
		}
	}
	public static void clearDataArray()
	{
		if(mDataArray != null)
			mDataArray.clear();
	}
	public static boolean isVisible(String iKey)
	{
		if(mDataArray.containsKey(iKey) && mDataArray.get(iKey).get(0).trim().equals(""))
			return true;
		else
			return false;
	}
	public static void toggleVisibility(String iKey)
	{
		if(mDataArray.containsKey(iKey))
		{
			if(mDataArray.get(iKey).get(0).equals(""))
				mDataArray.get(iKey).set(0, "1");
			else
				mDataArray.get(iKey).set(0, "");
		}
	}
	public static List<String> getPoints(String iKey)
	{
		if(mDataArray.containsKey(iKey))
			return mDataArray.get(iKey);
		return new ArrayList<String>();
	}
	public static boolean isVisible(List<String> iValue)
	{
		return iValue.get(0).equals("");
	}
	public static void setTitle( int iTitle, Context iContext )
	{
		mGraphTitle = iContext.getString(iTitle);
	}
	public static String getTitle()
	{
		return mGraphTitle;
	}
	public static String getHtmlStringFromData(String iHTMLString)
	{
		String retVal = iHTMLString;
		String dataPoints1 = "";
		String dataPoints2 = "";
		String dataPoints3 = "";
		
		retVal = retVal.replaceAll( "%TITLE%", getTitle());
		retVal = retVal.replaceAll("%LABEL1%", getLabels().get(0));
		if(getLabels().size() > 1)
		{
			retVal = retVal.replaceAll("%LABEL2%", getLabels().get(1));
			retVal = retVal.replaceAll("%LABEL3%", getLabels().get(2));
		}
		else
		{
			retVal = retVal.replaceAll("%LABEL2%", "");
			retVal = retVal.replaceAll("%LABEL3%", "");
		}
		
		List<String> keys = new ArrayList<String>(mDataArray.keySet()); 
		java.util.Collections.sort(keys);
		for(String key : keys)
		{
			if(key.equals("label"))
				continue;
			List<String> value = getPoints(key);
			if(isVisible(value))
			{
				// single entry measurements
				dataPoints1 = dataPoints1.concat("[" + key + "," + value.get(1) + "],");
				if(value.size() > 2)
				{
					// only blood pressure measurement
					dataPoints2 = dataPoints2.concat("[" + key + "," + value.get(2) + "],");
					dataPoints3 = dataPoints3.concat("[" + key + "," + value.get(3) + "],");
				}
			}
		}
		retVal = retVal.replaceAll("%DATA1%", dataPoints1);
		retVal = retVal.replaceAll("%DATA2%", dataPoints2);
		retVal = retVal.replaceAll("%DATA3%", dataPoints3);
		return retVal;
	}
}

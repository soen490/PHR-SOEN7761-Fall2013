package com.augmentedsociety.myphr.presentation.vitalsigns;

import java.util.Date;

import android.view.View;

public interface VitalSignsComponentInterface 
{
	public void submit(View iView, Date date);
	public void bluetooth(View iView);
	public void finish(View iView);
}

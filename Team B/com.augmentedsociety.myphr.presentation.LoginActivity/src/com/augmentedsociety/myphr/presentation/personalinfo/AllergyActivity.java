package com.augmentedsociety.myphr.presentation.personalinfo;

import com.augmentedsociety.myphr.R;

import com.augmentedsociety.myphr.presentation.PersonalInformationActivity;
import com.augmentedsociety.myphr.presentation.logs.LogEventEmitter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import android.widget.Toast;

import com.augmentedsociety.myphr.domain.AllergyMapper;
import com.augmentedsociety.myphr.domain.Allergy;
import com.augmentedsociety.myphr.domain.MapperException;
import com.augmentedsociety.myphr.domain.logs.LogEventType;

/**
 * Create new allergy activity
 * 
 * @author Rajan Jayakumar
 *
 */

public class AllergyActivity extends PersonalInfoComponent
{
	public AllergyActivity(Activity iAct)
	{
		super(iAct);
	}
	
	public AllergyActivity(Activity iAct, long iID)
	{
		super(iAct);
		this.mID = iID;
		this.exists = true;
	}
	
	public View fillExistingView(View iView)
	{
		Context context = iView.getContext();
		Allergy currentAllergy;
		try
		{
			currentAllergy = AllergyMapper.getOne(mID, context);
			TextView allergy = (TextView) iView.findViewById(R.id.allergy_details);
			allergy.setText(currentAllergy.getAllergic());
			TextView reaction = (TextView) iView.findViewById(R.id.allergy_reaction);
			reaction.setText(currentAllergy.getReaction());
			RadioButton sensitivity;
			sensitivity = (RadioButton)iView.findViewById(R.id.low_set);
			if(currentAllergy.getSeverity().equals(sensitivity.getText()))
			{
				sensitivity.setChecked(true);
			}
			sensitivity = (RadioButton)iView.findViewById(R.id.medium_set);
			if(currentAllergy.getSeverity().equals(sensitivity.getText()))
			{
				sensitivity.setChecked(true);
			}
			sensitivity = (RadioButton)iView.findViewById(R.id.high_set);
			if(currentAllergy.getSeverity().equals(sensitivity.getText()))
			{
				sensitivity.setChecked(true);
			}
		} catch (MapperException e)
		{
			Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
		}
		return iView;
	}
	
	@Override
  public void submit(View iView)
  {
	  	/**Get the view's current context for proper detection of activity launch*/
	    Context context = iView.getContext();
	    
			/**References to text box values*/
			TextView allergy, reaction;
			allergy = (TextView) mAct.findViewById(R.id.allergy_details);
			reaction = (TextView) mAct.findViewById(R.id.allergy_reaction);
			
			/**References to Radio Button Group*/
			RadioGroup severity;
			severity = (RadioGroup) mAct.findViewById(R.id.radioGroupAllergy);

			String tAllergy, tReaction, tSeverity = "";
			tAllergy = allergy.getText().toString();
			tReaction = reaction.getText().toString();

			RadioButton rSeverity;
			rSeverity = (RadioButton) mAct.findViewById(severity.getCheckedRadioButtonId());
			if(rSeverity != null)
			{
				tSeverity = rSeverity.getText().toString();
			}

			if (!tAllergy.isEmpty() && !tReaction.isEmpty() && !tSeverity.isEmpty())
			{
				try
				{
					if(exists)
					{
						AllergyMapper.update(new Allergy(mID, tAllergy, tReaction, tSeverity), context);
					}
					else
					{
						AllergyMapper.insert(new Allergy( AllergyMapper.getNextAvailableId(context), tAllergy, tReaction, tSeverity ), context);
					}
					
					/**Fires a LogEvent to the LogItemEditor*/
					LogEventEmitter.fireLogEvent(this, iView.getContext(), LogEventType.ALLERGY_CREATE);
					
					/**Calls a short text box confirming data save. Should not occur if an exception should be caught inside mapper*/
			    Toast.makeText(context, context.getString(R.string.confirm_save), Toast.LENGTH_SHORT).show();
					
				} catch (MapperException e)
				{
					Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
				}
								
		    /**After data save, text box values are reset and focus is placed to the first entry of the page list*/
				allergy.setText(null);
				reaction.setText(null);
				severity.clearCheck();
		    
				allergy.requestFocus();
			}
			else
			{
				//Request fill all fields
				Toast.makeText(context, context.getString(R.string.fill_all), Toast.LENGTH_SHORT).show();
			}
			cancel(iView);
  }
	
  @Override
  public void cancel(View iView)
  {
    PersonalInformationActivity wParent = (PersonalInformationActivity) mAct;
    wParent.HandleAllergiesButton(iView);
  }
}
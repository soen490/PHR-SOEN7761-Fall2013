package com.augmentedsociety.myphr.presentation.medications;

import android.widget.ImageView;
import android.widget.TextView;

/**
 * A struct-like class holding pointers to views that are displayed to the user.
 * Not a domain object structure, purely representation
 * @author Yuri Kitaev
 * @author Rajan Jayakumar
 *
 */
public class MedicationListItemViewHolder 
{
	 TextView mTextFieldMedicationType = null;
	 TextView mTextFieldMedicationName = null;
	 TextView mTextFieldMedicationPosology = null;
	 TextView mTextFieldMedicationStrength = null;
	 TextView mTextFieldMedicationFrequency = null;
	 TextView mTextFieldMedicationStartDate = null;
	 TextView mTextFieldMedicationEndDate = null;
	 TextView mTextFieldMedicationReasons = null;
	 TextView mTextFieldMedicationDoctor = null;
}
package com.augmentedsociety.myphr.presentation.notifications;

import android.widget.ImageView;
import android.widget.TextView;

/**
 * A struct-like class holding pointers to views that are displayed to the user.
 * Not a domain object structure, purely representation
 * @author Yuri Kitaev
 *
 */
public class NotificationListItemViewHolder 
{
	 TextView mTextFieldTitle = null;
	 TextView mTextFieldFirstOccurrence = null;
	 TextView mTextFieldPeriodicity = null;
	 TextView mTextFieldEnabledState = null;
	 ImageView mItemImage = null;
}
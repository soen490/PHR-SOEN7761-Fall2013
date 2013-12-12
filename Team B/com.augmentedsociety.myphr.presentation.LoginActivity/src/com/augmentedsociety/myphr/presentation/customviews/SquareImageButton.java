package com.augmentedsociety.myphr.presentation.customviews;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageButton;
/**
 * Extension of the ImageButton class
 * Make the button square shaped all the time by width = height
 * @author Dipesh Patel
 * 
 * @important 
 *   All 3 concstuctors are needed or else the app crashes
 */
public class SquareImageButton extends ImageButton {
  public SquareImageButton(Context iContext)
  {
    super(iContext);
  }

  public SquareImageButton(Context iContext, AttributeSet iAttrs)
  {
    super(iContext, iAttrs);
  }

  public SquareImageButton(Context iContext, AttributeSet iAttrs, int iDefStyle)
  {
    super(iContext, iAttrs, iDefStyle);
  }

  @Override
  public void onMeasure(int iWidth, int iHeigth)
  {
    super.onMeasure(iHeigth, iHeigth);
  }
}

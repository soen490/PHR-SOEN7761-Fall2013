package com.augmentedsociety.myphr.presentation.documents;

import java.io.ByteArrayOutputStream;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.augmentedsociety.myphr.R;
import com.augmentedsociety.myphr.domain.Document;
import com.augmentedsociety.myphr.domain.DocumentMapper;

public class DocumentFormActivity extends Activity
{

  @Override
  protected void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_document_form);

    Bitmap wImage = null;
    Uri data = null;
    Bundle extras; 
    ImageView imgView = (ImageView) 
        findViewById(R.id.imageview_document_preview_widget);
    
    extras = getIntent().getExtras();

    if (null != extras)
    {
      data = (Uri) extras.get("DOCUMENT_TO_ADD");
    }
    
    imgView.setImageURI(data);

  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu)
  {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.activity_document_form, menu);
    return true;
  }
  
  /** TODO This has to be added to the command instead.
   * 
   * @param iView
   */
  public void submit(View iView)
  {
    Document wDoc = null; 
    EditText wTitle = (EditText) findViewById(R.id.document_title_text_edit);
    EditText wDesc = (EditText) findViewById(R.id.document_description_text_edit);
    ImageView	wImgView = (ImageView) findViewById(R.id.imageview_document_preview_widget);
    
    /* Thanks to http://stackoverflow.com/questions/4435806/ */ 
    Drawable wDocPic = wImgView.getDrawable();
    Bitmap wBitmap = ((BitmapDrawable)wDocPic).getBitmap();
    ByteArrayOutputStream wStream = new ByteArrayOutputStream();
    wBitmap.compress(Bitmap.CompressFormat.JPEG, 100, wStream);
    byte[] wBitmapdata = wStream.toByteArray();
    String wData = new String(wBitmapdata);
    
    wDoc = new Document(wTitle.getText().toString(), 
                        wDesc.getText().toString(), wData);
    DocumentMapper.insert(wDoc, this);
  }
  /** 
   * On cancel, scrap everything, get back to documents
   * @param iView
   */
  public void cancel(View iView)
  {
    Intent intent = new Intent(this, DocumentsActivity.class);
    startActivity(intent);
  }

}

package com.augmentedsociety.myphr.presentation;


import android.os.Bundle;
//import java.util.ArrayList;

import com.augmentedsociety.myphr.R;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;


import android.text.TextUtils;
import android.view.View;

import android.widget.Toast;

public class ZBarScannerActivity  extends Activity
{
	
  private static final int ZBAR_SCANNER_REQUEST = 0;
  private static final int ZBAR_QR_SCANNER_REQUEST = 1;

  @Override
  public void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.first_page);
      
  }

  public void launchScanner(View v) {
      if (isCameraAvailable()) {
          Intent intent = new Intent(this, ZBarScannerActivity.class);
          startActivityForResult(intent, ZBAR_SCANNER_REQUEST);
      } else {
          Toast.makeText(this, "Rear Facing Camera Unavailable", Toast.LENGTH_SHORT).show();
      }
  }

  public void launchQRScanner(View v) {
      if (isCameraAvailable()) {
          Intent intent = new Intent(this, ZBarScannerActivity.class);
          startActivityForResult(intent, ZBAR_SCANNER_REQUEST);
         Toast.makeText(this, "Rear Facing Camera is available", Toast.LENGTH_SHORT).show();
      } else {
          Toast.makeText(this, "Rear Facing Camera Unavailable", Toast.LENGTH_SHORT).show();
      }
  }

  public boolean isCameraAvailable() {
      PackageManager pm = getPackageManager();
      return pm.hasSystemFeature(PackageManager.FEATURE_CAMERA);
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
      switch (requestCode) {
          case ZBAR_SCANNER_REQUEST:
          case ZBAR_QR_SCANNER_REQUEST:
              if (resultCode == RESULT_OK) {
                  Toast.makeText(this, "Scan Result = " + data.getStringExtra("SCAN_RESULT"), Toast.LENGTH_SHORT).show();
              } else if(resultCode == RESULT_CANCELED && data != null) {
                  String error =data.getStringExtra("ERROR_INFO");
                  if(!TextUtils.isEmpty(error)) {
                      Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
                  }
              }
              break;
      }
  } 

}

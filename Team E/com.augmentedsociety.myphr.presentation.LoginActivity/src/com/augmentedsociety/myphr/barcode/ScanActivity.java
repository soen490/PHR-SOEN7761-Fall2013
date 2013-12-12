package com.augmentedsociety.myphr.barcode;

import com.augmentedsociety.myphr.R;
import com.augmentedsociety.myphr.domain.MapperException;
import com.augmentedsociety.myphr.presentation.DisclaimerActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class ScanActivity extends Activity implements OnClickListener {
//	Button btn;
	public TextView tv;
	public String upc;
	public static int id=0;
	public static String Ireader="";
	SearchByRoom SR;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.first_page);
		//btn = (Button) findViewById(R.id.scanQR);
		tv = (TextView) findViewById(R.id.result);
	//	Intent intent= new Intent(this, SensorActivity.class);
		//startActivity(intent);
   
		IntentIntegrator.initiateScan(this);
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	//	getMenuInflater().inflate(R.menu.main_menu, menu);
		return true;
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case IntentIntegrator.REQUEST_CODE: {
			if (resultCode != RESULT_CANCELED) {
				IntentResult scanResult = IntentIntegrator.parseActivityResult(
						requestCode, resultCode, data);
				if (scanResult != null) {
					 upc = scanResult.getContents();
	        try
	        {
	         id= Integer.parseInt(upc.toString()); //strToInt(upc);
	        Ireader=upc.toString();
	        }
	        catch (NumberFormatException e) {
	        	Log.i("converting string into integer",e.getMessage() );
	        }
	      //  Toast.makeText(getApplicationContext(), id,Toast.LENGTH_SHORT).show();
					// put whatever you want to do with the code here
					//tv.setText(upc);
					//setContentView(tv);
	        
	        //Log.i("Barcode", String.valueOf(id));
					try
					{
					Intent intent = new Intent(this,PatientInfo.class);
					startActivity(intent);
				}catch (Exception e)
				{
					Log.i("problem 5",e.getMessage());
					//Toast.makeText(this, "the problem in scan", Toast.LENGTH_LONG).show();
					Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
				}
				}
			}
				
			break;
		}
		}
	}

	public void onClick(View v) {
		Log.d("Scan", "Onlick");
		switch (v.getId()) {
		case R.id.scanQR:
			SR.isItByRoom = false;
			IntentIntegrator.initiateScan(this);
			break;
		}
	}
}

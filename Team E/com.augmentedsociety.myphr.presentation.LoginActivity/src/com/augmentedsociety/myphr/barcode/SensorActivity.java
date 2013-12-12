package com.augmentedsociety.myphr.barcode;

//import com.augmentedsociety.myphr.R;

import java.util.List;

import com.augmentedsociety.myphr.R;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.hardware.Camera.Parameters;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
//import android.util.Log;
import android.widget.Toast;


public class SensorActivity extends Activity implements SensorEventListener
{
		public boolean isLightOn = false;
		SearchByRoom SR;

	 //private Camera camera;
		Button btn;
		public TextView tv;
		private  SensorManager sensorManager;
		private  Sensor lightSensor;
		//public float lightQuantity;
		
		//public  SensorActivity() { 
		public final void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	  	setContentView(R.layout.first_page);
			btn = (Button) findViewById(R.id.scanQR);
			tv = (TextView) findViewById(R.id.result);
		
	    sensorManager = (SensorManager)getSystemService(Context.SENSOR_SERVICE);
		  lightSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
		  
		}
		   
		protected void onResume (){
			super.onResume();
			sensorManager.registerListener(this,lightSensor,SensorManager.SENSOR_DELAY_NORMAL);
		}
		
		protected void onPause(){
			super.onPause();
			sensorManager.unregisterListener(this);
			
		}
		@Override
		public void onAccuracyChanged(Sensor event, int arg1)
		{
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onSensorChanged(SensorEvent event)
		{
			// TODO Auto-generated method stub
		//	s.tv.setText("value: " + event.values[0] + " lux" ); 
		    //if( event.sensor.getType() == Sensor.TYPE_LIGHT)
			

			WindowManager.LayoutParams layoutParams = getWindow().getAttributes();
      if (event.values[0]>40) {
       	
      	layoutParams.screenBrightness = 1F; // set 50% brightness
				getWindow().setAttributes(layoutParams);
        Toast.makeText(getApplicationContext(),"the flashlight is turn off!",Toast.LENGTH_SHORT).show();


			} 
      else {
				
					layoutParams.screenBrightness = 0.4F; // set 50% brightness
				getWindow().setAttributes(layoutParams);
        Toast.makeText(getApplicationContext(),"flashlight is turn on!",Toast.LENGTH_SHORT).show();


						}
		      
		        Toast.makeText(getApplicationContext(),"On SensorChanged"+ event.values[0],Toast.LENGTH_SHORT).show();
		  /*     
		     Context context = this;
						PackageManager pm = context.getPackageManager();
						camera = Camera.open();
						if (!pm.hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
							Log.e("err", "Device has no camera!");
							return;
						}
						final Parameters p = camera.getParameters();
					  //List<String> flashModes = p.getSupportedFlashModes();
					  //if(flashModes==null){
					  	//Log.i("info", "LED Not Available");
					  //}
					 // else
					  {
		        if (event.values[0]>120) {
 		        	
		        	layoutParams.screenBrightness = 1F; // set 50% brightness
							getWindow().setAttributes(layoutParams);
							Log.i("info", "the flashlight is turn off!");
			        Toast.makeText(getApplicationContext(),"the flashlight is turn off!",Toast.LENGTH_SHORT).show();
			        isLightOn =true;
							p.setFlashMode(Parameters.FLASH_MODE_OFF);
							camera.setParameters(p);
							camera.stopPreview();
		 
						} else {
		 					layoutParams.screenBrightness = 0.4F; // set 50% brightness
							getWindow().setAttributes(layoutParams);
							Log.i("info", "flashlight is turn on!");
			        Toast.makeText(getApplicationContext(),"flashlight is turn on!",Toast.LENGTH_SHORT).show();
			        isLightOn =true;
							p.setFlashMode(Parameters.FLASH_MODE_TORCH);
							
							camera.setParameters(p);
							camera.startPreview();
		 
						}
					  }*/
		}
	/*
		@Override
		protected void onStop() {
			super.onStop();
	 
			if (camera != null) {
				camera.release();
			}
		}
*/
		public void onClick(View v) {
			Log.d("Scan", "Onlick");
			switch (v.getId()) {
			case R.id.scanQR:
				SR.isItByRoom = false;
				Intent intent=new Intent(this,ScanActivity.class);
				startActivity(intent);
				//IntentIntegrator.initiateScan(this);
				break;
				
			}
		}
		public void onClick2(View v) {
			Log.d("Scan", "Onlick");
			switch (v.getId()) {
			
			case R.id.search:
				SR.isItByRoom = true;

				Intent intent2=new Intent(this,SearchByRoom.class);
				startActivity(intent2);
				break;
			}
		}
}
		
		
		
		
		
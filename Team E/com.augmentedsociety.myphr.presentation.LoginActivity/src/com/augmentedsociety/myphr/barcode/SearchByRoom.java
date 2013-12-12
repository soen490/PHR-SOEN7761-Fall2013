package com.augmentedsociety.myphr.barcode;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.augmentedsociety.myphr.R;

public class SearchByRoom extends Activity 
{
	public static boolean isItByRoom = false;
	public static String iroom="", ibed="";
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.search_by_room);
		
	}
	public void search()
	{
		
		EditText room,bed;
		room = (EditText) this.findViewById(R.id.RoomET);
		bed = (EditText) this.findViewById(R.id.BedET);
		iroom =room.getText().toString();
		ibed=bed.getText().toString();
	}
	public void onClick(View v) {
		Log.d("Scan", "Onlick");
		switch (v.getId()) {
		case R.id.SearchBT:
			search();
			if(iroom==""||ibed=="")
			{
        Toast.makeText(getApplicationContext(),"Please Enter Valid Numbers",Toast.LENGTH_SHORT).show();

			}
			else
			{
				isItByRoom = true;
			Intent intent=new Intent(this,PatientInfo.class);
			startActivity(intent);
			}
			break;

		}
	}
}

package com.augmentedsociety.myphr.presentation.documents;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.augmentedsociety.myphr.R;
import com.augmentedsociety.myphr.presentation.ToastMessage;

/**
 * The activity that handles managing documents. The user is given the abilities
 * to do the following: - add documents - remove all (or a selected) document -
 * sort the documents by name or date
 * 
 * @author psyomn
 * 
 */
public class DocumentsActivity extends Activity
{
	final String IMAGE_TYPE = "image/*";

	/**
	 * Static id for photo selecting intend (don't change this)
	 */
	private static final int SELECT_PHOTO = 100;

	private static final int CAPTURE_PHOTO = 200;

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_documents);
		
		ListView list = (ListView) findViewById(R.id.document_list);
		String[] tmp =
		{ "Document", "Document", "Document", "Document", "Document", "Document",
				"Document", "Document", "Document", "Document", "Document", "Document",
				"Document", "Document", "Document", "Document" };

		ArrayAdapter<String> arradapt = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, android.R.id.text1, tmp);

		list.setAdapter(arradapt);

		int currentapiVersion = android.os.Build.VERSION.SDK_INT;
		if (currentapiVersion >= android.os.Build.VERSION_CODES.FROYO)
		{
			getActionBar().setDisplayShowHomeEnabled(false);
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_documents, menu);

		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{

		switch (item.getItemId())
		{
		case R.id.menu_add_document:
			addDocumentFromImageGallery();
			break;
		case R.id.menu_add_document_from_camera:
			addDocumentFromSnapshot();
			break;
		case R.id.menu_clear_all_documents:
		  new ToastMessage(this,getString(R.string.clear_all_documents),Toast.LENGTH_LONG);
			break;
		case R.id.menu_sort_documents:
			new ToastMessage(this,getString(R.string.sort_documents), Toast.LENGTH_LONG);
			break;
		}


		return super.onOptionsItemSelected(item);
	}

	/**
	 * Routine to add new document
	 * 
	 * @param iView
	 */
	public void addDocument(View iView)
	{

	}

	/**
	 * Routine for if user wants to add a document from the current gallery in the
	 * filesystem.
	 * 
	 * Thanks to:
	 * http://stackoverflow.com/questions/2507898/how-to-pick-an-image-from
	 * -gallery-sd-card-for-my-app-in-android
	 */
	private void addDocumentFromImageGallery()
	{
		Intent intent = new Intent(Intent.ACTION_PICK);
		intent.setType(IMAGE_TYPE);
		startActivityForResult(intent, SELECT_PHOTO);
	}

	/**
	 * Routine to add a document by accessing the camera, and snapping a
	 * photograph.
	 * 
	 * http://developer.android.com/guide/topics/media/camera.html#intents
	 */
	private void addDocumentFromSnapshot()
	{
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		startActivityForResult(intent, CAPTURE_PHOTO);
	}

	/**
	 * 
	 * 
	 * Handle the callbacks from the camera/filesystem picker.
	 */
	@Override
	protected void onActivityResult(int iRequestCode, int iResultCode,
			Intent iData)
	{
		switch (iRequestCode)
		{
		case SELECT_PHOTO:
			handlePicturePickerReturn(iResultCode, iData);
			break;

		case CAPTURE_PHOTO:
			handlePictureCaptureReturn(iResultCode, iData);
			break;

		default:
			Log.e(this.getClass().getName(), getString(R.string.unhandled_callback_w_id)
					+ iRequestCode);
			break;
		}
	}

	/**
	 * This is similar to the other function that imports from filesystem but I'm
	 * keeping this in order to make sure there's no differences. May refactor in
	 * future.
	 * 
	 * 
	 * 
	 * @param iResultCode
	 * @param iData
	 */
	private void handlePictureCaptureReturn(int iResultCode, Intent iData)
	{
		Intent intent = null;

		if (RESULT_OK == iResultCode)
		{
			new ToastMessage(this, getString(R.string.imported_document), Toast.LENGTH_LONG);
			intent = new Intent(this, DocumentFormActivity.class);

			/* NB: We put the camera data to the new intent */
			intent.putExtra("DOCUMENT_TO_ADD", iData.getData());

			startActivity(intent);
		} else
			new ToastMessage(this, getString(R.string.import_not_successful), Toast.LENGTH_LONG);

	}

	/**
	 * handles the pictured picked return This opens the intent for adding info
	 * about the document
	 * 
	 * 
	 * 
	 * @param iResultCode
	 */
	private void handlePicturePickerReturn(int iResultCode, Intent iData)
	{
		Intent intent = null;

		if (RESULT_OK == iResultCode)
		{
			new ToastMessage(this, getString(R.string.imported_document), Toast.LENGTH_LONG);
			intent = new Intent(this, DocumentFormActivity.class);

			/* NB: We put the camera data to the new intent */
			intent.putExtra("DOCUMENT_TO_ADD", iData.getData());

			startActivity(intent);
		} else
			new ToastMessage(this, getString(R.string.import_not_successful), Toast.LENGTH_LONG);

	}
}

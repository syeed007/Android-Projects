package mahbubul.syeed.selfieme;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import mahbubul.syeed.adapter.ImageDescription;
import mahbubul.syeed.adapter.ImageListViewAdapter;
import mahbubul.syeed.alarmmanager.SetAlarm;

import android.app.ListActivity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;
import android.widget.Toast;

public class CameraActivity extends ListActivity {

	// file operation
	// private static FileOperations mFileOperation = new FileOperations();
	private static final String FILE_NAME = "selfiemelist.txt";
	

	private static Boolean onCreateState = true;
	static final int REQUEST_IMAGE_CAPTURE = 1; // to identify camera request
												// return result..

	static String mCurrentPhotoPath = "";

	// local variables
	private static SetAlarm mSetAlarm = null;

	private static Boolean isNoCameraIstalled = false;
	ImageListViewAdapter mImageAdapter = null;
	ImageDescription mImageDescription = null;
	private static String timeStamp = "";
	private static ArrayList<String> mImagePathList; // holds
														// the
														// paths
														// for
														// all
														// the
														// selfie
														// images..

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		

		// Set a emplty list view to show when the list is empty..
		/**
		 * NB: ALWAYS Load empty list view before initializing the adapter.
		 * Otherwise, the adapter will not update the list..
		 */
		this.setEmptyListView();

		// welcome toast :)
		if (onCreateState) {
			this.welcomeToast();
			onCreateState = false;
		}

		// set the actionbar color
		getActionBar().setBackgroundDrawable(
				new ColorDrawable(Color.parseColor("#ADA458")));

		// get the list adapter
		mImageAdapter = new ImageListViewAdapter(getApplicationContext());

		// Attach the adapter to this ListActivity's ListView
		getListView().setAdapter(mImageAdapter);

		mImagePathList = new ArrayList<String>();

		// set list item click listener
		this.setListItemClickListener();

		// set initial camera setup.
		this.initializeCameraFeature();

		// TODO: Implement for session handling
		if (mImageAdapter.getCount() == 0)
			loadImages();
	}

	/**
	 * 
	 */
	private void welcomeToast() {
		LayoutInflater inflater = getLayoutInflater();
		View layout = inflater.inflate(R.layout.toast_layout,
				(ViewGroup) findViewById(R.id.toast_layout_root));

		TextView text = (TextView) layout.findViewById(R.id.text);
		text.setText("Click Camera Icon to take picture");

		Toast toast = new Toast(getApplicationContext());
		toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
		toast.setDuration(Toast.LENGTH_LONG);
		toast.setView(layout);
		toast.show();
	}

	/**
	 * Persistence control.. starts here. ---------------------------------
	 */
	@Override
	public void onResume() {
		super.onResume();

		// Load saved images, if necessary
		// mImagePathList = new ArrayList<String>();

		if (mImageAdapter.getCount() == 0)
			loadImages();
	}

	@Override
	protected void onPause() {
		super.onPause();
		// FileOperations myFile = new FileOperations();
		// myFile.write(FILE_NAME, mAdapter);
		saveImages();
	}

	/*
	 * Save Images to file
	 */

	public void saveImages() {

		try {
			String fpath = "/sdcard/" + FILE_NAME;
			File file = new File(fpath);

			// If file exists, then erase it
			if (file.exists()) {
				file.delete();
			}

			// create new file and store..
			file.createNewFile();

			FileWriter fw = new FileWriter(file.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);

			for (int idx = 0; idx < mImageAdapter.getCount(); idx++) {
				ImageDescription mImgDes = (ImageDescription) mImageAdapter
						.getItem(idx);
				// Toast.makeText(ToDoManagerActivity.this, item.getTitle() +
				// "-" + item.getAlarm(), Toast.LENGTH_LONG).show();
				// System.out.println("alarm status: " + item.getAlarm());

				bw.append(mImgDes.getDescription());
				bw.newLine();
				bw.append(mImgDes.getImagePath());
				bw.newLine();
				bw.append(mImgDes.getDateTime());
				bw.newLine();

			}

			// bw.write(fcontent);
			bw.close();
			Log.d("Suceess", "Sucess");
			// return true;
		} catch (IOException e) {
			e.printStackTrace();
			// return false;
		}
	}

	/*
	 * Load and restore images to the list.
	 */

	public void loadImages() {
		BufferedReader br = null;
		String response = null;

		String description = null;
		String datetime = null;
		String imageFilePath = null;

		try {
			StringBuffer output = new StringBuffer();
			String fpath = "/sdcard/" + FILE_NAME;
			br = new BufferedReader(new FileReader(fpath));
			String line = "";
			while ((line = br.readLine()) != null) {
				// output.append(line + "\n");
				description = line;
				System.out.println("Desc: " + description);

				imageFilePath = br.readLine();
				System.out.println("FilePath: " + imageFilePath);

				datetime = br.readLine();
				System.out.println("Timestamp: " + datetime);

				// add the image to the list
				mImageAdapter.add(new ImageDescription(description,
						imageFilePath, datetime));

				// add the image path to the path list
				mImagePathList.add(imageFilePath);
			}
			response = output.toString();
		} catch (IOException e) {
			e.printStackTrace();
			// return null;
		}
		// return response;
	}

	

	// Persistence control ends here
	// ---------------------------------

	/**
	 * List item selection listener.
	 */
	private void setListItemClickListener() {
		getListView().setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				ImageDescription selected = (ImageDescription) mImageAdapter
						.getItem(position);
				// Toast.makeText(CameraActivity.this,
				// "You selected: " + selected.getImagePath(),
				// Toast.LENGTH_SHORT).show();

				Intent i = new Intent(CameraActivity.this,
						ImageViewerActivity.class);

				i.putStringArrayListExtra("images", mImagePathList);
				i.putExtra("position", position);
				startActivity(i);
			}
		});
	}

	/**
	 * empty list view to show the help menu
	 */
	private void setEmptyListView() {
		View emptyView = getLayoutInflater().inflate(
				R.layout.help_empty_layout, null);
		addContentView(emptyView, getListView().getLayoutParams());
		getListView().setEmptyView(emptyView);
	}

	/**
	 * Checks for a camera app availability in the device and alerts user
	 * accordingly.
	 */
	private void initializeCameraFeature() {

		// check if a camera
		PackageManager pm = getPackageManager();

		// if the camera is not installed then notify the user
		if (!pm.hasSystemFeature(PackageManager.FEATURE_CAMERA)) {

			// ask user to install a camera application
			Toast.makeText(CameraActivity.this,
					"No Camera Device found! Insatall one to proceed..",
					Toast.LENGTH_LONG).show();

			// disable the camera menu item.
			isNoCameraIstalled = true;

		}

		// the camera is installed and the app could function properly.
		else {

			// send ok message
			// Toast.makeText(CameraActivity.this,
			// "Camera App found! Happy Selfie me :)", Toast.LENGTH_LONG)
			// .show();

			// if camera menu is disabled then enable it.
			if (isNoCameraIstalled) {
				isNoCameraIstalled = false;
			}
		}
	}

	/**
	 * inflate the actionbar menu items
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.camera, menu);
		return true;
	}

	/**
	 * Handle action bar item clicks here.
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		int id = item.getItemId();

		switch (id) {
		case R.id.take_pic: { // if camera menu option is pressed..

			// open the camera app.. take pic.. and process..
			dispatchTakePictureIntent();

			// set the alarm for next selfie.
			mSetAlarm = new SetAlarm(getApplication());
			mSetAlarm.calculateAlarmTime();
			
		
			
			break;
		}
		
		case R.id.help: {
			Intent intent = new Intent(CameraActivity.this,HeloActivity.class);
			startActivity(intent);
			
			break;
		}
		case R.id.alarm_disable: {
			if(mSetAlarm != null)
				mSetAlarm.cancelAlarm();
			else
				Toast.makeText(CameraActivity.this, "No Alarm object found!",
						Toast.LENGTH_SHORT).show();
			break;
		}

		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * ---------------------------------------------------- Taking and
	 * processing the pictures starts here..
	 * ----------------------------------------------------
	 */

	/**
	 * calls the camera app to open and take picture..
	 */

	private void dispatchTakePictureIntent() {

		Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

		// check if the camera app is able to resolve the request
		// if yes start an activity.
		if (takePictureIntent.resolveActivity(getPackageManager()) != null) {

			// Create the File where the photo should go
			File photoFile = null;

			try {
				photoFile = createImageFile();
			} catch (IOException ex) {
				// Error occurred while creating the File
				Toast.makeText(CameraActivity.this,
						"Cannot create Image storgae file!!",
						Toast.LENGTH_SHORT).show();
			}
			// Continue only if the File was successfully created
			if (photoFile != null) {
				takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
						Uri.fromFile(photoFile));
				startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
			}
		} else {
			Toast.makeText(
					CameraActivity.this,
					"Something wrong with the default camera app! Reinstall it and return!!",
					Toast.LENGTH_SHORT).show();
		}
	}

	/**
	 * Create a image directory in the external storage to store image
	 */
	private File createImageFile() throws IOException {

		// Create an image file name
		timeStamp = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss")
				.format(new Date());

		String imageFileName = "SELFIE_" + timeStamp + "_";

		File storageDir = Environment
				.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);

		File image = File.createTempFile(imageFileName, /* prefix */
				".jpg", /* suffix */
				storageDir /* directory */
		);

		// Save a file: path for use with ACTION_VIEW intents
		mCurrentPhotoPath = image.getAbsolutePath();

		// test print
		// System.out.println(mCurrentPhotoPath);
		// Toast.makeText(CameraActivity.this, mCurrentPhotoPath,
		// Toast.LENGTH_LONG).show();

		return image;
	}

	/**
	 * Display the image in a image viewer..
	 */

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		// if the image taken by the camera successful
		if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
			// Toast.makeText(CameraActivity.this, "!! Activity Result",
			// Toast.LENGTH_SHORT).show();

			// create an Image description object
			mImageDescription = new ImageDescription();

			mImageDescription.setDescription("My Selfie");
			mImageDescription.setDateTime(timeStamp);
			mImageDescription.setImagePath(mCurrentPhotoPath);
			mImageAdapter.add(mImageDescription);

			// add the image path to the image list to update
			mImagePathList.add(mCurrentPhotoPath);

			// save to the file.. its required for orientation change control
			saveImages();

			/*
			 * Bundle extras = data.getExtras(); Bitmap imageBitmap = (Bitmap)
			 * extras.get("data"); mImageView.setImageBitmap(imageBitmap);
			 */
		}
	}

} // end activity

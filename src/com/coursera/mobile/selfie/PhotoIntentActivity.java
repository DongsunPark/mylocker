package com.coursera.mobile.selfie;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import android.app.AlarmManager;
import android.app.ListActivity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.coursera.mobile.selfie.storage.AlbumStorageDirFactory;
import com.coursera.mobile.selfie.storage.BaseAlbumDirFactory;
import com.coursera.mobile.selfie.storage.FroyoAlbumDirFactory;
import com.geeks.mylocker.R;


public class PhotoIntentActivity extends ListActivity {

	private static final int ACTION_TAKE_PHOTO_B = 1;

	private String mCurrentPhotoPath;
	private String mImageFileName;

	private static final String JPEG_FILE_PREFIX = "IMG_";
	private static final String JPEG_FILE_SUFFIX = ".jpg";

	private AlbumStorageDirFactory mAlbumStorageDirFactory = null;
	
	private PhotoViewAdapter mAdapter;
	
	DBHelper photoDB;
	
	private AlarmManager mAlarmManager;
	private Intent mNotificationReceiverIntent;
	private PendingIntent mNotificationReceiverPendingIntent;
	private static final long INITIAL_ALARM_DELAY = 2 * 60 * 1000L;
	protected static final long JITTER = 5000L;
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);

	      
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO) {
			mAlbumStorageDirFactory = new FroyoAlbumDirFactory();
		} else {
			mAlbumStorageDirFactory = new BaseAlbumDirFactory();
		}
		
		mAdapter = new PhotoViewAdapter(getApplicationContext());
		setListAdapter(mAdapter);
		
		photoDB= new DBHelper(this);
		try {
			List<Photo> photos = photoDB.getAllPhotos();
			if(photos !=null && !photos.isEmpty()) {
				loadPhotos(photos);
			}
			Log.v("TEST", "");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		// Get the AlarmManager Service
		mAlarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

		// Create an Intent to broadcast to the AlarmNotificationReceiver
		mNotificationReceiverIntent = new Intent(PhotoIntentActivity.this, AlarmNotificationReceiver.class);

		// Create an PendingIntent that holds the NotificationReceiverIntent
		mNotificationReceiverPendingIntent = PendingIntent.getBroadcast(PhotoIntentActivity.this, 0, mNotificationReceiverIntent, 0);
		
		mAlarmManager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME,
				SystemClock.elapsedRealtime() + 60 * 1000L,
				60 * 1000L,
				mNotificationReceiverPendingIntent);

		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		
		MenuInflater inflater = this.getMenuInflater();
		inflater.inflate(R.menu.main_activity_actions, menu);
		return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		
		if(item.getItemId() == R.id.action_camera) {
			if (isIntentAvailable(this, MediaStore.ACTION_IMAGE_CAPTURE)) {
				dispatchTakePictureIntent(ACTION_TAKE_PHOTO_B);
			} else {
				Toast.makeText(this, R.string.cannot, Toast.LENGTH_LONG).show();;
			}
			
			Log.d("TEST","camera selected");
		}
		return super.onOptionsItemSelected(item);
	}

	/* Photo album for this application */
	private String getAlbumName() {
		return getString(R.string.album_name);
	}
	
	private File getAlbumDir() {
		File storageDir = null;

		if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
			
			storageDir = mAlbumStorageDirFactory.getAlbumStorageDir(getAlbumName());

			if (storageDir != null) {
				if (! storageDir.mkdirs()) {
					if (! storageDir.exists()){
						Log.d("CameraSample", "failed to create directory");
						return null;
					}
				}
			}
			
		} else {
			Log.v(getString(R.string.app_name), "External storage is not mounted READ/WRITE.");
		}
		return storageDir;
	}

	private File createImageFile() throws IOException {
		// Create an image file name
		String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
		mImageFileName = JPEG_FILE_PREFIX + timeStamp + "_";
		File albumF = getAlbumDir();
		File imageF = File.createTempFile(mImageFileName, JPEG_FILE_SUFFIX, albumF);
		return imageF;
	}

	private File setUpPhotoFile() throws IOException {
		
		File f = createImageFile();
		mCurrentPhotoPath = f.getAbsolutePath();
		
		return f;
	}

	public static Bitmap setPic(int targetW, int targetH, String photoPath) {

		/* There isn't enough memory to open up more than a couple camera photos */
		/* So pre-scale the target bitmap into which the file is decoded */

		/* Get the size of the ImageView */
		
		/* Get the size of the image */
		BitmapFactory.Options bmOptions = new BitmapFactory.Options();
		bmOptions.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(photoPath, bmOptions);
		int photoW = bmOptions.outWidth;
		int photoH = bmOptions.outHeight;
		
		/* Figure out which way needs to be reduced less */
		int scaleFactor = 1;
		if ((targetW > 0) || (targetH > 0)) {
			scaleFactor = Math.min(photoW/targetW, photoH/targetH);	
		}

		/* Set bitmap options to scale the image decode target */
		bmOptions.inJustDecodeBounds = false;
		bmOptions.inSampleSize = scaleFactor;
		bmOptions.inPurgeable = true;

		/* Decode the JPEG file into a Bitmap */
		Bitmap bitmap = BitmapFactory.decodeFile(photoPath, bmOptions);
			
		return bitmap;
	}

	private void galleryAddPic() {
		    Intent mediaScanIntent = new Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE");
			File f = new File(mCurrentPhotoPath);
		    Uri contentUri = Uri.fromFile(f);
		    mediaScanIntent.setData(contentUri);
		    this.sendBroadcast(mediaScanIntent);
	}

	private void dispatchTakePictureIntent(int actionCode) {

		Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

		switch(actionCode) {
			case ACTION_TAKE_PHOTO_B:
				File f = null;
				
				try {
					f = setUpPhotoFile();
					mCurrentPhotoPath = f.getAbsolutePath();
					takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
				} catch (IOException e) {
					e.printStackTrace();
					f = null;
					mCurrentPhotoPath = null;
				}
				break;
	
			default:
				break;			
		} // switch

		startActivityForResult(takePictureIntent, actionCode);
	}

	private void handleCameraPhoto() {

		if (mCurrentPhotoPath != null) {
			
			LayoutInflater inflater  = LayoutInflater.from(this);
			View newView = inflater.inflate(R.layout.photo_view, null, false);
			ImageView v = (ImageView) newView.findViewById(R.id.photo);
			int targetW = v.getWidth();
			int targetH = v.getHeight();
			
			Bitmap bitmap = setPic(targetW, targetH, this.mCurrentPhotoPath);
			
			//new codes
			Photo photo = new Photo();
			photo.setName(mImageFileName);
			photo.setPath(mCurrentPhotoPath);
			photo.setCreatedDate(new Date());
			photo.setBitmap(bitmap);
			
			mAdapter.add(photo);
			
			photoDB.insertContact(photo);
			galleryAddPic();
			mCurrentPhotoPath = null;
		}
	}
	
	private void loadPhotos(List<Photo> photos) {
		
		LayoutInflater inflater  = LayoutInflater.from(this);
		View newView = inflater.inflate(R.layout.photo_view, null, false);
		ImageView v = (ImageView) newView.findViewById(R.id.photo);
		int targetW = v.getWidth();
		int targetH = v.getHeight();
		
		Bitmap bitmap;
		for(Photo photo : photos) {
		
			bitmap = setPic(targetW, targetH, photo.getPath());
			photo.setBitmap(bitmap);
			mAdapter.add(photo);
		}
	}
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		
		Intent fullViewIntent = new Intent(this, FullPhotoViewActivity.class);

		Photo photo = (Photo)mAdapter.getItem(position);
		
		//Create the bundle
		Bundle bundle = new Bundle();
		//Add your data to bundle
		bundle.putString("photoPath", photo.getPath());
		//Add the bundle to the intent
		fullViewIntent.putExtras(bundle);

        startActivity(fullViewIntent,bundle);
	    //Toast.makeText(this, "Clicked row " + position, Toast.LENGTH_SHORT).show();
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
			case ACTION_TAKE_PHOTO_B: {
				if (resultCode == RESULT_OK) {
					handleCameraPhoto();
				}
				break;
			} 
		} // switch
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		/*outState.putParcelable(BITMAP_STORAGE_KEY, mImageBitmap);
		outState.putBoolean(IMAGEVIEW_VISIBILITY_STORAGE_KEY, (mImageBitmap != null) );*/
		super.onSaveInstanceState(outState);
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
	}

	/**
	 * Indicates whether the specified action can be used as an intent. This
	 * method queries the package manager for installed packages that can
	 * respond to an intent with the specified action. If no suitable package is
	 * found, this method returns false.
	 * http://android-developers.blogspot.com/2009/01/can-i-use-this-intent.html
	 *
	 * @param context The application's environment.
	 * @param action The Intent action to check for availability.
	 *
	 * @return True if an Intent with the specified action can be sent and
	 *         responded to, false otherwise.
	 */
	public static boolean isIntentAvailable(Context context, String action) {
		final PackageManager packageManager = context.getPackageManager();
		final Intent intent = new Intent(action);
		List<ResolveInfo> list =
			packageManager.queryIntentActivities(intent,
					PackageManager.MATCH_DEFAULT_ONLY);
		return list.size() > 0;
	}
}
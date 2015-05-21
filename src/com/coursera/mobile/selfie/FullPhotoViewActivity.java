package com.coursera.mobile.selfie;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;

import com.geeks.mylocker.R;

public class FullPhotoViewActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setTitle(R.string.app_name);
		setContentView(R.layout.full_photo_view);
		
		Bundle bundle = getIntent().getExtras();
		String photoPath= bundle.getString("photoPath");

		Bitmap bitmap = BitmapFactory.decodeFile(photoPath);
		ImageView view = (ImageView)findViewById(R.id.fullPhoto);
		view.setImageBitmap(bitmap);
	}
}

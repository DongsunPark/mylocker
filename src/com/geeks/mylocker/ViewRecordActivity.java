package com.geeks.mylocker;

import com.geeks.mylocker.helper.MenuHelper;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class ViewRecordActivity extends Activity {

	protected final String TAG = getClass().getSimpleName();
	
	DataSource ds;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view_record);
		ds = new DataSource();
		ds.setup(this);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Bundle extras = null;
		return MenuHelper.onOptionsItemSelected(item, this, null);
	}
}

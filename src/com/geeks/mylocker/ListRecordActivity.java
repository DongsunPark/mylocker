package com.geeks.mylocker;

import java.util.List;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.HeaderViewListAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.geeks.mylocker.dao.Folder;
import com.geeks.mylocker.dao.Record;
import com.geeks.mylocker.helper.MenuHelper;

public class ListRecordActivity extends ListActivity {

	protected final String TAG = getClass().getSimpleName();
	
	public final static String EXTRA_MESSAGE = "com.geeks.mylocker.groulist.MESSAGE";
	
	DataSource ds;
	
	//Cursor cursor;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_group_list);
		
		ds = new DataSource();
		ds.setup(this);
		
		Folder folder = null;
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			Long id = extras.getLong(ListFolderActivity.FOLDER_ID_SELECTED);
			folder = ds.getFolderDao().load(id);
		}
		
		
		this.setListAdapter(this.createListAdapter(folder));
		
		addUIListeners();
	}
	
	private ListAdapter createListAdapter(Folder folder) {
		
		List<Record> records = folder.getRecords();
		/*RecordDao recordDao = ds.getRecordDao();
		cursor = ds.getDb().query(recordDao.getTablename(),recordDao.getAllColumns(),null,null,null,null,null);
		String[] from = {RecordDao.Properties.Name.columnName}; //column name
		int[] to = {android.R.id.text1};//location of field 
		
		SimpleCursorAdapter adapter = null;  
		
		adapter = new SimpleCursorAdapter(this, android.R.layout.simple_list_item_1, cursor, from, to);*/
		//return adapter;
		
		//HeaderViewListAdapter adapter = new HeaderViewListAdapter(headerViewInfos, footerViewInfos, adapter); 
		return null;
	}
	
	
	private void addUIListeners() {
		
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		Intent intent = new Intent(this,ViewRecordActivity.class);
		String message = "TEST";
		intent.putExtra(EXTRA_MESSAGE, message);
		startActivity(intent);
	}

	@Override
	protected void onResume() {
		super.onResume();
		Log.d(TAG,"Resumed");
		//cursor.requery();
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

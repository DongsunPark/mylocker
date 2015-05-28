package com.geeks.mylocker;

import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import com.geeks.mylocker.dao.Folder;
import com.geeks.mylocker.dao.FolderDao;
import com.geeks.mylocker.helper.MenuHelper;

public class ListFolderActivity extends ListActivity {

	protected final String TAG = getClass().getSimpleName();
	
	public final static String EXTRA_MESSAGE = "com.geeks.mylocker.groulist.MESSAGE";

	public final static String FOLDER_ID_SELECTED = "com.geeks.mylocker.folder.id.selected";
	
	DataSource ds;
	Cursor cursor;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_group_list);
		
		ds = new DataSource();
		ds.setup(this);
		
		this.setListAdapter(this.createListAdapter());
		
		addUIListeners();
	}
	
	private ListAdapter createListAdapter() {
		
		FolderDao folderDao = ds.getFolderDao();
		cursor = ds.getDb().query(folderDao.getTablename(),folderDao.getAllColumns(),null,null,null,null,null);
		String[] from = {FolderDao.Properties.Name.columnName}; //column name
		int[] to = {android.R.id.text1};//location of field 
		
		SimpleCursorAdapter adapter = null;  
		
		adapter = new SimpleCursorAdapter(this, android.R.layout.simple_list_item_1, cursor, from, to);
		return adapter;
	}
	
	
	private void addUIListeners() {
		
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		// TODO Auto-generated method stub
		/*folderDao.deleteByKey(id);
		Log.d(TAG,"Deleted folder, ID: " + id);
		cursor.requery();*/
		
		//Folder folder = ds.getFolderDao().load(id);
		
		
		Intent intent = new Intent(this, ListRecordActivity.class);
		Bundle extras = new Bundle();
		extras.putLong(FOLDER_ID_SELECTED, id);
		if(extras !=null) intent.putExtras(extras);
		this.startActivity(intent);
	}

	@Override
	protected void onResume() {
		super.onResume();
		Log.d(TAG,"Resumed");
		cursor.requery();
	}
	
	

	@Override
	protected void onPause() {
		super.onPause();
		//if(cursor !=null)  cursor.close();
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

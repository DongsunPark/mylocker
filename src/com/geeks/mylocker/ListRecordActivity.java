package com.geeks.mylocker;

import java.util.List;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.geeks.mylocker.adapter.RecordListAdapter;
import com.geeks.mylocker.async.DaoCommand;
import com.geeks.mylocker.async.DaoTask;
import com.geeks.mylocker.dao.Entity;
import com.geeks.mylocker.dao.Folder;
import com.geeks.mylocker.dao.Record;
import com.geeks.mylocker.helper.MenuHelper;

import de.greenrobot.dao.AbstractDao;

public class ListRecordActivity extends ListActivity {

	protected final String TAG = getClass().getSimpleName();
	
	public final static String EXTRA_MESSAGE = "com.geeks.mylocker.groulist.MESSAGE";
	public final static String SELECTED_ENTITY = "com.geeks.mylocker.entity";
	
	
	DataSource ds;
	
	Folder folder;
	//Cursor cursor;
	
	ListAdapter adapter;
	
	Activity self;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_group_list);
		
		/*ds = new DataSource();
		ds.setup(this);
		
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			Long id = extras.getLong(ListFolderActivity.FOLDER_ID_SELECTED);
			folder = ds.getFolderDao().load(id);
			this.setFolder(folder);
		}
		
		adapter = this.createListAdapter(folder);
		this.setListAdapter(adapter);*/
		self = this;
		
		addUIListeners();
	}
	
	private ListAdapter createListAdapter(Folder folder) {
		
		List<Record> records = folder.getRecords();
		
		return new RecordListAdapter(this, records);
	}
	
	
	private void addUIListeners() {
		
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void onListItemClick(ListView l, View v, int position, final long id) {
		/*Intent intent = new Intent(this,ViewRecordActivity.class);
		String message = "TEST";
		intent.putExtra(EXTRA_MESSAGE, message);
		startActivity(intent);*/
		
		Record record = new Record(id);
        AbstractDao<Record, Long> dao = ds.getDaoSession().getRecordDao(); 
		DaoCommand<Record> commandRecord = new DaoCommand<Record>(dao, record, DaoCommand.CRUD.SELECT);
			
		new DaoTask<Record>() {
				@Override
				protected Record executeDao(DaoCommand<Record> daoCommand) {
					Record entity = daoCommand.getEntity();
					if(daoCommand.getCrud() == DaoCommand.CRUD.SELECT) {
						return daoCommand.getDao().load(id+1);
					}
					return null;
				}
				@Override
				protected void updateUi(Entity result) {
					Record record = (Record)result;
					Toast.makeText(self, record.getName() + " selected", Toast.LENGTH_LONG).show();
					
					Intent intent = new Intent(self, ViewRecordActivity.class);
					Bundle extras = new Bundle();
					extras.putSerializable(SELECTED_ENTITY, result);;
					if(extras !=null) intent.putExtras(extras);
					self.startActivity(intent);
				}
			}.execute(commandRecord);
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		Log.d(TAG,"started");
		ds = new DataSource();
		ds.setup(this);
		
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			Long id = extras.getLong(ListFolderActivity.FOLDER_ID_SELECTED);
			folder = ds.getFolderDao().load(id);
			this.setFolder(folder);
		}
		
		adapter = this.createListAdapter(folder);
		this.setListAdapter(adapter);
		
	}

	@Override
	protected void onResume() {
		super.onResume();
		Log.d(TAG,"Resumed");
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Bundle extras = new Bundle();
		extras.putLong(ListFolderActivity.FOLDER_ID_SELECTED, folder.getId());
		return MenuHelper.onOptionsItemSelected(item, this, extras);
	}

	public Folder getFolder() {
		return folder;
	}

	public void setFolder(Folder folder) {
		this.folder = folder;
	}
	
}

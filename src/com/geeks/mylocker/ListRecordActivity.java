package com.geeks.mylocker;

import java.util.List;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.geeks.mylocker.adapter.RecordListAdapter;
import com.geeks.mylocker.async.DaoCommand;
import com.geeks.mylocker.async.DaoTask;
import com.geeks.mylocker.dao.Entity;
import com.geeks.mylocker.dao.Folder;
import com.geeks.mylocker.dao.Record;
import com.geeks.mylocker.helper.ActivityHelper;
import com.geeks.mylocker.helper.BaseActivityHelper;
import com.geeks.mylocker.helper.MenuHelper;

import de.greenrobot.dao.AbstractDao;

public class ListRecordActivity extends ListActivity implements BaseActivityHelper{

	protected final String TAG = getClass().getSimpleName();
	
	public final static String EXTRA_MESSAGE = "com.geeks.mylocker.groulist.MESSAGE";
	public final static String SELECTED_ENTITY = "com.geeks.mylocker.entity";
	
	private Config config;
	
	private DataSource ds;
	
	private Folder folder;
	//Cursor cursor;
	
	private ListAdapter adapter;
	
	private Activity self;
	
	private ListView listView;
	
	private ActivityHelper activityHelper;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			config = (Config) extras.getSerializable(Config.CONTEXT);
		}	
		
		activityHelper = new ActivityHelper(this);
		activityHelper.registerBaseActivityReceiver();
		
		//setContentView(R.layout.activity_group_list);
		
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
		
		listView = this.getListView();
		registerForContextMenu(listView);
		addUIListeners();
	}
	
	private ListAdapter createListAdapter(Folder folder) {
		
		return new RecordListAdapter(this, getRecords());
	}
	
	private List<Record> getRecords() {
		return folder.getRecords();
	}
	
	
	@SuppressWarnings("unchecked")
	private void addUIListeners() {
		listView.setOnItemLongClickListener(new OnItemLongClickListener() {

            public boolean onItemLongClick(AdapterView<?> arg0, View v, int index, long arg3) {

            		//Toast.makeText(self, "test", Toast.LENGTH_LONG).show();
            		
            		return false;
            }
		});
		
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, final long id) {
				
				RecordListAdapter adapter = (RecordListAdapter)parent.getAdapter();
				Record record = adapter.getItem(position);
				
				//Record record = new Record(id);
				AbstractDao<Record, Long> dao = null;
				DaoCommand<Record> commandRecord = new DaoCommand<Record>(dao, record, DaoCommand.CRUD.SELECT);
					
				new DaoTask<Record>() {
						@Override
						protected Record executeDao(DaoCommand<Record> daoCommand) {
							ds = new DataSource();
							ds.setup(self);
							AbstractDao<Record, Long> dao = ds.getDaoSession().getRecordDao(); 
							Record entity = daoCommand.getEntity();
							if(daoCommand.getCrud() == DaoCommand.CRUD.SELECT) {
								Record record = dao.load(entity.getId());
								ds.close();
								
								return record;
							}
							return null;
						}
						@Override
						protected void updateUi(Entity result) {
							Record record = (Record)result;
							Toast.makeText(self, record.getName() + " selected", Toast.LENGTH_LONG).show();
							
							Intent intent = new Intent(self, ViewRecordActivity.class);
							intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
							Bundle extras = new Bundle();
							extras.putSerializable(Config.CONTEXT, config);
							extras.putSerializable(SELECTED_ENTITY, result);
							if(extras !=null) intent.putExtras(extras);
							self.startActivity(intent);
						}
					}.execute(commandRecord);
				
			}
			
		});
		
		
	}

	/*@SuppressWarnings("unchecked")
	@Override
	protected void onListItemClick(ListView l, View v, int position, final long id) {
		Intent intent = new Intent(this,ViewRecordActivity.class);
		String message = "TEST";
		intent.putExtra(EXTRA_MESSAGE, message);
		startActivity(intent);
		
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
	}*/

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
		
		if(folder !=null) this.setTitle(folder.getName());
		
		adapter = this.createListAdapter(folder);
		this.setListAdapter(adapter);
		ds.close();
		
	}

	@Override
	protected void onResume() {
		super.onResume();
		Log.d(TAG,"Resumed");
	}
	
	@Override
    protected void onDestroy() {
    	super.onDestroy();
    	activityHelper.unRegisterBaseActivityReceiver();
    }
	
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,  ContextMenuInfo menuInfo) {
	  if ( v == listView) {
	    AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)menuInfo;
	    menu.setHeaderTitle("Menu");
	    String[] menuItems = getResources().getStringArray(R.array.record_view_menu);
	    for (int i = 0; i<menuItems.length; i++) {
	      menu.add(Menu.NONE, i, i, menuItems[i]);
	    }
	  }
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
		
		int position = info.position;
		long id = info.id;
		
		Record record = this.getRecords().get((int)id);
		
		int menuItemIndex = item.getItemId();
		String[] menuItems = getResources().getStringArray(R.array.record_view_menu);
		String menuItemName = menuItems[menuItemIndex];
		
		ds = new DataSource();
		ds.setup(this);
		if("delete".equalsIgnoreCase(menuItemName)) {
			Toast.makeText(self, "deleting " + record.getName(), Toast.LENGTH_SHORT).show();
			
			//view and model
			RecordListAdapter listAdapter = (RecordListAdapter)adapter;
			listAdapter.remove(record);
			listAdapter.notifyDataSetChanged();
			
			//db
			ds.getRecordDao().delete(record);
		} else if("view".equalsIgnoreCase(menuItemName)) {
			View view = listView.getSelectedView();
			listView.getOnItemClickListener().onItemClick(listView, view, position, id);
		}
		
		ds.close();
		return true;
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
		extras.putSerializable(Config.CONTEXT, config);
		return MenuHelper.onOptionsItemSelected(item, this, extras);
	}

	public Folder getFolder() {
		return folder;
	}

	public void setFolder(Folder folder) {
		this.folder = folder;
	}

	@Override
	public ActivityHelper getActivityHelper() {
		
		return this.activityHelper;
	}
	
	@Override
	public Config getConfig() {
		return this.config;
	}

	@Override
	public void setConfig(Config config) {
		this.config = config;
	}
}

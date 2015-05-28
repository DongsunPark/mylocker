package com.geeks.mylocker;

import java.text.DateFormat;
import java.util.Date;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.geeks.mylocker.dao.DaoSession;
import com.geeks.mylocker.dao.Field;
import com.geeks.mylocker.dao.Folder;
import com.geeks.mylocker.dao.Record;
import com.geeks.mylocker.helper.MenuHelper;

public class AddRecordActivity extends Activity {

	protected final String TAG = getClass().getSimpleName();
	
	private Folder folder = null;
	DataSource ds;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_record);

		ds = new DataSource();
		ds.setup(this);
		
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			Long id = extras.getLong(ListFolderActivity.FOLDER_ID_SELECTED);
			folder = ds.getFolderDao().load(id);
			if(folder !=null) {
				EditText uiFolderName = (EditText)this.findViewById(R.id.ui_add_record_folder_name);
				uiFolderName.setText(folder.getName());
			}
		}
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
	
	public void onSaveButtonClick(View view) {
		
		EditText uiFolderName = (EditText)findViewById(R.id.ui_add_record_folder_name);
		EditText uiRecordName = (EditText)findViewById(R.id.ui_add_record_record_name);
		EditText uiUserId = (EditText)findViewById(R.id.ui_add_record_user_id);
		EditText uiPassword = (EditText)findViewById(R.id.ui_add_record_user_password);
		
		String folderName = uiFolderName.getText().toString();
		if(folder == null || !folder.getName().equalsIgnoreCase(folderName)) {
			folder = new Folder(null, folderName, new Date());
			ds.getDaoSession().getFolderDao().insert(folder);
			Log.d(TAG, "Inserted new folder, ID: " + folder.getId());
		}
		
		String recordName = uiRecordName.getText().toString();
		String userId = uiUserId.getText().toString();
		String password = uiPassword.getText().toString();
		
		Record record = new Record(null, recordName, userId, password, new Date(),folder.getId());
		if(ds.getDaoSession().getRecordDao().insert(record) > 0L) {
			Log.d(TAG, "Inserted new record, ID: " + record.getId());
			//folder.getRecords().add(record);
		}
		
	}

	private void addNote() {

		/*DaoSession sesson = ds.getDaoMaster().newSession();
		//ds.getDb().beginTransaction();
		final DateFormat df = DateFormat.getDateTimeInstance(DateFormat.MEDIUM,
				DateFormat.MEDIUM);*/
		
		/*String comment = "Added on " + df.format(new Date());
		daoSession.getFolderDao().createTable(db, true);*/
		
		/*folder = new Folder(null, "group1", new Date());
		sesson.getFolderDao().insert(folder);
		Log.d(TAG, "Inserted new folder, ID: " + folder.getId());*/
		

		/*Record record = new Record(null, "record1", new Date(),14L);
		sesson.getRecordDao().insert(record);
		Log.d(TAG, "Inserted new record, ID: " + record.getId());
		
		Field field = new Field(null,"test", "value", 1, record.getId());
		sesson.getFieldDao().insert(field);
		Log.d(TAG, "Inserted new field, ID: " + field.getId());
		//cursor.requery();*/
		//ds.getDb().releaseReference();
		//ds.getDb().endTransaction();*/
	}

	
}

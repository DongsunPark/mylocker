package com.geeks.mylocker;

import java.util.Date;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.geeks.mylocker.async.CryptoTask;
import com.geeks.mylocker.dao.Folder;
import com.geeks.mylocker.dao.Record;
import com.geeks.mylocker.encrypto.Encryptor;
import com.geeks.mylocker.helper.MenuHelper;

public class AddRecordActivity extends Activity {
	
	public static String masterKey = "masterkey";

	protected final String TAG = getClass().getSimpleName();
	
	Encryptor encryptor;
	
	private Folder folder = null;
	DataSource ds;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_record);

		encryptor = Encryptor.select(Encryptor.PADDING_ENC_IDX);
		
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
		
		 new CryptoTask() {

             @Override
             protected String doCrypto() {
                 return encryptor.encrypt("TEST", "masterKey");
             }
             
			protected void updateUi(String ciphertext) {
				
             }
         }.execute();
		
		
		Record record = new Record(null, recordName, userId, password, new Date(),folder.getId());
		if(ds.getDaoSession().getRecordDao().insert(record) > 0L) {
			Log.d(TAG, "Inserted new record, ID: " + record.getId());
			//folder.getRecords().add(record);
		}
		
	}
}

package com.geeks.mylocker;

import java.util.Date;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.geeks.mylocker.async.CryptoTask;
import com.geeks.mylocker.async.DaoCommand;
import com.geeks.mylocker.async.DaoTask;
import com.geeks.mylocker.dao.Entity;
import com.geeks.mylocker.dao.Folder;
import com.geeks.mylocker.dao.Record;
import com.geeks.mylocker.encrypto.Encryptor;
import com.geeks.mylocker.helper.MenuHelper;

import de.greenrobot.dao.AbstractDao;

public class AddRecordActivity extends AppBaseActivity {
	
	protected final String TAG = getClass().getSimpleName();
	
	Encryptor encryptor;
	
	private Folder folder = null;
	DataSource ds;
	
	String recordName;
	String userId;
	String password;
	
	Activity self;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_record);

		encryptor = Encryptor.select(Encryptor.PADDING_ENC_IDX);
		
		ds = new DataSource();
		ds.setup(this);
		
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			Config config = (Config) extras.getSerializable(Config.CONTEXT);
			setConfig(config);
			Long id = extras.getLong(ListFolderActivity.FOLDER_ID_SELECTED);
			folder = ds.getFolderDao().load(id);
			if(folder !=null) {
				EditText uiFolderName = (EditText)this.findViewById(R.id.ui_add_record_folder_name);
				uiFolderName.setText(folder.getName());
			}
		}
		
		self = this;
		
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
		extras.putSerializable(Config.CONTEXT, this.getConfig());
		return MenuHelper.onOptionsItemSelected(item, this, extras);
	}
	
	@SuppressWarnings("unchecked")
	public void onSaveButtonClick(View view) {

		EditText uiFolderName = (EditText)findViewById(R.id.ui_add_record_folder_name);
		EditText uiRecordName = (EditText)findViewById(R.id.ui_add_record_record_name);
		EditText uiUserId = (EditText)findViewById(R.id.ui_add_record_user_id);
		EditText uiPassword = (EditText)findViewById(R.id.ui_add_record_user_password);
		
		String folderName = uiFolderName.getText().toString();
		if(folder == null || !folder.getName().equalsIgnoreCase(folderName)) {
			folder = new Folder(null, folderName, new Date());
			/*ds.getDaoSession().getFolderDao().insert(folder);
			Log.d(TAG, "Inserted new folder, ID: " + folder.getId());*/
			
			AbstractDao<Folder, Long> dao = ds.getDaoSession().getFolderDao(); 
			DaoCommand<Folder> command = new DaoCommand<Folder>(dao, folder, DaoCommand.CRUD.INSERT);
			
			folder =(Folder) new DaoTask<Folder>() {

				@Override
				protected Folder executeDao(DaoCommand<Folder> daoCommand) {
					Folder folder = (Folder) daoCommand.getEntity();
					if(daoCommand.getCrud() == DaoCommand.CRUD.INSERT) {
						daoCommand.getDao().insert(folder);
						Log.d(TAG, "Inserted new folder, ID: " + folder.getId());
						return folder;
					}
					return null;
				}
				@Override
				protected void updateUi(Entity result) {}
			}.executeDao(command);//Synchronous because of using executeDao instead of execute
		}
		
		recordName = uiRecordName.getText().toString();
		userId = uiUserId.getText().toString();
		password = uiPassword.getText().toString();
		
		String ciphertext = new CryptoTask() {
             @Override
             protected String doCrypto() {
                 return encryptor.encrypt(password, ((AddRecordActivity)self).getConfig().getMasterKey());
             }
			protected void updateUi(String ciphertext) {
				
             }
         }.doCrypto();  //uses instead of execute() method...its synchronous 
         
         
        Record record = new Record(null, recordName, userId, ciphertext, new Date(),folder.getId());
        AbstractDao<Record, Long> dao = ds.getDaoSession().getRecordDao(); 
		DaoCommand<Record> commandRecord = new DaoCommand<Record>(dao, record, DaoCommand.CRUD.INSERT);
			
		new DaoTask<Record>() {
				@Override
				protected Record executeDao(DaoCommand<Record> daoCommand) {
					Record entity = daoCommand.getEntity();
					if(daoCommand.getCrud() == DaoCommand.CRUD.INSERT) {
						if(daoCommand.getDao().insert(entity) > 0L) {
							Log.d(TAG, "Inserted new recod, ID: " + entity.getId());
							return entity;
						}
					}
					return null;
				}
				@Override
				protected void updateUi(Entity result) {
					Record record = (Record)result;
					Toast.makeText(AddRecordActivity.this, record.getName() + " added", Toast.LENGTH_LONG).show();
					
					Intent intent = new Intent(self, ViewRecordActivity.class);
					Bundle extras = new Bundle();
					extras.putSerializable(Config.CONTEXT, ((AddRecordActivity)self).getConfig());
					extras.putSerializable(ListRecordActivity.SELECTED_ENTITY, result);;
					if(extras !=null) intent.putExtras(extras);
					self.startActivity(intent);
				}
			}.execute(commandRecord);
	}
	
	public void addFieldsView(View view) {
		
		final LinearLayout layout = (LinearLayout)findViewById(R.layout.activity_add_record);
		
		//LinearLayout child = new LinearLayout(self);
		
		/*final RadioButton button2 = new RadioButton(self);
		button2.setChecked(true);
		child.addView(button2);*/
		
		final RadioGroup group = new RadioGroup(self);
		final RadioButton button1 = new RadioButton(self);
		button1.setId(1234); // this id can be generated as you like.
		group.addView(button1,
		    new RadioGroup.LayoutParams(
		        RadioGroup.LayoutParams.WRAP_CONTENT,    
		        RadioGroup.LayoutParams.WRAP_CONTENT));
		final RadioButton button2 = new RadioButton(self);
		button2.setId(3456); // this id can be generated as you like.
		button2.setChecked(true);
		group.addView(button2,
		    new RadioGroup.LayoutParams(
		        RadioGroup.LayoutParams.WRAP_CONTENT,    
		        RadioGroup.LayoutParams.WRAP_CONTENT));
		layout.addView(group,
		    new LinearLayout.LayoutParams(
		        LinearLayout.LayoutParams.MATCH_PARENT,    
		        LinearLayout.LayoutParams.WRAP_CONTENT));
		
		Toast.makeText(self,"test", Toast.LENGTH_LONG).show();
	}
}

package com.geeks.mylocker;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.Toast;

import com.geeks.mylocker.async.CryptoTask;
import com.geeks.mylocker.async.DaoCommand;
import com.geeks.mylocker.async.DaoTask;
import com.geeks.mylocker.dao.Entity;
import com.geeks.mylocker.dao.Field;
import com.geeks.mylocker.dao.Folder;
import com.geeks.mylocker.dao.Record;
import com.geeks.mylocker.encrypto.Encryptor;
import com.geeks.mylocker.helper.MenuHelper;

import de.greenrobot.dao.AbstractDao;

public class AddRecordActivity extends AppBaseActivity {
	
	private final int FIELD_NAME_ID_START = 30000;
	
	private final int FIELD_VALUE_ID_START = 40000;
	
	protected final String TAG = getClass().getSimpleName();
	
	Encryptor encryptor;
	
	private Folder folder = null;
	DataSource ds;
	
	String recordName;
	String userId;
	String password;
	
	Activity self;
	
	private int numDynamicField = 0;
	
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
         
        List<Field> fields = processDynamicFields();
        
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
					intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					Bundle extras = new Bundle();
					extras.putSerializable(Config.CONTEXT, ((AddRecordActivity)self).getConfig());
					extras.putSerializable(ListRecordActivity.SELECTED_ENTITY, result);;
					if(extras !=null) intent.putExtras(extras);
					self.startActivity(intent);
				}
			}.execute(commandRecord);
	}
	
	private List<Field> processDynamicFields() {

		List<Field> fields = new ArrayList<Field>();
		if(this.numDynamicField == 0) return fields;
		
		EditText nameEdit = null;
		EditText valueEdit = null;
		
		String name =  null;
		String value = null;
		
		for(int i=0; i < this.numDynamicField; i++) {
			nameEdit = (EditText)this.findViewById(FIELD_NAME_ID_START + i);
			valueEdit = (EditText)this.findViewById(FIELD_VALUE_ID_START + i);
			name = nameEdit.getText().toString();
			value = valueEdit.getText().toString();
			if(name !=null && !name.equals("")) {
				Field field = new Field();
				field.setName(name);
				field.setValue(value);
				field.setPosition(i);
			}
		}
		
		return fields;
	}

	public void addFieldsView(View view) {
		
		this.numDynamicField++;
		
		LinearLayout layout = (LinearLayout)view.getParent();
		
		final LinearLayout group = new LinearLayout(self);
		LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		params.setMargins(10, 10, 10, 10);
		group.setLayoutParams(params);
		group.setOrientation(LinearLayout.VERTICAL);
		//group.setBackgroundColor(0xff99ccff);

		
		final EditText nameView = new EditText(self);
		nameView.setId(FIELD_NAME_ID_START + this.numDynamicField); 
		nameView.setHint("Name");
		group.addView(nameView);

		final EditText ValueView = new EditText(self);
		ValueView.setId(FIELD_VALUE_ID_START + this.numDynamicField); 
		ValueView.setHint("Value");
		group.addView(ValueView);
		
		layout.addView(group, layout.getChildCount()-1);
	}
}

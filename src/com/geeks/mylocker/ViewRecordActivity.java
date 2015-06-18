package com.geeks.mylocker;

import android.app.Activity;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.geeks.mylocker.async.CryptoTask;
import com.geeks.mylocker.dao.Record;
import com.geeks.mylocker.encrypto.Encryptor;
import com.geeks.mylocker.helper.MenuHelper;

public class ViewRecordActivity extends Activity {

	protected final String TAG = getClass().getSimpleName();
	
	DataSource ds;
	
	private Record record;
	
	Activity self;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view_record);
		ds = new DataSource();
		ds.setup(this);
		
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			Record record = (Record) extras.getSerializable(ListRecordActivity.SELECTED_ENTITY);
			//reload detached entity.
			record = ds.getRecordDao().load(record.getId());
			this.setRecord(record);
		}
		self = this;
		updateUi();
	}

	private void updateUi() {
		TextView folderNameView = (TextView)this.findViewById(R.id.view_record_foler_name);
		folderNameView.setText(record.getFolder().getName());

		TextView recordNameView = (TextView)this.findViewById(R.id.view_record_name);
		recordNameView.setText(record.getName());

		TextView recordUserIdView = (TextView)this.findViewById(R.id.view_record_user_id);
		recordUserIdView.setText(record.getUserId());
		
		/*
		TextView recordUserPasswordView = (TextView)this.findViewById(R.id.view_record_user_password);
		recordUserPasswordView.setText(record.getUserPassword());*/
		
		decryptPassword();
	}
	
	public void onPasswordVisible(View view) {
		
		TextView recordUserPasswordView = (TextView)this.findViewById(R.id.view_record_user_password);;
		
		Button button = (Button)view;
		if(button.getText().equals("Hide")) {
			PasswordTransformationMethod transform = new PasswordTransformationMethod();
			recordUserPasswordView.setTransformationMethod(transform);
			button.setText("View");
		} else {
			recordUserPasswordView.setTransformationMethod(null);
			button.setText("Hide");
		}
		
		//Toast.makeText(ViewRecordActivity.this, "make password visible", Toast.LENGTH_LONG).show();;
	}
	
	private void decryptPassword() {

		new CryptoTask() {

			@Override
			protected String doCrypto() {
				Encryptor encryptor = Encryptor.select(Encryptor.PADDING_ENC_IDX);
				return encryptor.decrypt(record.getUserPassword(), "masterKey");
			}

			@Override
			protected void updateUi(String result) {
				TextView recordUserPasswordView = (TextView)self.findViewById(R.id.view_record_user_password);
				recordUserPasswordView.setText(result);
				
			}
        }.execute(); 
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

	public Record getRecord() {
		return record;
	}

	public void setRecord(Record record) {
		this.record = record;
	}
}

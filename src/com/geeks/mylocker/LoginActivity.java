package com.geeks.mylocker;

import com.geeks.mylocker.async.CryptoTask;
import com.geeks.mylocker.encrypto.Encryptor;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends AppBaseActivity {

	Activity self;
	
	protected final String TAG = getClass().getSimpleName();
	
	private static String APP_MASTER_KEY = "mylocker";
	private static String TEMP_MASTER_KEY = "masterKey";
	
	Button loginButton, cancelButton;
	
	EditText loginMasterkey;
	
	private String encryptedMasterKey = "0Yrz8ctzQSQZ7SpXOv760w==]ZcmisNvrFEGkDDVqnBQCKA==";

	TextView message;
	int counter = 3;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		
		self = this;

		loginButton = (Button) findViewById(R.id.login_btn_login);
		loginMasterkey = (EditText) findViewById(R.id.login_masterkey);
		
		//TODO
		loginMasterkey.getText().append(TEMP_MASTER_KEY);

		cancelButton = (Button) findViewById(R.id.login_btn_cancel);
		
		message = (TextView) findViewById(R.id.login_msg);
		message.setVisibility(View.GONE);
		
		//Encryptor encryptor = Encryptor.select(Encryptor.PADDING_ENC_IDX);
		//Log.i(TAG, "TEST:" +encryptor.encrypt(TEMP_MASTER_KEY, APP_MASTER_KEY));
		//Log.i(TAG, "TEST:" +encryptor.decrypt(encryptedMasterKey, APP_MASTER_KEY));

		loginButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				
				
				if (loginMasterkey.getText().toString().equals("")) {
					Toast.makeText(getApplicationContext(), "Please type your masterkey", Toast.LENGTH_SHORT).show();
				} else {
					decryptPassword();
				}
			}
		});

		cancelButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}
	
	private void decryptPassword() {

		new CryptoTask() {

			@Override
			protected String doCrypto() {
				Encryptor encryptor = Encryptor.select(Encryptor.PADDING_ENC_IDX);
				return encryptor.decrypt(encryptedMasterKey, APP_MASTER_KEY);
			}

			@Override
			protected void updateUi(String result) {

				if(loginMasterkey.getText().toString().equals(result)) {
					
					Config config = new Config();
					config.setLogined(Boolean.TRUE);
					config.setMasterKey(result);
					
					Intent intent = new Intent(self, ListFolderActivity.class);
					intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					Bundle extras = new Bundle();
					extras.putSerializable(Config.CONTEXT, config);
					if(extras !=null) intent.putExtras(extras);
					self.startActivity(intent);
					
				} else {
					Toast.makeText(getApplicationContext(),	"Wrong Credentials", Toast.LENGTH_SHORT).show();
								
					message.setVisibility(View.VISIBLE);
					message.setBackgroundColor(Color.RED);
					counter--;
					message.setText(Integer.toString(counter));
	
					if (counter == 0) {
						loginButton.setEnabled(false);
					}
				}
			}
        }.execute(); 
	}

	@Override
	protected void onPause() {
		super.onPause();
		loginMasterkey.getText().clear();
	}
	
	

}
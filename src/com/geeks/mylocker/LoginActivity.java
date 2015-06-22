package com.geeks.mylocker;

import com.geeks.mylocker.async.CryptoTask;
import com.geeks.mylocker.encrypto.Encryptor;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends Activity {

	private static String APP_MASTER_KEY = "mylocker";
	private static String TEMP_MASTER_KEY = "test";
	
	Button loginButton, cancelButton;
	
	EditText loginMasterkey;
	
	private String encryptedMasterKey;

	TextView message;
	int counter = 3;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);

		loginButton = (Button) findViewById(R.id.login_btn_login);
		loginMasterkey = (EditText) findViewById(R.id.login_masterkey);

		cancelButton = (Button) findViewById(R.id.login_btn_cancel);
		
		message = (TextView) findViewById(R.id.login_msg);
		message.setVisibility(View.GONE);

		loginButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				
				
				if (loginMasterkey.getText().toString().equals("")) {
					Toast.makeText(getApplicationContext(), "Please type your masterkey", Toast.LENGTH_SHORT).show();
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
				
			}
        }.execute(); 
	}

}
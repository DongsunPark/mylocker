package com.geeks.mylocker;

import android.app.Activity;
import android.os.Bundle;

import com.geeks.mylocker.helper.ActivityHelper;
import com.geeks.mylocker.helper.BaseActivityHelper;

public class AppBaseActivity extends Activity implements BaseActivityHelper {

	private ActivityHelper activityHelper;
	
	private Config config;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		
		activityHelper = new ActivityHelper(this);
		activityHelper.registerBaseActivityReceiver();
	}

	@Override
    protected void onDestroy() {
    	super.onDestroy();
    	activityHelper.unRegisterBaseActivityReceiver();
    }
	
	public ActivityHelper getActivityHelper() {
		return this.activityHelper;
	}

	public void setActivityHelper(ActivityHelper activityHelper) {
		this.activityHelper = activityHelper;
	}

	public Config getConfig() {
		return config;
	}

	public void setConfig(Config config) {
		this.config = config;
	}
}

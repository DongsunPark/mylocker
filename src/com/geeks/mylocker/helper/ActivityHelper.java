package com.geeks.mylocker.helper;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import com.geeks.mylocker.LoginActivity;

public class ActivityHelper {
	
	protected final String TAG = getClass().getSimpleName();
	
	public static final String FINISH_ALL_ACTIVITIES_ACTIVITY_ACTION = "com.geeks.mylocker.activity.FINISH_ALL_ACTIVITIES_ACTIVITY_ACTION";
	
	private BaseActivityReceiver baseActivityReceiver;
	
	private static final IntentFilter INTENT_FILTER = createIntentFilter();
	
	private Context context;
	
	public ActivityHelper(Context cxt) {
		this.context = cxt;
		baseActivityReceiver = new BaseActivityReceiver(cxt);
	}
	
	private static IntentFilter createIntentFilter(){
		IntentFilter filter = new IntentFilter();
		filter.addAction(FINISH_ALL_ACTIVITIES_ACTIVITY_ACTION);
		return filter;
	}
	
	public void registerBaseActivityReceiver() {
		context.registerReceiver(baseActivityReceiver, INTENT_FILTER);
	}
	
	public void unRegisterBaseActivityReceiver() {
		context.unregisterReceiver(baseActivityReceiver);
	}
	
	public class BaseActivityReceiver extends BroadcastReceiver{
		
		Context parentContext;
		
		public BaseActivityReceiver(Context context) {
			parentContext = context;
		}
		
		@Override
		public void onReceive(Context cxt, Intent intent) {
			if(intent.getAction().equals(FINISH_ALL_ACTIVITIES_ACTIVITY_ACTION)){
				if(cxt instanceof Activity && !(cxt instanceof LoginActivity)) {
					((Activity)cxt).finish();
				}
			}
		}
	} 
	
	public void closeAllActivities(){
		context.sendBroadcast(new Intent(FINISH_ALL_ACTIVITIES_ACTIVITY_ACTION));
	}
}

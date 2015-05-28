package com.geeks.mylocker.helper;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.geeks.mylocker.AddRecordActivity;
import com.geeks.mylocker.ListFolderActivity;
import com.geeks.mylocker.R;

public class MenuHelper {
	public static boolean onOptionsItemSelected( MenuItem item, Context context, Bundle extras) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		Intent intent = null;
		int id = item.getItemId();
		
		switch (id) {
			case R.id.action_add:
				intent = new Intent(context, AddRecordActivity.class);
				if(extras !=null) intent.putExtras(extras);
				context.startActivity(intent);
				break;
			case R.id.action_folder_list:
				intent = new Intent(context, ListFolderActivity.class);
				if(extras !=null) intent.putExtras(extras);
				context.startActivity(intent);
				break;
		}
		
		return true;
	}
}

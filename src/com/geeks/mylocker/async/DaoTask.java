package com.geeks.mylocker.async;

import android.os.AsyncTask;
import android.util.Log;

import com.geeks.mylocker.dao.Entity;

public abstract class DaoTask<T extends Entity> extends AsyncTask<DaoCommand<T>, Void, Entity> {

	private static final String TAG = DaoTask.class.getSimpleName();
    
	Exception error;
    
	@Override
    protected Entity doInBackground(DaoCommand<T>... params) {
        try {
        	if(params.length>1) return null;
            return executeDao(params[0]);
        } catch (Exception e) {
            error = e;
            Log.e(TAG, "Error: " + e.getMessage(), e);
            return null;
        }
    }

    protected abstract T executeDao(DaoCommand<T> daoCommand);
    
    @Override
    protected void onPostExecute(Entity result) {
        updateUi(result);
    }

    protected abstract void updateUi(Entity result);
}
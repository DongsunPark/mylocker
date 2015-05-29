package com.geeks.mylocker.async;

import android.os.AsyncTask;
import android.util.Log;

public abstract class CryptoTask extends AsyncTask<Void, Void, String> {

	private static final String TAG = CryptoTask.class.getSimpleName();
    
	Exception error;
    
	@Override
    protected String doInBackground(Void... params) {
        try {
            return doCrypto();
        } catch (Exception e) {
            error = e;
            Log.e(TAG, "Error: " + e.getMessage(), e);

            return null;
        }
    }

    protected abstract String doCrypto();
    
    @Override
    protected void onPostExecute(String result) {
        updateUi(result);
    }

    protected abstract void updateUi(String result);
}
package pt.utl.ist.cm.neartweetclient.services;

import android.os.AsyncTask;

public abstract class NearTweetService extends AsyncTask<String, Integer, Boolean> {
	
	protected String userId;
	
	public NearTweetService(String userId) {
		this.userId = userId;
	}
	
	protected abstract boolean run();
	protected abstract void afterRun();
	
	@Override
	protected Boolean doInBackground(String... params) {
		return run();
	}
	
	@Override
	 protected void onPostExecute(Boolean result) {
		afterRun();
	}
}

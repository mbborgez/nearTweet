package pt.utl.ist.cm.neartweetclient.services;

import pt.utl.ist.cm.neartweetEntities.pdu.RegisterPDU;
import pt.utl.ist.cm.neartweetclient.connectionTasks.ConnectionStatus;
import pt.utl.ist.cm.neartweetclient.exceptions.NearTweetException;
import pt.utl.ist.cm.neartweetclient.ui.LoginActivity;
import pt.utl.ist.cm.neartweetclient.utils.Constants;
import android.app.Activity;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;

public class RegisterUserService extends AsyncTask<String, Integer, Boolean> {
	
	String userName;
	Activity activity;
	
	public RegisterUserService(String username, Activity activity) {
		this.userName = username;
		this.activity = activity;
	}
	
	/**
	 * createCookieSession - it should only be activated when 
	 * the server responds with void (meaning that everything went ok)
	 */
	private void createCookieSession() {
		SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this.activity.getApplicationContext());
		SharedPreferences.Editor editor = settings.edit();
		editor.putString("username", this.userName);
		editor.commit();
	}
	
	private void registerUserOnServer() throws NearTweetException {
		ConnectionStatus.getInstance().sendPDU(new RegisterPDU(this.userName));
	}
	
	private void connectToServer() throws NearTweetException {
		ConnectionStatus.getInstance().connect(Constants.SERVER_ADDRESS, Constants.SERVER_PORT);
	}

	@Override
	protected Boolean doInBackground(String... params) {
		try {
			connectToServer();
			registerUserOnServer();
		} catch(NearTweetException e) {
			e.printStackTrace();
			Log.i("NEART WEET EXCEPTION", e.getMessage());
			return false;
		}
		return true;
	}
	
	@Override
	 protected void onPostExecute(Boolean result) {
         LoginActivity act = (LoginActivity) this.activity;
         if (result) {
        	 createCookieSession();
//        	 act.nextScreen(); 
         } else {
        	 act.invalidLogin();
         }
     }

}

package pt.utl.ist.cm.neartweetclient.services;

import java.io.IOException;
import java.net.UnknownHostException;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;
import pt.utl.ist.cm.neartweetEntities.pdu.RegisterPDU;
import pt.utl.ist.cm.neartweetclient.connectionTasks.Connection;
import pt.utl.ist.cm.neartweetclient.exceptions.NearTweetException;
import pt.utl.ist.cm.neartweetclient.ui.LoginActivity;

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
        try {
			Connection.getInstance().sendPDU(new RegisterPDU(this.userName));
		} catch (UnknownHostException e) {
			throw new NearTweetException(e.getClass().getName()); 
		} catch (IOException e) {
			throw new NearTweetException(e.getClass().getName()); 
		}
	}

	@Override
	protected Boolean doInBackground(String... params) {
		try {
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
        	 act.nextScreen(); 
         } else {
        	 act.invalidLogin();
         }
     }

}

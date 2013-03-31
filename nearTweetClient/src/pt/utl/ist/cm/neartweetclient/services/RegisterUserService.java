package pt.utl.ist.cm.neartweetclient.services;

import java.io.IOException;
import java.net.UnknownHostException;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import pt.utl.ist.cm.neartweetEntities.pdu.RegisterPDU;
import pt.utl.ist.cm.neartweetclient.connectionTasks.ConnectionStatus;
import pt.utl.ist.cm.neartweetclient.exceptions.NearTweetException;
import pt.utl.ist.cm.neartweetclient.ui.LoginActivity;

public class RegisterUserService extends AsyncTask<String, Integer, Boolean> {
	
	String userName;
	Activity activity;
	
	public RegisterUserService(String username, Activity activity) {
		this.userName = username;
		this.activity = activity;
	}
	
	private void createCookieSession() {
		SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this.activity.getApplicationContext());
		SharedPreferences.Editor editor = settings.edit();
		editor.putString("username", this.userName);
		editor.commit();
	}
	
	private void registerUserOnServer() throws NearTweetException {
        try {
			ConnectionStatus.getInstance().sendPDU(new RegisterPDU(this.userName));
		} catch (UnknownHostException e) {
			throw new NearTweetException(e.getClass().getName()); 
		} catch (IOException e) {
			throw new NearTweetException(e.getClass().getName()); 
		}
	}

	@Override
	protected Boolean doInBackground(String... params) {
		createCookieSession();
		try {
			registerUserOnServer();
		} catch(NearTweetException e) {
			return false;
		}
		return true;
	}
	
	@Override
	 protected void onPostExecute(Boolean result) {
         LoginActivity act = (LoginActivity) this.activity;
         if (result) {
        	 act.nextScreen(); 
         } else {
        	 act.invalidLogin();
         }
     }

}

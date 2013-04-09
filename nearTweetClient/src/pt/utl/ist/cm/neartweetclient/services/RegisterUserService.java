package pt.utl.ist.cm.neartweetclient.services;

import java.io.IOException;
import java.net.UnknownHostException;

import pt.utl.ist.cm.neartweetEntities.pdu.RegisterPDU;
import pt.utl.ist.cm.neartweetclient.sync.Connection;
import pt.utl.ist.cm.neartweetclient.ui.LoginActivity;
import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;

public class RegisterUserService extends AsyncTask<String, Integer, Boolean> {

	String userName;
	Activity activity;

	public RegisterUserService(String username, Activity activity) {
		this.userName = username;
		this.activity = activity;
	}

	@Override
	protected Boolean doInBackground(String... params) {
		Log.i("DEBUG", "doInBackground - start");
		try {
			Log.i("DEBUG", "doInBackground - before sendPDU");

			Connection.getInstance().sendPDU(new RegisterPDU(this.userName));

			Log.i("DEBUG", "doInBackground - after sendPDU");

		} catch (UnknownHostException e) {
			Log.i("DEBUG", "doInBackground - false");
			return false;
		} catch (IOException e) {
			Log.i("DEBUG", "doInBackground - false");
			return false;
		}
		Log.i("DEBUG", "doInBackground - true");
		return true;
	}

	@Override
	protected void onPostExecute(Boolean result) {
		LoginActivity act = (LoginActivity) this.activity;
		if (!result) {
			act.connectionError();
		}
	}

}

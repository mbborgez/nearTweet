package pt.utl.ist.cm.neartweetclient.services;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import pt.utl.ist.cm.neartweetclient.exceptions.NearTweetException;

public class RegisterUserService extends NearTweetService {
	
	String userName;
	Context activityContext;
	
	public RegisterUserService(String username, Context context) {
		this.userName = username;
		this.activityContext = context;
	}
	
	@Override
	public void execute() throws NearTweetException {
		SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this.activityContext);
		SharedPreferences.Editor editor = settings.edit();
		editor.putString("username", this.userName);
		editor.commit();
	}

}

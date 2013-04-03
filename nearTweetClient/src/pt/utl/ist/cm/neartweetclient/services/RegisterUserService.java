package pt.utl.ist.cm.neartweetclient.services;

import pt.utl.ist.cm.neartweetEntities.pdu.RegisterPDU;
import pt.utl.ist.cm.neartweetclient.connectionTasks.ConnectionStatus;
import pt.utl.ist.cm.neartweetclient.exceptions.NearTweetException;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class RegisterUserService extends NearTweetService {
	
	private String userName;
	private Context context;
	
	public RegisterUserService(String username, Context context) {
		this.userName = username;
		this.context = context;
	}

	@Override
	public void execute() throws NearTweetException {
		registerUserOnServer();
		createCookieSession();
	}
	
	/**
	 * createCookieSession - it should only be activated when 
	 * the server responds with void (meaning that everything went ok)
	 */
	private void createCookieSession() {
		SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
		SharedPreferences.Editor editor = settings.edit();
		editor.putString("username", this.userName);
		editor.commit();
	}
	/**
	 * sends a register request to the server
	 * @throws NearTweetException
	 */
	private void registerUserOnServer() throws NearTweetException {
		ConnectionStatus.getInstance().sendPDU(new RegisterPDU(this.userName));
	}
	
}

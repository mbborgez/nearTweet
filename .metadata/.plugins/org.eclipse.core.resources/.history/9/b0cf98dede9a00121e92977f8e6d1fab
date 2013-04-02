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

public class RegisterUserService implements Service {

	private String username;
	private Context context;
	Activity activity;

	public RegisterUserService(String username, Context context, Activity activity) {
		this.username = username;
		this.context = context;
		this.activity = activity;
	}

	@Override
	public void execute() {
		createCookieSession(username, context);
		connectToServer();
		startReceivingData();
		registUser();
	}
	
	private void createCookieSession(String username, Context context) {
		// save the username in the preference manager
		SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
		SharedPreferences.Editor editor = settings.edit();
		editor.putString("username", username);
		editor.commit();
	}
	
	private void connectToServer() {
		// Connect to the server
		Thread connectThread = new Thread(new AsynchConnectTask());
		connectThread.start();
	}
	
	private void startReceivingData() {
		// start receiving data from the server
		Thread receiveThread = new Thread(new AsynchReceiveTask(context));
		receiveThread.start();
	}

	private void registUser() {
		// send a regist request to the server.
		RegisterPDU registerPdu = new RegisterPDU(username);
		Thread sendThread = new Thread(new AsynchSendTask(registerPdu));
		sendThread.start();
	}
}

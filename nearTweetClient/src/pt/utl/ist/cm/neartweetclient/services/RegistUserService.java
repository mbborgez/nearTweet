package pt.utl.ist.cm.neartweetclient.services;

import pt.utl.ist.cm.neartweetEntities.PDU.RegisterPDU;
import pt.utl.ist.cm.neartweetclient.connectionTasks.AsynchConnectTask;
import pt.utl.ist.cm.neartweetclient.connectionTasks.AsynchReceiveTask;
import pt.utl.ist.cm.neartweetclient.connectionTasks.AsynchSendTask;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class RegistUserService implements Service{

	private String username;
	private Context context;
	
	public RegistUserService(String username, Context context){
		this.username = username;
		this.context = context;
	}
	
	@Override
	public void execute() {
		registerInServer(username, context);
	}

	private void registerInServer(String username, Context context) {

		//save the username in the preference manager
		SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
		SharedPreferences.Editor editor = settings.edit();
		editor.putString("username", username);
		editor.commit();
		
		// Connect to the server
		Thread connectThread = new Thread(new AsynchConnectTask());
		connectThread.start();

		// start receiving data from the server
		Thread receiveThread = new Thread(new AsynchReceiveTask(context));
		receiveThread.start();

		//send a regist request to the server.
		RegisterPDU registerPdu = new RegisterPDU(username);
		Thread sendThread = new Thread(new AsynchSendTask(registerPdu));
		sendThread.start();
		
	}
}

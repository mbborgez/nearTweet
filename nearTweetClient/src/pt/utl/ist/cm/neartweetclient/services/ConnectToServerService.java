package pt.utl.ist.cm.neartweetclient.services;

import pt.utl.ist.cm.neartweetclient.connectionTasks.ConnectionStatus;
import pt.utl.ist.cm.neartweetclient.exceptions.NearTweetException;
import android.util.Log;

public class ConnectToServerService extends NearTweetService {

	String serverAddress;
	int serverPort;

	public ConnectToServerService(String serverAddress, int serverPort) {
		this.serverAddress = serverAddress;
		this.serverPort = serverPort;
	}

	@Override
	public void execute() throws NearTweetException {
		connectToServer(serverAddress, serverPort);
	}
	
	private void connectToServer(String serverAddress, int serverPort) throws NearTweetException {
		Log.i(this.getClass().toString(), "Connecting..");
		ConnectionStatus.getInstance().connect(serverAddress, serverPort);
		Log.i(this.getClass().toString(), "Connected");
	}
	
}

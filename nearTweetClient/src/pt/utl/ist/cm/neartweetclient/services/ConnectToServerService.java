package pt.utl.ist.cm.neartweetclient.services;

import pt.utl.ist.cm.neartweetclient.connectionTasks.ConnectionStatus;
import pt.utl.ist.cm.neartweetclient.exceptions.ErrorConnectingException;
import android.util.Log;

public class ConnectToServerService implements Service {

	String serverAddress;
	int serverPort;

	public ConnectToServerService(String serverAddress, int serverPort) {
		this.serverAddress = serverAddress;
		this.serverPort = serverPort;
	}

	@Override
	public void execute() {
		connectToServer(serverAddress, serverPort);
	}
	
	private void connectToServer(String serverAddress, int serverPort){
		if(!ConnectionStatus.getInstance().isConnected()){
			try {
				Log.i(this.getClass().toString(), "Connecting..");
				ConnectionStatus.getInstance().connect(serverAddress, serverPort);
				Log.i(this.getClass().toString(), "Connected");
			} catch (ErrorConnectingException e) {
				e.printStackTrace();
				Log.e(this.getClass().toString(), "Error connecting");
			}
		}
	}
	
}

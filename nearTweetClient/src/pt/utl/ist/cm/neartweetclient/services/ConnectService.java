package pt.utl.ist.cm.neartweetclient.services;

import pt.utl.ist.cm.neartweetclient.exceptions.NearTweetException;
import pt.utl.ist.cm.neartweetclient.sync.Connection;

public class ConnectService implements INearTweetService {

	private String serverAddress;
	private int serverPort;

	public ConnectService(String serverAddress, int serverPort){
		this.serverAddress = serverAddress;
		this.serverPort = serverPort;
	}
	
	@Override
	public boolean execute() {
		try {
			Connection.getInstance().connect(serverAddress, serverPort);
		} catch (NearTweetException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

}

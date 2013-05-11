package pt.utl.ist.cm.neartweetclient.core;

import java.util.ArrayList;
import java.util.List;

import pt.utl.ist.cm.neartweetclient.sync.Connection;

public class PeersManager {

	public static boolean existsUser(String userId) {
		return Connection.getInstance().hasPeer(userId);
	}
	
	public static void unregisterUser(String userId) {
		if(Connection.getInstance().hasPeer(userId)){
			Connection.getInstance().removePeer(userId);
		}
	}
	
	public static List<String> getUsers() {
		List<String> userNames = new ArrayList<String>();
		for(Peer peer : Connection.getInstance().getPeers()) {
			userNames.add(peer.getDeviceName());
		}
		return userNames;
	}

}

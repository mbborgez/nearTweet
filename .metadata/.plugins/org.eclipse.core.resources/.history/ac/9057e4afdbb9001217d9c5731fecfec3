package pt.utl.ist.cm.neartweetclient.core;

import java.util.ArrayList;
import java.util.List;

public class ClientsManager {

	private static List<String> users = new ArrayList<String>();
	
	public static boolean existsUser(String userId) {
		return users.contains(userId);
	}
	
	public static void unregisterUser(String userId) {
		users.remove(userId);
	}
	
	public static void registerUser(String userId) {
		if(!existsUser(userId)){
			users.add(userId);
		}
	}

	public static List<String> getUsers() {
		return users;
	}

}

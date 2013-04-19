package pt.utl.ist.cm.neartweetclient.exceptions;

import pt.utl.ist.cm.neartweetclient.services.INearTweetService;
import pt.utl.ist.cm.neartweetclient.sync.Connection;

public class DisconnectService implements INearTweetService  {

	@Override
	public boolean execute(){
		try {
			Connection.getInstance().disconnect();
		} catch (NearTweetException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

}

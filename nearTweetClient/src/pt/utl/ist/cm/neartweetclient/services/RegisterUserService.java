package pt.utl.ist.cm.neartweetclient.services;

import pt.utl.ist.cm.neartweetEntities.pdu.RegisterPDU;
import pt.utl.ist.cm.neartweetclient.exceptions.NearTweetException;
import pt.utl.ist.cm.neartweetclient.sync.Connection;

public class RegisterUserService implements INearTweetService {

	String userId;

	public RegisterUserService(String userId) {
		this.userId = userId;
	}

	@Override
	public boolean execute() {
		try {
			Connection.getInstance().sendPDU(new RegisterPDU(userId, userId));
		} catch (NearTweetException e) {
			return false;
		}
		return true;
	}
}

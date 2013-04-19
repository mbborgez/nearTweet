package pt.utl.ist.cm.neartweetclient.services;


import pt.utl.ist.cm.neartweetEntities.pdu.SpamVotePDU;
import pt.utl.ist.cm.neartweetclient.sync.Connection;
import android.util.Log;

public class CreateSpamService implements INearTweetService {
	
	private String userId;
	private String targetMessageId;
	
	public CreateSpamService(String userId, String targetMessageID) {
		this.userId = userId;
		this.targetMessageId = targetMessageID;
	}

	@Override
	public boolean execute() {
		try {
			SpamVotePDU pdu = new SpamVotePDU(userId, targetMessageId);
			Connection.getInstance().sendPDU(pdu);
			return true;
		} catch(Exception e) {
			Log.i("DEGUB", e.getMessage());
			return false;
		}
	}

}

package pt.utl.ist.cm.neartweetclient.services;


import pt.utl.ist.cm.neartweetEntities.pdu.SpamVotePDU;
import pt.utl.ist.cm.neartweetclient.sync.Connection;
import android.util.Log;

public class CreateSpamService extends NearTweetService {
	
	private String targetMessageId;
	
	public CreateSpamService(String userID, String targetMessageID) {
		super(userID);
		this.targetMessageId = targetMessageID;
	}

	@Override
	protected boolean run() {
		try {
			SpamVotePDU pdu = new SpamVotePDU(this.userId, this.targetMessageId);
			Connection.getInstance().sendPDU(pdu);
			return true;
		} catch(Exception e) {
			Log.i("DEGUB", e.getMessage());
			return false;
		}
	}

	@Override
	protected void afterRun() {}

}

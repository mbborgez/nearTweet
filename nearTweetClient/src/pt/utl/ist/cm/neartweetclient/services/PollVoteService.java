package pt.utl.ist.cm.neartweetclient.services;

import pt.utl.ist.cm.neartweetEntities.pdu.PollVotePDU;
import pt.utl.ist.cm.neartweetclient.sync.Connection;
import android.util.Log;

public class PollVoteService extends NearTweetService {
	
	private String targetMessageId;
	private int optionPosition;
	
	public PollVoteService(String userID, String targetMessageID, int optionPosition) {
		super(userID);
		this.targetMessageId = targetMessageID;
		this.optionPosition = optionPosition;
	}

	@Override
	protected boolean run() {
		try {
			PollVotePDU pdu = new PollVotePDU(this.userId, this.targetMessageId, this.optionPosition);
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

package pt.utl.ist.cm.neartweetclient.services;


import pt.utl.ist.cm.neartweetEntities.pdu.SpamVotePDU;
import pt.utl.ist.cm.neartweetclient.sync.Connection;
import android.util.Log;

public class SpamVoteService implements INearTweetService {
	
	private String userId;
	String targetUserId;
	private String targetMessageId;
	
	public SpamVoteService(String userId, String targetMessageId, String targerUserId) {
		this.userId = userId;
		this.targetMessageId = targetMessageId;
		this.targetUserId = targerUserId;
	}

	@Override
	public boolean execute() {
		try {
			Log.i("DEBUG", "SpamVoteService - sending spamVote [userId: " + userId + ", targetMessageId: " + targetMessageId + ", targetUserId: " + targetUserId);
			
			SpamVotePDU pdu = new SpamVotePDU(userId, targetMessageId, targetUserId);
			Connection.getInstance().sendPDU(pdu);
			return true;
		} catch(Exception e) {
			Log.i("DEGUB", e.getMessage());
			return false;
		}
	}

}

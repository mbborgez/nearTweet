package pt.utl.ist.cm.neartweetclient.services;


import pt.utl.ist.cm.neartweetEntities.pdu.SpamVotePDU;
import pt.utl.ist.cm.neartweetclient.sync.Connection;
import pt.utl.ist.cm.neartweetclient.utils.Actions;
import android.content.Context;
import android.util.Log;

public class SpamVoteService implements INearTweetService {
	
	private String userId;
	String targetUserId;
	private String targetMessageId;
	private Context context;
	
	public SpamVoteService(String userId, String targetMessageId, String targerUserId, Context context) {
		this.userId = userId;
		this.targetMessageId = targetMessageId;
		this.targetUserId = targerUserId;
		this.context = context;
	}

	@Override
	public boolean execute() {
		try {
			Log.i("DEBUG", "SpamVoteService - sending spamVote [userId: " + userId + ", targetMessageId: " + targetMessageId + ", targetUserId: " + targetUserId);
			
			SpamVotePDU pdu = new SpamVotePDU(Actions.createUniqueID(context), userId, targetMessageId, targetUserId);
			Connection.getInstance().broadcastPDU(pdu);
			return true;
		} catch(Exception e) {
			Log.i("DEGUB", e.getMessage());
			return false;
		}
	}

}

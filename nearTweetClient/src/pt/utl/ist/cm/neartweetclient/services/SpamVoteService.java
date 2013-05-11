package pt.utl.ist.cm.neartweetclient.services;


import pt.utl.ist.cm.neartweetEntities.pdu.SpamVotePDU;
import pt.utl.ist.cm.neartweetclient.sync.Connection;
import pt.utl.ist.cm.neartweetclient.utils.Actions;
import android.content.Context;
import android.util.Log;

public class SpamVoteService implements INearTweetService {
	
	String targetUserId;
	private String targetMessageId;
	private Context context;
	
	public SpamVoteService(String targetMessageId, String targerUserId, Context context) {
		this.targetMessageId = targetMessageId;
		this.targetUserId = targerUserId;
		this.context = context;
	}

	@Override
	public boolean execute() {
		try {
			SpamVotePDU pdu = new SpamVotePDU(Actions.createUniqueID(context), Actions.getUserId(context), targetMessageId, targetUserId);
			Log.i("DEBUG", "SpamVoteService - sending spamVote - " + pdu); 
			Connection.getInstance().broadcastPDU(pdu);
			return true;
		} catch(Exception e) {
			Log.i("DEGUB", e.getMessage());
			return false;
		}
	}

}

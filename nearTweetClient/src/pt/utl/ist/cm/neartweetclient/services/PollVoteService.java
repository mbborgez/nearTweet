package pt.utl.ist.cm.neartweetclient.services;

import pt.utl.ist.cm.neartweetEntities.pdu.PollVotePDU;
import pt.utl.ist.cm.neartweetclient.sync.Connection;
import pt.utl.ist.cm.neartweetclient.utils.Actions;
import android.app.Activity;
import android.util.Log;

public class PollVoteService extends NearTweetService {
	
	private String targetMessageId;
	private int optionPosition;
	private Activity activity;
	
	public PollVoteService(String userID, String targetMessageID, int optionPosition) {
		super(userID);
		this.targetMessageId = targetMessageID;
		this.optionPosition = optionPosition;
	}

	@Override
	protected boolean run() {
		try {
			String tweetId = this.userId + Actions.getLastTweet(this.activity.getApplicationContext());
			
			PollVotePDU pdu = new PollVotePDU(this.userId, tweetId, this.targetMessageId, this.optionPosition);
			Connection.getInstance().sendPDU(pdu);

			Actions.incrementTweetID(this.activity.getApplicationContext());
			return true;
		} catch(Exception e) {
			Log.i("DEGUB", e.getMessage());
			return false;
		}
	}

	@Override
	protected void afterRun() {}
}

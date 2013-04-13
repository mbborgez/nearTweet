package pt.utl.ist.cm.neartweetclient.services;

import pt.utl.ist.cm.neartweetEntities.pdu.PollVotePDU;
import pt.utl.ist.cm.neartweetclient.sync.Connection;
import pt.utl.ist.cm.neartweetclient.utils.Actions;
import android.app.Activity;

public class PollVoteService extends NearTweetService {
	
	private String targetMessageId;
	private int optionPosition;
	private Activity activity;
	
	public PollVoteService(String userID, String targetMessageID, int optionPosition, Activity activity) {
		super(userID);
		this.targetMessageId = targetMessageID;
		this.optionPosition = optionPosition;
		this.activity = activity;
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
			e.printStackTrace();
			return false;
		}
	}

	@Override
	protected void afterRun() {}
}

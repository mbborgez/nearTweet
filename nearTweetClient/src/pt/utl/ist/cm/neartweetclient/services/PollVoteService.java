package pt.utl.ist.cm.neartweetclient.services;

import pt.utl.ist.cm.neartweetEntities.pdu.PollVotePDU;
import pt.utl.ist.cm.neartweetclient.sync.Connection;
import pt.utl.ist.cm.neartweetclient.utils.Actions;
import android.content.Context;

public class PollVoteService implements INearTweetService {
	
	private String targetMessageId;
	private int optionPosition;
	private Context context;
	
	public PollVoteService(String targetMessageID, int optionPosition, Context context) {
		this.targetMessageId = targetMessageID;
		this.optionPosition = optionPosition;
		this.context = context;
	}

	@Override
	public boolean execute() {
		try {
			String tweetId = Actions.getUserId(context) + Actions.getLastTweet(context);
			PollVotePDU pdu = new PollVotePDU(Actions.getUserId(context), tweetId, targetMessageId, optionPosition);
			
			Connection.getInstance().sendPDU(pdu);

			Actions.incrementTweetID(context);
			return true;
		} catch(Exception e) {
			e.printStackTrace();
			return false;
		}
	}

}

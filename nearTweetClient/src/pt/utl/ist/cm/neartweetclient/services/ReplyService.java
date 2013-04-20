package pt.utl.ist.cm.neartweetclient.services;

import android.content.Context;

import pt.utl.ist.cm.neartweetEntities.pdu.ReplyPDU;
import pt.utl.ist.cm.neartweetclient.sync.Connection;
import pt.utl.ist.cm.neartweetclient.utils.Actions;

public class ReplyService implements INearTweetService {

	private String targetMessageId;
	private String text;
	private Context context;
	private Boolean isBroadcast;
	private String targetUserId;
	
	public ReplyService(String targetMessageId, String targetUserId, String text, Boolean isBroadcast, Context context) {
		this.targetUserId = targetUserId;
		this.targetMessageId = targetMessageId;
		this.text = text;
		this.context = context;
		this.isBroadcast = isBroadcast;
	}

	@Override
	public boolean execute() {
		try {
			String userId = Actions.getUserId(context);
			String tweetId = userId + Actions.getLastTweet(context);
			ReplyPDU pdu = new ReplyPDU(userId, tweetId, targetMessageId, text, targetUserId, isBroadcast);
			
			Connection.getInstance().sendPDU(pdu);
			
			Actions.incrementTweetID(context);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

}

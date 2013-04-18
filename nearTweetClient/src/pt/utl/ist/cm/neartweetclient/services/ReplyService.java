package pt.utl.ist.cm.neartweetclient.services;

import android.content.Context;

import pt.utl.ist.cm.neartweetEntities.pdu.ReplyPDU;
import pt.utl.ist.cm.neartweetclient.sync.Connection;
import pt.utl.ist.cm.neartweetclient.utils.Actions;

public class ReplyService extends NearTweetService {

	private String tweetId;
	private String targetMessageId;
	private String text;
	private String addresse;
	private Context context;
	private Boolean isBroadcast;
	
	public ReplyService(String userId, String targetMessageId, String text, String targetId, Boolean isBroadcast,Context context) {
		super(userId);
		this.tweetId = userId + Actions.getLastTweet(context);
		this.targetMessageId = targetMessageId;
		this.text = text;
		this.context = context;
		this.addresse = targetId;
		this.isBroadcast = isBroadcast;
	}

	@Override
	protected boolean run() {
		try {
			ReplyPDU pdu = new ReplyPDU(this.userId, this.tweetId, this.targetMessageId, this.text, this.addresse, this.isBroadcast);
			Connection.getInstance().sendPDU(pdu);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	@Override
	protected void afterRun() {
		Actions.incrementTweetID(this.context);
	}

}

package pt.utl.ist.cm.neartweetclient.services;

import java.io.IOException;
import java.net.UnknownHostException;

import pt.utl.ist.cm.neartweetEntities.pdu.ReplyPDU;
import pt.utl.ist.cm.neartweetclient.sync.Connection;

public class ReplyService extends NearTweetService {

	private String tweetId;
	private String targetMessageId;
	private String text;
	private String addresse;
	
	public ReplyService(String userId, String targetMessageId, String text, String targetId) {
		super(userId);
		this.tweetId = "aa";
		this.targetMessageId = targetMessageId;
		this.text = text;
		this.addresse = targetId;
	}

	@Override
	protected boolean run() {
		try {
			ReplyPDU pdu = new ReplyPDU(this.userId, this.tweetId, this.targetMessageId, this.text, this.addresse);
			Connection.getInstance().sendPDU(pdu);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	@Override
	protected void afterRun() {
		// TODO Auto-generated method stub
		
	}

}

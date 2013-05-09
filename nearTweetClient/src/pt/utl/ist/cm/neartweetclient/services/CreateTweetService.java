package pt.utl.ist.cm.neartweetclient.services;

import android.content.Context;
import pt.utl.ist.cm.neartweetEntities.pdu.TweetPDU;
import pt.utl.ist.cm.neartweetclient.exceptions.NearTweetException;
import pt.utl.ist.cm.neartweetclient.sync.Connection;
import pt.utl.ist.cm.neartweetclient.utils.Actions;

public class CreateTweetService implements INearTweetService {

	private String tweetText;
	private byte[] tweetImageBytes;
	private Context context;

	public CreateTweetService(String tweetText, byte[] tweetImageBytes, Context context) {
		this.tweetText  = tweetText;
		this.tweetImageBytes = tweetImageBytes;
		this.context = context;
	}

	@Override
	public boolean execute() {
		try {
			String userId = Actions.getUserId(context);
			String tweetId = Actions.createUniqueID(context);
			
			TweetPDU pdu = new TweetPDU(userId, tweetId, tweetText, tweetImageBytes);
			Connection.getInstance().broadcastPDU(pdu);
			
			return true;
		} catch (NearTweetException e) {
			e.printStackTrace();
			return false;
		}
	}


}

package pt.utl.ist.cm.neartweetclient.services;

import java.util.ArrayList;

import pt.utl.ist.cm.neartweetEntities.pdu.PublishPollPDU;
import pt.utl.ist.cm.neartweetclient.sync.Connection;
import pt.utl.ist.cm.neartweetclient.utils.Actions;
import android.app.Activity;
import android.util.Log;

public class CreatePollService extends NearTweetService {

	private String tweetId;
	private String tweetMessage;
	private ArrayList<String> pollOptions;
	private Activity activity;
	
	public CreatePollService(String userId, String text, ArrayList<String> options, Activity activity) {
		super(userId);
		this.tweetId = userId + Actions.getLastTweet(activity.getApplicationContext());
		this.tweetMessage = text;
		this.pollOptions = options;
		this.activity = activity;
	}

	@Override
	protected boolean run() {
		try {
			PublishPollPDU pdu = new PublishPollPDU(
					this.userId, 
					this.tweetId, 
					this.tweetMessage, 
					this.pollOptions);
			Connection.getInstance().sendPDU(pdu);
			return true;
		} catch(Exception e) {
			Log.i("DEGUB", e.getMessage());
			return false;
		}
	}

	@Override
	protected void afterRun() {
		Actions.incrementTweetID(this.activity.getApplicationContext());
		
	}

}

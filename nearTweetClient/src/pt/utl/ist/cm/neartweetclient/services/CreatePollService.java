package pt.utl.ist.cm.neartweetclient.services;

import java.util.ArrayList;

import pt.utl.ist.cm.neartweetEntities.pdu.PublishPollPDU;
import pt.utl.ist.cm.neartweetclient.sync.Connection;
import pt.utl.ist.cm.neartweetclient.utils.Actions;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

public class CreatePollService implements INearTweetService {

	private String tweetMessage;
	private ArrayList<String> pollOptions;
	private Context context;

	public CreatePollService(String tweetMessage, ArrayList<String> pollOptions, Context context) {
		this.tweetMessage = tweetMessage;
		this.pollOptions = pollOptions;
		this.context = context;
	}

	@Override
	public boolean execute() {
		try {
			SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
	    	String userId = settings.getString("username", null);
	    	
			String tweetId = userId + Actions.getLastTweet(context);
			PublishPollPDU publishPdu = new PublishPollPDU(userId,	tweetId, tweetMessage,pollOptions);
			
			Connection.getInstance().sendPDU(publishPdu);
			Actions.incrementTweetID(context);
			
			return true;
		} catch (Exception e) {
			Log.i("DEGUB", e.getMessage());
			return false;
		}
	}
}

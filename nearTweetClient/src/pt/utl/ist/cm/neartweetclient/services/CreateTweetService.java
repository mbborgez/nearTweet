package pt.utl.ist.cm.neartweetclient.services;

import java.io.IOException;
import java.net.UnknownHostException;

import pt.utl.ist.cm.neartweetEntities.pdu.TweetPDU;
import pt.utl.ist.cm.neartweetclient.exceptions.NearTweetException;
import pt.utl.ist.cm.neartweetclient.sync.Connection;
import pt.utl.ist.cm.neartweetclient.ui.NewTweet;
import pt.utl.ist.cm.neartweetclient.utils.Actions;
import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;

public class CreateTweetService extends AsyncTask<String, Integer, Boolean> {

	private String userName;
	private String content;
	private Activity activity;
	
	public CreateTweetService(String userName, String content, Activity activity) {
		this.userName = userName;
		this.content  = content;
		this.activity = activity;
	}
	
	@Override
	protected Boolean doInBackground(String... params) {
		try {
			String lastTweetID = this.userName + Actions.getLastTweet(this.activity.getApplicationContext());
			sendTweet(lastTweetID);
		} catch(NearTweetException e) {
			e.printStackTrace();
			Log.i("NEART WEET EXCEPTION", e.getMessage());
			return false;
		}
		return true;
	}
	
	@Override
	 protected void onPostExecute(Boolean result) {
        NewTweet act = (NewTweet) this.activity;
        if (result) {
       	 act.nextScreen(); 
        } else {
       	 act.errorMessage();
        }
    }
	
	private void sendTweet(String tweetID) {
		try {
			Connection connection = Connection.getInstance();
			TweetPDU pdu = new TweetPDU(this.userName, tweetID, this.content, null);
			connection.sendPDU(pdu);
			Actions.incrementTweetID(this.activity.getApplicationContext());
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}

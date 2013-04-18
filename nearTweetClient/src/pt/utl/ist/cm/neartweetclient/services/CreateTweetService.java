package pt.utl.ist.cm.neartweetclient.services;

import pt.utl.ist.cm.neartweetEntities.pdu.TweetPDU;
import pt.utl.ist.cm.neartweetclient.exceptions.NearTweetException;
import pt.utl.ist.cm.neartweetclient.sync.Connection;
import pt.utl.ist.cm.neartweetclient.ui.NewTweetActivity;
import pt.utl.ist.cm.neartweetclient.utils.Actions;
import android.app.Activity;
import android.os.AsyncTask;

public class CreateTweetService extends AsyncTask<String, Integer, Boolean> {

	private String userName;
	private String content;
	private Activity activity;
	private byte[] tweetImageBytes;
	
	public CreateTweetService(String userName, String content, byte[] tweetImageBytes, Activity activity) {
		this.userName = userName;
		this.content  = content;
		this.activity = activity;
		this.tweetImageBytes = tweetImageBytes;
	}
	
	@Override
	protected Boolean doInBackground(String... params) {
		try {
			String lastTweetID = this.userName + Actions.getLastTweet(this.activity.getApplicationContext());
			sendTweet(lastTweetID);
		} catch(NearTweetException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	@Override
	 protected void onPostExecute(Boolean result) {
		NewTweetActivity act = (NewTweetActivity) this.activity;
        if (result) {
       	 act.nextScreen(); 
        } else {
       	 act.errorMessage();
        }
    }
	
	private void sendTweet(String tweetID) {
		try {
			
			TweetPDU pdu = new TweetPDU(this.userName, tweetID, this.content, this.tweetImageBytes);
			Connection.getInstance().sendPDU(pdu);
			Actions.incrementTweetID(this.activity.getApplicationContext());
		} catch (NearTweetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}

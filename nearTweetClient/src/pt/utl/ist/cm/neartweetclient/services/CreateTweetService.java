package pt.utl.ist.cm.neartweetclient.services;

import java.io.IOException;
import java.net.UnknownHostException;

import pt.utl.ist.cm.neartweetEntities.pdu.TweetPDU;
import pt.utl.ist.cm.neartweetclient.connectionTasks.ConnectionStatus;
import pt.utl.ist.cm.neartweetclient.exceptions.NearTweetException;
import pt.utl.ist.cm.neartweetclient.ui.NewTweet;
import android.app.Activity;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
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
			String lastTweetID = getLastTweet();
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
			ConnectionStatus connection = ConnectionStatus.getInstance();
			TweetPDU pdu = new TweetPDU(this.userName, tweetID, this.content, null);
			connection.sendPDU(pdu);
			incrementTweetID();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private String getLastTweet() {
		SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this.activity.getApplicationContext());
    	int tweetID = settings.getInt("lastTweetID", 1);
    	return this.userName + tweetID;
	}
	
	private void incrementTweetID() {
		SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this.activity.getApplicationContext());
    	int tweetID = settings.getInt("lastTweetID", 1);
    	SharedPreferences.Editor editor = settings.edit();
		editor.putInt("lastTweetID", tweetID+1);
		editor.commit();
	}

}

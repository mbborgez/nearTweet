package pt.utl.ist.cm.neartweetclient.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class Actions {
	
	public static final String POLL_VOTE = "pt.utl.ist.cm.neartweetclient.POLL_VOTE";
	public static final String POLL_VOTE_DATA = "pt.utl.ist.cm.neartweetclient.POLL_VOTE_DATA";
	public static final String SUCCESS_LOGIN = "successLogin";
	public static String BROADCAST_TWEET = "pt.utl.ist.cm.neartweetclient.BC_TWEET";
	public static String REGISTER_CONFIRMATION = "pt.utl.ist.cm.neartweetclient.REGISTER";
	public static String TWEET_DATA = "TWEET";
	
	public static int getLastTweet(Context context) {
		SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
    	return settings.getInt("lastTweetID", 1);
	}
	
	public static void incrementTweetID(Context context) {
		SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
    	int tweetID = settings.getInt("lastTweetID", 1);
    	SharedPreferences.Editor editor = settings.edit();
		editor.putInt("lastTweetID", tweetID+1);
		editor.commit();
	}

}

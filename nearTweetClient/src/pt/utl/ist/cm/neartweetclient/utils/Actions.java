package pt.utl.ist.cm.neartweetclient.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class Actions {
	
	private static final String LAST_TWEET_ID_PROPERTY = "lastTweetID";
	private static final String USERNAME_PROPERTY = "username";
	public static final String POLL_VOTE = "pt.utl.ist.cm.neartweetclient.POLL_VOTE";
	public static final String POLL_VOTE_DATA = "pt.utl.ist.cm.neartweetclient.POLL_VOTE_DATA";
	public static final String SUCCESS_LOGIN = "successLogin";
	public static final String BROADCAST_SPAMMER_BLOCKED = "pt.utl.ist.cm.neartweetclient.BROADCAST_SPAMMER_BLOCKED";
	public static final String SPAMMER_ID_DATA = "pt.utl.ist.cm.neartweetclient.SPAMMER_ID_DATA";
	public static String BROADCAST_TWEET = "pt.utl.ist.cm.neartweetclient.BC_TWEET";
	public static String REGISTER_CONFIRMATION = "pt.utl.ist.cm.neartweetclient.REGISTER";
	public static String TWEET_DATA = "TWEET";
	
	public static String getUserId(Context context){
		SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
    	return settings.getString(USERNAME_PROPERTY, null);
	}
	
	public static int getLastTweet(Context context) {
		SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
    	return settings.getInt(LAST_TWEET_ID_PROPERTY, 1);
	}
	
	public static void incrementTweetID(Context context) {
		SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
    	int tweetID = settings.getInt(LAST_TWEET_ID_PROPERTY, 1);
    	SharedPreferences.Editor editor = settings.edit();
		editor.putInt(LAST_TWEET_ID_PROPERTY, tweetID+1);
		editor.commit();
	}

}

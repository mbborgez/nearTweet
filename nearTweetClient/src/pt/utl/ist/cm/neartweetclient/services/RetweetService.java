package pt.utl.ist.cm.neartweetclient.services;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.Uri;
import android.preference.PreferenceManager;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;

public class RetweetService implements INearTweetService {
	
	/**
	 * Currently we have our consumer keys hardcoded on the application.
	 * They can be on a configuration file later on 
	 * */
	static String TWITTER_CONSUMER_KEY = "5u1RhGvI5jfC3lzBeRaMA";
	static String TWITTER_CONSUMER_SECRET = "p4FompxHFbwGOK2ktjNvQBrM81NJMzBBEyFvrp5A4Hw";

	// Dummy constants used in the hole process of authentication
	static String PREFERENCE_NAME = "twitter_oauth";
	static final String PREF_KEY_OAUTH_TOKEN = "oauth_token";
	static final String PREF_KEY_OAUTH_SECRET = "oauth_token_secret";
	static final String PREF_KEY_TWITTER_LOGIN = "isTwitterLogedIn";
	static final String TWITTER_CALLBACK_URL = "oauth://nearTweet";
	static final String URL_TWITTER_AUTH = "auth_url";
	static final String URL_TWITTER_OAUTH_VERIFIER = "oauth_verifier";
	static final String URL_TWITTER_OAUTH_TOKEN = "oauth_token";
	
	private static final String OAUTH_KEY = "userOAuthToken_";
	
	// Services attributes
	private Twitter twitter;
	private static RequestToken requestToken;
	private AccessToken accessToken;
	private String message;
	private String userId;
	private Configuration configRequest;
	SharedPreferences settings;
	
	public RetweetService(String userId, Context context) {
		this.userId = userId;
		this.settings = PreferenceManager.getDefaultSharedPreferences(context);
		
		ConfigurationBuilder builder = new ConfigurationBuilder();
		builder.setOAuthConsumerKey(TWITTER_CONSUMER_KEY);
		builder.setOAuthConsumerSecret(TWITTER_CONSUMER_SECRET);
		this.configRequest = builder.build();
		
		TwitterFactory factory = new TwitterFactory(configRequest);
		this.twitter = factory.getInstance();
	}
	
	public boolean intentFromOAuthCallback(Intent intent) {
		Uri uri = intent.getData();
		return (uri != null && uri.toString().startsWith(TWITTER_CALLBACK_URL));
	}
	
	public void setUserId(String userId) {
		this.userId = userId;
	}
	
	public void setMessage(String message) {
		this.message = message;
	}
	
	public String getRequestToken() {
		return requestToken.getToken();
	}
	
	public void setRequestToken() {
		try {
			requestToken = twitter.getOAuthRequestToken(TWITTER_CALLBACK_URL);
		} catch (TwitterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public String getPublicToken() {
		return this.settings.getString(OAUTH_KEY + this.userId + "_public", null);
	}
	
	public void cacheTweetId(String tweetId) {
		Editor editor = this.settings.edit();
		editor.putString("currentTweet", tweetId);
		editor.commit();
	}
	
	public String takeFromCacheStoredTweet() {
		return this.settings.getString("currentTweet", null);
	}
	
	public String getSecretToken() {
    	return this.settings.getString(OAUTH_KEY + this.userId + "_private", null);
    	
	}
	
	private void setAuthToken(String newPublicToken, String newSecretToken) {
		Editor editor = this.settings.edit();
		System.out.println(newPublicToken);
		System.out.println(newSecretToken);
		editor.putString(OAUTH_KEY + this.userId + "_public", newPublicToken);
		editor.putString(OAUTH_KEY + this.userId + "_private",newSecretToken);
		editor.commit();
	}
	
	public boolean userAlreadyLoggedIn() {
		return this.getPublicToken() != null;
	}
	
	public boolean handleOAuthCallback(Uri uri) {
		if(userAlreadyLoggedIn()) {
			this.accessToken = new AccessToken(this.getPublicToken(), this.getSecretToken());
		}
		
		if (uri != null && uri.toString().startsWith(TWITTER_CALLBACK_URL)) {
			try {
				String verifier = uri.getQueryParameter(URL_TWITTER_OAUTH_VERIFIER);
				this.accessToken = twitter.getOAuthAccessToken(requestToken, verifier);
				setAuthToken(accessToken.getToken(), accessToken.getTokenSecret());
				return true;
			} catch (TwitterException e) {
				e.printStackTrace();
			}
		}
		return false;
	}
	
	public String getAuthenticationUrl() {
		return requestToken.getAuthenticationURL();
	}
	
	public boolean sendTweet(String message) {
		try {
			if (!this.userAlreadyLoggedIn()) {
				return false;
			} else if(accessToken == null) {
				this.accessToken = new AccessToken(this.getPublicToken(), this.getSecretToken());
			}
			ConfigurationBuilder builder = new ConfigurationBuilder();
			builder.setOAuthConsumerKey(TWITTER_CONSUMER_KEY);
			builder.setOAuthConsumerSecret(TWITTER_CONSUMER_SECRET);
			this.twitter = new TwitterFactory(this.configRequest).getInstance(this.accessToken);
			this.twitter.updateStatus(message);
			return true;
		} catch (TwitterException e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public boolean execute() {
		if (!this.userAlreadyLoggedIn()) {
			return false;
		} else {
			this.accessToken = new AccessToken(this.getPublicToken(), this.getSecretToken());
		}
		try {
			ConfigurationBuilder builder = new ConfigurationBuilder();
			builder.setOAuthConsumerKey(TWITTER_CONSUMER_KEY);
			builder.setOAuthConsumerSecret(TWITTER_CONSUMER_SECRET);
			Twitter twitter = new TwitterFactory(this.configRequest).getInstance(this.accessToken);
			twitter4j.Status response = twitter.updateStatus(this.message);
			return (response != null);
		} catch (TwitterException e) {
			e.printStackTrace();
		}
		return false;
	}

}

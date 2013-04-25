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
	static final String PREF_KEY_OAUTH_TOKEN = "oauth_token_";
	static final String PREF_KEY_OAUTH_SECRET = "oauth_token_secret_";
	static final String URL_TWITTER_OAUTH_VERIFIER = "oauth_verifier";
	
	// warning - if you change this callback URL you must change to 
	// manifesto intent reference as well
	static final String TWITTER_CALLBACK_URL = "oauth://nearTweet";
	
	// Services attributes
	private Twitter twitter;
	
	/**
	 * We want the attribute stored statically for callback reasons.
	 * When the callback happens if requestToken was volatile 
	 * twitter will raise an exception. 
	 * We have to generate this token once 
	 */
	private static RequestToken requestToken;
	private AccessToken accessToken;
	private String message;
	private String userId;
	private Configuration configRequest;
	SharedPreferences settings;
	
	/**
	 * Constructor of Retweet Service - This Service handle communication with Twitter API
	 * when the user of nearTweet wants to make a retweet on the APP
	 * @param userId
	 * @param context
	 */
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
	
	/**
	 * Updates the status on the twitter with the message from RETWEET
	 * @return true if the post was successful, false otherwise
	 */
	@Override
	public boolean execute() {
		if (this.message == null || !this.userAlreadyLoggedIn()) {
			return false;
		} 
		
		if (this.accessToken == null) {
			this.accessToken = new AccessToken(this.getPublicToken(), this.getSecretToken());
		}
		
		try {
			this.twitter = new TwitterFactory(this.configRequest).getInstance(this.accessToken);
			twitter4j.Status response = twitter.updateStatus(this.message);
			return (response != null);
		} catch (TwitterException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	/**
	 * Checks if the intent came from the OAuth protocol or just from some other 
	 * activity on the nearTweet Application
	 * @param intent - the content of intent can have the url with token request
	 * @return true if intent origin is from OAuth Request, 
	 * false otherwise
	 */
	public boolean intentFromOAuthCallback(Intent intent) {
		Uri uri = intent.getData();
		return (uri != null && uri.toString().startsWith(TWITTER_CALLBACK_URL));
	}
	
	/**
	 * checks if the user logged on has the access token cached on the storage or not
	 * @return true if user has the access token saved, false otherwise
	 */
	public boolean userAlreadyLoggedIn() {
		return (this.getPublicToken() != null && this.getSecretToken() != null);
	}
	
	/**
	 * Getter of Authentication URL
	 * @return returns the Authentication URL for the future handshake
	 */
	public String getAuthenticationUrl() {
		return requestToken.getAuthenticationURL();
	}
	
	/**
	 * Setter of Message to Send
	 * @param message - the message that user wants to send to twitter
	 */
	public void setMessage(String message) {
		this.message = message;
	}
	
	/**
	 * Setter of RequestToken - requestToken must be initialized just once
	 * twitter Wrapper has problems in the connection with twitter API if this
	 * parameter changes more than once
	 * @throws TwitterException
	 */
	public void setRequestToken() throws TwitterException {
		if (requestToken == null) {
			requestToken = twitter.getOAuthRequestToken(TWITTER_CALLBACK_URL);
		}
	}
	
	/**
	 * handleOAuthCallback is responsible to generate the access token
	 * with the arguments from Callback URL and store the generated token
	 * on the global configurations
	 * @param uri - has important parameters like the verifier to generate the access token
	 * @return returns true if the handle went fine, false otherwise
	 */
	public boolean handleOAuthCallback(Uri uri) {
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
	
	/**
	 * getPublicToken - returns the first part of the pair to construct a valid access token
	 * @return the public oauth token or null
	 */
	private String getPublicToken() {
		return this.settings.getString(PREF_KEY_OAUTH_TOKEN + this.userId, null);
	}
	
	/**
	 * getPrivateToken - returns the second part of the pair to construct a valid access token
	 * @return the secret oauth token or null
	 */
	private String getSecretToken() {
    	return this.settings.getString(PREF_KEY_OAUTH_SECRET + this.userId, null);
	}
	
	/**
	 * responsible for cache the access token for future usage. It stores the access token
	 * with the key suffix userId 
	 * @param newPublicToken
	 * @param newSecretToken
	 */
	private void setAuthToken(String newPublicToken, String newSecretToken) {
		Editor editor = this.settings.edit();
		editor.putString(PREF_KEY_OAUTH_TOKEN + this.userId, newPublicToken);
		editor.putString(PREF_KEY_OAUTH_SECRET + this.userId,newSecretToken);
		editor.commit();
	}
}

package pt.utl.ist.cm.neartweetclient.ui;

import pt.utl.ist.cm.neartweetEntities.pdu.TweetPDU;
import pt.utl.ist.cm.neartweetclient.R;
import pt.utl.ist.cm.neartweetclient.core.MemCacheProvider;
import pt.utl.ist.cm.neartweetclient.services.RetweetService;
import pt.utl.ist.cm.neartweetclient.sync.HTTPConnection;
import pt.utl.ist.cm.neartweetclient.utils.Actions;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;
import android.app.ListActivity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class RetweetActivity extends ListActivity {
	
	public static final String TWEET_ID_EXTRA = "tweet_item";
	private TextView tweetDetailsTextView;
	private ImageView tweetImage;

	private Button retweetButton;

	private String tweetId;
	private TweetPDU tweetPdu;
	private RetweetService service;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_retweet);
		
		service = new RetweetService(Actions.getUserId(getApplicationContext()), 
				getApplicationContext());
		
		HTTPConnection connection = new HTTPConnection(getApplicationContext());
		if (!connection.isConnectingToInternet()) {
			Toast.makeText(getApplicationContext(), 
					"There are no internet connection. We cannot Proceed!",
					Toast.LENGTH_LONG).show();
			finish();
		}
		
		// first check if the user have already token to log on
		
		
		if (service.intentFromOAuthCallback(getIntent())) {
			service.handleOAuthCallback(getIntent().getData());
			populateView();
			
		} if(service.userAlreadyLoggedIn()) {
			populateView();
		} else {
			try {
				service.setRequestToken();
				service.cacheTweetId(getIntent().getStringExtra(TWEET_ID_EXTRA));
				startActivity(new Intent(Intent.ACTION_VIEW, 
						Uri.parse(service.getAuthenticationUrl())));
			} catch (TwitterException e) {
				Toast.makeText(getApplicationContext(), 
						"Something went wrond during the connection", 
						Toast.LENGTH_LONG).show();
				finish();
			}
		}
	}
	
	private void populateView() {
		tweetDetailsTextView = (TextView) findViewById(R.id.retweet_details_text);
		tweetImage = (ImageView) findViewById(R.id.retweet_details_image);
		retweetButton = (Button) findViewById(R.id.submitRetweetButton);
		retweetButton.setOnClickListener(retweetClickListener);
		tweetId = service.takeFromCacheStoredTweet();
		tweetPdu = (TweetPDU) MemCacheProvider.getTweet(tweetId);
		populateTweetDetails();
	}
	
	private void populateTweetDetails() {
		tweetDetailsTextView.setText(tweetPdu.GetText());
		if(tweetPdu.GetMediaObject()!=null && tweetPdu.GetMediaObject().length>0){
			Bitmap bitmap = BitmapFactory.decodeByteArray(tweetPdu.GetMediaObject(), 0, tweetPdu.GetMediaObject().length);
			tweetImage.setImageBitmap(bitmap);
			tweetImage.setVisibility(View.VISIBLE);
		} else {
			tweetImage.setVisibility(View.GONE);
		}
	}
	
	private final OnClickListener retweetClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			System.out.println("I Will implement Tweet feature tomorrow :)!"); 
		}
	};
}

package pt.utl.ist.cm.neartweetclient;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.widget.TextView;

public class TweetDetailsAcitivity extends Activity {

	public static final String TWEET_ID = "pt.utl.ist.cm.neartweetclient.TweetDetailsAcitivity.tweet_id";
	
	TextView tweetDetailsTextView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tweet_details_acitivity);
		
		String tweetId = getIntent().getStringExtra(TWEET_ID);
		if(tweetId.length()>0){
			tweetDetailsTextView = (TextView) findViewById(R.id.tweet_details_text);
			tweetDetailsTextView.setText(tweetId);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.tweet_details_acitivity, menu);
		return true;
	}

}

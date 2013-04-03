package pt.utl.ist.cm.neartweetclient.ui;

import pt.utl.ist.cm.neartweetclient.R;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
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
			// IDEIA: ter um servi�o que vai buscar os dados do tweet pelo id
			// e popular o ecra com essa informa�ao.
			tweetDetailsTextView.setText(tweetId);
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle item selection
	    switch (item.getItemId()) {
	        case R.id.new_tweet_button:
	            showTweetScreen();
	            return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.tweets_stream, menu);
		return true;
	}
	
	
	private void showTweetScreen() {
		startActivity(new Intent(this, NewTweet.class));
	}
}

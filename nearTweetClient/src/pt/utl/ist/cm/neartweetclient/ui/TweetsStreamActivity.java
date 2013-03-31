package pt.utl.ist.cm.neartweetclient.ui;

import pt.utl.ist.cm.neartweetclient.R;
import pt.utl.ist.cm.neartweetclient.utils.UiMessages;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.app.ListActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class TweetsStreamActivity extends ListActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		greetingUser();
		
		// storing string resources into Array
		// tmp variable
		final String[] tweetsList = new String[]{"t1", "t2", "t3", "t4", "t5", "t5", "t5", "t5", "t5", "t5", "t5", "t5", "t5", "t5", "t5", "t5", "t5", "t5", "t5", "t5", "t5", "t5","t5", "t5", "t5", "t5", "t5", "t5", "t5", "t5", "t5"};
		this.setListAdapter(new ArrayAdapter<String>(this, R.layout.activity_tweets_stream, 
				R.id.tweets_stream_text, 
				tweetsList));

		ListView tweetsListView = getListView();
		tweetsListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				//enviar o ID do tweet que foi clicado
				showTweetDetails(tweetsList[position]);
			}
			
		});
	}
	
	private void greetingUser() {
		SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
		String greeting = String.format(UiMessages.WELCOME_MESSAGE,settings.getString("username", null));
    	Toast.makeText(this, greeting, Toast.LENGTH_LONG).show();
	}
	
	protected void showTweetDetails(String tweetId) {
		Intent tweetDetailsIntent = new Intent(this, TweetDetailsAcitivity.class);
		tweetDetailsIntent.putExtra(TweetDetailsAcitivity.TWEET_ID, tweetId);
		startActivity(tweetDetailsIntent);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.tweets_stream, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    switch (item.getItemId()) {
	        case R.id.new_tweet_button:
	            showTweetScreen();
	            return true;
	        case R.id.new_pool_button:
	            showPollScreen();
	            return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}
	
	private void showTweetScreen() {
		startActivity(new Intent(this, NewTweet.class));
	}
	
	private void showPollScreen() {
		startActivity(new Intent(this, CreatePollActivity.class));
	}
}

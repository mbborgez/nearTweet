package pt.utl.ist.cm.neartweetclient.ui;

import java.util.ArrayList;

import pt.utl.ist.cm.neartweetclient.R;
import pt.utl.ist.cm.neartweetclient.connectionTasks.StreamingHandler;
import pt.utl.ist.cm.neartweetclient.connectionTasks.TweetsReceiver;
import pt.utl.ist.cm.neartweetclient.utils.UiMessages;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.app.ListActivity;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
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
	
	/* Handles the incoming tweets */
	private BroadcastReceiver streammingReceiver;
	private ArrayList<String> tweets;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		greetingUser();
		tweets = new ArrayList<String>();
		Thread t = new Thread(new StreamingHandler(this));
		t.start();
		
		streammingReceiver = new TweetsReceiver(this);
	    IntentFilter filter = new IntentFilter();
        filter.addAction("new_tweets");
        registerReceiver(streammingReceiver, filter);
        
	    setListAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_list_item_single_choice,
	            android.R.id.text1, this.tweets));
		ListView tweetsListView = getListView();
		tweetsListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				showTweetDetails("tw");
			}
			
		});
	}
	
	@Override
    protected void onResume() {
        IntentFilter filter = new IntentFilter();
        filter.addAction("new_tweets");
        registerReceiver(streammingReceiver, filter);
        super.onResume();
    }

    @Override
    protected void onPause() {
        unregisterReceiver(streammingReceiver);
        super.onPause();
    }
	
	/**
	 * After User Login the system should greeting him with a Toast Message!
	 */
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
	
	public void updateList(String newTweet) {
		tweets.add(newTweet);
        @SuppressWarnings("unchecked")
		ArrayAdapter<String> a = (ArrayAdapter<String>) getListView().getAdapter();
        a.notifyDataSetChanged();
	}
}

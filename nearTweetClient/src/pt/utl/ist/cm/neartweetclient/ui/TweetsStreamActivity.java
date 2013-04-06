package pt.utl.ist.cm.neartweetclient.ui;

import java.util.ArrayList;
import pt.utl.ist.cm.neartweetclient.R;
import pt.utl.ist.cm.neartweetclient.sync.StreamingHandler;
import pt.utl.ist.cm.neartweetclient.utils.Actions;
import pt.utl.ist.cm.neartweetclient.utils.UiMessages;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.app.ListActivity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Toast;

public class TweetsStreamActivity extends ListActivity {
	
	private ArrayList<String> tweets;
	BroadcastReceiver tweetsReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			if (intent.getAction().equals(Actions.NEW_TWEET)) {
				String tweet = intent.getStringExtra("tweet");
				updateList(tweet);
			}
			
		}
    };
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		greetingUser();
		tweets = new ArrayList<String>();
	    setListAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_list_item_single_choice,
	            android.R.id.text1, this.tweets));
	    
		// Start listening the socket for future tweets
		AsyncTask<Void, Void, Void> backgroundTask = new StreamingHandler(this.getApplicationContext());
		backgroundTask.execute();
		
		IntentFilter iff = new IntentFilter();
        iff.addAction(Actions.NEW_TWEET);
        // Put whatever message you want to receive as the action
        this.registerReceiver(this.tweetsReceiver,iff);
	}
	
	@Override
    public void onResume() {
        super.onResume();
        IntentFilter iff = new IntentFilter();
        iff.addAction(Actions.NEW_TWEET);
        // Put whatever message you want to receive as the action
        this.registerReceiver(this.tweetsReceiver,iff);
    }
    @Override
    public void onPause() {
        super.onPause();
        this.unregisterReceiver(this.tweetsReceiver);
    }
	
	/**
	 * After User Login the system should greeting him with a Toast Message!
	 */
	private void greetingUser() {
		SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
		String greeting = String.format(UiMessages.WELCOME_MESSAGE,settings.getString("username", null));
    	Toast.makeText(this, greeting, Toast.LENGTH_LONG).show();
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

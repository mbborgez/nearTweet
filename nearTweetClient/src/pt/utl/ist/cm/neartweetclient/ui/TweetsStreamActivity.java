package pt.utl.ist.cm.neartweetclient.ui;

import java.util.ArrayList;

import pt.utl.ist.cm.neartweetEntities.pdu.PDU;
import pt.utl.ist.cm.neartweetEntities.pdu.PublishPollPDU;
import pt.utl.ist.cm.neartweetEntities.pdu.TweetPDU;
import pt.utl.ist.cm.neartweetclient.MemCacheProvider;
import pt.utl.ist.cm.neartweetclient.R;
import pt.utl.ist.cm.neartweetclient.core.TweetAdapter;
import pt.utl.ist.cm.neartweetclient.utils.Actions;
import pt.utl.ist.cm.neartweetclient.utils.UiMessages;
import android.app.ListActivity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

public class TweetsStreamActivity extends ListActivity {
	
	private TweetAdapter tweetAdapter;
	private ArrayList<PDU> list;
	Button createTweetButton;
	Button createPollButton;
	
	BroadcastReceiver tweetsReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			Log.i("DEGUB", "RECEIVED SOMETHING");
			if (intent.getAction().equals(Actions.BROADCAST_TWEET)) {
				Log.i("DEBUG", "New Tweet");
				String tweet = intent.getStringExtra(Actions.TWEET_DATA);
				updateList(tweet);
			}
			if (intent.getAction().equals(Actions.POLL_VOTE)) {
				Log.i("DEBUG", "NEW VOTE");
				Toast.makeText(getApplicationContext(), "NEW VOTE ", Toast.LENGTH_LONG).show();
			}
			
		}
    };
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_tweets_stream);
		
		greetingUser();
		
		list = new ArrayList<PDU>();
		tweetAdapter = new TweetAdapter(this.getApplicationContext(), R.layout.tweet_layout, list);
	    setListAdapter(this.tweetAdapter);
		
		IntentFilter iff = new IntentFilter();
        iff.addAction(Actions.BROADCAST_TWEET);
        iff.addAction(Actions.POLL_VOTE);
        // Put whatever message you want to receive as the action
        this.registerReceiver(this.tweetsReceiver,iff);
        
        // Start listening income tweets from current connection
        createTweetButton = (Button) findViewById(R.id.createTweet);
        createTweetButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
					startActivity(new Intent(getApplicationContext(), NewTweetActivity.class));
			}
		});
        
        createPollButton = (Button) findViewById(R.id.createPoll);
        createPollButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				startActivity(new Intent(getApplicationContext(), CreatePollActivity.class));
			}
		});
        
		ListView tweetsListView = getListView();
		tweetsListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Log.i("DEBUG", "TweetStreamActivity.clickItem!!!!");
				showTweetDetails(list.get(position));
			}
		});
	}
	
	@Override
    public void onResume() {
        super.onResume();
        IntentFilter iff = new IntentFilter();
        iff.addAction(Actions.BROADCAST_TWEET);
        iff.addAction(Actions.POLL_VOTE);
        // Put whatever message you want to receive as the action
        this.registerReceiver(this.tweetsReceiver,iff);
        
        //Update all the PDUS on the Adapter
        if (!MemCacheProvider.isEmpty()) {
	        list = MemCacheProvider.toArrayList();
	        tweetAdapter = new TweetAdapter(this, R.layout.tweet_layout, list);
	        setListAdapter(tweetAdapter);
        }
        
    }
    @Override
    public void onPause() {
        super.onPause();
        this.unregisterReceiver(this.tweetsReceiver);
    }
    
    protected void showTweetDetails(PDU pdu) {
    	if (pdu instanceof TweetPDU) {
    		Intent tweetDetailsIntent = new Intent(this, TweetDetailsActivity.class);
    		tweetDetailsIntent.putExtra("tweet_item", ((TweetPDU) pdu).GetTweetId());
    		startActivity(tweetDetailsIntent);
    	} else if (pdu instanceof PublishPollPDU) {
    		PublishPollPDU publishPollPdu = (PublishPollPDU) pdu;
    		if(!MemCacheProvider.isMyPoll(publishPollPdu.GetTweetId())){
	    		Intent tweetDetailsIntent = new Intent(this, PollVoteActivity.class);
	    		tweetDetailsIntent.putExtra(PollVoteActivity.TWEET_ID_EXTRA, ((PublishPollPDU) pdu).GetTweetId());
	    		startActivity(tweetDetailsIntent);
    		} else {
	    		Intent tweetDetailsIntent = new Intent(this, PollVotesDetailsActivity.class);
	    		tweetDetailsIntent.putExtra(PollVotesDetailsActivity.TWEET_ID_EXTRA, ((PublishPollPDU) pdu).GetTweetId());
	    		startActivity(tweetDetailsIntent);
    		}
    	}
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
		startActivity(new Intent(this, NewTweetActivity.class));
	}
	
	private void showPollScreen() {
		startActivity(new Intent(this, CreatePollActivity.class));
	}
	
	public void updateList(String newTweet) {
		list.add(0, MemCacheProvider.getTweet(newTweet));
		tweetAdapter = new TweetAdapter(getApplicationContext(), R.layout.tweet_layout, list);
        setListAdapter(tweetAdapter);
	}
}

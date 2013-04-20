package pt.utl.ist.cm.neartweetclient.ui;

import pt.utl.ist.cm.neartweetEntities.pdu.PDU;
import pt.utl.ist.cm.neartweetEntities.pdu.PublishPollPDU;
import pt.utl.ist.cm.neartweetEntities.pdu.TweetPDU;
import pt.utl.ist.cm.neartweetclient.R;
import pt.utl.ist.cm.neartweetclient.core.MemCacheProvider;
import pt.utl.ist.cm.neartweetclient.core.TweetStreamAdapter;
import pt.utl.ist.cm.neartweetclient.utils.Actions;
import pt.utl.ist.cm.neartweetclient.utils.UiMessages;
import android.app.ListActivity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
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
	
	private Button createTweetButton;
	private Button createPollButton;
	
	private TweetStreamAdapter tweetStreamAdapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tweets_stream);
		
		showGreetingUser();
		
		tweetStreamAdapter = new TweetStreamAdapter(getApplicationContext(), R.layout.tweet_layout, MemCacheProvider.getTweetsStream());
        setListAdapter(tweetStreamAdapter);
		
        createTweetButton = (Button) findViewById(R.id.createTweet);
        createPollButton = (Button) findViewById(R.id.createPoll);

        createTweetButton.setOnClickListener(createTweetClickListener);
		createPollButton.setOnClickListener(createPollClickListener);
	    
		ListView tweetsListView = getListView();
		tweetsListView.setOnItemClickListener(tweetsStreamItemClickListener);
		
		
	    // Put whatever message you want to receive as the action
		IntentFilter iff = new IntentFilter();
        iff.addAction(Actions.BROADCAST_TWEET);
        iff.addAction(Actions.POLL_VOTE);
        iff.addAction(Actions.BROADCAST_SPAMMER_BLOCKED);
        this.registerReceiver(tweetsReceiver, iff);
	}

	@Override
    public void onResume() {
        super.onResume();

        // Put whatever message you want to receive as the action
        IntentFilter iff = new IntentFilter();
        iff.addAction(Actions.BROADCAST_TWEET);
        iff.addAction(Actions.POLL_VOTE);
        this.registerReceiver(this.tweetsReceiver,iff);
        
        //Update all the PDUS on the Adapter
        tweetStreamAdapter.notifyDataSetChanged();
        
    }
    @Override
    public void onPause() {
        super.onPause();
        this.unregisterReceiver(this.tweetsReceiver);
    }
    
    protected void showTweetDetails(PDU pdu) {
    	if (pdu instanceof TweetPDU) {
    		showTweetDetailsScreen((TweetPDU) pdu);
    	} else if (pdu instanceof PublishPollPDU) {
    		PublishPollPDU publishPollPdu = (PublishPollPDU) pdu;
    		if(!MemCacheProvider.isMyPoll(publishPollPdu.GetTweetId())){
	    		showPollVoteScreen(publishPollPdu);
    		} else {
	    		showPollDetailsScreen(publishPollPdu);
    		}
    	}
	}

	private void showTweetDetailsScreen(TweetPDU pdu) {
		Intent tweetDetailsIntent = new Intent(this, TweetDetailsActivity.class);
		tweetDetailsIntent.putExtra(TweetDetailsActivity.TWEET_ID_EXTRA, pdu.GetTweetId());
		startActivity(tweetDetailsIntent);
	}

	private void showPollDetailsScreen(PublishPollPDU pdu) {
		Intent tweetDetailsIntent = new Intent(this, PollVotesDetailsActivity.class);
		tweetDetailsIntent.putExtra(PollVotesDetailsActivity.TWEET_ID_EXTRA,  pdu.GetTweetId());
		startActivity(tweetDetailsIntent);
	}

	private void showPollVoteScreen(PublishPollPDU pdu) {
		Intent tweetDetailsIntent = new Intent(this, PollVoteActivity.class);
		tweetDetailsIntent.putExtra(PollVoteActivity.TWEET_ID_EXTRA,  pdu.GetTweetId());
		startActivity(tweetDetailsIntent);
	}
	
	/**
	 * After User Login the system should greeting him with a Toast Message!
	 */
	private void showGreetingUser() {
		String greeting = String.format(UiMessages.WELCOME_MESSAGE, Actions.getUserId(getApplicationContext()));
    	Toast.makeText(this, greeting, Toast.LENGTH_LONG).show();
	}
	
	private void showNewTweetScreen() {
		startActivity(new Intent(this, NewTweetActivity.class));
	}
	
	private void showCreatePollScreen() {
		startActivity(new Intent(this, CreatePollActivity.class));
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.tweets_stream, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    switch (item.getItemId()) {
	        case R.id.new_tweet_button:
	            showNewTweetScreen();
	            return true;
	        case R.id.new_pool_button:
	            showCreatePollScreen();
	            return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}
	
	/****************************************************************************
	 *************************** Click Listeners ********************************
	 ****************************************************************************/

    private final OnClickListener createTweetClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
				startActivity(new Intent(getApplicationContext(), NewTweetActivity.class));
		}
	};
	
	private final OnClickListener createPollClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			startActivity(new Intent(getApplicationContext(), CreatePollActivity.class));
		}
	};
	
	private final OnItemClickListener tweetsStreamItemClickListener = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			showTweetDetails(MemCacheProvider.getTweetsStream().get(position));
		}
	};
	
	private final BroadcastReceiver tweetsReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			if (intent.getAction().equals(Actions.BROADCAST_TWEET)) {
				tweetStreamAdapter.notifyDataSetChanged();	
				Toast.makeText(getApplicationContext(), "Received a new tweet", Toast.LENGTH_SHORT).show();
			}
			if (intent.getAction().equals(Actions.POLL_VOTE)) {
				Toast.makeText(getApplicationContext(), "Received a new Vote", Toast.LENGTH_SHORT).show();
			}
			if (intent.getAction().equals(Actions.BROADCAST_SPAMMER_BLOCKED)) {
				String spammerId = intent.getExtras().getString(Actions.SPAMMER_ID_DATA);
				Toast.makeText(getApplicationContext(), "The spammer " + spammerId + " is now blocked", Toast.LENGTH_SHORT).show();
			}
		}
    };
}

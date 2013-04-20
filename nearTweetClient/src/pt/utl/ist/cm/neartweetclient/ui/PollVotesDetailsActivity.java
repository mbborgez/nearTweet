package pt.utl.ist.cm.neartweetclient.ui;

import java.util.Map;

import pt.utl.ist.cm.neartweetclient.R;
import pt.utl.ist.cm.neartweetclient.core.MemCacheProvider;
import pt.utl.ist.cm.neartweetclient.utils.Actions;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

public class PollVotesDetailsActivity extends Activity {

	public static final String TWEET_ID_EXTRA = "tweet_id";
	private LinearLayout votesLinearLayout;
	TextView pollDetailsEditText;
	private String tweetId;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_poll_votes_details);

		tweetId = getIntent().getStringExtra(TWEET_ID_EXTRA);

		pollDetailsEditText = (TextView) findViewById(R.id.pollVotes_description_textView);
		votesLinearLayout = (LinearLayout) findViewById(R.id.pollVotes_linearLayout);

		populateVotes(MemCacheProvider.getVotesForPoll(tweetId));

		registerConversationListener();
	}

	private void registerConversationListener() {
		IntentFilter iff = new IntentFilter();
		iff.addAction(Actions.BROADCAST_TWEET);
		this.registerReceiver(this.repliesReceiver,iff);
	}
	
	@Override
    public void onResume() {
        super.onResume();

        registerConversationListener();
        populateVotes(MemCacheProvider.getVotesForPoll(tweetId));
    }
	
    @Override
    public void onPause() {
        super.onPause();
        this.unregisterReceiver(repliesReceiver);
    }

	private void populateVotes(Map<String, Integer> votes){
		for(java.util.Map.Entry<String, Integer> voteEntry : votes.entrySet()){
			TextView pollVoteTextLine = new EditText(getApplicationContext());
			pollVoteTextLine.setTextColor(Color.BLACK);
			pollVoteTextLine.setText(voteEntry.getKey() + " - " + voteEntry.getValue());
			votesLinearLayout.addView(pollVoteTextLine);
		}
	}

	private BroadcastReceiver repliesReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			Log.i("DEGUB", "RECEIVED SOMETHING");
			if (intent.getAction().equals(Actions.BROADCAST_TWEET)) {
				populateVotes(MemCacheProvider.getVotesForPoll(tweetId));
			}
		}
	};
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.poll_votes_details, menu);
		return true;
	}

}

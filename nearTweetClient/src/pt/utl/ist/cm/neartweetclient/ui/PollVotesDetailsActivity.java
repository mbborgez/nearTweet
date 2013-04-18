package pt.utl.ist.cm.neartweetclient.ui;

import java.util.Map;

import pt.utl.ist.cm.neartweetEntities.pdu.PublishPollPDU;
import pt.utl.ist.cm.neartweetclient.MemCacheProvider;
import pt.utl.ist.cm.neartweetclient.R;
import pt.utl.ist.cm.neartweetclient.R.color;
import android.app.Activity;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.os.DropBoxManager.Entry;
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
	}

	private void populateVotes(Map<String, Integer> votes){
		for(java.util.Map.Entry<String, Integer> voteEntry : votes.entrySet()){
			TextView pollVoteTextLine = new EditText(getApplicationContext());
			pollVoteTextLine.setTextColor(Color.BLACK);
			pollVoteTextLine.setText(voteEntry.getKey() + " - " + voteEntry.getValue());
			votesLinearLayout.addView(pollVoteTextLine);
		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.poll_votes_details, menu);
		return true;
	}

}

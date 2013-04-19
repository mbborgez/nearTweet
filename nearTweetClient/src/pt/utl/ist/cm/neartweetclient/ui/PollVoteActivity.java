package pt.utl.ist.cm.neartweetclient.ui;

import pt.utl.ist.cm.neartweetEntities.pdu.PublishPollPDU;
import pt.utl.ist.cm.neartweetclient.MemCacheProvider;
import pt.utl.ist.cm.neartweetclient.R;
import pt.utl.ist.cm.neartweetclient.core.PollVotesAdapter;
import pt.utl.ist.cm.neartweetclient.services.PollVoteService;
import android.app.ListActivity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class PollVoteActivity extends ListActivity {

	public static final String TWEET_ID_EXTRA = "tweet_item";
	private TextView pollDescriptionTextView;
	private Button pollVoteButton;
	private PublishPollPDU pollPdu;
	
	private int selectedVoteIndex;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_poll_details);

		pollDescriptionTextView = (TextView) findViewById(R.id.poll_description_textView);
		pollVoteButton = (Button) findViewById(R.id.poll_submitVote_button);
		
		pollPdu = (PublishPollPDU) MemCacheProvider.getTweet(getIntent().getExtras().getString(TWEET_ID_EXTRA));

		pollDescriptionTextView.setText(pollPdu.GetText());
		pollVoteButton.setOnClickListener(createSubmitPollVoteClickListener());
		setListAdapter(new PollVotesAdapter(getApplicationContext(),  R.layout.poll_option_layout, pollPdu.GetOptions()));
	}
	
	/*****************************************************************************
	 ***************************** Click Listeners *******************************
	 *****************************************************************************/

	private final OnClickListener createSubmitPollVoteClickListener(){
		return new OnClickListener() {
			@Override
			public void onClick(View v) {
				(new SubmitPollVoteTask(selectedVoteIndex)).execute();
			}
		};
	}
	
    @Override 
    public void onListItemClick(ListView l, View v, int position, long id) {
        selectedVoteIndex = position;
    }
	
	/*****************************************************************************
	 ******************************** Async Tasks ********************************
	 *****************************************************************************/
	private class SubmitPollVoteTask extends AsyncTask<String, Void, Boolean> {
		private int pollVoteIndex;

		public SubmitPollVoteTask(int pollVoteIndex){
			this.pollVoteIndex = pollVoteIndex;
		}

		@Override
		protected Boolean doInBackground(String... params) {
			return (new PollVoteService(pollPdu.GetUserId(), pollPdu.GetTweetId(), pollVoteIndex, getApplicationContext())).execute();
		}

		@Override
		protected void onPostExecute(Boolean result) {
			if(result){
				showSuccessMessage();
				finish();
			} else {
				showErrorMessage();
			}
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.poll_details, menu);
		return true;
	}

	/*****************************************************************************
	 ********************************* Messages **********************************
	 *****************************************************************************/
	private void showSuccessMessage() {
		Toast.makeText(this, "Submitted: vote in " + pollPdu.GetOptions().get(selectedVoteIndex), Toast.LENGTH_LONG).show();
	}

	public void showErrorMessage() {
		Toast.makeText(getApplicationContext(), "Error sending poll vote", Toast.LENGTH_SHORT).show();
	}

}

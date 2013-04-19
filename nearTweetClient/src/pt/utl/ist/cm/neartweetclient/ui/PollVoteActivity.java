package pt.utl.ist.cm.neartweetclient.ui;

import java.util.ArrayList;

import pt.utl.ist.cm.neartweetEntities.pdu.PublishPollPDU;
import pt.utl.ist.cm.neartweetclient.MemCacheProvider;
import pt.utl.ist.cm.neartweetclient.R;
import pt.utl.ist.cm.neartweetclient.services.PollVoteService;
import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

public class PollVoteActivity extends Activity {

	public static final String TWEET_ID_EXTRA = "tweet_item";
	private TextView pollDescriptionTextView;
	private Button pollVoteButton;
	private RadioGroup pollOptions;
	private RadioButton selectedPollVoteButton;
	private PublishPollPDU pollPdu;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_poll_details);

		pollDescriptionTextView = (TextView) findViewById(R.id.poll_description_textView);
		pollVoteButton = (Button) findViewById(R.id.poll_submitVote_button);
		pollOptions = (RadioGroup) findViewById(R.id.pollDetails_vote_options);

		pollPdu = (PublishPollPDU) MemCacheProvider.getTweet(getIntent().getExtras().getString(TWEET_ID_EXTRA));

		pollDescriptionTextView.setText(pollPdu.GetText());
		pollVoteButton.setOnClickListener(submitPollVoteClickListener);
		
		populatePollVoteOptions(pollPdu.GetOptions());

	}
	
	private void populatePollVoteOptions(ArrayList<String> pollOptionsText) {
		for (int voteIndex=0; voteIndex<pollOptionsText.size(); ++voteIndex){
			RadioButton radioButton = new RadioButton(getApplicationContext());
			radioButton.setText(pollOptionsText.get(voteIndex));

			radioButton.setOnClickListener(pollSelectionClickListener);
			pollOptions.addView(radioButton);
		}
		pollOptions.setSelected(false);
		pollVoteButton.setEnabled(false);
	}
	
	/*****************************************************************************
	 ***************************** Click Listeners *******************************
	 *****************************************************************************/
	
	private final OnClickListener submitPollVoteClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			(new SubmitPollVoteTask(pollPdu.GetOptions().lastIndexOf(getSelectedPollVoteText()))).execute();
		}
	};
	
	private final OnClickListener pollSelectionClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			selectedPollVoteButton = ((RadioButton) v);
			pollVoteButton.setEnabled(true);
		}
	};


	
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
			return (new PollVoteService(pollPdu.GetTweetId(), pollVoteIndex, getApplicationContext())).execute();
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
		Toast.makeText(this, "Submitted: " + selectedPollVoteButton.getText().toString(), Toast.LENGTH_LONG).show();
	}
	
	public void showErrorMessage() {
		Toast.makeText(getApplicationContext(), "Error sending poll vote", Toast.LENGTH_SHORT).show();
	}
	
	private String getSelectedPollVoteText() {
		return selectedPollVoteButton.getText().toString();
	}
}

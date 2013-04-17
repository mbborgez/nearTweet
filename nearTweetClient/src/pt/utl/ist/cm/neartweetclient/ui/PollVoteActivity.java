package pt.utl.ist.cm.neartweetclient.ui;

import java.util.ArrayList;

import pt.utl.ist.cm.neartweetEntities.pdu.PublishPollPDU;
import pt.utl.ist.cm.neartweetclient.MemCacheProvider;
import pt.utl.ist.cm.neartweetclient.R;
import pt.utl.ist.cm.neartweetclient.services.PollVoteService;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.app.Activity;
import android.content.SharedPreferences;
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
	private PublishPollPDU pdu;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_poll_details);

		ArrayList<String> pollOptionsTexts = null;
		String descriptionText = null;
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			pdu = (PublishPollPDU) MemCacheProvider.getTweet(extras.getString("tweet_item"));
			descriptionText = pdu.GetText();
			pollOptionsTexts = pdu.GetOptions();
		}

		pollDescriptionTextView = (TextView) findViewById(R.id.poll_description_textView);
		pollVoteButton = (Button) findViewById(R.id.poll_submitVote_button);
		pollOptions = (RadioGroup) findViewById(R.id.pollDetails_vote_options);

		pollDescriptionTextView.setText(descriptionText);
		createPollVoteOptions(pollOptionsTexts);
		pollVoteButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				submitVote(selectedPollVoteButton);
			}
		});

	}

	protected void submitVote(RadioButton selectedPollVoteButton) {
		SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
    	String username = settings.getString("username", null);
    	int index = pdu.GetOptions().lastIndexOf(selectedPollVoteButton.getText().toString());
		PollVoteService service = new PollVoteService(username,pdu.GetTweetId(), index, this);
		service.execute();
		System.out.println(pdu.GetOptions().lastIndexOf(selectedPollVoteButton.getText().toString()) + "");
		showText("Submitted: " + selectedPollVoteButton.getText().toString());
		finish();
	}

	public void showText(String text) {
		Toast.makeText(this, text, Toast.LENGTH_LONG).show();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.poll_details, menu);
		return true;
	}

	private void createPollVoteOptions(ArrayList<String> pollOptionsText) {
		for (int i=0; i<pollOptionsText.size(); ++i){
			String pollOptionText = pollOptionsText.get(i);
			RadioButton radioButton = new RadioButton(this);
			radioButton.setText(pollOptionText);
			radioButton.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					onPollOptionSelected((RadioButton) v);
				}

			});
			pollOptions.addView(radioButton);
		}
		pollOptions.setSelected(false);
		pollVoteButton.setEnabled(false);
	}

	private void onPollOptionSelected(RadioButton v) {
		selectedPollVoteButton = v;
		pollVoteButton.setEnabled(true);
	}

}

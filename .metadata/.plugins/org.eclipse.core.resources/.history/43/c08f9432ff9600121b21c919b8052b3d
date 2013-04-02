package pt.utl.ist.cm.neartweetclient;

import java.util.ArrayList;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

public class PollDetailsActivity extends Activity {

	public static String POLL_DESCRIPTION = "pt.utl.ist.cm.neartweetclient.PollDetailsActivity.PollDescritpion";
	public static String POLL_OPTIONS = "pt.utl.ist.cm.neartweetclient.PollDetailsActivity.PollOptions";

	private TextView pollDescriptionTextView;
	private Button pollVoteButton;
	private RadioGroup pollOptions;
	private RadioButton selectedPollVoteButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_poll_details);

		ArrayList<String> pollOptionsTexts = null;
		String descriptionText = null;
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			descriptionText = extras.getString(POLL_DESCRIPTION);
			pollOptionsTexts = extras.getStringArrayList(POLL_OPTIONS);
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
		// TODO Auto-generated method stub
		showText(selectedPollVoteButton.getText().toString());
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
		for (String pollOptionText : pollOptionsText) {
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

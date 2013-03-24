package pt.utl.ist.cm.neartweetclient;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

public class CreatePollActivity extends Activity {

	private static final int MAX_NUM_POLL_OPTIONS = 4;
	protected static final CharSequence MESSAGE_ENTER_POLL_OPTION = "Please enter a description";
	private RadioGroup pollOptions;
	private Button addPollOptionButton;
	private EditText newPollOptionEditText;
	private Button removePollOptionButton;
	private Button startPollButton;
	private RadioButton selectedPollOption;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_create_poll);

		pollOptions = (RadioGroup) findViewById(R.id.poll_vote_options);
		addPollOptionButton = (Button) findViewById(R.id.poll_addOption_button);
		removePollOptionButton = (Button) findViewById(R.id.poll_removeOption_button);
		newPollOptionEditText = (EditText) findViewById(R.id.poll_newOption_text);
		startPollButton = (Button) findViewById(R.id.poll_startPoll);
		
		//buttons
		removePollOptionButton.setEnabled(false);
		
		//Add PollOption action
		addPollOptionButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(newPollOptionEditText.getText().toString().length()>0){
					String newPollOptionText = newPollOptionEditText.getText().toString();
					addPollOption(newPollOptionText);
				} else {
					showEnterPollOptionError();
				}
			}

		});
		
		
		removePollOptionButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(selectedPollOption!=null){
					removePollOption((RadioButton) selectedPollOption);
				}
			}
		});
		
		startPollButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}
	
	private void addPollOption(String pollOptionText){
		RadioButton radioButton = new RadioButton(this);
		radioButton.setText(pollOptionText);
		radioButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				onPollOptionSelected( (RadioButton) v );
			}
		});
		pollOptions.addView(radioButton);
		newPollOptionEditText.setText("");
		
		addPollOptionButton.setEnabled(pollOptions.getChildCount()<MAX_NUM_POLL_OPTIONS);
	}
	
	private void removePollOption(RadioButton radioButton){
		pollOptions.removeView(radioButton);
	}

	private void onPollOptionSelected(RadioButton radioButton) {
		Toast.makeText(this, "EI " + radioButton.getText(), Toast.LENGTH_LONG).show();
		selectedPollOption = radioButton;
	}

	private void showEnterPollOptionError() {
		Toast.makeText(this, MESSAGE_ENTER_POLL_OPTION, Toast.LENGTH_LONG).show();				
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.create_poll, menu);
		return true;
	}

}

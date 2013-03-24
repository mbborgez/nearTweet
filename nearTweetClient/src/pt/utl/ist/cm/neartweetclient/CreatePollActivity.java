package pt.utl.ist.cm.neartweetclient;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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
	private static final CharSequence MESSAGE_ENTER_MINIMUM_POLL_OPTIONS = "Enter at least one option";
	protected static final int MIN_NUM_POLL_OPTIONS = 1;
	private RadioGroup pollOptions;
	private Button addPollOptionButton;
	private EditText newPollOptionEditText;
	private Button removePollOptionButton;
	private Button startPollButton;
	private RadioButton selectedPollOption;
	private int numberOfWords;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_create_poll);

		numberOfWords = 0;
		pollOptions = (RadioGroup) findViewById(R.id.poll_vote_options);
		addPollOptionButton = (Button) findViewById(R.id.poll_addOption_button);
		removePollOptionButton = (Button) findViewById(R.id.poll_removeOption_button);
		newPollOptionEditText = (EditText) findViewById(R.id.poll_newOption_text);
		startPollButton = (Button) findViewById(R.id.poll_startPoll);
		
		newPollOptionEditText.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				numberOfWords = count;
				addPollOptionButton.setEnabled(numberOfWords > 0);
				updateWordCounter(numberOfWords);
			}
			

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
				// DO NOTHING
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				// DO NOTHING
			}
		});
		
		addPollOptionButton.setEnabled(false);
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
		
		removePollOptionButton.setEnabled(false);
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
				if(pollOptions.getChildCount()>MIN_NUM_POLL_OPTIONS){
					startPoll();
					finish();
				} else {
					showEnterPollOptionsError();
				}
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

		numberOfWords = 0;
		newPollOptionEditText.setText("");
		addPollOptionButton.setEnabled(numberOfWords > 0 && pollOptions.getChildCount()<MAX_NUM_POLL_OPTIONS);
	}
	
	private void startPoll() {
		//TODO
	}

	private void removePollOption(RadioButton radioButton){
		pollOptions.removeView(radioButton);
		selectedPollOption = null;
		removePollOptionButton.setEnabled(false);
	}

	private void onPollOptionSelected(RadioButton radioButton) {
		selectedPollOption = radioButton;
		removePollOptionButton.setEnabled(true);
	}

	private void showEnterPollOptionError() {
		Toast.makeText(this, MESSAGE_ENTER_POLL_OPTION, Toast.LENGTH_LONG).show();				
	}

	private void showEnterPollOptionsError() {
		Toast.makeText(this, MESSAGE_ENTER_MINIMUM_POLL_OPTIONS, Toast.LENGTH_LONG).show();				
	}
	
	private void updateWordCounter(int count) {
		//TODO
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.create_poll, menu);
		return true;
	}

}

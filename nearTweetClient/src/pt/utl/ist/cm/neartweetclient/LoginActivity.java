package pt.utl.ist.cm.neartweetclient;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends Activity {
	private static final String WELLCOME = "Wellcome ";
	private static final CharSequence MESSAGE_ENTER_LOGIN = "Please enter a user name";
	
	Button loginButton;
	EditText userNameText;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);

		loginButton = (Button) findViewById(R.id.loginButton);
		userNameText = (EditText) findViewById(R.id.usernameText);

		loginButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(userNameText.getText().toString().length()>0){
					showWellcomeMessage();
					changeToTweetsStreamActiviy();
				} else {
					showEmptyLoginMessage();
				}
			}
		});
	}
	
	private void changeToTweetsStreamActiviy() {
		startActivity(new Intent(this, TweetsStreamActivity.class));
	}	
	
//	private void changeToPollCreationActivity2() {
//		
//		ArrayList<String> pollVoteOptions = new ArrayList<String>();
//		pollVoteOptions.add("Option1");
//		pollVoteOptions.add("Option2");
//		pollVoteOptions.add("Option3");
//		pollVoteOptions.add("Option4");
//
//		String pollVoteDescription = "BLBLBLA descritpion";
//		
//		Intent changeAcivityIntent = new Intent(this, PollDetailsActivity.class);
//		changeAcivityIntent.putStringArrayListExtra(PollDetailsActivity.POLL_OPTIONS, pollVoteOptions);
//		changeAcivityIntent.putExtra(PollDetailsActivity.POLL_DESCRIPTION, pollVoteDescription);
//		startActivity(changeAcivityIntent);
//	}
	
	private void showEmptyLoginMessage() {
		Toast.makeText(this, MESSAGE_ENTER_LOGIN,Toast.LENGTH_SHORT).show();
	}
	
	private void showWellcomeMessage() {
		Toast.makeText(this, WELLCOME + userNameText.getText(), Toast.LENGTH_SHORT).show();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.login, menu);
		return true;
	}

}

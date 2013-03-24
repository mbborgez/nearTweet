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
				showWellcomeMessage();
				changeToPollCreationActivity();
			}
		});
	}
	
	private void changeToPollCreationActivity() {
		Intent changeAcivityIntent = new Intent(this, CreatePollActivity.class);
		startActivity(changeAcivityIntent);
	}
	
	private void showWellcomeMessage() {
		Toast.makeText(this, WELLCOME + userNameText.getText(), Toast.LENGTH_LONG).show();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.login, menu);
		return true;
	}

}

package pt.utl.ist.cm.neartweetclient.ui;

import pt.utl.ist.cm.neartweetclient.R;
import pt.utl.ist.cm.neartweetclient.exceptions.NearTweetException;
import pt.utl.ist.cm.neartweetclient.services.RegisterUserService;
import pt.utl.ist.cm.neartweetclient.utils.UiMessages;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends Activity {
	
	private Button loginButton;
	private EditText userNameText;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		
		// referencing objects
		loginButton = (Button) findViewById(R.id.loginButton);
		userNameText = (EditText) findViewById(R.id.usernameText);
		
		//arming listeners
	    userNameText.addTextChangedListener(textWatcherGuard());
		loginButton.setOnClickListener(loginRequestCallback());
	}
	
	/**
	 * registerUser - it is responsible to delegate the Registration to the 
	 * Service Layer
	 * @param username - the name which this user will identify future interactions with
	 * the remaining entities on the network
	 */
	private void registerUser(String username) {
		RegisterUserService service = new RegisterUserService(username, this);
		try {
			service.execute();
		} catch(NearTweetException e) {
			Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
			loginButton.setEnabled(false);
		}
	}
	
	/**
	 * Login Button should only be active when text field area isn't empty.  
	 * @return Watcher Logic to deal with his responsibility
	 */
	private TextWatcher textWatcherGuard() {
		loginButton.setEnabled(false);
		return new TextWatcher() {
			@Override
			public void afterTextChanged(Editable arg0) {/**empty**/}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
				loginButton.setEnabled(s.length() > 0);
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				loginButton.setEnabled(s.length() > 0);
			}
		};
	}
	
	/**
	 * Method responsible for dealing with the logic before making a Server Request
	 * @return
	 */
	private OnClickListener loginRequestCallback() {
		return new OnClickListener() {
			@Override
			public void onClick(View v) {
				String userName = userNameText.getText().toString();
				if (userName.length() > 0) {
					registerUser(userName);
					return;
				}
				invalidTextFormat();
			}
		};
	}
	
	/**
	 * Actually this transition should be synchronous and wait by RegisterUserService Response
	 * If the network already has one user the name selected we should present an error to the user   
	 * it will be able to start a new activity
	 */
	public void nextScreen() {
		startActivity(new Intent(this, TweetsStreamActivity.class));
	}
	
	/**
	 *  Toast messages for invalid text format and connection error
	 */
	
	/**
	 * shows an alert message with the user didn't type anything on the text field
	 */
	public void invalidTextFormat() {
		Toast.makeText(this, 
				UiMessages.ENTER_LOGIN, 
				Toast.LENGTH_SHORT).show();
	}
	
	/**
	 * shows an alert message with invalid Login
	 */
	public void invalidLogin() {
		Toast.makeText(this, 
				UiMessages.ERROR_MESSAGE,
				Toast.LENGTH_SHORT).show();
	}
	 
}

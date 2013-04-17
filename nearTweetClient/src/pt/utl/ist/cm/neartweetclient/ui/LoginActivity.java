package pt.utl.ist.cm.neartweetclient.ui;

import pt.utl.ist.cm.neartweetEntities.pdu.RegisterPDU;
import pt.utl.ist.cm.neartweetclient.R;
import pt.utl.ist.cm.neartweetclient.exceptions.NearTweetException;
import pt.utl.ist.cm.neartweetclient.sync.AuthenticationHandler;
import pt.utl.ist.cm.neartweetclient.sync.Connection;
import pt.utl.ist.cm.neartweetclient.utils.UiMessages;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
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
	private boolean connectionError;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		
		this.connectionError = false;
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
	private void registerUser(final String username) {
		try {
			new AsyncTask<String, Void, Boolean>() {
				@Override
				protected Boolean doInBackground(String... params) {
					try {
						Connection.getInstance().sendPDU(new RegisterPDU(username));
					} catch (NearTweetException e) {
						return false;
					}
					return true;
				}

				@Override
				protected void onPostExecute(Boolean result) {
					if (!result) {
						connectionError();
					} else {
						waitForAuthenticationResponse();
					}
				}
			}.execute();
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
					verifyConnection();
					registerUser(userName);
					return;
				}
				invalidTextFormat();
			}
		};
	}
	
	/**
	 * Method responsible for dealing with the logic before making a Server response
	 * @return
	 */
	public void loginResponseCallback(boolean authenticated) {
		String name = userNameText.getText().toString();
		if (authenticated) {
			Connection.getInstance().startAsyncReceive(getApplicationContext());
			createCookieSession(name);
			nextScreen();
		} else {
			invalidLogin(name);
		}
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
	public void invalidLogin(String name) {
		Toast.makeText(this, 
				String.format(UiMessages.INVALID_LOGIN, name),
				Toast.LENGTH_SHORT).show();
	}
	
	/**
	 * shows an alert message with invalid Login
	 */
	public void connectionError() {
		this.connectionError = true;
		Toast.makeText(this, 
				UiMessages.ERROR_MESSAGE,
				Toast.LENGTH_SHORT).show();
	}
	
	/**
	 * createCookieSession - it should only be activated when 
	 * the server responds with void (meaning that everything went ok)
	 */
	protected void createCookieSession(String userName) {
		SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this.getApplicationContext());
		SharedPreferences.Editor editor = settings.edit();
		editor.putString("username", userName);
		editor.commit();
	}
	
	protected void verifyConnection() {
		if (connectionError == true) {
 			// Start listening the socket for authentication response
 			new AuthenticationHandler(this).execute();
		}
	}
	

	private void waitForAuthenticationResponse() {
		// Start listening the socket for authentication response
		new AuthenticationHandler(this).execute();
		
		
	}
	 
}

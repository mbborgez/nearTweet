package pt.utl.ist.cm.neartweetclient.ui;

import pt.utl.ist.cm.neartweetEntities.PDU.GenericMessagePDU;
import pt.utl.ist.cm.neartweetclient.R;
import pt.utl.ist.cm.neartweetclient.connectionTasks.ConnectionStatus;
import pt.utl.ist.cm.neartweetclient.services.RegisterUserService;
import pt.utl.ist.cm.neartweetclient.utils.UiMessages;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends Activity {
	Button loginButton;
	EditText userNameText;

	private final LoginStatusReceiver loginStatusReceiver = new LoginStatusReceiver();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);

		loginButton = (Button) findViewById(R.id.loginButton);
		userNameText = (EditText) findViewById(R.id.usernameText);

		loginButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String nameCollected = userNameText.getText().toString();
				if (nameCollected.length() > 0) {
					registerUser(nameCollected);
					return;
				}
				invalidLogin();
			}
		});
	}
	
	/**
	 * registerUser - it is responsible to delegate the Registration to the 
	 * Service Layer
	 * @param username - the name which this user will identify future interactions with
	 * the remaining entities on the network
	 */
	private void registerUser(String username) {
		RegisterUserService registUserService = new RegisterUserService(username, getApplicationContext());
		registUserService.execute();
		
		// Register for the connection status action
		IntentFilter filter = new IntentFilter(ConnectionStatus.GENERIC_MESSAGE_PDU_RECEIVED);
		LocalBroadcastManager.getInstance(getApplicationContext()).registerReceiver(loginStatusReceiver, filter);
	}

	/**
	 * Actually this transition should be synchronous and wait by RegisterUserService Response
	 * If the network already has one user the name selected we should present an error to the user   
	 * it will be able to start a new activity
	 * FIXME! - AQ
	 */
	private void nextScreen() {
		startActivity(new Intent(this, TweetsStreamActivity.class));
	}
	
	/**
	 * shows an alert message with invalid Login
	 */
	private void invalidLogin() {
		Toast.makeText(this, UiMessages.MESSAGE_ENTER_LOGIN, Toast.LENGTH_SHORT).show();
	}

	public class LoginStatusReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			if (ConnectionStatus.GENERIC_MESSAGE_PDU_RECEIVED.equals(intent.getAction())) {
				Bundle bundle = intent.getExtras();
				if(bundle!=null){
					Object receivedObj = bundle.get(ConnectionStatus.MESSAGE_RECEIVED_DATA);
					if(receivedObj instanceof GenericMessagePDU){
						GenericMessagePDU pdu = (GenericMessagePDU) receivedObj;
						Toast.makeText(getApplicationContext(), pdu.GetDescription(), Toast.LENGTH_SHORT).show();
						nextScreen();
//						unregisterUser();
					}
				}
			}
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		return true;
	}

	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		unregisterUser();
	}
	
	public void unregisterUser() {
		//TODO send a unregister request to the server
		//TODO close the socket
		//TODO kill the threads
		unregisterReceiver(loginStatusReceiver);
	}

}

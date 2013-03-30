package pt.utl.ist.cm.neartweetclient.ui;

import pt.utl.ist.cm.neartweetEntities.PDU.PDU;
import pt.utl.ist.cm.neartweetclient.R;
import pt.utl.ist.cm.neartweetclient.connectionTasks.ConnectTask;
import pt.utl.ist.cm.neartweetclient.connectionTasks.ConnectionStatus;
import pt.utl.ist.cm.neartweetclient.connectionTasks.ReceiveService;
import pt.utl.ist.cm.neartweetclient.connectionTasks.SendTask;
import pt.utl.ist.cm.neartweetclient.services.RegisterUserService;
import pt.utl.ist.cm.neartweetclient.utils.UiMessages;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends Activity {
	
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
				String nameCollected = userNameText.getText().toString();
				if (nameCollected.length() > 0) {
					registerUser(nameCollected);
					nextScreen();
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
		RegisterUserService service = new RegisterUserService(username,getApplicationContext());
		service.execute();
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
		Toast.makeText(this, UiMessages.MESSAGE_ENTER_LOGIN,Toast.LENGTH_SHORT).show();
	}
	
	/**
	 * This code should live inside of Service Layer instead.
	 */
	protected void registerInServer() {
		//Connect to the server
		new ConnectTask().execute();
        
		// Starts receiving messages from the server
		PduReceiver pduReceiver = new PduReceiver();
        IntentFilter pduReceiverFilter = new IntentFilter(ConnectionStatus.PDU_RECEIVED_DATA);
		LocalBroadcastManager.getInstance(this).registerReceiver(pduReceiver, pduReceiverFilter);

//		startService(new Intent(ConnectionStatus.RECEIVE_PDU_SERVICE));
		startService(new Intent(this, ReceiveService.class));
		// Sends a register request
		new SendTask().execute("borgez");
	}
	
	// Broadcast receiver for receiving status updates from the IntentService
	private class PduReceiver extends BroadcastReceiver
	{
	    // Prevents instantiation
	    private PduReceiver() {}
	    // Called when the BroadcastReceiver gets an Intent it's registered to receive
		@Override
		public void onReceive(Context context, Intent intent) {	        
			/*
	         * Handle Intents here.
	         */	
			Object obj = intent.getSerializableExtra(ConnectionStatus.PDU_RECEIVED_DATA);
			if(obj!=null && obj instanceof PDU){
				throw new RuntimeException("EIIIIIIIII a new pdu arrived " + (PDU) obj);
			}
		}
	}

}

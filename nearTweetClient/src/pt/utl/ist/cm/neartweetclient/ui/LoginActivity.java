package pt.utl.ist.cm.neartweetclient.ui;

import pt.utl.ist.cm.neartweetEntities.pdu.GenericMessagePDU;
import pt.utl.ist.cm.neartweetclient.R;
import pt.utl.ist.cm.neartweetclient.connectionTasks.AsynchReceiveTask;
import pt.utl.ist.cm.neartweetclient.connectionTasks.ConnectionStatus;
import pt.utl.ist.cm.neartweetclient.exceptions.NearTweetException;
import pt.utl.ist.cm.neartweetclient.services.ConnectToServerService;
import pt.utl.ist.cm.neartweetclient.services.RegisterUserService;
import pt.utl.ist.cm.neartweetclient.utils.Constants;
import pt.utl.ist.cm.neartweetclient.utils.UiMessages;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends Activity {

	private Button loginButton;
	private EditText userNameText;
	private Thread asyncReceiveThread;

	private BroadcastReceiver loginStatusReceiver = new BroadcastReceiver(){
		@Override
		public void onReceive(Context context, Intent intent) {
			if (Constants.GENERIC_MESSAGE_PDU_RECEIVED.equals(intent.getAction())) {
				Bundle bundle = intent.getExtras();
				if(bundle!=null){
					Object receivedObj = bundle.get(Constants.MESSAGE_RECEIVED_DATA);
					if(receivedObj instanceof GenericMessagePDU){
						GenericMessagePDU pdu = (GenericMessagePDU) receivedObj;
						showWellcomeMessage(pdu);
						nextScreen();
					}
				}
			}
		}
	};
	
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

	private OnClickListener loginRequestCallback() {
		return new OnClickListener() {
			@Override
			public void onClick(View v) {
				connectUser(userNameText.getText().toString());
			}
		};
	}

	private void connectUser(final String userName) {
		AsyncTask<Void, Void, Boolean> connectTask = new AsyncTask<Void, Void, Boolean>() {
			
			@Override
			protected Boolean doInBackground(Void... params) {
				try{
					ConnectToServerService connectService = new ConnectToServerService(Constants.SERVER_ADDRESS, Constants.SERVER_PORT);
					connectService.execute();
					return true;
				} catch(NearTweetException e){
					return false;
				}
			}
			
			@Override
			 protected void onPostExecute(Boolean result) {
				//only registers in the server after connecting
				if(result){
					startReceivingMessages();
					registerUser(userName);
				} else {
					errorConnecting();
				}
			}

		};
		connectTask.execute();
	}
	
	/**
	 * registerUser - it is responsible to delegate the Registration to the 
	 * Service Layer
	 * @param username - the name which this user will identify future interactions with
	 * the remaining entities on the network
	 */
	private void registerUser(final String username) {
		AsyncTask<Void, Void, Boolean> registerUserTask = new AsyncTask<Void, Void, Boolean>() {
			@Override
			protected Boolean doInBackground(Void... params) {
				try{
					RegisterUserService registerUserService = new RegisterUserService(username, getApplicationContext());
					registerUserService.execute();
					return true;
				} catch(NearTweetException e){
					return false;
				}
			}
			
			@Override
			protected void onPostExecute(Boolean result){
				if(!result){
					invalidLogin();
				}
			}
		};
		registerUserTask.execute();
	}
	
	private void startReceivingMessages() {
		// Register for the connection status action
		IntentFilter receivedPDUFilter = new IntentFilter(Constants.GENERIC_MESSAGE_PDU_RECEIVED);
		LocalBroadcastManager.getInstance(getApplicationContext()).registerReceiver(loginStatusReceiver, receivedPDUFilter);
		
		//Start a thread that receives messages in the background
		asyncReceiveThread = new Thread(new AsynchReceiveTask(getApplicationContext()));
		asyncReceiveThread.start();
	}
	
	/**
	 * shows an alert message notifying the user that the connection has failed
	 */
	private void errorConnecting() {
		Toast.makeText(getApplicationContext(), UiMessages.ERROR_CONNECTING_MESSAGE, Toast.LENGTH_SHORT).show();
	}
	
	/**
	 * Actually this transition should be synchronous and wait by RegisterUserService Response
	 * If the network already has one user the name selected we should present an error to the user   
	 * it will be able to start a new activity
	 */
	private void nextScreen() {
		startActivity(new Intent(this, TweetsStreamActivity.class));
	}

	/**
	 * shows an alert message with invalid Login
	 */
	private void invalidLogin() {
		Toast.makeText(this, 
				UiMessages.ERROR_MESSAGE,
				Toast.LENGTH_SHORT).show();
	}

	/**
	 * shows an alert message with the user didn't type anything on the text field
	 */
//	private void invalidTextFormat() {
//		Toast.makeText(this, 
//				UiMessages.ENTER_LOGIN, 
//				Toast.LENGTH_SHORT).show();
//	}
	
	/**
	 * shows an alert message when the user registers in the server
	 */
	private void showWellcomeMessage(GenericMessagePDU pdu) {
		Toast.makeText(getApplicationContext(), pdu.GetDescription(), Toast.LENGTH_SHORT).show();
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		unregisterUser();
		System.exit(0);
	}

	private void unregisterUser() {
		AsyncTask<Void, Void, Void> unregisterUserTask = new AsyncTask<Void, Void, Void>() {
			@Override
			protected Void doInBackground(Void... params) {
				//TODO send a unregister request to the server
				//TODO close the socket
				ConnectionStatus.getInstance().disconnect();
				//TODO kill the threads
				unregisterReceiver(loginStatusReceiver);
				try {
					asyncReceiveThread.join();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				return null;
			}
		};
		unregisterUserTask.execute();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		return true;
	}

}

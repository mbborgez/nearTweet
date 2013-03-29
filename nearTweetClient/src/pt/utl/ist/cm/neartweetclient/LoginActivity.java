package pt.utl.ist.cm.neartweetclient;

import pt.utl.ist.cm.neartweetEntities.PDU.PDU;
import pt.utl.ist.cm.neartweetclient.connectionTasks.ConnectTask;
import pt.utl.ist.cm.neartweetclient.connectionTasks.ConnectionStatus;
import pt.utl.ist.cm.neartweetclient.connectionTasks.ReceiveService;
import pt.utl.ist.cm.neartweetclient.connectionTasks.SendTask;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.content.LocalBroadcastManager;
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
					registerUser(userNameText.getText().toString());
					registerInServer();
					changeToTweetsStreamActiviy();
				} else {
					showEmptyLoginMessage();
				}
			}
		});
	}
	
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

	private void changeToTweetsStreamActiviy() {
		startActivity(new Intent(this, TweetsStreamActivity.class));
	}	
	

	private void showEmptyLoginMessage() {
		Toast.makeText(this, MESSAGE_ENTER_LOGIN,Toast.LENGTH_SHORT).show();
	}
	
	private void showWellcomeMessage() {
		Toast.makeText(this, WELLCOME + userNameText.getText(), Toast.LENGTH_SHORT).show();
	}
	
	private void registerUser(String username) {
		SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
		SharedPreferences.Editor editor = settings.edit();
		editor.putString("username", username);
		editor.commit();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.login, menu);
		return true;
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

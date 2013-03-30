package pt.utl.ist.cm.neartweetclient;

import pt.utl.ist.cm.neartweetEntities.PDU.GenericMessagePDU;
import pt.utl.ist.cm.neartweetclient.connectionTasks.ConnectionStatus;
import pt.utl.ist.cm.neartweetclient.services.RegistUserService;
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
	private static final CharSequence MESSAGE_ENTER_LOGIN = "Please enter a user name";

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
				if (userNameText.getText().toString().length() > 0) {
					registerUser(userNameText.getText().toString());
				} else {
					showEmptyLoginMessage();
				}
			}
		});
	}

	protected void registerUser(String username) {

		RegistUserService registUserService = new RegistUserService(username, getApplicationContext());
		registUserService.execute();
		
		// Register for the connection status action
		IntentFilter filter = new IntentFilter(ConnectionStatus.GENERIC_MESSAGE_PDU_RECEIVED);
		LocalBroadcastManager.getInstance(getApplicationContext()).registerReceiver(loginStatusReceiver, filter);
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
						changeToTweetsStreamActiviy();
//						unregisterUser();
					}
				}
			}
		}
	}

	private void changeToTweetsStreamActiviy() {
		startActivity(new Intent(this, TweetsStreamActivity.class));
	}

	private void showEmptyLoginMessage() {
		Toast.makeText(this, MESSAGE_ENTER_LOGIN, Toast.LENGTH_SHORT).show();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.login, menu);
		return true;
	}

	
	@Override
	protected void onDestroy() {
		unregisterUser();
	}
	
	public void unregisterUser() {
		//TODO send a unregister request to the server
		//TODO close the socket
		//TODO kill the threads
		unregisterReceiver(loginStatusReceiver);
	}
	
}

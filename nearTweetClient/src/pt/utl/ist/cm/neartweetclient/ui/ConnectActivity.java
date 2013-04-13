package pt.utl.ist.cm.neartweetclient.ui;

import pt.utl.ist.cm.neartweetclient.R;
import pt.utl.ist.cm.neartweetclient.exceptions.NearTweetException;
import pt.utl.ist.cm.neartweetclient.sync.Connection;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ConnectActivity extends Activity {
	Button disconnectButton;
	Button connectButton;
	EditText serverAddressEditText;
	EditText serverPortEditText;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_connect);
		
		serverAddressEditText = (EditText) findViewById(R.id.serverAddressText);
		serverPortEditText = (EditText) findViewById(R.id.serverPortText);
		connectButton = (Button) findViewById(R.id.connectButton);
		disconnectButton = (Button) findViewById(R.id.disconnectButton);
		
		connectButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				connect(serverAddressEditText.getText().toString(), Integer.parseInt(serverPortEditText.getText().toString()));
			}
		});
		
		disconnectButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				disconnect();
			}
		});
		
		serverAddressEditText.setText(Connection.getInstance().DEFAULT_IP_ADDRESS);
		serverPortEditText.setText("" + Connection.getInstance().DEFAULT_PORT);
		
	}

	protected void connect(final String serverAddress, final int serverPort) {
		new AsyncTask<String, Void, Boolean>() {
			@Override
			protected Boolean doInBackground(String... params) {
				try {
					Connection.getInstance().connect(serverAddress, serverPort);
				} catch (NearTweetException e) {
					e.printStackTrace();
					return false;
				}
				return true;
			}

			@Override
			protected void onPostExecute(Boolean result) {
				if (!result) {
					connectionError();
				} else {
					nextScreen();
				}
			}
		}.execute();
	}
	
	protected void nextScreen() {
		startActivity(new Intent(getApplicationContext(), LoginActivity.class));
	}

	protected void connectionError() {
		Toast.makeText(getApplicationContext(), "Error Connecting, please try again", Toast.LENGTH_LONG).show();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_connect, menu);
		return true;
	}
	
	private void disconnect(){
		new AsyncTask<String, Void, Boolean>() {
			@Override
			protected Boolean doInBackground(String... params) {
				try {
					Connection.getInstance().disconnect();
				} catch (NearTweetException e) {
					e.printStackTrace();
					return false;
				}
				return true;
			}

			@Override
			protected void onPostExecute(Boolean result) {
				if (!result) {
					Toast.makeText(getApplicationContext(), "Disconnected", Toast.LENGTH_SHORT).show();
				} else {
					Toast.makeText(getApplicationContext(), "Error Disconnecting", Toast.LENGTH_SHORT).show();
				}
			}
		}.execute();
	}

}

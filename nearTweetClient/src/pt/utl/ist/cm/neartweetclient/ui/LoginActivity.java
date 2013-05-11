package pt.utl.ist.cm.neartweetclient.ui;

import pt.utl.ist.cm.neartweetclient.R;
import pt.utl.ist.cm.neartweetclient.SimWifiP2pBroadcastReceiver;
import pt.utl.ist.cm.neartweetclient.core.MemCacheProvider;
import pt.utl.ist.cm.neartweetclient.sync.Connection;
import pt.utl.ist.cm.neartweetclient.sync.ConnectionsReceiverRunnable;
import pt.utl.ist.cm.neartweetclient.utils.UiMessages;
import pt.utl.ist.cmov.wifidirect.SimWifiP2pBroadcast;
import pt.utl.ist.cmov.wifidirect.SimWifiP2pDeviceList;
import pt.utl.ist.cmov.wifidirect.SimWifiP2pInfo;
import pt.utl.ist.cmov.wifidirect.SimWifiP2pManager;
import pt.utl.ist.cmov.wifidirect.SimWifiP2pManager.Channel;
import pt.utl.ist.cmov.wifidirect.SimWifiP2pManager.GroupInfoListener;
import pt.utl.ist.cmov.wifidirect.service.SimWifiP2pService;
import pt.utl.ist.cmov.wifidirect.sockets.SimWifiP2pSocketManager;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Messenger;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class LoginActivity extends Activity implements GroupInfoListener {
	
	private Button loginButton;
	private SimWifiP2pBroadcastReceiver receiver;
	private SimWifiP2pManager mManager = null;
	private Messenger mService = null;
	private Channel mChannel = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		
		// referencing objects
		loginButton = (Button) findViewById(R.id.loginButton);
		
		//arming listeners
		loginButton.setOnClickListener(loginRequestCallback());
		
		// initialize the WDSim API
		SimWifiP2pSocketManager.Init(getApplicationContext());

		// register broadcast receiver
		IntentFilter filter = new IntentFilter();
		filter.addAction(SimWifiP2pBroadcast.WIFI_P2P_STATE_CHANGED_ACTION);
		filter.addAction(SimWifiP2pBroadcast.WIFI_P2P_PEERS_CHANGED_ACTION);
		filter.addAction(SimWifiP2pBroadcast.WIFI_P2P_NETWORK_MEMBERSHIP_CHANGED_ACTION);
		filter.addAction(SimWifiP2pBroadcast.WIFI_P2P_GROUP_OWNERSHIP_CHANGED_ACTION);
		receiver = new SimWifiP2pBroadcastReceiver(this);
		registerReceiver(receiver, filter);
				
	}
	
	private ServiceConnection mConnection = new ServiceConnection() {
		// callbacks for service binding, passed to bindService()

		@Override
		public void onServiceConnected(ComponentName className, IBinder service) {
			Log.i(UiMessages.NEARTWEET_TAG, "service connected");
			mService = new Messenger(service);
			mManager = new SimWifiP2pManager(mService);
			mChannel = mManager.initialize(getApplication(), getMainLooper(), null);
			mManager.requestGroupInfo(mChannel, (GroupInfoListener) LoginActivity.this);
			new Thread(new ConnectionsReceiverRunnable(getApplicationContext())).start();
		}

		@Override
		public void onServiceDisconnected(ComponentName arg0) {
			Log.i(UiMessages.NEARTWEET_TAG, "service disconnected");
			mService = null;
			mManager = null;
			mChannel = null;
		}
	};
	
	
	/**
	 * Method responsible for dealing with the logic before making a Server Request
	 * @return
	 */
	private OnClickListener loginRequestCallback() {
		return new OnClickListener() {
			@Override
			public void onClick(View v) {
				bindService();		
			}
		};
	}
		
	@Override
	public void onGroupInfoAvailable(SimWifiP2pDeviceList devices, SimWifiP2pInfo groupInfo) {
		createCookieSession(groupInfo.getDeviceName());
		Connection.getInstance().setGroupInfo(groupInfo);
		receiver.updateNetworkInfo();
		nextScreen();
	}
	
	private void bindService(){
		Intent intent = new Intent(getApplicationContext(), SimWifiP2pService.class);
		bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
		
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
	 * createCookieSession - it should only be activated when 
	 * the server responds with void (meaning that everything went ok)
	 */
	public void createCookieSession(String userName) {
		SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this.getApplicationContext());
		SharedPreferences.Editor editor = settings.edit();
		editor.putString("username", userName);
		editor.commit();
		MemCacheProvider.setUserName(userName);
	}
			 
}

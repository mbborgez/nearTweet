package pt.utl.ist.cm.neartweetclient;

import java.util.Collection;

import pt.utl.ist.cm.neartweetclient.core.MemCacheProvider;
import pt.utl.ist.cm.neartweetclient.core.Peer;
import pt.utl.ist.cm.neartweetclient.sync.Connection;
import pt.utl.ist.cm.neartweetclient.sync.MessagesReceiverRunnable;
import pt.utl.ist.cm.neartweetclient.utils.Constants;
import pt.utl.ist.cm.neartweetclient.utils.UiMessages;
import pt.utl.ist.cmov.wifidirect.SimWifiP2pBroadcast;
import pt.utl.ist.cmov.wifidirect.SimWifiP2pDeviceList;
import pt.utl.ist.cmov.wifidirect.SimWifiP2pInfo;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

public class SimWifiP2pBroadcastReceiver extends BroadcastReceiver {

	private Activity mActivity;

//	private SimWifiP2pDeviceList deviceList;
//	private SimWifiP2pInfo groupInfo;

	public SimWifiP2pBroadcastReceiver(Activity activity) {
		super();
		this.mActivity = activity;
//		deviceList = null;
//		groupInfo = null;
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		String action = intent.getAction();
		if (SimWifiP2pBroadcast.WIFI_P2P_STATE_CHANGED_ACTION.equals(action)) {

			// This action is triggered when the WDSim service changes state:
			// - creating the service generates the WIFI_P2P_STATE_ENABLED event
			// - destroying the service generates the WIFI_P2P_STATE_DISABLED event

			int state = intent.getIntExtra(SimWifiP2pBroadcast.EXTRA_WIFI_STATE, -1);
			if (state == SimWifiP2pBroadcast.WIFI_P2P_STATE_ENABLED) {
				Toast.makeText(mActivity, "WiFi Direct enabled",
						Toast.LENGTH_SHORT).show();
			} else {
				Toast.makeText(mActivity, "WiFi Direct disabled",
						Toast.LENGTH_SHORT).show();
			}

		} else if (SimWifiP2pBroadcast.WIFI_P2P_PEERS_CHANGED_ACTION.equals(action)) {

			// Request available peers from the wifi p2p manager. This is an
			// asynchronous call and the calling activity is notified with a
			// callback on PeerListListener.onPeersAvailable()

			SimWifiP2pDeviceList deviceList = (SimWifiP2pDeviceList) intent.getSerializableExtra(SimWifiP2pBroadcast.EXTRA_DEVICE_LIST);
			Connection.getInstance().setDeviceList(deviceList);
			
			Log.i("DEVICES_LIST", deviceList.getDeviceList().toString());

			Toast.makeText(mActivity, "Peer list changed: " + deviceList.getDeviceList().toString(),
					Toast.LENGTH_SHORT).show();

			updateNetworkInfo();

		} else if (SimWifiP2pBroadcast.WIFI_P2P_NETWORK_MEMBERSHIP_CHANGED_ACTION.equals(action)) {

			SimWifiP2pInfo groupInfo = (SimWifiP2pInfo) intent.getSerializableExtra(SimWifiP2pBroadcast.EXTRA_GROUP_INFO);
			Connection.getInstance().setGroupInfo(groupInfo);
			
			updateUserID(groupInfo.getDeviceName());

			Toast.makeText(mActivity, "Network membership changed", Toast.LENGTH_SHORT).show();

			updateNetworkInfo();

		} else if (SimWifiP2pBroadcast.WIFI_P2P_GROUP_OWNERSHIP_CHANGED_ACTION.equals(action)) {

			SimWifiP2pInfo groupInfo = (SimWifiP2pInfo) intent.getSerializableExtra(SimWifiP2pBroadcast.EXTRA_GROUP_INFO);
			Connection.getInstance().setGroupInfo(groupInfo);
			
			updateUserID(groupInfo.getDeviceName());

			groupInfo.print();

			Toast.makeText(mActivity, "Group ownership changed", Toast.LENGTH_SHORT).show();
			updateNetworkInfo();

		}
	}

	private void updateUserID(String userId){
		SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(mActivity);
		SharedPreferences.Editor editor = settings.edit();
		editor.putString("username", userId);
		editor.commit();
		MemCacheProvider.setUserName(userId);
		Log.i(UiMessages.NEARTWEET_TAG, "# Device name: " + userId);
	}

	public synchronized void updateNetworkInfo() {
		Log.i("NearTweet", "before updateNetworkInfo");

		SimWifiP2pInfo groupInfo = Connection.getInstance().getGroupInfo();
		if (groupInfo != null) {

			groupInfo.print();

			MemCacheProvider.setIsGroupOwner(groupInfo.askIsGO());

			Collection<String> groupOwners = groupInfo.getHomeGroups();
			Log.i(UiMessages.NEARTWEET_TAG, "update for GOs " + groupOwners);
			Log.i(UiMessages.NEARTWEET_TAG, "peers: " + Connection.getInstance().getPeers());
			for (String groupOwner : groupOwners) {
				Log.i(UiMessages.NEARTWEET_TAG, "update for GO " + groupOwner);
				if (!groupInfo.getDeviceName().equals(groupOwner) && !Connection.getInstance().hasPeer(groupOwner)) {

					Log.i("NearTweet-UPDATE", "Need to perform a new conenction to GO named "
							+ groupOwner);
					Thread connectThread = new Thread(new ConnectRunnable(groupOwner, getDeviceAddress(groupOwner), mActivity));
					connectThread.start();
					try {
						connectThread.join();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}
		Log.i("NearTweet", "after updateNetworkInfo");

	}

	public String getDeviceAddress(String deviceName){
		SimWifiP2pDeviceList deviceList = Connection.getInstance().getDeviceList();
		if(deviceList!=null && deviceList.getByName(deviceName)!=null){
			return deviceList.getByName(deviceName).getVirtIp();
		} else {
			return null;
		}
	}

	public class ConnectRunnable implements Runnable {

		private String deviceName;
		private String deviceAddress;
		private Context context;

		public ConnectRunnable(String deviceName, String deviceAddress, Context context) {
			this.deviceName = deviceName;
			this.deviceAddress = deviceAddress;
			this.context = context;
		}

		@Override
		public void run() {
			Thread thread = null;
			Peer peer = null;
				try {
					Log.i(UiMessages.NEARTWEET_TAG, "Connecting to device " + deviceName + " - " + deviceAddress);
					peer = new Peer(deviceName, deviceAddress, Constants.CONNECTION_LISTENER_PORT);
					peer.connect();
					Log.i(UiMessages.NEARTWEET_TAG, "Connected with " + deviceName + " - " + deviceAddress);
					thread = new Thread(new MessagesReceiverRunnable(context, peer));
					thread.start();
//					thread.join();
				} catch (Exception e) {
					Log.e(UiMessages.NEARTWEET_TAG, "Error connecting with [deviceName: " + deviceName + ", deviceAddress: " + deviceAddress + "]");
					if( peer != null && !peer.isClosed() ) { peer.closeConnection(); }
					if( thread != null && thread.isAlive() ) { thread.interrupt(); }
				}
			}
	}

}

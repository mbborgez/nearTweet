package pt.utl.ist.cm.neartweetclient;

import java.util.Collection;

import pt.utl.ist.cmov.wifidirect.SimWifiP2pBroadcast;
import pt.utl.ist.cmov.wifidirect.SimWifiP2pDeviceList;
import pt.utl.ist.cmov.wifidirect.SimWifiP2pInfo;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

public class SimWifiP2pBroadcastReceiver extends BroadcastReceiver {

    private Activity mActivity;
    
    private SimWifiP2pDeviceList deviceList;
    private SimWifiP2pInfo groupInfo;

    public SimWifiP2pBroadcastReceiver(Activity activity) {
        super();
        this.mActivity = activity;
        deviceList = null;
        groupInfo = null;
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
        	
        	deviceList = (SimWifiP2pDeviceList) intent.getSerializableExtra(
            		SimWifiP2pBroadcast.EXTRA_DEVICE_LIST);

        	Log.i("DEVICES_LIST", deviceList.getDeviceList().toString());
        	
        	Toast.makeText(mActivity, "Peer list changed: " + deviceList.getDeviceList().toString(),
    				Toast.LENGTH_SHORT).show();
        	
    		updateNetworkInfo();
        	
        } else if (SimWifiP2pBroadcast.WIFI_P2P_NETWORK_MEMBERSHIP_CHANGED_ACTION.equals(action)) {

        	groupInfo = (SimWifiP2pInfo) intent.getSerializableExtra(
        			SimWifiP2pBroadcast.EXTRA_GROUP_INFO);
        	
        	groupInfo.print();
    		Toast.makeText(mActivity, "Network membership changed",
    				Toast.LENGTH_SHORT).show();

    		updateNetworkInfo();

        } else if (SimWifiP2pBroadcast.WIFI_P2P_GROUP_OWNERSHIP_CHANGED_ACTION.equals(action)) {

        	groupInfo = (SimWifiP2pInfo) intent.getSerializableExtra(
        			SimWifiP2pBroadcast.EXTRA_GROUP_INFO);
        	groupInfo.print();

        	Toast.makeText(mActivity, "Group ownership changed",
    				Toast.LENGTH_SHORT).show();
    		updateNetworkInfo();
    		
        }
    }
//TODO
	private void updateNetworkInfo() {
		Log.i("NearTweet", "before");

		if (this.getGroupInfo() != null) {
			Log.i("NearTweet-UPDATE", "before2");

			this.getGroupInfo().print();

			Collection<String> groupOwners = this.getGroupInfo().getHomeGroups();

			for (String groupOwner : groupOwners) {
				//				if (!receiver.getGroupInfo().getDeviceName().equals(groupOwner) && !clientsSockets.containsKey(groupOwner)) {
				if (!this.getGroupInfo().getDeviceName().equals(groupOwner) /*&& !Connection.getInstance().hasPeer(groupOwner)*/) {

					Log.i("NearTweet-UPDATE",
							"Need to perform a new conenction to GO named "
									+ groupOwner);
					//new ConnectTask().execute(groupOwner);
				}
			}

		}
	}
	
	public String getDeviceAddress(String deviceName){
		if(getDeviceList()!=null && getDeviceList().getByName(deviceName)!=null){
			return getDeviceList().getByName(deviceName).getVirtIp();
		} else {
		return null;
		}
	}

	public SimWifiP2pDeviceList getDeviceList() {
		return deviceList;
	}

	public void setDeviceList(SimWifiP2pDeviceList deviceList) {
		this.deviceList = deviceList;
	}

	public SimWifiP2pInfo getGroupInfo() {
		return groupInfo;
	}

	public void setGroupInfo(SimWifiP2pInfo groupInfo) {
		this.groupInfo = groupInfo;
	}

}
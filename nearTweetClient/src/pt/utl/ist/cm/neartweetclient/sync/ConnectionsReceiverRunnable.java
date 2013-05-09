package pt.utl.ist.cm.neartweetclient.sync;

import java.io.IOException;

import pt.utl.ist.cm.neartweetclient.core.Peer;
import pt.utl.ist.cm.neartweetclient.utils.Constants;
import pt.utl.ist.cm.neartweetclient.utils.UiMessages;
import pt.utl.ist.cmov.wifidirect.sockets.SimWifiP2pSocket;
import pt.utl.ist.cmov.wifidirect.sockets.SimWifiP2pSocketServer;
import android.content.*;
import android.util.Log;

public class ConnectionsReceiverRunnable implements Runnable {

	private Context context;
	private Boolean isReceivingConnections = false;
	private SimWifiP2pSocketServer mSrvSocket = null;

	public ConnectionsReceiverRunnable(Context context){
		this.context = context;
	}

	@Override
	public void run() {
		synchronized (isReceivingConnections) {
			if (!isReceivingConnections) {
				isReceivingConnections = true;

				Log.d(UiMessages.NEARTWEET_TAG, "IncommingCommTask started (" + this.hashCode() + ").");
				Connection.getInstance().init();
				try {
					mSrvSocket = new SimWifiP2pSocketServer(Constants.CONNECTION_LISTENER_PORT);
					while (!Thread.currentThread().isInterrupted()) {
						try {
							SimWifiP2pSocket sock = mSrvSocket.accept();
							
							new Thread(new MessagesReceiverRunnable(context, new Peer(sock))).start();
						} catch (Exception e) {
							Log.d("Error accepting socket:", "error");
							e.printStackTrace();
							break;
						}
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
				isReceivingConnections = false;
			}
		}
	}
}

package pt.utl.ist.cm.neartweetclient.connectionTasks;

import pt.utl.ist.cm.neartweetEntities.PDU.PDU;
import android.app.IntentService;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

public class ReceiveService extends IntentService {
	
	public ReceiveService(String name) {
		super(ConnectionStatus.RECEIVE_PDU_SERVICE);
	}

	@Override
    protected void onHandleIntent(Intent workIntent) {
        // Gets data from the incoming Intent
		// String dataString = workIntent.getDataString();
        // Do work here, based on the contents of dataString
		Object receivedObj;
		try {
			receivedObj = ConnectionStatus.getInstance().getInputStream().readObject();
		} catch (Exception e) {
			receivedObj = null;
		}	
		
		if(receivedObj!=null && receivedObj instanceof PDU){
			Intent localIntent = new Intent(ConnectionStatus.RECEIVE_PDU_SERVICE);
            // Puts the status into the Intent
			localIntent.putExtra(ConnectionStatus.PDU_RECEIVED_DATA, (PDU) receivedObj);
		    // Broadcasts the Intent to receivers in this app.
		    LocalBroadcastManager.getInstance(this).sendBroadcast(localIntent);
		}
	}
}
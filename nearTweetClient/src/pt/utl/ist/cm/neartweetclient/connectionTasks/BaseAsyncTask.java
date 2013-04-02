package pt.utl.ist.cm.neartweetclient.connectionTasks;

import pt.utl.ist.cm.neartweetEntities.pdu.PDU;
import pt.utl.ist.cm.neartweetclient.exceptions.ErrorConnectingException;
import pt.utl.ist.cm.neartweetclient.exceptions.NearTweetException;
import android.os.AsyncTask;
import android.util.Log;

public abstract class BaseAsyncTask <Params, Progress, Result> extends AsyncTask<Params, Progress, Result>{

	public boolean connect(String serverAddress, int serverPort){
		try {
			if(!ConnectionStatus.getInstance().isConnected()){
				Log.i(this.getClass().toString(), "Connecting..");
				ConnectionStatus.getInstance().connect(serverAddress, serverPort);
				Log.i(this.getClass().toString(), "Connected");
			}
			return true;
		} catch (ErrorConnectingException e) {
			Log.e(this.getClass().toString(), "Error connecting");
			return false;
		}catch(NearTweetException e){
			return false;
		}
	}

	public boolean sendPDU(PDU pdu){
		if(!ConnectionStatus.getInstance().isConnected()){
			return false;
		}

		try {
			Log.i(this.getClass().toString(), "Sending pdu " + pdu);
			ConnectionStatus.getInstance().sendPDU(pdu);
			Log.i(this.getClass().toString(), "Pdu sent! " + pdu);
			return true;
		} catch (NearTweetException e) {
			return false;
		}
	}


}

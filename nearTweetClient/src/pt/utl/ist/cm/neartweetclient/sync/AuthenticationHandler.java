package pt.utl.ist.cm.neartweetclient.sync;

import pt.utl.ist.cm.neartweetEntities.pdu.GenericMessagePDU;
import pt.utl.ist.cm.neartweetEntities.pdu.PDU;
import pt.utl.ist.cm.neartweetclient.ui.LoginActivity;
import android.os.AsyncTask;
import android.util.Log;

public class AuthenticationHandler extends AsyncTask<Void, Void, Boolean> {
	
	private LoginActivity activity;

	public AuthenticationHandler(LoginActivity activity) {
		this.activity= activity;
	}

	@Override
	protected Boolean doInBackground(Void... arg0) {
		try {
			PDU pdu;
			pdu = Connection.getInstance().receiveData();
			Log.i("DEBUG", "PDU ARRIVED: " + pdu.getClass().getName());
			if (pdu != null && pdu instanceof GenericMessagePDU) {
				GenericMessagePDU response = (GenericMessagePDU) pdu;
				return response.getResponse();
			}
			return false;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	@Override
	protected void onPostExecute(Boolean result) {
		if(result == null) {
			this.activity.connectionError();
		} else {
			this.activity.loginResponseCallback(result);
		}
	}

}

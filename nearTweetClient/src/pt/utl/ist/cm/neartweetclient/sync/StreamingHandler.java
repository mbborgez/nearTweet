package pt.utl.ist.cm.neartweetclient.sync;

import pt.utl.ist.cm.neartweetEntities.pdu.PDU;
import pt.utl.ist.cm.neartweetEntities.pdu.PDUVisitor;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

public class StreamingHandler extends AsyncTask<Void, Void, Void> {

	private Context context;
	private PDUVisitor visitor;

	public StreamingHandler(Context context){
		this.context = context;
		this.visitor = new PDUHandler(this.context);
	}

	@Override
	protected Void doInBackground(Void... arg0) {
		try {
			PDU pdu;
			while(Connection.getInstance().isAlive()){
				pdu = Connection.getInstance().receiveData();
				if (pdu != null) {
					Log.i("DEBUG", "PDU ARRIVED: " + pdu.getClass().getName());
					pdu.accept(this.visitor);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}

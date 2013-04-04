package pt.utl.ist.cm.neartweetclient.connectionTasks;


import pt.utl.ist.cm.neartweetEntities.pdu.PDU;
import android.app.Activity;
import android.util.Log;

public class StreamingHandler implements Runnable {

	private Activity activity;

	public StreamingHandler(Activity activity){
		this.activity = activity;
	}

	@Override
	public void run() {
		try {
			PDUHandler pduVisitor;
			PDU pdu;
			while(Connection.getInstance().isAlive()){
				pdu = Connection.getInstance().receiveData();
				if (pdu != null) {
					pduVisitor = new PDUHandler(this.activity.getApplicationContext());
					Log.i(pdu.getClass().getName(), "PDU ARRIVED");
					pdu.accept(pduVisitor);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}

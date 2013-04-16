package pt.utl.ist.cm.neartweetclient.sync;

import pt.utl.ist.cm.neartweetEntities.pdu.PDU;
import pt.utl.ist.cm.neartweetEntities.pdu.PDUVisitor;
import android.content.Context;
import android.util.Log;

public class MessagesReceiverRunnable implements Runnable {

	private Context context;
	private PDUVisitor visitor;

	public MessagesReceiverRunnable(Context context){
		this.context = context;
		this.visitor = new PDUHandler(this.context);
	}

	@Override
	public void run() {
		try {
			PDU receivedPdu;
			while(Connection.getInstance().isAlive()){
				receivedPdu = Connection.getInstance().receiveData();
				if (receivedPdu != null) {
					Log.i("DEBUG", "PDU ARRIVED: " + receivedPdu.getClass().getName());
					receivedPdu.accept(this.visitor);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}

package pt.utl.ist.cm.neartweetclient.sync;

import java.util.concurrent.LinkedBlockingQueue;

import pt.utl.ist.cm.neartweetEntities.pdu.PDU;
import android.content.Context;
import android.util.Log;

public class MessagesSenderRunnable implements Runnable {

	private Context context;

	private static LinkedBlockingQueue<PDU> sendQueue = new LinkedBlockingQueue<PDU>();

	public MessagesSenderRunnable(Context context){
		this.context = context;
	}

	@Override
	public void run() {
		PDU pduToSend = null;
		while(Connection.getInstance().isAlive())
			pduToSend = sendQueue.peek();
			if (pduToSend != null) {
				Log.i("DEBUG", "Sending pdu : " + pduToSend.getClass().getName() + "...");
			}
	}

}

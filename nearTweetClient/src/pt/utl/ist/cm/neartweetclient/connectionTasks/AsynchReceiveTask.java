package pt.utl.ist.cm.neartweetclient.connectionTasks;

import pt.utl.ist.cm.neartweetEntities.pdu.PDU;
import pt.utl.ist.cm.neartweetclient.exceptions.NotConnectedException;
import android.content.Context;

public class AsynchReceiveTask implements Runnable {

	private Context context;

	public AsynchReceiveTask(Context context){
		this.context = context;
	}

	@Override
	public void run() {
		
		if(!ConnectionStatus.getInstance().isConnected()){
			throw new NotConnectedException("Trying to receive before connecting");
		}

		while(ConnectionStatus.getInstance().isConnected()){
			handlePDU(ConnectionStatus.getInstance().receivePDU());
		}
	}
	
	private void handlePDU(PDU pdu){
		ClientPDUVisitor pduVisitor = new ClientPDUVisitor(context);
		pdu.accept(pduVisitor);
	}

}
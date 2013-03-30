package pt.utl.ist.cm.neartweetclient.connectionTasks;

import pt.utl.ist.cm.neartweetEntities.PDU.PDU;
import android.content.Context;
import android.util.Log;

public class AsynchReceiveTask implements Runnable {
  
	private Context context;
	
	public AsynchReceiveTask(Context context){
		this.context = context;
	}

	@Override
	public void run() {
		
		while(!ConnectionStatus.getInstance().isConnected()){
			waitForConnection();
		}
		
		while(ConnectionStatus.getInstance().isConnected()){
			Object receivedObj;
			try {
				receivedObj = ConnectionStatus.getInstance().getInputStream().readObject();
			} catch (Exception e) {
				e.printStackTrace();
				receivedObj = null;
			}
			
			if(receivedObj!=null && receivedObj instanceof PDU){
				ClientPDUVisitor pduVisitor = new ClientPDUVisitor(context);
				PDU pdu = (PDU) receivedObj;
				Log.i(this.getClass().toString(), "PDU received " + pdu);
				pdu.accept(pduVisitor);
			}
		}
	}
	
	private void waitForConnection() {
		try {
			Thread.sleep(500); /* Wait for the connection */
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
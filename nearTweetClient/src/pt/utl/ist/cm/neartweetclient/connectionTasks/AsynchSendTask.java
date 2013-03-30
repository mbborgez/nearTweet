package pt.utl.ist.cm.neartweetclient.connectionTasks;

import java.io.IOException;
import java.net.UnknownHostException;
import android.util.Log;
import pt.utl.ist.cm.neartweetEntities.PDU.PDU;

public class AsynchSendTask implements Runnable {

	private PDU pdu;

	public AsynchSendTask(PDU pdu) {
		this.pdu = pdu;
	}

	@Override
	public void run() {
		while (ConnectionStatus.getInstance().getOutputStream() == null) {
			waitForConnection();
		}

		try {
			Log.i(this.getClass().toString(), "Sending pdu " + pdu);
			ConnectionStatus.getInstance().getOutputStream().writeObject(pdu);
			ConnectionStatus.getInstance().getOutputStream().flush();
			Log.i(this.getClass().toString(), "Pdu sent! " + pdu);
		} catch (UnknownHostException e) {
			e.printStackTrace();
			Log.e(this.getClass().toString(), "Error sending pdu " + pdu);
		} catch (IOException e) {
			e.printStackTrace();
			Log.e(this.getClass().toString(), "Error sending pdu " + pdu);
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
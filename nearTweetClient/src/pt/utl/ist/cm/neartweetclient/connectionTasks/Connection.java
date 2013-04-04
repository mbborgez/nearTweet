package pt.utl.ist.cm.neartweetclient.connectionTasks;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import android.util.Log;

import pt.utl.ist.cm.neartweetEntities.pdu.PDU;

public class Connection {
	public static Connection currentConnection;

	private ObjectOutputStream outputStream;
	private ObjectInputStream inputStream;
	private Socket socket;
	
	private Connection() throws UnknownHostException, IOException {
		Log.i("CONNECTION", "STARTING CONNECTION");
		this.socket = new Socket("10.0.2.2", 8000);
		Log.i("CONNECTION", "CONNECTION STARTED SUCCESSFULL");
		this.outputStream = new ObjectOutputStream(socket.getOutputStream());
		this.inputStream  = new ObjectInputStream(socket.getInputStream());
	}
	
	public static Connection getInstance() throws UnknownHostException, IOException {
		if (currentConnection == null) {
			currentConnection = new Connection();
		}
		return currentConnection;
	}
	
	/**
	 * sendPDU sends a pdu through the socket created
	 * @param pdu
	 * @throws IOException
	 */
	public void sendPDU(PDU pdu) throws IOException {
		if (this.outputStream != null) {
			this.outputStream.writeObject(pdu);
			this.outputStream.flush();
		}
	}
	
	public PDU receiveData() {
		PDU pdu = null;
		Object obj = null;
		
		if (this.inputStream != null) {
			while(pdu == null) {
				try {
					obj = inputStream.readObject();
					if(obj != null && obj instanceof PDU) {
						pdu = (PDU) obj;
					}
				} catch (Exception e) { obj = null; }
			}
		}
		return pdu;		
	}
	
	public boolean isAlive() {
		return (this.socket != null);
	}
}

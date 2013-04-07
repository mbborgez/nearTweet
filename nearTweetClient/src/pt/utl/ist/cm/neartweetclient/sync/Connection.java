package pt.utl.ist.cm.neartweetclient.sync;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import android.util.Log;

import pt.utl.ist.cm.neartweetEntities.pdu.PDU;

public class Connection {
	
	// Network configurations
	public final String IP_ADDRESS = "192.168.1.66";
	public final int PORT = 8000;
	
	public static Connection currentConnection;
	private ObjectOutputStream outputStream;
	private ObjectInputStream inputStream;
	private Socket socket;
	
	private Connection() throws UnknownHostException, IOException {
		Log.i("DEBUG", "STARTING CONNECTION");
		this.socket = new Socket(IP_ADDRESS, PORT);
		Log.i("DEBUG", "CONNECTION STARTED SUCCESSFULL");
		this.outputStream = new ObjectOutputStream(socket.getOutputStream());
		this.inputStream  = new ObjectInputStream(socket.getInputStream());
	}
	
	public static Connection getInstance() throws UnknownHostException, IOException {
		if (currentConnection == null) {
			Log.i("DEBUG","CREATING A NEW CONNECTION");
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
			Log.i("DEBUG", "SEND PDU: " + pdu.getClass().getName());
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
					Log.i("DEBUG", "NEW OBJECT ARRIVED");
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
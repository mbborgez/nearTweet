package pt.utl.ist.cm.neartweetclient.sync;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import pt.utl.ist.cm.neartweetEntities.pdu.PDU;
import pt.utl.ist.cm.neartweetclient.exceptions.NearTweetException;
import android.content.Context;
import android.util.Log;

public class Connection {

	// Network configurations
	public final String DEFAULT_IP_ADDRESS = "10.0.2.2";
	public final int DEFAULT_PORT = 8004;

	public static Connection currentConnection;
	private ObjectOutputStream outputStream;
	private ObjectInputStream inputStream;
	private Socket socket;

	private Connection() { /* Avoid instantiation */ }

	public static Connection getInstance() {
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
	public void sendPDU(PDU pdu) throws NearTweetException {
		try{
			if (this.outputStream != null) {
				Log.i("DEBUG", "SEND PDU: " + pdu.getClass().getName());
				this.outputStream.writeObject(pdu);
				this.outputStream.flush();
			}
		}
		catch(Exception e){
			e.printStackTrace();
			throw new NearTweetException(e.getMessage());
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

	public void connect(String serverAddress, int serverPort) throws NearTweetException{
		try {
			Log.i("DEBUG", "STARTING CONNECTION");
			this.socket = new Socket(serverAddress, serverPort);
			Log.i("DEBUG", "CONNECTION STARTED SUCCESSFULL");
			
			this.outputStream = new ObjectOutputStream(socket.getOutputStream());
			this.inputStream  = new ObjectInputStream(socket.getInputStream());
		} catch (UnknownHostException e) {
			throw new NearTweetException(e.getMessage() + "\n" + "Error Connecting");
		} catch (IOException e) {
			throw new NearTweetException(e.getMessage() + "\n" + "Error Connecting");
		}
	}

	public void disconnect() throws NearTweetException {
		try {
			this.socket.close();
			this.socket = null;
			this.outputStream = null;
			this.inputStream = null;
		} catch (IOException e) {
			e.printStackTrace();
			throw new NearTweetException(e.getMessage());
		}
	}

	public void startAsyncReceive(Context contex) throws NearTweetException{
		try{
			new Thread(new MessagesReceiverRunnable(contex)).start();
		}
		catch(Exception e){
			e.printStackTrace();
			throw new NearTweetException(e.getMessage());
		}
	}
}

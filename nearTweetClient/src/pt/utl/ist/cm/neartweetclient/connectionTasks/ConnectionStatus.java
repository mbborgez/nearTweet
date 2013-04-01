package pt.utl.ist.cm.neartweetclient.connectionTasks;

import java.io.IOException;
//import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import android.util.Log;

import pt.utl.ist.cm.neartweetEntities.pdu.PDU;

public class ConnectionStatus {
	public static ConnectionStatus currentConnection;

	private ObjectOutputStream outputStream;
	//private ObjectInputStream inputStream;
	private Socket socket;
	
	private ConnectionStatus() throws UnknownHostException, IOException {
		Log.i("CONNECTION", "STARTING CONNECTION");
		this.socket = new Socket("localhost", 8000);
		Log.i("CONNECTION", "STARTING CONNECTION");
		this.outputStream = new ObjectOutputStream(socket.getOutputStream());
		//this.inputStream  = new ObjectInputStream(socket.getInputStream());
	}
	
	public static ConnectionStatus getInstance() throws UnknownHostException, IOException {
		if (currentConnection == null) {
			currentConnection = new ConnectionStatus();
		}
		return currentConnection;
	}
	
	public void sendPDU(PDU pdu) throws IOException {
		 this.outputStream.writeObject(pdu);
		 this.outputStream.flush();
	}

	
}

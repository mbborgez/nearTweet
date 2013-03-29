package pt.utl.ist.cm.neartweetclient.connectionTasks;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ConnectionStatus {
	public static final ConnectionStatus instance = new ConnectionStatus();

	public static final String RECEIVE_PDU_SERVICE = "pt.utl.ist.cm.neartweetclient.connectionTasks.ConnectionStatus.pduReceived";
	public static final String PDU_RECEIVED_DATA = "pt.utl.ist.cm.neartweetclient.connectionTasks.ConnectionStatus.receivedData";

	private ObjectOutputStream outputStream;
	private ObjectInputStream inputStream;
	private Socket socket;
	
	public static ConnectionStatus getInstance() {
		return instance;
	}

	public ObjectInputStream getInputStream() {
		return inputStream;
	}

	public ObjectOutputStream getOutputStream() {
		return outputStream;
	}

	public void setOutputStream(ObjectOutputStream outputStream) {
		this.outputStream = outputStream;
	}

	public void setInputStream(ObjectInputStream inputStream) {
		this.inputStream = inputStream;
	}

	public void setSocket(Socket clientSocket) {
		this.socket = clientSocket;
	}
	
	public Socket getSocket(Socket clientSocket) {
		return socket;
	}
	
}

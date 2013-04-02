package pt.utl.ist.cm.neartweetclient.connectionTasks;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import android.util.Log;

import pt.utl.ist.cm.neartweetEntities.pdu.PDU;
import pt.utl.ist.cm.neartweetclient.exceptions.AllreadyConnectedException;
import pt.utl.ist.cm.neartweetclient.exceptions.ErrorConnectingException;
import pt.utl.ist.cm.neartweetclient.exceptions.ErrorSendingMessageException;
import pt.utl.ist.cm.neartweetclient.exceptions.NotConnectedException;

public class ConnectionStatus {
	private static ConnectionStatus currentConnection;

	private ObjectOutputStream outputStream;
	private ObjectInputStream inputStream;
	private Socket socket;
	
	private ConnectionStatus() { /* Avoid instantiation */}
	
	public static ConnectionStatus getInstance() {
		if (currentConnection == null) {
			currentConnection = new ConnectionStatus();
		}
		return currentConnection;
	}
	
	public synchronized void connect(String host, int port) {
		if(isConnected()){
			throw new AllreadyConnectedException();
		}
		try {
			Log.i("ConnectionStatus", "STARTING CONNECTION");
			socket = new Socket(host, port);
			outputStream = new ObjectOutputStream(socket.getOutputStream());
			inputStream  = new ObjectInputStream(socket.getInputStream());
			Log.i("ConnectionStatus", "CONNECTED");
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.e(this.getClass().toString(), "Error connecting");
			throw new ErrorConnectingException(e.getMessage());
		} catch (IOException e) {
			e.printStackTrace();
			Log.e(this.getClass().toString(), "Error connecting");
			throw new ErrorConnectingException(e.getMessage());
		}

	}
	
	public synchronized void sendPDU(PDU pdu) throws ErrorSendingMessageException {
		if(!isConnected()){
			throw new NotConnectedException();
		}
		try {
			Log.i("SEND_PDU - ", "pdu: " + pdu);
			Log.i("SEND_PDU - ", "outputStream: " + outputStream);
			outputStream.writeObject(pdu);
			Log.i("SEND_PDU - ", "afterSend: " );
			outputStream.flush();
			Log.i("SEND_PDU - ", "afterFlush: " );
		} catch (IOException e) {
			e.printStackTrace();
			Log.e(this.getClass().toString(), "Error sending pdu " + pdu);
			throw new ErrorSendingMessageException(pdu);
		}
	}
	
	public synchronized PDU receivePDU() {
		Log.i(getClass().toString(), "ReceivePDU");
		if(!isConnected()){
			throw new NotConnectedException();
		}
		
		PDU receivedPDU = null;
		while(receivedPDU == null){
			Object receivedObj;
			try {
				receivedObj = inputStream.readObject();
				Log.i(getClass().toString(), "ReceivePDU - object arrived - " + receivedObj);
				if(receivedObj != null && receivedObj instanceof PDU){
					receivedPDU = (PDU) receivedObj;
					Log.i(getClass().toString(), "ReceivePDU - pdu arrived - " + receivedPDU);
				}
			} catch (Exception e) {
				Log.i(getClass().toString(), "ReceivePDU - exception catched - " + e);
				e.printStackTrace();
				receivedObj = null;
			}
		}
		return receivedPDU;
	}

	public synchronized void disconnect(){
		if(!isConnected()){
			throw new NotConnectedException();
		}
		try {
			socket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		currentConnection = null;
	}
	
	public synchronized boolean isConnected(){
		return socket != null && outputStream!=null && inputStream!=null;
	}
}

package pt.utl.ist.cm.neartweetclient.core;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import android.util.Log;

import pt.utl.ist.cm.neartweetEntities.pdu.PDU;
import pt.utl.ist.cm.neartweetEntities.pdu.RegisterPDU;
import pt.utl.ist.cm.neartweetclient.utils.UiMessages;
import pt.utl.ist.cmov.wifidirect.sockets.SimWifiP2pSocket;

public class Peer {

	private String deviceName;
	private String deviceAddress;
	private int devicePort;
	
	private SimWifiP2pSocket socket;
	private ObjectOutputStream out;
	private ObjectInputStream in;

	public Peer(SimWifiP2pSocket socket) throws Exception {
		try {
			setSocket(socket);
			setOut(new ObjectOutputStream(socket.getOutputStream()));
			setIn(new ObjectInputStream(socket.getInputStream()));
		} catch (Exception e) {
			Log.i("CHAT-PEER", "Error creating peer by socket");
			e.printStackTrace();
			throw new Exception("Error constructing peer connection");
		}
	}

	public Peer(String deviceName, String deviceAddress, int devicePort) {
		this.deviceName = deviceName;
		this.deviceAddress = deviceAddress;
		this.devicePort = devicePort;
	}
	
	public synchronized void connect() throws Exception {
		try {
			setSocket(new SimWifiP2pSocket(deviceAddress, devicePort));
			setOut(new ObjectOutputStream(socket.getOutputStream()));
			setIn(new ObjectInputStream(socket.getInputStream()));
		} catch (Exception e) {
			Log.i("CHAT-PEER", "Error connecting");
			e.printStackTrace();
			throw new Exception("Error connecting");
		}
	}
	
	public synchronized boolean isClosed() {
		return  getOut()==null || 
				getIn() ==null || 
				getSocket()==null ||
				getSocket().isClosed();
	}
	
	public boolean sendConnectPDU(String id) {
		return sendPDU(new RegisterPDU(id, deviceName));
	}
	
	public boolean sendPDU(PDU pdu) {
		Log.i(UiMessages.NEARTWEET_TAG, "sending pdu " + pdu + " to " + deviceName);
		try {
			getOut().writeObject(pdu);
			getOut().flush();
			
			return true;
		} catch (IOException e) {
			Log.e(UiMessages.NEARTWEET_TAG, "Error sending message [ " + pdu + " ]" + "\n");
			e.printStackTrace();
			return false;
		}
	}
	
	public PDU receivePDU() {
		while (true) {
			Object receivedObj = null;
			try {
				receivedObj = getIn().readObject();
				if (receivedObj != null && receivedObj instanceof PDU) {
					Log.i(UiMessages.NEARTWEET_TAG, "received a new pdu " + receivedObj + " from " + deviceName);
					return (PDU) receivedObj;
				}
			} catch (Exception e) {
				Log.e(UiMessages.NEARTWEET_TAG, "Error receiving message " + "\n");
				e.printStackTrace();
				return null;
			}
		}
	}
	
	public void closeConnection () {
		if(!isClosed()) {
			try {
				Log.i(UiMessages.NEARTWEET_TAG, "closing socket");
				getSocket().close();
			} catch (Exception e) {
				Log.e(UiMessages.NEARTWEET_TAG, "Error closing socket connection: " + e.getMessage());
				e.printStackTrace();
			}
		}
	}

	/********************************************************************
	 *							GETS & SETS								*
	 ********************************************************************/

	public String getDeviceName() {
		return deviceName;
	}

	public void setDeviceName(String deviceName) {
		this.deviceName = deviceName;
	}

	public String getDeviceAddress() {
		return deviceAddress;
	}

	public void setDeviceAddress(String deviceAddress) {
		this.deviceAddress = deviceAddress;
	}

	public int getDevicePort() {
		return devicePort;
	}

	public void setDevicePort(int devicePort) {
		this.devicePort = devicePort;
	}

	public synchronized SimWifiP2pSocket getSocket() {
		return socket;
	}

	public synchronized void setSocket(SimWifiP2pSocket socket) {
		this.socket = socket;
	}

	public synchronized ObjectOutputStream getOut() {
		return out;
	}

	public synchronized void setOut(ObjectOutputStream out) {
		this.out = out;
	}

	public synchronized ObjectInputStream getIn() {
		return in;
	}

	public synchronized void setIn(ObjectInputStream in) {
		this.in = in;
	}
	
	@Override
	public String toString() {
		return "Peer: [ deviceName: " + deviceName + ", deviceAddress: " + deviceAddress + ", devicePort: " + devicePort + ", socket: " + socket + ", out: " + out + ", in: " + in + " ] ";
	}
}

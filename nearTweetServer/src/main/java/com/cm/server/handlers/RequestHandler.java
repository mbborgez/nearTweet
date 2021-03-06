package main.java.com.cm.server.handlers;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;

import main.java.com.cm.server.utils.Database;
import main.java.com.cm.server.visitors.SlimServerDispatcher;
import pt.utl.ist.cm.neartweetEntities.pdu.PDU;

public class RequestHandler implements Runnable {
	
	public Socket socket;
	public Database memory;
	private SlimServerDispatcher dispatcher;
	public ObjectOutputStream connection;
	private Thread currentContext;

	public RequestHandler(Socket socket, Database memory) throws IOException {
		this.socket = socket;
		this.memory = memory;
		this.dispatcher = new SlimServerDispatcher(this);
		this.connection = new ObjectOutputStream(socket.getOutputStream());
	}
	
	public void sendDirectedPDU(PDU pdu, ObjectOutputStream stream) {
		try {
			System.out.println("[nearTweet Server] Sending pdu for user: " + this.memory.GetUserID(stream) + "start");
			stream.writeObject(pdu);
			stream.flush();
			System.out.println("[nearTweet Server] Sending pdu for user: " + this.memory.GetUserID(stream)+ "end");
		} 
		catch (IOException e) {
			System.out.println("[nearTweet Server] Removing User: " + this.memory.GetUserID(this.connection));
			this.memory.RemoveUser(stream);
		}
	}
	
	public void sendToConnectedUser(PDU pdu) {
		sendDirectedPDU(pdu, this.connection);
	}
	
	public void sendDirectedPDU(PDU pdu, String userId) {
		if(memory.VerifyIfUserExists(userId) && memory.GetUserStream(userId)!=null){
			ObjectOutputStream userStream = memory.GetUserStream(userId);
			sendDirectedPDU(pdu, userStream);
		} else {
			System.err.println("[nearTweet Server] Error Sending directPdu because the user " + userId + " is not registered in the server.");
		}
	}
	
	public void broadcastPDU(PDU pdu) {
		for(ObjectOutputStream obj : memory.listUsers.values()) {
			try {
				obj.writeObject(pdu);
				obj.flush();
				System.out.println("[nearTweet Server] Sending pdu for user: " + this.memory.GetUserID(obj));
			}
			catch (IOException e) {
				System.out.println("[nearTweet Server] Removing User: " + this.memory.GetUserID(this.connection));
				abortThread();
			}
		} 
	}
	
	public void sendDirectedPDU(PDU pdu, List<String> users) {
		for(String userId : users) {
			sendDirectedPDU(pdu, userId);
		}
	}
	
	public void setContextThread(Thread context) {
		this.currentContext = context;
	}
	
	@Override
	public void run() {
		try {
			System.out.println("Openning connection with port number: " + socket.getPort());
			ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
			while (true) {
				try {
					PDU pdu = (PDU) objectInputStream.readObject();
					pdu.accept(this.dispatcher);
				} catch (IOException e) {
					abortThread();
					return;
				} catch (ClassNotFoundException e) {
					abortThread();
					return;
				}
			}
		} catch (IOException e) {
			abortThread();
		}
	}
	
	/**
	 * When a connection is closed we need to kill the thread that was handling that socket in
	 * order to keep the broadcast behavior safe
	 */
	private void abortThread() {
		if (this.currentContext ==  null) {
			System.exit(-1);
		} else {
			System.out.println("[nearTweet Server] Removing User: " + this.memory.GetUserID(this.connection));
			System.out.println("[nearTweet Server] Closing Connection: " + this.socket.getPort());
			this.memory.RemoveUser(this.connection);
			this.currentContext.interrupt();
		}
	}
}

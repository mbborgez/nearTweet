package main.java.com.cm.server.handlers;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import main.java.com.cm.server.utils.Database;
import main.java.com.cm.server.visitors.ServerDispatcher;

import pt.utl.ist.cm.neartweetEntities.pdu.PDU;


public class RequestHandler implements Runnable {
	
	public Socket socket;
	public Database memory;
	private ServerDispatcher dispatcher;
	public ObjectOutputStream connection;

	public RequestHandler(Socket socket, Database memory) throws IOException {
		this.socket = socket;
		this.memory = memory;
		this.dispatcher = new ServerDispatcher(this);
		this.connection = new ObjectOutputStream(socket.getOutputStream());
	}
	
	public void sendPollResponse(PDU pdu, ObjectOutputStream stream) {
		try {
			stream.writeObject(pdu);
			stream.flush();
		} 
		catch (IOException e) {
			this.memory.RemoveUser(stream);
			System.err.println("Removed user with ID: " + memory.GetUserID(stream));
		}
	}
	
	public void sendTweetToList(PDU pdu) {
		for(ObjectOutputStream obj : memory.listUsers.values()) {
			try {
				obj.writeObject(pdu);
				obj.flush();	
			}
			catch (IOException e) {
				System.err.println("Removed user with ID: " + memory.GetUserID(obj));
				this.memory.RemoveUser(obj);
			}
		} 
	}
	
	@Override
	public void run() {
		try {
			System.out.println("Socket connected on port: " + socket.getPort());
			ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
			while (true) {
				try {
					PDU pdu = (PDU) objectInputStream.readObject();
					pdu.accept(this.dispatcher);
				} catch (IOException e) {
					e.printStackTrace();
//					continue;
					//TODO: disconnect user
					System.exit(-1);
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
					System.exit(-1);
					//TODO: disconnect user
					//					continue;
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.exit(-1);
		}
	}
}

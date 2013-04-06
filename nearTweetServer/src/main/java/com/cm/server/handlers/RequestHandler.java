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
	private Thread currentContext;

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
			try {
				stream.close();
				this.memory.RemoveUser(stream);
			} catch (IOException e1) {
				abortThread();
			}
		}
	}
	
	public void sendTweetToList(PDU pdu) {
		for(ObjectOutputStream obj : memory.listUsers.values()) {
			try {
				obj.writeObject(pdu);
				obj.flush();	
			}
			catch (IOException e) {
				try {
					obj.close();
					this.memory.RemoveUser(obj);
					System.err.println("Removed user with ID: " + memory.GetUserID(obj));
				} catch (IOException e1) {
					abortThread();
				}
			}
		} 
	}
	
	public void setContextThread(Thread context) {
		this.currentContext = context;
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
		System.err.println("Aborting Thread " + this.currentContext.getId());
		if (this.currentContext ==  null) {
			System.exit(-1);
		} else {
			this.currentContext.interrupt();
		}
	}
}

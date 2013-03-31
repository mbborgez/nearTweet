package main.java.com.cm.server;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

import main.java.com.cm.server.handlers.RequestHandler;
import main.java.com.cm.server.utils.Database;

public class ServerProcess 
{
	private int port;
    private String server;
    private Database memory;
    private ServerSocket serverSocket;
    private InetAddress myInnetInfo;
	
	public ServerProcess(String serverName, int port) {
		try {
			this.myInnetInfo = InetAddress.getLocalHost();
			this.memory = new Database();
			this.server = serverName;
			this.port = port;
		}
		catch (java.io.IOException e) {
			System.err.println("An error ocurred while getting InetAddress: " + e.getMessage()); 
			System.exit(-1);
		}
		
	}
	
	/**
	 * make all the surround procedures to push 
	 * the server up running.
	 * For Each connection is created a new Thread
	 * with a Request Handler on it
	 */
	public void run() {
		try {
			startingMessage();
			this.serverSocket = new ServerSocket(port);
			while(true) {
				Socket socket = this.serverSocket.accept();
				(new Thread(new RequestHandler(socket, this.memory))).start();
			}
		}
		catch (IOException e) {
			System.err.println("Fatal error ocurred: " + e);
		}
	}
	
	/**
	 * Close all connections and shutdown the server
	 */
	public void close() {
		try {
			this.serverSocket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Logging Message
	 */
	private void startingMessage() {
		System.out.println("########## Server Information ##########");
		System.out.println("########## Server Name: "+ this.server);
		System.out.println("########## Port in Use: "+ this.port);
        System.out.println("########## HostName: "+ this.myInnetInfo.getHostName());
        System.out.println("########## HostAddress: "+ this.myInnetInfo.getHostAddress());
		System.out.println("############# Server Ready #############");
	}
}
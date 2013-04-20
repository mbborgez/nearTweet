package main.java.com.cm.server;

import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;

import main.java.com.cm.server.handlers.RequestHandler;
import main.java.com.cm.server.utils.Database;

public class ServerProcess
{
	private int port;
    private String server;
    private Database memory;
    private ServerSocket serverSocket;

	public ServerProcess(String serverName, int port) {
		this.memory = new Database();
		this.server = serverName;
		this.port = port;
	}

	/**
	 * make all the surround procedures to push
	 * the server up running.
	 * For Each connection is created a new Thread
	 * with a Request Handler on it
	 */
	public void run() {
		Thread t;
		RequestHandler handler;
		try {
			this.serverSocket = new ServerSocket(this.port, 300, findEthernetIP());
			startingMessage();
			while(true) {
				try {
					Socket socket = this.serverSocket.accept();
					handler = new RequestHandler(socket, this.memory);
					t = new Thread(handler);
					handler.setContextThread(t);
					t.start();
				} catch(Exception e) {
					System.err.println("[nearTweet Server]" + " Something went wrong with this connection: " + e.getClass());
					continue;
				}
			}
		}
		catch (IOException e) {
			System.err.println("Fatal error ocurred: " + e);
			close();
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
        System.out.println("########## HostName: "+ this.serverSocket.getInetAddress().getHostName());
        System.out.println("########## HostAddress: "+ this.serverSocket.getInetAddress().getHostAddress());
		System.out.println("############# Server Ready #############");
	}

	/**
	 * Not the beautiful method to get our IP Address [We Accept suggestions to improve it]
	 * @return
	 * @throws UnknownHostException
	 * @throws SocketException
	 */
	private InetAddress findEthernetIP() throws UnknownHostException, SocketException {
		byte [] ipBytes = null;
		Enumeration<NetworkInterface> n = NetworkInterface.getNetworkInterfaces();
		while (n.hasMoreElements()) {
			NetworkInterface e = n.nextElement();
            if (e.getDisplayName().equals("wlan0")) {
            	Enumeration<InetAddress> a = e.getInetAddresses();
                while (a.hasMoreElements()) {
                	InetAddress addr = a.nextElement();
                    ipBytes = addr.getAddress();
                }
            }
		}
		return InetAddress.getByAddress(ipBytes);
	}
}
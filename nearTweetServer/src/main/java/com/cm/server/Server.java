package main.java.com.cm.server;


public class Server 
{	
	public static void main(String args[])
	{
		ServerProcess server = new ServerProcess(args[0], 8000);
		server.run();
	}
}
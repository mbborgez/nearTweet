package main.java.com.cm.server;


public class Server 
{	
	public static void main(String args[])
	{
		String serverName;
		if(args.length<1){
			serverName = "DEFAULT_SERVER_NAME";
		} else {
			serverName = args[0];
		}
		ServerProcess server = new ServerProcess(serverName, 8000);
		server.run();
	}
}

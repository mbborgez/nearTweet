import java.io.IOException;

import Server.ServerProcess;

public class Server 
{	
	public static void main(String args[]) throws IOException
	{
		ServerProcess server = new ServerProcess("NearTweetServer", 8000);
		server.run();
		server.GetServerSocket().close();
	}
}

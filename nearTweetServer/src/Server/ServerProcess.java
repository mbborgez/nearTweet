package Server;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerProcess 
{
    private InetAddress myInnetInfo;
    private String server;
    private int port;
    private ServerSocket serverSocket;
    private TempMemory memory;
	
	public ServerProcess(String serverName, int port)
	{
		try
		{
			this.myInnetInfo = InetAddress.getLocalHost();
			this.server = serverName;
			this.port = port;
			this.memory = new TempMemory();
		}
		catch (java.io.IOException e) 
		{
			System.err.println("An error ocurred while getting InetAddress: " + e); 
			System.exit(-1);
		}
		
	}

	public ServerSocket GetServerSocket()
	{
		return this.serverSocket;
	}
	
	public void run()
	{
		try 
		{
			System.out.println("########## Server Information ##########");
			System.out.println("########## Server name: "+ server);
			System.out.println("########## Port Used: "+ port);
            System.out.println("########## HostName: "+ myInnetInfo.getHostName());
			System.out.println("############# Server Ready #############");
            
			serverSocket = new ServerSocket(port);

			while(true) 
			{
				Socket socket = serverSocket.accept();
				(new Thread(new RequestHandler(socket, memory))).start();
			}
		}
		catch (IOException e) 
		{
			System.err.println("Fatal error ocurred: " + e);
		}
	}
}

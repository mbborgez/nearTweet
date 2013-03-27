package Server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import PDU.PDU;
import PDU.PDUVisitor.ClientServerVisitor;

public class RequestHandler implements Runnable
{
	public Socket socket;
	public ObjectOutputStream objectOutputStream;
	public TempMemory memory;

	public RequestHandler(Socket socket, TempMemory memory) throws IOException
	{
		this.socket = socket;
		this.memory = memory;
		objectOutputStream = new ObjectOutputStream(socket.getOutputStream());


	}
	
	public void sendPollResponse(PDU pdu, ObjectOutputStream stream) 
	{
		try 
		{
			stream.writeObject(pdu);
			stream.flush();
		} 
		catch (IOException e) 
		{
			this.memory.RemoveUser(stream);
			System.err.println("Removed user with ID: " + memory.GetUserID(stream));
		}
	}
	
	public void sendTweetToList(PDU pdu)
	{
		for(ObjectOutputStream obj : memory.listUsers.values())
		{
			try
			{
				obj.writeObject(pdu);
				obj.flush();	
			}
			catch (IOException e) 
			{
				System.err.println("Removed user with ID: " + memory.GetUserID(obj));
				this.memory.RemoveUser(obj);
			}
		} 
	}
	
	@Override
	public void run() 
	{
		try
	    {
			System.out.println("Socket connected on port: " + socket.getPort());
			ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
			while (true) 
			{
				PDU pdu = (PDU) objectInputStream.readObject();
				pdu.accept(new ClientServerVisitor(this));
			}
		}
		catch (java.io.EOFException e) 
		{ 
			System.out.println("Request handler for socket " + socket + " shutting down!" + e); 
		}
		catch (java.io.IOException e)  
		{ 
			System.err.println("Request handler for socket " + socket + " shutting down!" + e); 
		}
		catch (java.lang.ClassNotFoundException e)  
		{ 
			System.err.println("Unrecognized data format in socket: " + e); 
		}
		catch (java.lang.ClassCastException e) 
		{ 
			System.err.println("Unrecognized data format in socket: " + e + " " + socket); 
		}
	}
}
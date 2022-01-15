package sockets.programming;



import java.net.*; 
import java.io.*; 

public class Client 
{ 
	
	private Socket socket;
	private BufferedReader input;
	private DataOutputStream out;

	
	public Client(String address, int port) 
	{ 
		
		try
		{ 
			
			socket = new Socket(address, port); 
			System.out.println("Connected"); 

			
			input = new BufferedReader(new InputStreamReader(System.in));

			
			out = new DataOutputStream(socket.getOutputStream()); 
		} 
		catch(UnknownHostException u) 
		{ 
			System.out.println(u); 
		} 
		catch(IOException i) 
		{ 
			System.out.println(i); 
		} 

		// string to read message from input 
		String line = ""; 

		 
		while (!line.equals("LEAVE")) 
		{ 
			try
			{ 
				line = input.readLine(); 
				
				out.writeUTF(line); 
			} 
			catch(IOException i) 
			{ 
				System.out.println(i); 
			} 
		} 

		// close the connection 
		try
		{ 
			input.close(); 
			out.close(); 
			socket.close(); 
		} 
		catch(IOException i) 
		{ 
			System.out.println(i); 
		} 
	} 

	public static void main(String args[]) 
	{ 
		Client client = new Client("127.0.0.1", 6666); 
	} 
} 
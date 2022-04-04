//import the required packages
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import javax.json.Json;
import javax.json.JsonObject;
import java.net.Socket;
//create a class 
public class ChatNode extends Thread {
	private BufferedReader reader;
	public ChatNode(Socket socket) throws IOException {
		reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));	
	}
//run() is invoked from with a newly spawned json object
	public void run() 
	{
		boolean flag = true;
		while(flag) 
		{
			try 
			{
				JsonObject jsonobject = Json.createReader(reader).readObject();
				if(jsonobject.containsKey("username")) {
					System.out.println("[" + jsonobject.getString("username") + "]: "+ jsonobject.getString("message"));
				}
			}
//print the debugging info for any exception occured!
			catch(Exception e) {
		  System.out.println("One of the connection terminated");
				flag = false;
				interrupt();
			}
		}
	}
}


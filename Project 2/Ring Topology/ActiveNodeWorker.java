//import the required packages
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
//create a class
public class ActiveNodeWorker extends Thread {

	public ActiveNode node;
	public Socket socket;
	public PrintWriter stream;

	public ActiveNodeWorker(Socket socket, ActiveNode activeNode) {
		this.node = activeNode;
		this.socket = socket;
	}
//method to retrieve both the input and output data.
	public PrintWriter getPrintWriter() {
		return stream;
	}
//run() is invoked from with a newly spawned thread and reading the text from a character-based input stream
	public void run() {
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
			this.stream = new PrintWriter(socket.getOutputStream(), true);
			
			while(true) {
				String str = reader.readLine();
			
				node.sendMessage(str);
				
			}
			
		}
//print the debugging info for any exception occured!
		catch(Exception e) {
      System.out.println("ERROR! ActiveNodeWorker: void run()");
			node.getServlets().remove(this);
		}
	}

}

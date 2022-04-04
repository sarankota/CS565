//import packages
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;
//create a class
public class Peer {
	public static void main(String args[]) throws Exception {
		Scanner scanner = new Scanner(System.in);
		String input;
		String inpu;
		String message = "";
//create objects
		ActiveNode activeNode;
		NodeInfo node = new NodeInfo();
		Join joinrequest= new Join();
		Leave leaves=new Leave();
		MessageContent note = new MessageContent();
		System.out.println("Ring topology p2p chat-room");
		System.out.println("===========================");
		input = scanner.nextLine();
		String[] arrOfStr = input.split(" ", 3);
		if( arrOfStr[0].equalsIgnoreCase("JOIN")) { 
			BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
			node.Name = arrOfStr[1];
			node.Port = Integer.parseInt(arrOfStr[2]);
			activeNode = new ActiveNode(node.Port);
			activeNode.start();
//update only the successor
			node.next = false;
			System.out.println("Joined sucessfully!!");
			System.out.println("Enter the name and port number you want to connect with?");
			String inputs = reader.readLine();
			joinrequest.Listener(inputs, node);
//if successfully joined
			if(joinrequest.hasJoined == true) {
				inpu = scanner.nextLine();
//if user entered NOTE
				if(inpu.equalsIgnoreCase("NOTE")) {
					note.Communicate(reader, node.Name, activeNode, node);  
				}
//if user entered SHUTDOWN
				else if(inpu.equalsIgnoreCase("SHUTDOWN")) {
					leaves.shutdown(node.Port, activeNode);
				}
//if user entered SHUTDOWNALL
				else if(inpu.equalsIgnoreCase("SHUTDOWNALL")){
					shutdownall.leav();
				}
//if user entered LEAVE
				else if(inpu.equalsIgnoreCase("LEAVE"))
				{
					leaves.leave(node.Port, activeNode);
					
					

					
				}
			}
		
		}
		
		scanner.close();
	}
	
	public String mymsg(BufferedReader reader) throws IOException {
		String msg;
		msg = reader.readLine();
		return msg;
	}

}
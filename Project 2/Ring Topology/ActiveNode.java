//import the required packages
import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;

//create a class to deal the active peer

public class ActiveNode extends Thread {

  NodeInfo node = new NodeInfo();

  int activePort;
  boolean active=false;
  private ServerSocket serverSocket;
  public ArrayList<ActiveNodeWorker> servlets = new ArrayList<ActiveNodeWorker>();

  public ActiveNode(int port) throws IOException {
    this.activePort = port;
    this.serverSocket = new ServerSocket(port);
  }
//method to retrieve the active peer
  public int getActiveNode() {
    return activePort;
  }
//run() is invoked from with a newly spawned thread
  public void run() {
    try {
      while(true) {
      
        ActiveNodeWorker i = new ActiveNodeWorker(serverSocket.accept(), this);
      
        i.node.active=true;
      
        servlets.add(i);
       
        i.start();
        
      }
    }
//print the debugging info for any exception occured!
    catch(Exception e) {
      System.out.println("ERROR! ActiveNode: void run()");
      e.printStackTrace();
    }    	
  }

  public void sendMessage(String msg) {
    try {
      
      servlets.forEach(i->{
        if(i.node.active)
        {
          i.getPrintWriter().println(msg);
          
        }
      });
    }
//print the debugging info for any exception occured!
    catch(Exception e) {
      System.out.println("ERROR! ActiveNode: void sendMessage()");
      e.printStackTrace();
    }
  }
  
  public ArrayList<ActiveNodeWorker> getServlets(){
      return servlets;  	
  }
      
  boolean Join(String name,int port) {		
    return true;
  }
	
}

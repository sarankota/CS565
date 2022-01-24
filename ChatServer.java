/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chatapp;

//importing methods and classes
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author lenovo
 */
public class ChatServer {
    
    //connection related variables
    private static int uniqueId;
    private ArrayList<ClientThread> al;
    private boolean keepGoing;
    private int port;
    private ServerSocket serverSocket;

    //constructor
    public ChatServer(int port)
    {
        this.port = port;
        //ArrayList to contain all participants information
        al = new ArrayList<ClientThread>();
    }
    // start server
    public void start() throws IOException
    {
        this.keepGoing = true;
        try{
          serverSocket = new ServerSocket(port);
          while(this.keepGoing){
              display("Server waiting for Clients on port " + port + ".");
              Socket socket = serverSocket.accept();
              if(!this.keepGoing)
                  break;
              ClientThread t = new ClientThread(socket);
              al.add(t);
              t.start();
           }
        }   
        catch(IOException e){
            display("SERVER TERMINATED");
            
        }
   }
    // stop server
    public void stopServer(){
        try{
              serverSocket.close();
              for(int i = 0;i<al.size();i++){
                  ClientThread tc = al.get(i);
                  try{
                      tc.sInput.close();
                      tc.sOutput.close();
                      tc.socket.close();
                  }
                  catch(IOException e){
                      
                  }
              }
           }
          catch(Exception e){
                      display("Exception closing the server and clients: " );
                      }
       
    }    
        
// display the message     
private void display(String msg){
    System.out.println(msg);
}
     
private synchronized void broadcast(String message){
   // System.out.println(message+"\n");
    for(int i = al.size(); --i >= 0;){
        ClientThread ct = al.get(i);
        if(!ct.writeMsg(message+"\n")){
            al.remove(i);
            display("Disconnected client "+ct.username+" removed from list");
        }
      
    }
}
synchronized void remove(int id) {
     for(int i = 0; i < al.size(); ++i) {
         ClientThread ct = al.get(i);
         if(ct.id == id) {
           al.remove(i);
           return;
       }
     }
}
//main 
public static void main(String[] args) throws IOException {
    int portNumber = 8080;
    ChatServer server = new ChatServer(portNumber);
    server.start();

}
public class ClientThread  extends Thread{
    Socket socket;
    ObjectInputStream sInput;
    ObjectOutputStream sOutput;
    int id;
    String username;
    ChatMessage cm;

    ClientThread(Socket socket) throws IOException{
      id = ++uniqueId;
      this.socket = socket;
      try{
        sOutput = new ObjectOutputStream(socket.getOutputStream());
        sInput  = new ObjectInputStream(socket.getInputStream());
        username = (String) sInput.readObject();
        display(username + " joined chat.");
      }
     catch (IOException e) {
         display("Exception creating new Input/output Streams: " );
         return;
     }
      catch (ClassNotFoundException e) {
      }
    }
    public void run() {
        boolean keepGoing = true;
        while(keepGoing) {
            try {
                cm = (ChatMessage) sInput.readObject();
            }
            catch (IOException e) {
              //display(username + " Exception reading Streams: " +e );
               break;
            }
           catch(ClassNotFoundException e2) {
               break;
           }
        String message = cm.getMessage();
        switch(cm.getType()) {
            // client sending the message
            case ChatMessage.MESSAGE:
              broadcast(username + ": " + message);
              break;
            case ChatMessage.SHUTDOWN:
              //message to be displayed on the client side
              broadcast(username + " disconnected with a SHUTDOWN message.");
              //message to be displayed on the server side
              System.out.println(username+" SHUTDOWN");
               keepGoing = false;
              break;  
            // when the users enters LEAVE  
           case ChatMessage.LEAVE:
              //message to be displayed on the client 
              broadcast(username + " disconnected with a LEAVE message.");
              //message to be displayed on the server
              System.out.println(username+" LEFT");
              for(int i = 0; i < al.size(); ++i) {
                   ClientThread ct = al.get(i);
                   if(ct.username.equalsIgnoreCase(username)){
                       remove(ct.id);
                   }
              }
             
              break; 
             //when SHUTDOWNALL is entered 
            case ChatMessage.SHUTDOWNALL:
                //writeMsg("List of the users connected \n"); 
                for(int i = 0; i < al.size(); ++i) {
                    ClientThread ct = al.get(i);
                     broadcast(ct.username+" disconnected with a SHUTDOWNALL message");
                }
                // terminate server
                
                stopServer();
                
                break;
          }
     }
        remove(id);
        close();
    }
    public void close(){
        try{
            if(sOutput!=null)
                sOutput.close();
        }
        catch(IOException e){
            
        }
        try{
            if(sInput!=null)
                sInput.close();
        }
        catch(IOException e){
            
        }
        try{
            if(socket!=null)
                socket.close();
        }
        catch(Exception e){
            
        }
    }
    private boolean writeMsg(String msg) {
        if(!socket.isConnected()) {
            close();
            return false;
        }
        try{
            sOutput.writeObject(msg);
        }
        catch(IOException e) {
            display("Error sending message to " + username);
             display(e.toString());
        }
     return true;

    }
        
    }
  }

    
 


    

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chatapp;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.Properties;
import java.util.Scanner;

/**
 *
 * @author lenovo
 */
public class ChatClient {
    private ObjectInputStream sInput; 
    private ObjectOutputStream sOutput;
    private Socket socket;
    private String server, username;
    private int port;
    
    ChatClient(String server, int port, String username){
        this.server = server;
        this.port = port;
        this.username = username;
    }
    public boolean start() throws IOException{
       try {
           socket = new Socket(server, port);
       }
       catch(Exception ec) {
        display("Error connectiong to server:" + ec);
        return false;
       }
       String msg = "Connection accepted " + socket.getInetAddress() + ":" + socket.getPort();
       display(msg);
       try{
         sInput  = new ObjectInputStream(socket.getInputStream());
         sOutput = new ObjectOutputStream(socket.getOutputStream());
       }
       catch (IOException eIO) {
           display("Exception creating new Input/output Streams: " + eIO);
           return false;
       }
       new ListenFromServer().start();
       try{
           sOutput.writeObject(username);
       }
       catch(IOException eIO) {
           display("Exception doing login : " + eIO);
           disconnect();
           return false;
       }   
return true;
     }
    
     private void display(String msg) {
         System.out.println(msg); 
     }
     void sendMessage(ChatMessage msg) {
         try{
           if(socket==null)
               System.out.println("Please Register before sending message");
           else
            sOutput.writeObject(msg);
         }
         catch(IOException e) {
             display("Exception writing to server: " + e);
         }
   }
     private void disconnect() {
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

public static void main(String[] args) throws IOException {
    Properties prop = new Properties();
    FileReader reader= new FileReader("src\\chatapp\\conn.properties");
    prop.load(reader);
    int portNumber=Integer.parseInt(prop.getProperty("port"));
    String serverAddress = prop.getProperty("ipaddress");
    System.out.println("Enter UserName");
    Scanner in = new Scanner(System.in);
    String userName=in.nextLine();
   ChatClient client = new ChatClient(serverAddress, portNumber, userName);
//   if(!client.start())
//     return;
   while(true){
       System.out.print("> ");
      String msg = in.nextLine();
      if(msg.equalsIgnoreCase("JOIN 127.0.0.1 8080")){
          if(!client.start())
               return;
      }
      else if(msg.equalsIgnoreCase("LEAVE")) {
         client.sendMessage(new ChatMessage("",ChatMessage.LEAVE));
         
        // break;
      }
      else if(msg.equalsIgnoreCase("SHUTDOWN")) {
         client.sendMessage(new ChatMessage("",ChatMessage.SHUTDOWN));
         break;
      }
     else if(msg.equalsIgnoreCase("SHUTDOWNALL")) {
          client.sendMessage(new ChatMessage("",ChatMessage.SHUTDOWNALL)); 
     }
    else {
         client.sendMessage(new ChatMessage(msg,ChatMessage.MESSAGE));
     }
   }
client.disconnect();
}
class ListenFromServer extends Thread {
    public void run() {
        while(true){
            try{
               
               String msg = (String) sInput.readObject();
               System.out.println(msg);
               
               System.out.print("> ");
                
            }
          catch(IOException e) {
             display("Server has close the connection: " + e);
             break; 
          }
          catch(ClassNotFoundException e) {
              
          } 
        }
    }

}

}

 
    
        
  



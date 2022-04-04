/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

 // Import the required packages
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

 //create a class that allows mutiple clients to connect to the server given ipaddress,port
public class ChatClient {
    private static int count;
    private ObjectInputStream sInput; 
    private ObjectOutputStream sOutput;
    private Socket socket;
    private String server, username;
    private int port;
    
    ChatClient(String server, int port, String username){
        // Refer to the current objects that we are dealing with
        this.server = server;
        this.port = port;
        this.username = username;
        count++;
    }
    public boolean start() throws IOException{
        // Implement a try catch to check proper connection to server
       try {
           socket = new Socket(server, port);
       }
       catch(Exception ec) {
        display("Error connectiong to the server:");
        return false;
       }
       String msg = "Connection accepted " + socket.getInetAddress() + ":" + socket.getPort();
       display(msg);
       // Implement a try catch to check if socket is connected and is listening from client
       try{
         sInput  = new ObjectInputStream(socket.getInputStream());
         sOutput = new ObjectOutputStream(socket.getOutputStream());
       }
       catch (IOException eIO) {
           display("Exception creating new Input/output Streams: " );
           return false;
       }
       new ListenFromServer().start();
       try{
           sOutput.writeObject(username);
       }
       catch(IOException eIO) {
           display("Exception doing login : " );
           disconnect();
           return false;
       }   
return true;
     }
    // method to display
     private void display(String msg) {
         System.out.println(msg); 
     }
     //method to send message to another client via server
     void sendMessage(ChatMessage msg) {
         try{
           if(socket==null)
               System.out.println("JOIN the chat before sending message");
           else
            sOutput.writeObject(msg);
         }
         catch(IOException e) {
             display("Exception writing to server: " );
         }
   }
   //method to cutoff from server
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

//initiate main function
    public static void main(String[] args) throws IOException {
    Properties prop = new Properties();
    //load the connection properties file that has ip address and port.
    FileReader reader= new FileReader("/Users/sarankota/Documents/Spring 2022/CS 565/Project 1.1/Chat Central Server and Client/src/chatapp/conn.properties");
    prop.load(reader);
    int portNumber=Integer.parseInt(prop.getProperty("port"));
    String serverAddress = prop.getProperty("ipaddress");
    System.out.println("Enter UserName");
    Scanner in = new Scanner(System.in);
    String userName=in.nextLine();
   ChatClient client = new ChatClient(serverAddress, portNumber, userName);
   while(true){
       System.out.print("> ");
      String msg = in.nextLine();
      //when to start client joining
      if(msg.equalsIgnoreCase("JOIN 127.0.0.1 8080")){
          if(!client.start())
               break;
      }
      //what to do for SHUTDOWN command
      else if(msg.equalsIgnoreCase("SHUTDOWN")) {
         client.sendMessage(new ChatMessage("",ChatMessage.SHUTDOWN));
          break;
      }
      //what to do for SHUTDOWNALL command
     else if(msg.equalsIgnoreCase("SHUTDOWNALL")) {
          client.sendMessage(new ChatMessage("",ChatMessage.SHUTDOWNALL));
          
           
        }
        //what to do for LEAVE command
      else if(msg.equalsIgnoreCase("LEAVE")) {
         client.sendMessage(new ChatMessage("",ChatMessage.LEAVE));

      }
    else {
         client.sendMessage(new ChatMessage(msg,ChatMessage.MESSAGE));
     }
   }
client.disconnect();
}
//create a class that can listen from the server via another client.
class ListenFromServer extends Thread {
    public void run() {
        while(true){
            try{
               
               String msg = (String) sInput.readObject();
               System.out.println(msg);
               
               System.out.print("> ");
                
            }
          catch(IOException e) {
             display("connection terminated");
             break; 
          }
          catch(ClassNotFoundException e) {
              
          } 
        }
    }

}

}

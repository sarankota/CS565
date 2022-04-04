/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chatapp;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

/**
 *
 * @author lenovo
 */
//Create a class for handling client by implementing runnable interface.
public class ClientHandler  implements Runnable{
    public static ArrayList<ClientHandler> clients = new ArrayList<ClientHandler>();
    private ObjectInputStream objInStr;
    private ObjectOutputStream objOutStr;
    private Socket socket;
    private String clientName;
    
  public ClientHandler(Socket socket)
  {
      try{
          this.socket = socket;
          objInStr=new ObjectInputStream(new DataInputStream(socket.getInputStream()));
          objOutStr=new ObjectOutputStream(new DataOutputStream(socket.getOutputStream()));
          
       }
      catch(IOException e){
          closeEverything(socket,objInStr,objOutStr);
      }
     
  }
  
  
      @Override
    public void run() {
        String msgFromClient;
        //tell socket what to do
        while(socket.isConnected()){
        try{
         Message msg= (Message)objInStr.readObject();
         //when JOIN command is given by user
          if(msg.getType().equals("JOIN"))
        {
          NodeInfo nodeInfo=(NodeInfo) msg.getContent();
          clientName= nodeInfo.getName();
          clients.add(this);
          broadCastMessage("SERVER "+clientName+" has entered the chat");
        }
        //when NOTE command is given by user
          else if(msg.getType().equals("NOTE")){
              msgFromClient=(String)msg.getContent();
              broadCastMessage(msgFromClient);
          }
          //when LEAVE command is given by user
           else if(msg.getType().equals("LEAVE"))
        {
           NodeInfo nodeInfo=(NodeInfo) msg.getContent();
           removeClientHandler();  
        }
       
    }
        catch(IOException e){
            closeEverything(socket,objInStr,objOutStr);
            break;
        }
    catch(ClassNotFoundException e){
        
    }    
    }
    }
    
    public void broadCastMessage(String msgToSend){
        
        for(ClientHandler clientHandler:clients){
            try{
            clientHandler.objOutStr.writeObject(new Message(msgToSend,MessageTypes.NOTE));
            }
            catch(IOException e){
                closeEverything(socket,objInStr,objOutStr);
            }
        }
        
    }
    
    public void removeClientHandler(){
        clients.remove(this);
        broadCastMessage("SERVER "+clientName+" has left the chat");
        
    }
    
    public void closeEverything(Socket socket,ObjectInputStream objInStr,ObjectOutputStream objOutStr){
        removeClientHandler();
        try{
            if(objInStr !=null){
                objInStr.close();
            }
            if(objOutStr != null){
                objOutStr.close();
            }
            if(socket!=null)
            {
                socket.close();
            }
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }
}

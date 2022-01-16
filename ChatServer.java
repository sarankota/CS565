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
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class ChatServer {

    public static void main(String[] args) throws Exception {
        ServerSocket sSocket = new ServerSocket(8080); 
        Socket soc=sSocket.accept();
        DataInputStream inStrm = new DataInputStream(soc.getInputStream()); 
        DataOutputStream outStrm = new DataOutputStream(soc.getOutputStream()); 
        ObjectInputStream objInStr = new ObjectInputStream(inStrm); 
        ObjectOutputStream objOutStr = new ObjectOutputStream(outStrm);
        ArrayList<String> clients = new ArrayList<String>();
        Message msg=null;
        while(true){
            try{
        msg =(Message) objInStr.readObject();
            }
            catch(IOException e){
                System.out.println(e.getMessage());
            }
        String name="";
        if(msg.getType().equals("JOIN"))
        {
          NodeInfo nodeInfo=(NodeInfo) msg.getContent();
          name= nodeInfo.getName();
          //System.out.println(name);
          clients.add(name);
        }
        else if(msg.getType().equals("LEAVE"))
        {
           NodeInfo nodeInfo=(NodeInfo) msg.getContent();
           name= nodeInfo.getName();
           clients.remove(name);
          
           //System.out.println(b);
        }
        else {
                for(String str:clients)
                 objOutStr.writeObject(new Message(str+":"+msg.getContent(),MessageTypes.NOTE));
        }
       
        
        if(clients.size()==0)
          break;
      }
        sSocket.close();
    }
    
}

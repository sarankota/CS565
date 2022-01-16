/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chatapp;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Properties;
import java.util.Scanner;

/**
 *
 * @author lenovo
 */
public class ChatClient {
    
    public static void main(String [] args) throws Exception{
        Properties prop = new Properties();
        //FileReader reader = new FileReader("src\\chatapp\\conn.properties");
        FileReader reader = new FileReader("/Users/sarankota/Downloads/ChatApp/src/chatapp/conn.properties");
        prop.load(reader);
        String ip=prop.getProperty("ipaddress");
        String port=prop.getProperty("port");
        Socket socket = new Socket(ip, Integer.parseInt(port)); 
        Scanner in=null;
        DataOutputStream dOut = new DataOutputStream(socket.getOutputStream()); 
        DataInputStream dInp = new DataInputStream(socket.getInputStream()); 
        ObjectOutputStream mapOpStream = new ObjectOutputStream(dOut);
        ObjectInputStream mapIpStream = new ObjectInputStream(dInp);
        int i=0;
        String name="";
        while(true){
        
        in = new Scanner(System.in); 
        System.out.println("\n Please choose from one of the options :");
        System.out.println("\n 1. JOIN \n 2. LEAVE \n 3. SHUTDOWN\n 4. SEND TEXT");
        int choice = in.nextInt();
                     
        Message msg=null;
        if(choice==1){
            name="Client "+i;
            i++;
            NodeInfo nodeInfo = new NodeInfo(ip,Integer.parseInt(port),name);
            msg=new Message(nodeInfo,MessageTypes.JOIN);
        }
        else if(choice==2){
          NodeInfo nodeInfo = new NodeInfo(ip,Integer.parseInt(port),name);
          msg=new Message(nodeInfo,MessageTypes.LEAVE);  
        }
        else if(choice==3){
            NodeInfo nodeInfo = new NodeInfo(ip,Integer.parseInt(port),name);
            msg=new Message(nodeInfo,MessageTypes.LEAVE);
            mapOpStream.writeObject(msg);
            socket.close();
            System.exit(0);
        }
        else if(choice==4){
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            String text = br.readLine();
            msg=new Message(text,MessageTypes.NOTE);
        }
        
        mapOpStream.writeObject(msg); 
       
        if(choice==4){
        Message inMsg= (Message)mapIpStream.readObject();
        if(inMsg.getType().equals("NOTE"))
            System.out.println(inMsg.getContent());
        }

   }

    } 
}

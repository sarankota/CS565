/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chatapp;

/**
 *
 * @author lenovo
 */
public class NodeInfo implements java.io.Serializable{
   private String IP;
   private int port;
   private String name;
   //setter
    public void setIP(String IP) {
        this.IP = IP;
    }
    //setter
    public void setPort(int port) {
        this.port = port;
    }
    //setter
    public void setName(String name) {
        this.name = name;
    }
    //getter
    public String getIP() {
        return IP;
    }
    //getter
    public int getPort() {
        return port;
    }
    //getter
    public String getName() {
        return name;
    }
    
    //constructor
    public NodeInfo(String IP, int port, String name) {
        this.IP = IP;
        this.port = port;
        this.name = name;
    }
}

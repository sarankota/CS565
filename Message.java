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
public class Message implements java.io.Serializable,MessageTypes{
    //content for certain message type
    private Object content;
    //type of message
    private String type;

    //constructor for Message
    public Message(Object content, String type) {
        this.content = content;
        this.type = type;
    }
    //getter
    public Object getContent() {
        return content;
    }
    //getter
    public String getType() {
        return type;
    }
    //setter
    public void setContent(Object content) {
        this.content = content;
    }
    //setter
    public void setType(String type) {
        this.type = type;
    }
    
    
}

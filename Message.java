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
    private Object content;
    private String type;

    public Message(Object content, String type) {
        this.content = content;
        this.type = type;
    }

    public Object getContent() {
        return content;
    }

    public String getType() {
        return type;
    }

    public void setContent(Object content) {
        this.content = content;
    }

    public void setType(String type) {
        this.type = type;
    }
    
    
}

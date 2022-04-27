package transaction.transaction_occ_lock.common;

import java.io.Serializable;
//A serialized class to send between client and server

public class Message implements Serializable {

    private int messageID;
    private int accountNumber;
    private int amount;
    private int transactionID;

   public Message(){}
    //OPEN
    public Message(int id) {
        this.messageID = id;
    }

    //READ
    public Message(int id, int accountNumber, int transactionID) {
        this.messageID = id;
        this.accountNumber = accountNumber;
        this.transactionID = transactionID;
    }
    //WRITE

    public Message(int id, int accountNumber, int amount, int transactionID) {
        this.messageID = id;
        this.accountNumber = accountNumber;
        this.amount = amount;
        this.transactionID = transactionID;
    }

    //CLOSE
    public Message(int id, int transactionID) {
        this.messageID = id;
        this.transactionID = transactionID;
    }

    public void setTransactionID(int transactionID) {
        this.transactionID = transactionID;
    }

    public int getTransactionID() {
        return transactionID;
    }

    public int getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(int accountNumber) {
        this.accountNumber = accountNumber;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getMessageID() {
        return messageID;
    }

    public void setMessageID(int messageID) {
        this.messageID = messageID;
    }

    @Override
    public String toString() {
        return "Message [messageID=" + messageID + ", accountNumber=" + accountNumber + ", amount=" + amount + "]";
    }

}

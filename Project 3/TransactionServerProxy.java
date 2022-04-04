package transaction.transaction_occ_lock.client;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import transaction.transaction_occ_lock.common.Message;
import transaction.transaction_occ_lock.common.MessageTypes;

public class TransactionServerProxy {
	Socket socket = null;
	ObjectOutputStream oos = null;
	ObjectInputStream ois = null;

	private int port;
	private String host;

	TransactionServerProxy(String host, int port) {
		this.port = port;
		this.host = host;
	}

	public int openTransaction() {
//		System.err.println("****************** OPENING TRANSACTION *****************");
		int transactionID = -1;
                Message message = new Message();
		try {
			// Initializing a client Socket
			socket = new Socket(host, port);

			// Writing to server with opening code
			oos = new ObjectOutputStream(new BufferedOutputStream(socket.getOutputStream()));
			oos.writeObject(new Message(MessageTypes.OPEN_TRANSACTION));
			oos.flush();
			// Read from server TransactionID
			ois = new ObjectInputStream(new BufferedInputStream(socket.getInputStream()));

			try {

				message = (Message) ois.readObject();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}

		} catch (IOException e) {
			e.printStackTrace();
		}

		return message.getTransactionID();

	}

	public int closeTransaction(int transactionID) {

		try {
			// Writing to server with CLOSE_TRANSACTION code
			oos.writeObject(new Message(MessageTypes.CLOSE_TRANSACTION,transactionID));
                        oos.flush();
//			System.err.println("****************** CLOSE TRANSACTION *******************");
			oos.close();
			ois.close();
			socket.close();
		} catch (IOException e) {

			e.printStackTrace();
		}
		return 0;

	}

	

	public int readTransaction(int accountNumber,int transactionID) {
		// read the server response message
//		System.err.println("****************** READ TRANSACTION *******************");
		int balance = -1;
		Message message = null;
		try {

			// Writing to server with READ_REQUEST code and AccountNumber
			
			oos.writeObject(new Message(MessageTypes.READ_REQUEST, accountNumber, transactionID));
			oos.flush();
			
			try {

				balance = (int) ois.readObject();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println("Balance of account " + accountNumber + " is -> " + balance);
		} catch (IOException e) {

			e.printStackTrace();
		}

		return balance;

	}

	public void writeTransaction(int account, int amount,int transactionID) {
//		System.err.println("****************** WRITE TRANSACTION *****************");
		// write to socket using ObjectOutputStream
		try {
			System.out.println("Writing Transaction in Account Number -> " + account + " of amount -> " + amount);
			// Writing to server with WRITE_REQUEST code and AccountNumber
			oos.writeObject(new Message(MessageTypes.WRITE_REQUEST, account, amount,transactionID));
			oos.flush();
			
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}

package transaction.transaction_occ_lock.server.transaction;

import java.util.ArrayList;
import java.util.HashMap;

import transaction.transaction_occ_lock.server.TransactionServer;

public class Transaction {
	int transactionID;
	int transactionNumber = 0;
	int lastCommittedTransactionNumber;

	private ArrayList<Integer> readSet = new ArrayList<>(); //Account Numbers
	private HashMap<Integer, Integer> writeSet = new HashMap<>();  // <Account_Number , Balance>

	private StringBuffer log = new StringBuffer("");

	
	public Transaction() {

	}

	Transaction(int transactionID, int lastCommittedTransactionNumber) {
		this.lastCommittedTransactionNumber = lastCommittedTransactionNumber;
		this.transactionID = transactionID;
	}

	public int read(int accountNumber,int transactionID) {
		log.append("Reading account Number -> "+ accountNumber);
		Integer balance;
		balance = writeSet.get(accountNumber);

		if (balance == null) {
			balance = TransactionServer.accountManager.readAccount(accountNumber);
		}
		if (!readSet.contains(accountNumber)) {
			readSet.add(accountNumber);
		}
		System.out.println("(#"+transactionID +") ---  Balance of Account Number " + accountNumber + " is " + balance);
		return balance;
	}

	public int write(int accountNumber, int balance,int transactionID) {
		log.append("Writing "+ balance+" into Account Number -> "+ accountNumber);
		int priorBalance = read(accountNumber,transactionID);

		if (!writeSet.containsKey(accountNumber)) {

		}
		TransactionServer.accountManager.writeAccount(accountNumber, balance);
		writeSet.put(accountNumber, balance);
		System.out.println("(#"+transactionID +") --  Writing "+ balance+" into Account Number -> "+ accountNumber);
		
		return accountNumber;
	}
	
	public String getLogs() {
		return log.toString();
	}

	public int getTransactionID() {
		return (int) Math.floor(Math.random() * 100000);
	}


	public int getTransactionNumber() {
		 ++transactionNumber;
		return transactionNumber;
	}

	

	public ArrayList<Integer> getReadSet() {
		return readSet;
	}

	public HashMap<Integer, Integer> getWriteSet() {
		return writeSet;
	}
	

}

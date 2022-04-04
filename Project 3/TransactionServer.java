package transaction.transaction_occ_lock.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.SocketException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import transaction.transaction_occ_lock.server.account.AccountManager;
import transaction.transaction_occ_lock.server.lock.LockManager;
import transaction.transaction_occ_lock.server.transaction.TransactionManager;
import transaction.transaction_occ_lock.util.PropertyHandler;

public class TransactionServer implements Runnable {

	public static AccountManager accountManager = null;
	public static TransactionManager transactionManager = null;
	public static LockManager lockManager = null;

	public static ServerSocket serverSocket = null;

	static boolean keepgoing = true;

	static boolean isLockingEnabled = true;

	static int messageCount = 0;

	TransactionServer() {
		Properties properties = null;

		int numberAccounts;
		int initialBalance;

		properties = PropertyHandler.readProperties();

		
		isLockingEnabled = Boolean.valueOf(properties.getProperty("LOCKING_ENABLED"));
		//Intializing Transaction Manager
                TransactionServer.transactionManager = new TransactionManager();

		numberAccounts = Integer.parseInt(properties.getProperty("NUMBER_ACCOUNTS"));
		initialBalance = Integer.parseInt(properties.getProperty("INITIAL_BALANCE"));
		//Intializing Locking Manager
		TransactionServer.lockManager = new LockManager(isLockingEnabled);
                //Initializing Account Manager
		TransactionServer.accountManager = new AccountManager(numberAccounts, initialBalance);

		try {
                       //Creating a new Server Socket..
			serverSocket = new ServerSocket(Integer.parseInt(properties.getProperty("PORT")));
			System.out.println("Server started");
			System.out.println("Waiting for a client ...");
		} catch (IOException ex) {

			System.out.println("Could not create Connection");
			System.exit(1);
		}
	}

	public static synchronized int getMessageCount() {
		return ++messageCount;
	}
        //To close the server 
	public static void shutdown() {
		try {
			keepgoing = false;
			serverSocket.close();
		} catch (IOException ex) {
			Logger.getLogger(TransactionServer.class.getName()).log(Level.SEVERE, null, ex);
		}
	}
       //Printing summary after all the threads compelted their execution
	public void printOutSummary() {
		System.out.println("PRINTOUT SUMMARY\n");
                 //Reading Total amount in all the Accounts
                 //It must be the product of (Initail_Balance * Number_Accounts)
		int total = TransactionServer.accountManager.getTotalAmountInBank();
		

		System.err.println("*****************  Total Amount in the Bank -> " + total + "\n\n");
		
                
                //Printing each Account Balance
		TransactionServer.accountManager.getAccounts().stream().forEach(each -> {
			System.out.println("(Account Number , Balance) ==> ("+ each.getAccountNumber() + " , " + each.getBalance()+")");
		});
	}

	@Override
	public void run() {

		while (keepgoing) {
			try {
                            //Calling Transaction Manager to create a separate thread
				transactionManager.runTransaction(serverSocket.accept());

			} catch (SocketException e) {
				
			} catch (IOException e) {
			
			}
		}
		try {
			Thread.sleep(100);
		} catch (InterruptedException ex) {

		}
		
                //After execution of all the threads, Print Summary
		printOutSummary();
	}
         //Running Server
	public static void main(String[] args) {
		
			new TransactionServer().run();
		
	}

}

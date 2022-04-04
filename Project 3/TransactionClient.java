package transaction.transaction_occ_lock.client;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Properties;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.IntStream;

import transaction.transaction_occ_lock.common.Message;
import transaction.transaction_occ_lock.common.MessageTypes;
import transaction.transaction_occ_lock.util.PropertyHandler;

public class TransactionClient extends Thread {
Logger logger = Logger.getLogger(TransactionClient.class.getSimpleName());
	public static int numberTransaction = 0;
	public static int numberAccounts;
	public static int initialBalance;

	public static String host;
	public static int port;

	public ArrayList<Thread> threads = new ArrayList<>();
	public static boolean restartTransactions = true;

	public TransactionClient() {

		Properties properties = null;

		try {
                       //Reading Configurable properties 
			properties = PropertyHandler.readProperties();
			host = properties.getProperty("HOST");
			port = Integer.parseInt(properties.getProperty("PORT"));
			numberAccounts = Integer.parseInt(properties.getProperty("NUMBER_ACCOUNTS"));
			initialBalance = Integer.parseInt(properties.getProperty("INITIAL_BALANCE"));
			numberTransaction = Integer.parseInt(properties.getProperty("NUMBER_TRANSACTIONS"));
			
                       
			System.out.println("Total accounts -> " + numberAccounts);
			System.out.println("Initial Balance -> " + initialBalance);
			System.out.println("Total Transactions -> " + numberTransaction);
                       
			
			
		} catch (NumberFormatException ex) {
			ex.printStackTrace();
		}

	}

	@Override
	public void run() {

		Socket dbConnection=null;
		ObjectOutputStream writeToNet;



		
		//Doing this to make the threads wait
		CountDownLatch latch = new CountDownLatch(numberTransaction);
	        ExecutorService executor = Executors.newCachedThreadPool();
	        IntStream.range(0, numberTransaction).forEach(item -> executor.execute(new TransactionThread(latch)));
	        executor.shutdown();

	    try {
                //Waiting for all threads to execute
	        latch.await();
	    } catch(InterruptedException ex) {
	        System.out.println(ex);
	    }
	

		try {
                        //Making a connection to send Server SHUTDOWN messsage
			System.out.println(" Closing Socket....");
			dbConnection = new Socket(host, port);
			writeToNet = new ObjectOutputStream(dbConnection.getOutputStream());
			writeToNet.writeObject(new Message(MessageTypes.SHUTDOWN));
			writeToNet.flush();
			dbConnection.close();
		} catch (IOException ex) {
			ex.printStackTrace();
		}

	}

        // Every client runs on separate thread
	public class TransactionThread extends Thread {
		private CountDownLatch latch;
		int numberTransaction = 0;
		
		TransactionThread(){}
		TransactionThread(CountDownLatch latch){
			this.latch = latch;
		}

		@Override
		public void run() {

			int transactionID;

			int accountFrom;
			int accountTo;

			int amount;
			int balance;

			

                        //(Math.random() * (max-min)) + min 
			accountFrom = (int) (Math.random() * (numberAccounts - 1) + 1);
			accountTo = (int) (Math.random() * (numberAccounts - 1) + 1);
			amount = (int) (Math.random() * (initialBalance - 1) + 1);

			
			
			
			try {
				Thread.sleep((int) Math.floor(Math.random() * 1000));
			} catch (InterruptedException ex) {
				Logger.getLogger(TransactionClient.class.getName()).log(Level.SEVERE, null, ex);
			}

			do {
                                //Using TransactionServerProxy to communicate 
				TransactionServerProxy transaction = new TransactionServerProxy(host, port);
                                //1. OPEN
				transactionID = transaction.openTransaction();
                                System.out.println("\n  Transaction ID ("+transactionID+") Initaited (Src, Destination) -> Amount   (" + accountFrom + ","+ accountTo+") ->  $"+amount );
                                //2. READ Source Account 
				balance = transaction.readTransaction(accountFrom,transactionID);
                                //3. WRITE Source Account 
				transaction.writeTransaction(accountFrom, balance - amount,transactionID);
                                //4. READ Destination Account 
				balance = transaction.readTransaction(accountTo,transactionID);
				//5. WRITE Destination Account 
                                transaction.writeTransaction(accountTo, balance + amount,transactionID);

				// Making a thread wait for sometime after one transaction.
				try {
					Thread.sleep((int) Math.floor(Math.random() * 1000));
				} catch (InterruptedException ex) {
					Logger.getLogger(TransactionClient.class.getName()).log(Level.SEVERE, null, ex);
				}
                                //6. CLOSE 
				  transaction.closeTransaction(transactionID);

				numberTransaction++;
			} while (numberTransaction < this.numberTransaction);
					

	        this.latch.countDown();
		}
	}

	public static void main(String[] args) {

		new TransactionClient().run();

	}
}

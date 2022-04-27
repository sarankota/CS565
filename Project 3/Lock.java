
package transaction.transaction_lock.server.lock;


public class Lock {

    private int currentTransactionID = -1;
    private int lockRequestors    = 0;
    

  public synchronized void acquire(int transactionID) throws InterruptedException{
    lockRequestors++;
    //Dont let the current transaction wait if there is some Transaction already waiting for lock to release
   if(lockRequestors > 1){
       System.out.println("Deadlock occured for Transaction ID #" + transactionID);
       System.out.println("Transaction Aborted ID #" + transactionID);
   }
    //Thread will wait here if conflicts
    while(! checkingForConflicts(transactionID)){
      wait();
    }
     System.out.println("Lock acquired by TID #" + transactionID);
     
     this.currentTransactionID = transactionID;
    
    lockRequestors--;
   
  }

  public synchronized void release(int transactionID) throws InterruptedException{
   System.out.println("Lock released by TID #" + transactionID);
   this.currentTransactionID = -1;
    notifyAll();
  }


  private boolean checkingForConflicts(int ID){
     //false means it will wait
    if(currentTransactionID == -1)    return true;
 
    return false;
  }

}

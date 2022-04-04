package transaction.transaction_occ_lock.server.lock;

import java.util.logging.Level;
import java.util.logging.Logger;


public class LockManager {
	
	private boolean isLockingEnabled =false;
	
	public LockManager(boolean flag){
		this.isLockingEnabled = flag;
	}
	
	
	//Creating an object of Custom Lock
	Lock lock = new Lock();
	
        
        //Assigning Lock to Current Thread
        //if there exist a lock
	public  void setLock(int transactionID) {
            System.err.println("Setting Lock on Transaction ID  #"+transactionID);
		if(this.isLockingEnabled)
		try {
                    lock.acquire(transactionID);
                } catch (InterruptedException ex) {
                    Logger.getLogger(LockManager.class.getName()).log(Level.SEVERE, null, ex);
                } 
	}
	
        //Removing Lock on Transaction
        //Running through locks of a transaction object
        // Dont run through all locks of all trasaction
	public void setUnlock(int transactionID) throws InterruptedException {
                        System.err.println("UnSetting Lock on Transaction ID  #"+transactionID);
		if(this.isLockingEnabled)
		lock.release(transactionID);
	}

}

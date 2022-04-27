package transaction.transaction_occ_lock.common;

//Message codes to be send across client and server
public class MessageTypes {
	    public static final int OPEN_TRANSACTION = 1;
	    public static final int OPEN_TRANSACTION_RESPONSE = 11;
	    public static final int CLOSE_TRANSACTION = 2;
	    public static final int ABORT_TRANSACTION = 3;
	    public static final int READ_REQUEST = 4;
	    public static final int WRITE_REQUEST = 5;
	    public static final int READ_REQUEST_RESPONSE = 6;
	    public static final int WRITE_REQUEST_RESPONSE = 7;
	    public static final int TRANSACTION_COMMITED = 8 ;
	    public static final int TRANSACTION_ABORTED = 9;
	    public static final int SHUTDOWN = 10 ;
}

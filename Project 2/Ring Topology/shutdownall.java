//create a class to deal with shutting down the entire connection
public class shutdownall {
    public static void leav() throws Exception {
		try{
			System.exit(0);
		}
//print the debugging info for any exception occured!
		catch(Exception e) {
			e.printStackTrace();
		}
	}
}

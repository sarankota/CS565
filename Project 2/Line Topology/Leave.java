// create a class
public class Leave {
	//method to shutdown
	public void shutdown(int port, ActiveNode node) throws Exception {
		try{
			//System.out.println("Size=" + (node.servlets.size()));
			System.out.println("Your connection terminated");
			node.servlets.remove(node);
// prompt the connectivity information
			for(ActiveNodeWorker j: node.servlets)
						{		
							try {
								j.getPrintWriter().println("node " + port + "");
							  }
							  catch(Exception e) 
							  {	
								e.printStackTrace();
							  }
						}
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
//method to leave & prompt that node has left
	public void leave(int port, ActiveNode node) throws Exception {
		try{
						System.out.println("you left");
						node.active=false;
						for(ActiveNodeWorker j: node.servlets)
						{	
							try {
								j.getPrintWriter().println("node " + port + "removed");	
							  }
//print the debugging info for any exception occured!
							  catch(Exception e) {
								e.printStackTrace();
							  }
						}
			}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
}

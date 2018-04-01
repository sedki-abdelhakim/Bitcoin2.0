import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;


public class Test {

	public Test() {
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args) {
		Thread anounceTransaction = new Thread() {
		    public void run() {
		    	InputStreamReader isr = new InputStreamReader(System.in);
		    	BufferedReader bf = new BufferedReader(isr );
		    	
		       
		    	try {
		    		String te = "";
		    		 do{ 
		    		 
		    		  System.out.println("Please choose a number: ");
		    		  System.out.println("1 to join the network: ");
		    		  System.out.println("2 to create a transaction: ");
		    		  System.out.println("3 to announce a transaction: ");
		    		  if(te.equals("1")){
		    			  System.out.println("Joining.....");
		    			  
		    		  }else if(te.equals("2")){
		    			  System.out.println("Creating.....");
		    		  }else if (!te.equals("")){
		    			  System.out.println("invalid input please try again");
		    		  }
		    		 }while((te = bf.readLine()) != null);
		    			 
		    			
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		    	
		       }
		    
		    	
		    	
		};
		anounceTransaction.start();
	}

}

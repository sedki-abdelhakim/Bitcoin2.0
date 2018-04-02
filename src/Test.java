import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;

public class Test {

	public static void main(String[] args) throws Exception {
		Network n = new Network();
		Node n1 = new Node();
		Node n2 = new Node();
		Node n3 = new Node();
		Node n4 = new Node();
		if (n1.joinNetwork())
			System.out.println("Node " + n1.getNodeID() + " Joined");
		if (n2.joinNetwork())
			System.out.println("Node " + n2.getNodeID() + " Joined");
		if (n3.joinNetwork())
			System.out.println("Node " + n3.getNodeID() + " Joined");
		if (n4.joinNetwork())
			System.out.println("Node " + n4.getNodeID() + " Joined");
		Network.printNetworkGraph();

		// add some coins in the network
		n1.ownedCoins.add(new Output("1233", "owner script top secret".getBytes()));
		n1.ownedCoins.add(new Output("1234", "owner script top secret 2".getBytes()));
		n2.ownedCoins.add(new Output("1233", "owner script top secret".getBytes()));
		n3.ownedCoins.add(new Output("1234", "owner script top secret 2".getBytes()));
		n4.ownedCoins.add(new Output("1233", "owner script top secret".getBytes()));
		n4.ownedCoins.add(new Output("1234", "owner script top secret 2".getBytes()));

		//n1 create a valid transaction to n2 sending 1 coin
		Transaction t = n1.createTransaction(1, n2.getPublicKey(), "owner script top secret".getBytes());
		n1.anounceTransaction(t);

		System.out.println("------------------------------------------");
		//n2 create a fake transaction to n3 sending 1 coin
		Transaction t2 = n2.createTransaction(1, n3.getPublicKey(), "owner script top secret".getBytes());
		t2.setID("12"); //change some attributes in the transaction (transID)		
		n2.anounceTransaction(t2);

		

	}

}

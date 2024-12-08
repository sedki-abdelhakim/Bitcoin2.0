
import java.util.LinkedList;

public class Test {

	public static void main(String[] args) throws Exception {
		Network n = new Network();
		Node n1 = new Node();
		Node n2 = new Node();
		if (n1.joinNetwork())
			System.out.println("Node " + n1.getNodeID() + " Joined");
		if (n2.joinNetwork())
			System.out.println("Node " + n2.getNodeID() + " Joined");
		
		Network.printNetworkGraph();
		
		//create genesis block to add some money into the network
		Transaction t1 = n1.createTransaction(new LinkedList<Output>(),n1.getPublicKey(), n1.getPublicKey());

		n1.anounceTransaction(t1);

		Block b1 = n2.createBlock(2, n2.getTransactions(), n2.getPublicKey(),"".getBytes());
		Block b2 = n2.createBlock(2, n2.getTransactions(), n2.getPublicKey(), "".getBytes());
		
		n2.anounceBlock(b1);
		n2.anounceBlock(b2);
		Network.updateLedger();

		Block b3 = n2.createBlock(2, n2.getTransactions(), n2.getPublicKey(), n2.getCache().getLast().getHash());
		n2.anounceBlock(b3);
		Network.updateLedger();

	}

}

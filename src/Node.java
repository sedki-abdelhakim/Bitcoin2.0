import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.Key;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.LinkedList;

public class Node {
	//LinkedList conecctedNodes; // implemented in Network
	private Key puKey;
	private Key prKey;
	private String nodeID; // to be discussed
	private BlockChain ledger;
	private LinkedList<Transaction> unverifiedTransactions;
	private LinkedList<Transaction> verifiedTransactions;
	private Thread anounceTransactionThread;
	private Thread receiveTransactionThread;
	private ArrayList<Transaction> reciveMSGQueue;

	public Node() throws NoSuchAlgorithmException, NoSuchProviderException {
		this.generateKeyPair();
		ledger = new BlockChain();
		unverifiedTransactions = new LinkedList<Transaction>();
		unverifiedTransactions = new LinkedList<Transaction>();
		reciveMSGQueue = new ArrayList<Transaction>();

		anounceTransactionThread = new Thread() {
			public void run() {
				InputStreamReader isr = new InputStreamReader(System.in);
				BufferedReader bf = new BufferedReader(isr);

				try {
					String te = "";
					Transaction createdTransaction = null;
					do {

						System.out.println("Please choose a number: ");
						System.out.println("1 to join the network: ");
						System.out.println("2 to create a transaction: ");
						System.out.println("3 to announce a transaction: ");

						if (te.equals("1")) {
							System.out.println("Joining.....");
							joinNetwork();
						} else if (te.equals("2")) {
							//Need to complete it 
							System.out.println("Creating.....");
						} else if (te.equals("3")) {
							System.out.println("Announcing....");
							if (createdTransaction != null) {
								anounceTransaction(createdTransaction);
							} else {
								System.out.println("You need to create a transaction first");
							}

						} else if (!te.equals("")) {
							System.out.println("invalid input please try again");
						}

						// ToDO complete all inside if steps
					} while ((te = bf.readLine()) != null);

				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}

		};
		anounceTransactionThread.start();

		receiveTransactionThread = new Thread() {
			public void run() {
				while (true) {
					if (reciveMSGQueue.size() != 0) {
						receiveTransaction();

					}
				}
			}
		};
		receiveTransactionThread.start();
	}

	public void generateKeyPair() throws NoSuchAlgorithmException {
		KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
		keyGen.initialize(1024);
		KeyPair k = keyGen.genKeyPair();
		puKey = k.getPublic();
		prKey = k.getPrivate();
	}

	public void generateID() {

	}

	public boolean verifyTrasaction(Transaction transaction) {
		// ToDO add layers of verification to a transaction 

		return true;
	}

	public boolean verifyBlock(Block block) {
		// ToDO add layers of verification to a block 

		return true;
	}

	public Block createBlock() {
		// Create block

		return null;
	}

	public Transaction createTransaction() {// need to do it in m1

		return null;
	}

	public void joinNetwork() {
		Network.addNode(this);

	}

	public void updateLedger() {

	}

	public Byte[] createMerkleTree() {

		return null;
	}

	public Byte[] CreateBlockHash() {

		return null;
	}

	public void receiveTransaction() {// need to do it in m1

		Transaction transaction = reciveMSGQueue.get(0);
		boolean isVerified = verifyTrasaction(transaction);
		if (isVerified) {
			if (!verifiedTransactions.contains(transaction)) {
				verifiedTransactions.add(transaction);
				anounceTransaction(transaction);

			}
		} else {
			if (!unverifiedTransactions.contains(transaction)) {
				unverifiedTransactions.add(transaction);

			}
		}

		reciveMSGQueue.remove(0);
	}

	public void anounceTransaction(Transaction transaction) {// need to do it in m1

		Network.announceTransaction(this, transaction);
	}

	public String getNodeID() {
		return nodeID;
	}

	public void setNodeID(String nodeID) {
		this.nodeID = nodeID;
	}

	public ArrayList<Transaction> getReciveMSGQueue() {
		return reciveMSGQueue;
	}

	public void addReciveMSGQueue(Transaction transaction) {
		this.reciveMSGQueue.add(transaction);
	}

}

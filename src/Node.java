import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.Key;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.sql.Timestamp;
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
	private LinkedList<Transaction> reciveMSGQueue;

	public Node() throws NoSuchAlgorithmException, NoSuchProviderException {
		this.generateKeyPair();
		ledger = new BlockChain();
		unverifiedTransactions = new LinkedList<Transaction>();
		unverifiedTransactions = new LinkedList<Transaction>();
		reciveMSGQueue = new LinkedList<Transaction>();
		nodeID =  generate_UniqueID();
		
/*
		new Thread() {
			public void run() {
				InputStreamReader isr = new InputStreamReader(System.in);
				BufferedReader bf = new BufferedReader(isr);

				try {
					String tt = "";
					Transaction createdTransaction = null;
					do {

						tt = bf.readLine();
						String te = tt.split(", ")[1];
						String node = tt.split(", ")[0];
						if (node.equals(nodeID)) {
							if (te.equals("1")) {
								if (joinNetwork())
									System.out.println("Node: " + nodeID + " joined.....");
								else
									System.out.println("Node: " + nodeID + " already joined.....");
							} else if (te.equals("2")) {
								//Need to complete it 
								System.out.println("Created transaction.....");
							} else if (te.equals("3")) {
								System.out.println("Announced the transaction....");
								if (createdTransaction != null) {
									anounceTransaction(createdTransaction);
								} else {
									System.out.println("You need to create a transaction first");
								}
							}
						}
					} while (tt != null);

				} catch (IOException e) {
					e.printStackTrace();
				}

			}

		}.start();
		*/

		new Thread() {
			public void run() {
				while (true) {
					if (reciveMSGQueue.size() != 0) {
						receiveTransaction();

					}
				}
			}
		}.start();
	}

	public void generateKeyPair() throws NoSuchAlgorithmException {
		KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
		keyGen.initialize(1024);
		KeyPair k = keyGen.genKeyPair();
		this.puKey = k.getPublic();
		this.prKey = k.getPrivate();
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

	public boolean joinNetwork() {
		return Network.addNode(this);

	}

	public void updateLedger() {

	}

	public Byte[] createMerkleTree() {

		return null;
	}

	public Byte[] CreateBlockHash() {

		return null;
	}

	public void receiveTransaction() {

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
				//we don't propagate invalide transaction

			}
		}

		reciveMSGQueue.remove(0);
	}

	public void anounceTransaction(Transaction transaction) {

		Network.announceTransaction(this, transaction);
	}

	public String getNodeID() {
		return nodeID;
	}

	public void setNodeID(String nodeID) {
		this.nodeID = nodeID;
	}

	public LinkedList<Transaction> getReciveMSGQueue() {
		return reciveMSGQueue;
	}

	public void addReciveMSGQueue(Transaction transaction) {
		this.reciveMSGQueue.add(transaction);
	}
	public String generate_UniqueID() {
		// Timestamp
		Timestamp ts = new Timestamp(System.currentTimeMillis());
		// Random number
		int random_Number = (int) Math.random();
		// adding them together
		String idNew = "" + ts.getTime() + random_Number;
		return idNew;

	}

}

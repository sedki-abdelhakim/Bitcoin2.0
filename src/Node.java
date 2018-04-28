import java.security.Key;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.LinkedList;

import javax.crypto.Cipher;

public class Node {
	private Key puKey;
	private Key prKey;
	private String nodeID;
	private BlockChain ledger;
	private LinkedList<Transaction> unverifiedTransactions;
	private LinkedList<Transaction> verifiedTransactions;
	private LinkedList<Transaction> reciveMSGQueue;
	private LinkedList<Block> blocks;
	public LinkedList<Output> ownedCoins;

	public Node() throws NoSuchAlgorithmException, NoSuchProviderException {
		this.generateKeyPair();
		ledger = new BlockChain();
		unverifiedTransactions = new LinkedList<Transaction>();
		verifiedTransactions = new LinkedList<Transaction>();
		ownedCoins = new LinkedList<Output>();
		reciveMSGQueue = new LinkedList<Transaction>();
		nodeID = generate_UniqueID();

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

		/*new Thread() {
			public void run() {
				while (true) {
					if (reciveMSGQueue.size() != 0) {
						receiveTransaction();
		
					}
				}
			}
		}.start();*/
	}

	public void generateKeyPair() throws NoSuchAlgorithmException {
		KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
		keyGen.initialize(1024);
		KeyPair k = keyGen.genKeyPair();
		this.puKey = k.getPublic();
		this.prKey = k.getPrivate();
	}

	public boolean verifyTrasaction(Transaction transaction) throws Exception {
		byte[] decryptMsg = transaction.decrypt(transaction.getPublicKey_Sender(), transaction.getHash());
		byte[] newHash = transaction.Generate_Hash();
		if(Arrays.equals(decryptMsg ,newHash))
			return true;
		else
			return false;
	}

	public boolean verifyBlock(Block block) {
		// ToDO add layers of verification to a block 

		return true;
	}

	public Block createBlock() {
		// Create block

		return null;
	}

	public byte[] encrypt(Key PR, byte[] message) throws Exception {
		Cipher cipherText = Cipher.getInstance("RSA");
		cipherText.init(Cipher.ENCRYPT_MODE, PR);
		return cipherText.doFinal(message);
	}

	public Transaction createTransaction(int amount, Key reciever, byte[] secretOwnerScript) throws Exception {
		LinkedList<Output> input = new LinkedList<Output>();
		if (amount > ownedCoins.size())
			return null;
		while (amount != 0) {
			input.add(ownedCoins.get(0));
			ownedCoins.remove(0);
			amount--;
		}
		byte[] c = encrypt(reciever, secretOwnerScript); // encrypting the owner script by the public key of receiver
		//so only the reciever can decrypt it using his private, thus proving his ownership of the coin


		Transaction t = new Transaction(input, c, this.puKey, reciever, new LinkedList<byte[]>()); // ownership proof of the coin next milestone
		t.Sign_Hash(this.prKey);
		return t;
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

	public void receiveTransaction() throws Exception {

		if (reciveMSGQueue.size() != 0) {
			Transaction transaction = reciveMSGQueue.get(0);
			System.out.print("Node " + nodeID + " received a transaction");
			boolean isVerified = verifyTrasaction(transaction);
			if (isVerified) {
				if (!verifiedTransactions.contains(transaction)) {
					System.out.println(" >>> Propagate to next node");
					verifiedTransactions.add(transaction);
					anounceTransaction(transaction);

				}
				else{
					System.out.println(" >>> Do not propagate to next node");

				}
			} else {
				if (!unverifiedTransactions.contains(transaction)) {
					unverifiedTransactions.add(transaction);
					//we don't propagate invalide transaction
				}
				System.out.println(" >>> Do not Propagate to next node");
			}

			reciveMSGQueue.remove(0);
		}
	}

	public void anounceTransaction(Transaction transaction) throws Exception {

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

	public Key getPublicKey() {
		return puKey;
	}

	public void addReciveMSGQueue(Transaction transaction) {
		this.reciveMSGQueue.add(transaction);
	}
	
	public void addReceiveBLKQueue(Block block) {
		this.blocks.add(block);
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

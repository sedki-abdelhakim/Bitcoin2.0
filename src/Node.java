import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.MessageDigest;
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
	private LinkedList<Transaction> transactions;
	private LinkedList<Block> blocks;
	private LinkedList<Block> cache;
	private LinkedList<Output> ownedCoins;
	private int cacheLifeTime;

	public Node() throws NoSuchAlgorithmException, NoSuchProviderException {
		this.generateKeyPair();
		ledger = new BlockChain();
		ownedCoins = new LinkedList<Output>();
		blocks = new LinkedList<Block>();
		transactions = new LinkedList<Transaction>();
		nodeID = generate_UniqueID();
		cache = new LinkedList<Block>();
		cacheLifeTime = 1;
	}

	public void generateKeyPair() throws NoSuchAlgorithmException {
		KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
		keyGen.initialize(1024);
		KeyPair k = keyGen.genKeyPair();
		this.puKey = k.getPublic();
		this.prKey = k.getPrivate();
	}

	public boolean verifyTrasaction(Transaction transaction) throws Exception {
		//for now we verify the integrty and authenticty of the transaction
		//later will add verification to the ownership of the sent coins as well (through verifying the ownerscript in each ouput)
		byte[] decryptMsg = transaction.decrypt(transaction.getPublicKey_Sender(), transaction.getHash());
		byte[] newHash = transaction.Generate_Hash();
		if (!Arrays.equals(decryptMsg, newHash))
			return false;
		if (ledger.getBlocks().size() == 0) {//bypass the transaction ownership of coins for the genesis block
			return true;
		}
		return true;

	}

	public boolean verifyBlock(Block block) throws Exception {
		int difficulty = 2;
		String hashTarget = new String(new char[difficulty]).replace('\0', '0');
		byte[] decryptMsg = block.decrypt(block.getPuKey(), block.getHash());
		String str = new String(decryptMsg, StandardCharsets.UTF_8);
		if (!str.substring(0, difficulty).equals(hashTarget))
			return false;
		byte[] newHash = block.generateHash();
		if (!Arrays.equals(decryptMsg, newHash))
			return false;
		for (Transaction var : block.getTransactionsList()) {
			if (!verifyTrasaction(var))
				return false;
			;
		}
		return true;
	}

	public Block retrieveBlock(byte[] hash) {
		for (int i = 0; i < cache.size(); i++) {
			if (Arrays.equals(cache.get(i).getHash(), hash)) {
				return cache.get(i);
			}
		}
		return null;
	}

	public Block createBlock(int difficulty, LinkedList<Transaction> transactions, Key puKey, byte[] prevhash) throws Exception {
		Block block;
		if (ledger.getBlocks().size() == 0)
			block = new Block(new byte[0], transactions, puKey);
		else
			block = new Block(prevhash, transactions, puKey);

		block.blockMinning(difficulty);
		block.Sign_Hash(prKey);
		return block;
	}

	public byte[] encrypt(Key PR, byte[] message) throws Exception {
		Cipher cipherText = Cipher.getInstance("RSA");
		cipherText.init(Cipher.ENCRYPT_MODE, PR);
		return cipherText.doFinal(message);
	}

	public Transaction createTransaction(LinkedList<Output> input, Key sender, Key reciever) throws Exception {
		MessageDigest msg_digest = MessageDigest.getInstance("SHA-256");
		String originalString = "" + reciever.hashCode();
		byte[] encodedhash = msg_digest.digest(originalString.getBytes(StandardCharsets.UTF_8));
		byte[] signedHash = encrypt(reciever, encodedhash); // encrypting the hash of reciever pukey by the public key of receiver (xd)
		Transaction t = new Transaction(input, signedHash, this.puKey, reciever, new LinkedList<byte[]>()); // ownership proof of the coin next milestone
		t.Sign_Hash(this.prKey);
		return t;
	}

	public boolean joinNetwork() {
		return Network.addNode(this);

	}

	public void updateLedger() {
		cacheLifeTime++;
		boolean updated = false;
		LinkedList<Block> dumbList = new LinkedList<Block>();
		while (!blocks.isEmpty()) {
			int i = (int) (Math.random() * blocks.size());
			Block b = blocks.get(i);
			blocks.remove(i);
			dumbList.add(b);
			if (ledger.getBlocks().isEmpty()) {
				ledger.addBlock(b);
				updated = true;
				dumbList.remove(b);
				while (!dumbList.isEmpty()) {
					cache.add(dumbList.get(0));
					dumbList.remove(0);
					cacheLifeTime = 1;
				}

				break;
			}
			if (recUpdateLedger(b, 0)){
				updated = true;
				break;
			}

		}
		if (updated) {
			while (!blocks.isEmpty()) {
				cache.add(blocks.get(0));
				blocks.remove(0);
				cacheLifeTime = 1;
			}
		}
		if (cacheLifeTime == 2) {
			clearCache();
		}

	}

	public boolean recUpdateLedger(Block b, int depth) {
		if (depth >= 3) {
			return false;
		}
		if (ledger.getBlocks().isEmpty()||Arrays.equals(b.getPrevHash(), ledger.getBlocks().getLast().getHash())) {
			ledger.addBlock(b);
			return true;
		} else {
			Block temp = ledger.getBlocks().removeLast();
			Block c = retrieveBlock(b.getPrevHash());
			if (c != null && recUpdateLedger(c, ++depth)) {
				ledger.addBlock(b); //replace
				return true;
			} else {
				ledger.getBlocks().add(temp); //back to original state
				return false;
			}
		}

	}

	public void clearCache() {
		while (!cache.isEmpty()) {
			cache.remove();
		}
	}

	public void receiveTransaction(Transaction transaction) throws Exception {

		boolean isVerified = verifyTrasaction(transaction);
		System.out.print("Node " + nodeID + " received a transaction");
		if (isVerified) {
			System.out.print(" <verified>");
			if (!transactions.contains(transaction)) {
				System.out.println(" >>> Propagate to next node");
				transactions.add(transaction);
				anounceTransaction(transaction);

			} else {
				System.out.println(" >>> Do not propagate to next node");

			}
		} else {
			System.out.print(" <unverified>");
			System.out.println(" >>> Do not Propagate to next node");
			transactions.remove(0);
		}

	}

	public void anounceTransaction(Transaction transaction) throws Exception {

		Network.announceTransaction(this, transaction);
	}

	public void anounceBlock(Block block) throws Exception {

		Network.announceBlocks(this, block);
	}

	public String getNodeID() {
		return nodeID;
	}

	public void setNodeID(String nodeID) {
		this.nodeID = nodeID;
	}

	public LinkedList<Transaction> getTransactions() {
		return transactions;
	}

	public Key getPublicKey() {
		return puKey;
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

	public void receiveBlock(Block block) throws Exception {
		System.out.print("Node " + nodeID + " received a block");
		boolean isVerified = verifyBlock(block);
		if (isVerified) {
			System.out.print(" <verified>");
			if (!blocks.contains(block)) {
				System.out.println(" >>> Propagate to next node");
				blocks.add(block);
				anounceBlock(block);
			} else {
				System.out.println(" >>> Do not propagate to next node");

			}
		} else {
			System.out.print(" <unverified>");
			System.out.println(" >>> Do not Propagate to next node");
		}

	}
	public BlockChain getLedger(){
		return ledger;
	}

	public LinkedList<Block> getCache(){
		return cache;
	}
}

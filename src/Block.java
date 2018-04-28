import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.LinkedList;

public class Block {
	private byte [] prevHash;
	private byte [] Hash;
	private int nonce;
	private int numOfTransactions;
	private LinkedList<Transaction> transactionsList;
	private String merkleTreeRootHash;
	
	
	public Block( byte[] prevHash, byte [] Hash, int nonce,  LinkedList<Transaction> transactionsList,String merkleTreeRootHash){
		 this.prevHash = prevHash;
		 this.Hash = Hash;
		 this.nonce = nonce; 
		 this.transactionsList = transactionsList;
		 this.numOfTransactions = transactionsList.size();
		 this.merkleTreeRootHash =merkleTreeRootHash;
	}
	
	
	public byte[] generateHash() throws NoSuchAlgorithmException {
		MessageDigest msg_digest = MessageDigest.getInstance("SHA-256");
		String originalString = "" + Integer.toString(nonce).hashCode() + transactionsList.hashCode() 
								+ prevHash.hashCode();
		byte[] encodedhash = msg_digest.digest(originalString.getBytes(StandardCharsets.UTF_8));
		return encodedhash;

	}
	
	public void blockMinning(int difficulty) throws NoSuchAlgorithmException {
		//Create a string with difficulty * "0"
		String target = new String(new char[difficulty]).replace('\0', '0'); 
		while(!Hash.toString().substring( 0, difficulty).equals(target)) {
			nonce ++;
			Hash = generateHash();
		}
		System.out.println("Block Mined: " + Hash);
	}


	public byte [] getPrevHash() {
		return prevHash;
	}


	public byte [] getHash() {
		return Hash;
	}


	public int getNonce() {
		return nonce;
	}


	public int getNumOfTransactions() {
		return numOfTransactions ;
	}



	public LinkedList<Transaction> getTransactionsList() {
		return transactionsList;
	}


	public String getMerkleTreeRootHash() {
		return merkleTreeRootHash;
	}

}

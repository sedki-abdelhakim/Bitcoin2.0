import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class Block {
	private String prevHash;
	private String Hash;
	private int nonce;
	private int numOfTransactions;
	private LinkedList<Transaction> transactionsList;
	private String merkleTreeRootHash;
	
	
	public Block( String prevHash, String Hash, int nonce,  LinkedList<Transaction> transactionsList,String merkleTreeRootHash){
		 this.prevHash = prevHash;
		 this.Hash = Hash;
		 this.nonce = nonce; 
		 this.transactionsList = transactionsList;
		 this.numOfTransactions = transactionsList.size();
		 this.merkleTreeRootHash =merkleTreeRootHash;
	}


	public String getPrevHash() {
		return prevHash;
	}


	public void setPrevHash(String prevHash) {
		this.prevHash = prevHash;
	}


	public String getHash() {
		return Hash;
	}


	public void setHash( String hash) {
		Hash = hash;
	}


	public int getNonce() {
		return nonce;
	}


	public void setNonce(int nonce) {
		this.nonce = nonce;
	}


	public int getNumOfTransactions() {
		return numOfTransactions ;
	}


	public void setNumOfTransactions(int numOfTransactions) {
		this.numOfTransactions = numOfTransactions;
	}


	public LinkedList<Transaction> getTransactionsList() {
		return transactionsList;
	}


	public void setTransactionsList(LinkedList<Transaction> transactionsList) {
		this.transactionsList = transactionsList;
	}


	public String getMerkleTreeRootHash() {
		return merkleTreeRootHash;
	}


	public void setMerkleTreeRootHash(String merkleTreeRootHash) {
		this.merkleTreeRootHash = merkleTreeRootHash;
	}

}

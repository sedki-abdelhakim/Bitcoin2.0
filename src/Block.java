import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class Block {
	private Map<String, String> prevHash;
	private Map<String, String> Hash;
	private int nonce;
	private int numOfTransactions;
	private LinkedList<Transaction> transactionsList;
	
	
	public Block(Map<String, String> prevHash, Map<String, String> Hash, int nonce, int numOfTransactions, LinkedList<Transaction> transactionsList){
		 this.prevHash = prevHash;
		 this.Hash = Hash;
		 this.nonce = nonce;
		 this.numOfTransactions = numOfTransactions;
		 this.transactionsList = transactionsList;
	}


	public Map<String, String> getPrevHash() {
		return prevHash;
	}


	public void setPrevHash(Map<String, String> prevHash) {
		this.prevHash = prevHash;
	}


	public Map<String, String> getHash() {
		return Hash;
	}


	public void setHash(Map<String, String> hash) {
		Hash = hash;
	}


	public int getNonce() {
		return nonce;
	}


	public void setNonce(int nonce) {
		this.nonce = nonce;
	}


	public int getNumOfTransactions() {
		return numOfTransactions;
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

}

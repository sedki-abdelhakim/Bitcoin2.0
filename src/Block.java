import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.LinkedList;

import javax.crypto.Cipher;

public class Block {
	private byte [] prevHash;
	private byte [] Hash;
	private int nonce;
	private LinkedList<Transaction> transactionsList;
	
	
	public Block(byte[] prevHash,  LinkedList<Transaction> transactionsList) throws NoSuchAlgorithmException{
		 this.prevHash = prevHash;
		 this.transactionsList = transactionsList;
	}
	
	
	public byte[] generateHash() throws NoSuchAlgorithmException {
		MessageDigest msg_digest = MessageDigest.getInstance("SHA-256");
		String originalString = "" + Integer.toString(nonce) + transactionsList.hashCode() 
								+ prevHash.hashCode();
		byte[] encodedhash = msg_digest.digest(originalString.getBytes(StandardCharsets.UTF_8));
		return encodedhash;

	}
	
	public void blockMinning(int difficulty) throws NoSuchAlgorithmException {
		//Create a string with difficulty * "0"
		String target = new String(new char[difficulty]).replace('\0', '0');
		String str;
		do {
			this.nonce = (int)Math.random()*1000000000;
			Hash = generateHash();
			str = new String(this.Hash, StandardCharsets.UTF_8);
		}	
		while(!str.substring( 0, difficulty).equals(target));
			System.out.println("Block Mined: " + Hash);
	}
	
	public byte[] decrypt(Key PR, byte [] message) throws Exception {
		Cipher cipherText = Cipher.getInstance("RSA");  
		cipherText.init(Cipher.DECRYPT_MODE, PR);
		return cipherText.doFinal(message);
	}



	public byte[] getPrevHash() {
		return prevHash;
	}


	public byte[] getHash() {
		return Hash;
	}


	public int getNonce() {

		return nonce;
	}


	public Transaction getTransaction(int i) {
		return transactionsList.get(i);
	}
	
	public void addTransaction(Transaction t) {
		transactionsList.add(t);
	}

	public LinkedList<Transaction> getTransactionsList() {
		return transactionsList;
	}
	
}

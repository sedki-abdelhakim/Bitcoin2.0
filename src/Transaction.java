

import java.nio.charset.StandardCharsets;
import java.util.LinkedList;
import java.security.Key;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.sql.Timestamp;
import javax.crypto.Cipher;

public class Transaction {
	
	private String id;
	private LinkedList<String> previous_Transactions ;
	private String hash_Next_Owner ;
	private Key PublicKey_Sender ;
	private Key PublicKey_Receiver ;
	private String hash ;
	
	
	// Constructor
	Transaction( LinkedList<String> input_prevTrans ,String input_hash_Next_Owner , Key PU_Sender ,
			Key PU_Reciever , String input_Hash){
		this.previous_Transactions = input_prevTrans ;
		this.hash_Next_Owner = input_hash_Next_Owner ;
		this.PublicKey_Sender = PU_Sender ;
		this.PublicKey_Receiver = PU_Reciever ;
		this.hash = input_Hash ;
		
	}
	
	// Generate unique id
	String generate_UniqueID() {
		// Timestamp
		Timestamp ts = new Timestamp(System.currentTimeMillis());
		System.out.println("Timestamp  :"+ ts.getTime());
		// Random number
		int random_Number = (int)Math.random() ;
		// adding them together
		id = ""+ts.getTime()+random_Number ;
		return id ;
		
	}
	
	// Generate Hashing Method
	byte[] Generate_Hash() throws NoSuchAlgorithmException{
		MessageDigest msg_digest = MessageDigest.getInstance("SHA-256");
		String originalString = ""+generate_UniqueID()+previous_Transactions+hash_Next_Owner
								+PublicKey_Sender+PublicKey_Receiver;
		byte[] encodedhash = msg_digest.digest(originalString.getBytes(StandardCharsets.UTF_8));
		return encodedhash ;
		
	}
	
	// encryption
	byte[] encrypt(Key PR, String message) throws Exception {
	        Cipher cipherText = Cipher.getInstance("RSA");  
	        cipherText.init(Cipher.ENCRYPT_MODE, PR);  
	        return cipherText.doFinal(message.getBytes());  
	    }

	// Hashing Msg 
	String Sign_Hash(PublicKey pub, PrivateKey pvt) throws Exception {
		
		byte [] hashed_msg = Generate_Hash(); 
		
		KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
		kpg.initialize(2048);
		KeyPair kp = kpg.generateKeyPair();
	    pub = kp.getPublic();
		pvt = kp.getPrivate();
		
		// public then private
		byte [] encrypt_msg = encrypt(pub, hashed_msg.toString());     
        System.out.println(new String(encrypt_msg));
		
        byte [] doubled_encrypt_msg = encrypt(pvt, encrypt_msg.toString()); 
        hash = doubled_encrypt_msg.toString() ;
		
        return hash ;
		
		
	}
	

}

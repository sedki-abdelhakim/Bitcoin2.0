import java.nio.charset.StandardCharsets;
import java.util.LinkedList;
import java.security.Key;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import javax.crypto.Cipher;

public class Transaction {

	private String id;
	private LinkedList<Output> previous_Transactions;
	private byte[] hash_Next_Owner;
	private Key PublicKey_Sender;
	private Key PublicKey_Receiver;
	private LinkedList<byte[]> OwnershipOfCoin;
	private byte[] hash;

	// Constructor
	Transaction(LinkedList<Output> input_prevTrans, byte[] input_hash_Next_Owner, Key PU_Sender, Key PU_Reciever,  LinkedList<byte[]> OwnershipOfCoin) throws NoSuchAlgorithmException {
		this.previous_Transactions = input_prevTrans;
		this.hash_Next_Owner = input_hash_Next_Owner;
		this.PublicKey_Sender = PU_Sender;
		this.PublicKey_Receiver = PU_Reciever;
		this.OwnershipOfCoin = OwnershipOfCoin;
		id = this.generate_UniqueID();
		hash = this.Generate_Hash();

	}

	// Generate unique id
	public String generate_UniqueID() {
		// Timestamp
		Timestamp ts = new Timestamp(System.currentTimeMillis());
		// Random number
		int random_Number = (int) Math.random();
		// adding them together
		String idNew = "" + ts.getTime() + random_Number;
		return idNew;

	}

	// Generate Hashing Method
	public byte[] Generate_Hash() throws NoSuchAlgorithmException {
		MessageDigest msg_digest = MessageDigest.getInstance("SHA-256");
		String originalString = "" + id.hashCode() + previous_Transactions.hashCode() + hash_Next_Owner.hashCode()
				+ PublicKey_Sender.hashCode() + PublicKey_Receiver.hashCode()+OwnershipOfCoin.hashCode();
		byte[] encodedhash = msg_digest.digest(originalString.getBytes(StandardCharsets.UTF_8));
		return encodedhash;

	}

	// encryption
	public byte[] encrypt(Key PR, byte[] message) throws Exception {
		Cipher cipherText = Cipher.getInstance("RSA");
		cipherText.init(Cipher.ENCRYPT_MODE, PR);
		return cipherText.doFinal(message);
	}

	// Hashing Msg 
	public byte[] Sign_Hash(Key pvt) throws Exception {

		byte[] encrypt_msg = encrypt(pvt, hash);
		return encrypt_msg;

	}

	public String getID() {
		return this.id;
	}

	public byte[] getHashNextOwner() {
		return this.hash_Next_Owner;
	}

	/* public static void main(String[] args) throws Exception {
		LinkedList<String> l = new LinkedList<String>();
		l.add("element");
	
		Transaction t = new Transaction(l, "", pus, pus);
		t.Sign_Hash(prs);
		
		System.out.println(t.hash.length);
		Cipher cipherText = Cipher.getInstance("RSA");
		cipherText.init(Cipher.DECRYPT_MODE, pus);
	
		byte[] ta = cipherText.doFinal(t.hash);
		byte[] ts = t.Generate_Hash();
	
		System.out.println(new String(ta, "UTF-8"));
	
	}*/
}

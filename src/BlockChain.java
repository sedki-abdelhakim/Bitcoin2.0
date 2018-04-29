import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.LinkedList;

public class BlockChain {
	private LinkedList<Block> blocks;
	private int noOfBlocks;
	private byte[] Hash;
	private int difficulty = 2;
	private HashMap<String, byte[]> UTXO;
	
	public BlockChain() throws NoSuchAlgorithmException {
		this.noOfBlocks = 0;
		this.blocks = new LinkedList<Block>();
		UTXO = new HashMap<String, byte[]>();
	}

	public void addBlock(Block block) throws NoSuchAlgorithmException {
		if (blockVerfication()) {
			this.blocks.add(block);
			this.noOfBlocks++;
		}
	}

	public void addToUTXO(Transaction t) {
		this.UTXO.put(t.getID(), t.getHashNextOwner());
	}
	
	public Boolean blockVerfication() throws NoSuchAlgorithmException {
		Block currentBlock; 
		Block previousBlock;
		String hashTarget = new String(new char[difficulty]).replace('\0', '0');
			
		//loop through blockchain to check hashes:
		for(int i=1; i < blocks.size(); i++) {
			currentBlock = blocks.get(i);
			previousBlock = blocks.get(i-1);
			//compare registered hash and calculated hash:
				if(!currentBlock.getHash().equals(currentBlock.generateHash()) ){
					System.out.println("Current Hashes not equal");			
					return false;
				}	
				//compare previous hash and registered previous hash
				if(!previousBlock.getPrevHash().equals(currentBlock.getPrevHash()) ) {
				System.out.println("Previous Hashes not equal");
				return false;
				}
				//check if hash is solved
				if(!currentBlock.getHash().toString().substring( 0, difficulty).equals(hashTarget)) {
				System.out.println("This block hasn't been mined");
					return false;
				}
			}
		return true;
	}

}

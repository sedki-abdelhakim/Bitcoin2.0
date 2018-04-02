import java.util.HashMap;
import java.util.LinkedList;

public class BlockChain {
	private LinkedList<Block> blocks;
	private int noOfBlocks;
	private HashMap<String, byte[]> UTXO;

	public BlockChain() {
		this.noOfBlocks = 0;
		blocks = new LinkedList<Block>();
		UTXO = new HashMap<String, byte[]>();
	}

	public void addBlock(Block block) {
		// should verify first in next milestone
		this.blocks.add(block);
		this.noOfBlocks++;
	}

	public void addToUTXO(Transaction t) {
		this.UTXO.put(t.getID(), t.getHashNextOwner());
	}

}

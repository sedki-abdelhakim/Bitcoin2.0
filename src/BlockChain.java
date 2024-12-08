import java.util.HashMap;
import java.util.LinkedList;

public class BlockChain {
	private LinkedList<Block> blocks;
	private HashMap<String, byte[]> UTXO;
	
	public BlockChain() {
		this.blocks = new LinkedList<Block>();
		UTXO = new HashMap<String, byte[]>();
	}

	public void addBlock(Block block) {
			this.blocks.add(block);
	}

	public void addToUTXO(Transaction t) {
		this.UTXO.put(t.getID(), t.getHashNextOwner());
	}

	public LinkedList<Block> getBlocks(){
		return blocks;
	}
	
	public HashMap<String, byte[]> getUTXO(){
		return UTXO;
	}
	

}

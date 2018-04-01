import java.util.HashMap;
import java.util.LinkedList;


public class BlockChain {
private static LinkedList <Block> blocks;
private static  int noOfBlocks;
private static HashMap<String,String> UTXO;
	public BlockChain() {
		this. noOfBlocks= 0;
		blocks = new LinkedList<Block>();
	}

	public static void addBlock(Block block) {
		// should verify first in next milestone
		blocks.add(block);
		noOfBlocks++;
		
	}
	
	

}

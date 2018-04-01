import java.util.HashMap;
import java.util.LinkedList;


public class BlockChain {
private static LinkedList <Block> blocks;
private static  int noOfBlocks;
private static HashMap<String,String> UTXO;
	public BlockChain(int noOfBlocks) {
		this. noOfBlocks= noOfBlocks;
		blocks = new LinkedList<Block>();
	}

	public static void addBlock(Block block) {
		// should verify first in next milestone
		blocks.add(block);
		
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}

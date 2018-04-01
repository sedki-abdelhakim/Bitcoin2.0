import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Random;


public class Network {
	private static HashMap<Node, LinkedList<Node>> bitcoinNetwork;
	private static int noOfNodes  ;
	private static int maxNoOfConnectedNodes;
	public Network() {		
		
		bitcoinNetwork = new HashMap<Node,LinkedList<Node>>();
		noOfNodes = 0 ;
		maxNoOfConnectedNodes = 5;
	
	}
public static void addNode(Node node){
	
	LinkedList<Node> list =  new LinkedList<Node>();
	if(noOfNodes != 0 ){
			
	int noOfConnectedNodes = generateRandomNo(maxNoOfConnectedNodes);
	
	HashSet<Integer> dub = new HashSet<Integer>();
	for (int i = 0; i < noOfConnectedNodes; i++) {
		int theIndexOfConnectedNode = generateRandomNo(noOfNodes)-1;
		
		if(!dub.contains(theIndexOfConnectedNode)){
			dub.add(theIndexOfConnectedNode);
			Node tempNode = (Node)(bitcoinNetwork.keySet().toArray())[theIndexOfConnectedNode];
			list.add(tempNode);
			bitcoinNetwork.get(tempNode).add(node);
		}else {
			
			i--;
		}
		
		
	}
	
	
}
	bitcoinNetwork.put(node, list);
	noOfNodes +=1;
}

public static void announceTransaction(Node node,Transaction transaction){
	int myConnectedNodeSize = bitcoinNetwork.get(node).size();
	if(myConnectedNodeSize !=0){
	
	int noOfAnnouncedNodes = generateRandomNo(myConnectedNodeSize);
	HashSet<Integer> dub = new HashSet<Integer>();
	
	
	for (int i = 0; i < noOfAnnouncedNodes; i++) {
		int randomAnnounce = generateRandomNo(myConnectedNodeSize)-1;
		
		if(!dub.contains(randomAnnounce)){
			dub.add(randomAnnounce);
		Node NodeToAnnounce = bitcoinNetwork.get(node).get(randomAnnounce);
		NodeToAnnounce.addReciveMSGQueue(transaction);
		}else {
			
			i--;
		}
	}
	
}
	
	
}

public static int generateRandomNo(int n){
	Random rand = new Random();
	return rand.nextInt(n) + 1;
	
}

}

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Random;

public class Network {
	private static HashMap<Node, LinkedList<Node>> bitcoinNetwork;
	private static int noOfNodes;

	public Network() {

		bitcoinNetwork = new HashMap<Node, LinkedList<Node>>();
		noOfNodes = 0;

	}

	public static boolean addNode(Node node) {
		if (bitcoinNetwork.containsKey(node))
			return false;

		LinkedList<Node> list = new LinkedList<Node>();
		if (noOfNodes != 0) {
			int noOfConnectedNodes = generateRandomNo(noOfNodes);
			HashSet<Integer> dub = new HashSet<Integer>();
			for (int i = 0; i < noOfConnectedNodes; i++) {
				int theIndexOfConnectedNode = generateRandomNo(noOfNodes) - 1;
				if (!dub.contains(theIndexOfConnectedNode)) {
					dub.add(theIndexOfConnectedNode);
					Node tempNode = (Node) bitcoinNetwork.keySet().toArray()[theIndexOfConnectedNode];
					list.add(tempNode);
					bitcoinNetwork.get(tempNode).add(node);
				} else {
					i--;
				}
			}

		}

		bitcoinNetwork.put(node, list);
		noOfNodes += 1;
		return true;
	}

	public static void announceTransaction(Node node, Transaction transaction) {
		int myConnectedNodeSize = bitcoinNetwork.get(node).size();
		if (myConnectedNodeSize != 0) {

			int noOfAnnouncedNodes = generateRandomNo(myConnectedNodeSize);
			HashSet<Integer> dub = new HashSet<Integer>();

			for (int i = 0; i < noOfAnnouncedNodes; i++) {
				int randomAnnounce = generateRandomNo(myConnectedNodeSize) - 1;

				if (!dub.contains(randomAnnounce)) {
					dub.add(randomAnnounce);
					Node NodeToAnnounce = bitcoinNetwork.get(node).get(randomAnnounce);
					NodeToAnnounce.addReciveMSGQueue(transaction);
				} else {

					i--;
				}
			}

		}

	}

	public static int generateRandomNo(int n) {
		Random rand = new Random();
		return rand.nextInt(n) + 1;

	}

}

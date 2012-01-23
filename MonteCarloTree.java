import java.util.*;

//What does a node need? Must keep track of wins and visits at that particular game state. If choose this move-node, run simulations and update results. 
//Each node would then to be one of the available LegalMoves. Make node for each LegalMove.

public class MonteCarloTree {
		
		private Node root;

		private class Node {
				private int wins;
				private int visits;
				private Node parent;
				private List<Node> children;
		}
		
		public MonteCarloTree(int wins, int visits) {
				root = new Node();
				root.wins = wins;
				root.visits = visits;
				root.children = new ArrayList<Node>();
		}
}



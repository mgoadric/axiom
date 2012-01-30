import java.util.*;

//What does a node need? Must keep track of wins and visits at that particular game state. If choose this move-node, run simulations and update results. 
//Each node would then to be one of the available LegalMoves. Make node for each LegalMove.

public class MonteCarloTree {
		
		private Node root;
		private int visitLimit;

		private class Node {
				private int wins;
				private int visits;
				private Node parent;
				private List<Node> children;
		}
		
		public MonteCarloTree(int wins, int visits, int visitLimit) {
				this.visitLimit = visitLimit;
				root = new Node();
				root.wins = wins;
				root.visits = visits;
				root.children = new ArrayList<Node>();
		}
		
		public void findMove(Gameboard board){
			//root node empty, generate children based on legalmoves. Need to bring in player. Will also need opponent player to play randomly against.
			//Choose node, has no children, hasn't reached visitLimit, then random game using that move M.
			//Make move M. For legal moves of Random Opponent, choose one. Generate legalmoves for Me at that move, etc.
			//Update visits to node on game completion. When gameOver, update wins if won for that player.
			//How to choose moves which are doing well? Can't continuously pick random moves to make, want to research moves doing well.
			//On visitLimit reached, generate children for that move. 
		}
		
		public int getBestMove(){
				//return best move found in findMove. Move with most wins?, some kind of other metric
			return -1;	
		}
}



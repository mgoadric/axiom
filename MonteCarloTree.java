import java.util.*;

//What does a node need? Must keep track of wins and visits at that particular game state. If choose this move-node, run simulations and update results. 
//Each node would then to be one of the available LegalMoves. Make node for each LegalMove.

public class MonteCarloTree {
		
	  private Node root;
		private int visitLimit;

		private class Node {
				private int wins = 0;
				private int visits = 0;
				private Node parent;
				private List<Node> children;
		}
		
		public MonteCarloTree(int visitLimit) {
				this.visitLimit = visitLimit;
			//	root = new Node();
			//	root.children = new ArrayList<Node>();
		}
		
		public void createRoot(){
				root = new Node();
				root.children = new ArrayList<Node>();
		}
		
		public void findMove(BoardGame board, MonteCarloPlayer player){
			//1/30/12
			//root node empty, generate children based on legalmoves. Need to bring in player. Will also need opponent player to play randomly against.
			//Choose node, has no children, hasn't reached visitLimit, then random game using that move M.
			//Make move M. For legal moves of Random Opponent, choose one. Generate legalmoves for Me at that move, etc.
			//Update visits to node on game completion. When gameOver, update wins if won for that player.
			//How to choose moves which are doing well? Can't continuously pick random moves to make, want to research moves doing well.
			//On visitLimit reached, generate children for that move. 
			if(root.children.size() == 0){
					generateChildren(root, board, player);
			}
			//2/4/12
			//Make better method to choose node to explore
			//Just trying to get it to select some move, not generating children if visited visitLimit yet.
			Random random = new Random();
		//	int move = random.nextInt(root.children.size());
			int move = selectNode();
			root.children.get(move).visits+= 1;
			BoardGame nextBoard = (BoardGame)board.clone();
			nextBoard.makeMove(player, move);
			RandomPlayer opp = new RandomPlayer(player.opp, player.num);
			RandomPlayer me = new RandomPlayer(player.num, player.opp);
			RandomPlayer pointer = me;
			int count = 0;
			while(!nextBoard.gameOver() && count < 1000){
					if(pointer == opp){
							pointer = me;
					}			
					else {
							pointer = opp;	
					}
				ArrayList<Integer> t = nextBoard.legalMoves(pointer);
        int move2 = t.get((int)(Math.random() * t.size()));
        nextBoard.makeMove(pointer, move2);
        count++;
			}
			if (nextBoard.hasWon(player.num)) {
				root.children.get(move).wins+=1;
			}
		}
		
		public int selectNode(){
			double bestFoundValue = 0;
			int bestFoundMove = 0;
			for(Node c : root.children){
					double value;
					if(c.visits > 10){
						double winrate = ((double)c.wins / c.visits);
						//figure out formula for choosing nodes. Using example for now.
						value = winrate + (0.44 * Math.sqrt(Math.log(c.visits) / c.visits));
					}
					else{
						value = 10000 + 1000*Math.random();	
					}
					if(value > bestFoundValue){
						bestFoundValue = value;
						bestFoundMove = root.children.indexOf(c);
					}
			}
			//System.out.println("Move:" + bestFoundMove + " Value:" + bestFoundValue);
			System.out.print(".");
			return bestFoundMove;
		}
		
		public void generateChildren(Node parent, BoardGame board, MonteCarloPlayer player) {
			List<Node> children_list = new ArrayList<Node>();
			for (Integer m : board.legalMoves(player)) {
				Node child = new Node();
				child.parent = parent;
				children_list.add(child);
			}
			parent.children = children_list;
		}
		
		public int getBestMove(){
				Node winner = root.children.get(0);
				int winning_move = 0;
				for (Node c : root.children){
						System.out.println(c.wins);
						if(c.visits > winner.visits){
							winner = c;	
							winning_move = root.children.indexOf(c);
						}
				}
				return winning_move;
		}
}



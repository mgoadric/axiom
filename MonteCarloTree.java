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
				private int level = 0;
				
				public Node(Node parent){
						this.parent = parent;
						if(parent != null){
								this.level = parent.level+1;
						}
				}
				
				public void win_calc(Boolean win){
						if((win && level % 2 != 0) || (!win && level % 2 != 1)){
							wins++;
						}
						visits++;
						if(parent != null){
							parent.win_calc(win);
						}
				}
		}
		
		public MonteCarloTree(int visitLimit) {
				this.visitLimit = visitLimit;
		}
		
		public void createRoot(){
				root = new Node(null);
				root.children = new ArrayList<Node>();
		}
		
		public void findMove(BoardGame board, MonteCarloPlayer player){
			if(root.children.size() == 0){
					generateChildren(root, board, player);
			}
			Random random = new Random();
			int move = selectNode(root);
			int new_move = 0;
			Node current_node = root.children.get(move);
		//	current_node.visits+= 1;
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
							
				if(current_node.visits > this.visitLimit && current_node.children == null){
					generateChildren(current_node, nextBoard, pointer);
				}
				if(current_node.children != null){
						new_move = selectNode(current_node);
						current_node = current_node.children.get(new_move);
					//	current_node.visits += 1;
				}
				else{
					ArrayList<Integer> t = nextBoard.legalMoves(pointer);
        	new_move = t.get((int)(Math.random() * t.size()));
        }
        nextBoard.makeMove(pointer, new_move);
        count++;
			}
			current_node.win_calc(nextBoard.hasWon(player.num));
				//root.children.get(move).wins+=1;
		}
		
		
		public int selectNode(Node node){
			double bestFoundValue = 0;
			int bestFoundMove = 0;
			for(Node c : node.children){
					double value;
					if(c.visits > this.visitLimit){
						double winrate = ((double)c.wins / c.visits);
						value = winrate + (Math.sqrt(Math.log(node.visits) / (5*c.visits)));
					}
					else{
						value = (10000 + 1000*Math.random());	
					}
					if(value > bestFoundValue){
						bestFoundValue = value;
						bestFoundMove = node.children.indexOf(c);
					}
			}
		//	System.out.println("Move:" + bestFoundMove + " Value:" + bestFoundValue);
			System.out.print(".");
			return bestFoundMove;
		}
		
		public void generateChildren(Node parent, BoardGame board, Player player) {
			List<Node> children_list = new ArrayList<Node>();
			for (Integer m : board.legalMoves(player)) {
				Node child = new Node(parent);
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



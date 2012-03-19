import java.util.*;
import java.io.*;

//What does a node need? Must keep track of wins and visits at that particular game state. If choose this move-node, run simulations and update results. 
//Each node would then to be one of the available LegalMoves. Make node for each LegalMove.

public class MonteCarloTree{
        
      private Node root;
        private int visitLimit;
        private static int index = 0;
        
        private class Node {
                private int wins = 0;
                private int visits = 0;
                private Node parent;
                private int my_index;
                private String name;
                private List<Node> children;
                private int level = 0;
                
                public Node(Node parent, String name){
                        this.parent = parent;
                        this.name = name;
                        //System.out.println("Creating " + index);
                        this.my_index = index++;
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
                public String treeString(){
                        StringBuffer buffer = new StringBuffer();
                        if (parent != null) {
                                buffer.append(""+parent.my_index+"->"+my_index + ";\n");   
                            }
                            buffer.append("" + my_index + " [ label =\"" + name + "\\n" + wins + "/" + visits + "\"];" + "\n");
                        if(children != null){
                            
                            
                            for(Node n : children){
                                buffer.append(n.treeString()); 
                            }
                        }
                        return buffer.toString();
                }
        }
        
        public MonteCarloTree(int visitLimit) {
                this.visitLimit = visitLimit;
        }
        
        public void createRoot(){
                index = -1;
                root = new Node(null, "Root");
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
        //  current_node.visits+= 1;
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
                    //System.out.println("added children to " + current_node.my_index);
                }
                if(current_node.children != null){
                        new_move = selectNode(current_node);
                        current_node = current_node.children.get(new_move);
                    //  current_node.visits += 1;
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
                        value = winrate + 0.52*(Math.sqrt((Math.log(node.visits)) / (c.visits)));
                    }
                    else{
                        value = (10000 + 1000*Math.random());   
                    }
                    if(value > bestFoundValue){
                        bestFoundValue = value;
                        bestFoundMove = node.children.indexOf(c);
                    }
            }
          //System.out.println("Move:" + bestFoundMove + " Value:" + bestFoundValue);
          //  System.out.print(".");
            return bestFoundMove;
        }
        
        public void generateChildren(Node parent, BoardGame board, Player player) {
            List<Node> children_list = new ArrayList<Node>();
            for (Integer m : board.legalMoves(player)) {
                Node child = new Node(parent, "" + m + ": " + board.showMove(m));
                child.parent = parent;
                children_list.add(child);
            }
            parent.children = children_list;
        }
        
        public int getBestMoveVisits(){
                        Node winner = root.children.get(0);
                        int winning_move = 0;
                        for (Node c : root.children){
                                if(c.visits > winner.visits){
                                        winner = c; 
                                        winning_move = root.children.indexOf(c);
                                }
                        }
                        return winning_move;
        }
        
        public int getBestMoveSecure(){
            Node winner = root.children.get(0);
            int winning_move = 0;
            for(Node c : root.children){
                    double winrate = ((double)c.wins / c.visits);
                    double node_value = winrate + (1/Math.sqrt(c.visits));
                    double winner_winrate = ((double)winner.wins / winner.visits);
                    double winner_value = winner_winrate + (1/Math.sqrt(winner.visits));
                    System.out.println(node_value);
                    if(node_value > winner_value){
                        winner = c;
                        winning_move = root.children.indexOf(c);
                    }
            }
            return winning_move;
        }
        
        public void dotGraph(){
            try{
                String fileName =  "trees/montecarlo.dot";
              File file = new File(fileName);
              boolean exist = file.createNewFile();
              FileWriter fstream = new FileWriter(fileName);
              BufferedWriter out = new BufferedWriter(fstream);
              StringBuffer string = new StringBuffer();
              string.append("digraph " + "montecarlo" + " {");
              string.append(root.treeString());
              string.append("}");
              out.write(string.toString());
              out.close();
            }
            catch(Exception e){
                e.printStackTrace();
                System.exit(-1);
            }
        }
}



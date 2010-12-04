import java.util.*;

/*
 *      Axiom.java
 *      
 */


public class Axiom implements BoardGame{

    // data members
    private HashMap<String, Cube> board;
    private ArrayList<String> moves;
    private Player[] players = new Player[2];
    private int turn = 0;
    
    // constructor
    public Axiom() {
        // Initial Game-state
        board = new HashMap<String, Cube>();
        Cube b1 = new Cube(-1, 0, 2, Cube.XUP, Cube.NONE, Cube.BLACK);
        b1.addSceptre(Cube.ZUP, Cube.BLACK);
        b1.setBoard(board); // link board to cube
        board.put(b1.getName(), b1); // link cube to board
        Cube b2 = new Cube(0, 0, 2, Cube.ZDOWN, Cube.NONE, Cube.BLACK);
        b2.setBoard(board);
        board.put(b2.getName(), b2);
        Cube b3 = new Cube(1, 0, 2, Cube.XUP, Cube.NONE, Cube.BLACK);
        b3.addSceptre(Cube.ZUP, Cube.BLACK);
        b3.setBoard(board);
        board.put(b3.getName(), b3);
    }
    
    public Axiom(HashMap<String, Cube> board) {
        this.board = board;
    }
    
    public void firstPlayer(Player p1) {
    	players[0] = p1;
    }
    
    public void secondPlayer(Player p2) {
    	players[1] = p2;
    }
    
    public void swapPlayers() {
		turn = 1 - turn;
    }
    
    // interface methods 
    // Detect if the color has won in the current board
    public boolean hasWon(int color) {
    	// if it is not your turn and there are different color sceptres on one cube
    	// or if there are two of the same on the same cube and it is your turn
    	for (String k : board.keySet()) {
    		Cube c = board.get(k);
    		if (c.twoSceptreDifferentColor() && color != players[turn].getNum()) {
    			return true;
    		}
    		if (c.twoSceptreSameColor() && color != c.getFace(c.firstSceptre())) {
    			return true;
    		}
    	}
		// or, it is not your turn and the current player has no moves
		if (color != players[turn].getNum()) {
			movesCheck(players[turn]);
			if (moves.size() == 0) {
				return true;
			}
		}
        return false;
    }

    // JMARTIN2 TO DO
    // Detect if the game is over in the current board
    public boolean gameOver() {
    	
    	// when two sceptres on one cube
    	
    	// or the current player has no moves.
    
        return false;
    }

	// JMARTIN2 TO DO
	// Generate a list of strings representing the possible moves
	// for the Player p. num of Player is color.
	public void generateMoves(Player p) {
		moves = new ArrayList<String>();
		// evaluate each cube
		
			// can it be moved?
				
				// find all locations where this cube can be placed
			
			// does it have sceptres?
			
				// find all places where this sceptre can be placed
	}

	// JMARTIN2 TO DO
    public boolean makeMove(Player p, int m) {
		// look up move in moves ArrayList
		
		// if Cube move
		
			// remove from board
			
			// rotate to new orientation
			
			// add to board
		
		// if Sceptre move
		
			// remove from current location
			
			// add to new location

		swapPlayers();

		// never gets another move in this game, so always return false
        return false;
    }
    
    public ArrayList<Integer> legalMoves(Player p) {
		movesCheck(p);
		ArrayList<Integer> t = new ArrayList<Integer>();
		for (int i = 0; i < moves.size(); i++) {
			t.add(i);
		}
        return t;
    }
    
    public boolean legalMove(Player p, int m) {
		movesCheck(p);
        return m >= 0 && m < moves.size();
    }
    
    public String showMoves(Player p) {
		movesCheck(p);
    	String s = "";
    	for (String tm : moves) {
    		s += tm + "\n";
    	}
    	return s;
    }

	public void movesCheck(Player p) {
    	if (moves == null) {
    		generateMoves(p);
    	}	
	}

    // Returns a copy of the current board, for searching
    public BoardGame clone() {
        HashMap<String, Cube> b = new HashMap<String, Cube>();
        for (String s : board.keySet()) {
            Cube c = board.get(s).clone();
            c.setBoard(b);
            b.put(s, c);
        }
        return new Axiom(b);
    }

    public String toString() {
        String s = "Current Axiom Cubes\n";
        for (String k : board.keySet()) {
            Cube c = board.get(k);
            s += c + "\n";
            if (c.isOccupied()) {
                s += c.toSceptreString(c.firstSceptre()) + "\n";
                if (c.secondSceptre() != Cube.NONE) {
                    s += c.toSceptreString(c.secondSceptre()) + "\n";
                }
            }
        }
        return s;
    }

    public static void main (String args[]) {
        Axiom g = new Axiom();
        Player p1 = new Player(Cube.BLACK, Cube.WHITE, Player.HUMAN, 2);
        Player p2 = new Player(Cube.WHITE, Cube.BLACK, Player.RANDOM, 0);
        g.firstPlayer(p1);
        g.secondPlayer(p2);
        Host.hostGame(g, p1, p2);
    }
}

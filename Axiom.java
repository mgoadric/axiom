import java.util.*;

/*
 *      Axiom.java
 *      
 */


public class Axiom implements BoardGame{

    // data members
    HashMap<String, Cube> board;
    ArrayList<String> moves;
    
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
    
    // interface methods 
    // JMARTIN2 TO DO
    // Detect if the game is over in the current board
    public boolean gameOver() {
        return false;
    }

    // JMARTIN2 TO DO
    // Detect if the color has won in the current board
    public boolean hasWon(int color) {
        return false;
    }
    
    public boolean makeMove(Player p, int m) {
        return false;
    }
    
    public ArrayList<Integer> legalMoves(Player p) {
        return null;
    }
    
    public boolean legalMove(Player p, int m) {
        return false;
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
        Player p1 = new Player(Cube.BLACK, Cube.WHITE, Player.MINIMAX, 2);
        Player p2 = new Player(Cube.WHITE, Cube.BLACK, Player.RANDOM, 0);
        Host.hostGame(g, p1, p2);
    }
}

import java.util.*;

/*
 *      Axiom.java
 *      
 */


public class Axiom implements BoardGame{

	// data members
	
	// constructor
	
	// interface methods FILL THESE IN
	public boolean gameOver() {
		return false;
	}

	public boolean hasWon(int player) {
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

	public BoardGame clone() {
		return null;
	}

	// Really should be method of superclass...
	public void hostGame(Player player1, Player player2) {
        Player currPlayer = player1; 
        Player waitPlayer = player2;
        while (!(gameOver())) {
            System.out.println(this);
            int move = currPlayer.chooseMove(this);
            while (!(legalMove(currPlayer, move))) {
                System.out.println("" + move + " is not legal");
                move = currPlayer.chooseMove(this);
            }
            makeMove(currPlayer, move);
            Player temp = currPlayer;
            currPlayer = waitPlayer;
            waitPlayer = temp;
		}

		// Someone just won the game!
        System.out.println(this);
        if (hasWon(currPlayer.getNum())) {
            System.out.println("Player" + currPlayer + " wins!");
        } else if (hasWon(waitPlayer.getNum())) {
            System.out.println("Player" + waitPlayer + " wins!");
        } else {
            System.out.println("Tie Game");
		}
	}
	
    public static void main (String args[]) {
    	Axiom g = new Axiom();
    	Player p1 = new Player(Cube.BLACK, Cube.WHITE, Player.MINIMAX, 2);
    	Player p2 = new Player(Cube.WHITE, Cube.BLACK, Player.RANDOM, 0);
    	g.hostGame(p1, p2);
    }
}

import java.util.*;

/**
 * Translated from the original python written by Christine Alvorado
 */

public class RandomPlayer extends Player {

    // Constructor    
    public RandomPlayer(int playerNum, int oppNum) {
        super(playerNum, oppNum, 0);
    }

    public int chooseMove(BoardGame board) {
        // Returns the next move that this player wants to make
        int move = -1;
        if (Host.VERBOSE) {
	        System.out.println(board.showMoves(this));
	    }
        ArrayList<Integer> t = board.legalMoves(this);
        move = t.get((int)(Math.random() * t.size()));
        if (Host.VERBOSE) {
	        System.out.println("" + num + ": Making move " + move + " " + board.showMove(move));
	    }
        return move;
    }
} 

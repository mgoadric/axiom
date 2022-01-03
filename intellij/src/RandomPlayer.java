import java.util.*;

/**
 * Translated from the original python written by Christine Alvorado
 */

public class RandomPlayer extends Player {

    // Constructor    
    public RandomPlayer(int playerNum, int oppNum) {
        super(playerNum, oppNum);
    }

    public int chooseMove(BoardGame board) {
        // Returns the next move that this player wants to make
        int move = -1;
        ArrayList<Integer> t = board.legalMoves(this);
        move = t.get((int)(Math.random() * t.size()));
        return move;
    }
} 

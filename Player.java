import java.util.*;

/**
 * Translated from the original python written by Christine Alvorado
 */

abstract public class Player {

    // Data Members
    protected int num;
    protected int opp;
    protected int type;
    protected int ply;
    protected int nodes;

    // Constructor    
    public Player(int playerNum, int oppNum, int ply) {
        this.num = playerNum;
        this.opp = oppNum;
        this.ply = ply;
        this.nodes = 0;
    }

    // Methods
    public String toString() {
        return "" + this.num;
    }    
    
    public int getNum() {
        return num;
    }
    
    // The default player defines a very simple score function
    // You will write the score function in the MancalaPlayer below
    // to improve on this function.
    public int score(BoardGame board) {
        // Returns the score for this player given the state of the board
        Axiom b = (Axiom)board;
        if (board.hasWon(this.num)) {
            return 100;
        } else if (board.hasWon(this.opp)) {
            return -100;
        } else {
//            return b.freeCubes(this.num) - b.freeCubes(this.opp);
//            return b.numCubes(this.num) - b.numCubes(this.opp);
            return b.freeCubes(this.num) + b.numCubes(this.num) - (b.freeCubes(this.opp) + b.numCubes(this.opp));
//            return b.numCubes(this.num);
        }
    }

    abstract public int chooseMove(BoardGame board);
} 

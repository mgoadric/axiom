import java.util.*;

/**
 * Translated from the original python written by Christine Alvorado
 */

public class MiniMaxPlayer extends Player {

    // Constructor    
    public MiniMaxPlayer(int playerNum, int oppNum, int ply) {
        super(playerNum, oppNum, ply);
    }


    public int minimaxMove(BoardGame board, int ply ) {
        // Choose the best minimax move.  Returns (move, val) 
        int move = -1;
        int score = Integer.MIN_VALUE;
        MiniMaxPlayer turn = this;
        for (Integer m : board.legalMoves( this )) {
            if (ply == 0) {
                return m;
            }
            if (board.gameOver()) {
                return -1;  // Can't make a move, the game is over
            }
            BoardGame nb = (BoardGame)board.clone();
            nb.makeMove(this, m);
            MiniMaxPlayer opp = new MiniMaxPlayer(this.opp, this.num, this.ply);
            int s[] = opp.minValue(nb, ply-1, turn);
            System.out.println("Move " + m + " score " + s[0] + " reply " + s[1]);
            if (s[0] > score) {
                move = m;
                score = s[0];
            }
        }
        return move;
    }

    public int[] maxValue(BoardGame board, int ply, Player turn) {
        // Find the minimax value for the next move for this player
        //    at a given board configuation
        //    Returns (score, oppMove)
        if (board.gameOver()) {
            int[] t = {turn.score(board), -1};
            return t;
        }
        int[] s = {Integer.MIN_VALUE, -1};
        for (Integer m : board.legalMoves( this )) {
            if (ply == 0) {
                s[0] = turn.score(board);
                s[1] = m;
                return s;
            }

            // make a new player to play the other side
            MiniMaxPlayer opponent = new MiniMaxPlayer(this.opp, this.num, this.ply);

            // Copy the board so that we don't ruin it
            BoardGame nextBoard = (BoardGame)board.clone();

            nextBoard.makeMove( this, m );
            int[] s2 = opponent.minValue(nextBoard, ply-1, turn);
            if (s2[0] > s[0]) {
                s[0] = s2[0];
                s[1] = m;            
            }
        }
        return s;
    }
    
    public int[] minValue(BoardGame board, int ply, Player turn ) {
        // Find the minimax value for the next move for this player
        //    at a given board configuation
        if (board.gameOver()) {
            int[] t = {turn.score(board), -1};
            return t;
        }
        int[] s = {Integer.MAX_VALUE, -1};
        for (Integer m : board.legalMoves( this )) {
            if (ply == 0) {
                s[0] = turn.score(board);
                s[1] = m;
                return s;
            }
            // make a new player to play the other side
            MiniMaxPlayer opponent = new MiniMaxPlayer(this.opp, this.num, this.ply);
            // Copy the board so that we don't ruin it
            BoardGame nextBoard = (BoardGame)board.clone();
            nextBoard.makeMove( this, m );
            int[] s2 = opponent.maxValue(nextBoard, ply-1, turn);
            if (s2[0] < s[0]) {
                s[0] = s2[0];
                s[1] = m;            
            }
        }
        return s;
    }

    public int chooseMove(BoardGame board) {
        // Returns the next move that this player wants to make
        int move = -1;
        if (Host.VERBOSE) {
	        System.out.println(board.showMoves(this));
	    }
        move = minimaxMove( board, ply );
        if (Host.VERBOSE) {
	        System.out.println("" + num + ": Making move " + move + " " + board.showMove(move));
	    }
        return move;
    }
} 

import java.util.*;

/**
 * Translated from the original python written by Christine Alvorado
 */

public class AlphaBetaPlayer extends Player {

    protected int ply;

    // Constructor    
    public AlphaBetaPlayer(int playerNum, int oppNum, int ply) {
        super(playerNum, oppNum);
        this.ply = ply;
    }

    public int alphaBetaMove(BoardGame board, int ply ) {
        // Choose the best minimax move.  Returns (move, val) 
        int move = -1;
        int score = Integer.MIN_VALUE;
        int alpha = Integer.MIN_VALUE;
        int beta = Integer.MAX_VALUE;

        AlphaBetaPlayer turn = this;
        for (Integer m : board.legalMoves( this )) {
            if (ply == 0) {
                return m;
            }
            if (board.gameOver()) {
                return -1;  // Can't make a move, the game is over
            }
            BoardGame nb = (BoardGame)board.clone();
            nb.makeMove(this, m);
            AlphaBetaPlayer opp = new AlphaBetaPlayer(this.opp, this.num, this.ply);
            int s[] = opp.alphaBetaMinValue(nb, ply-1, turn, alpha, beta);
            if (s[1] == -1) {
            	s[0] *= 100;
            }
            if (Host.VERBOSE) {
                System.out.println("Move " + m + " score " + s[0] + " reply " + s[1]);
            }
            if (s[0] > score) {
                move = m;
                score = s[0];
            }
        }
        return move;
    }

    public int[] alphaBetaMaxValue(BoardGame board, int ply, Player turn, int alpha, int beta) {
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
            AlphaBetaPlayer opponent = new AlphaBetaPlayer(this.opp, this.num, this.ply);

            // Copy the board so that we don't ruin it
            BoardGame nextBoard = (BoardGame)board.clone();

            nextBoard.makeMove( this, m );
            int[] s2 = opponent.alphaBetaMinValue(nextBoard, ply-1, turn, alpha, beta);
            if (s2[0] > s[0]) {
                s[0] = s2[0];
                s[1] = m;            
            }
            if (s[0] >= beta) {
                break;
            }
            alpha = (int)Math.max(alpha, s[0]);
        }
        return s;
    }
    
    public int[] alphaBetaMinValue(BoardGame board, int ply, Player turn, int alpha, int beta ) {
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
            AlphaBetaPlayer opponent = new AlphaBetaPlayer(this.opp, this.num, this.ply);
            // Copy the board so that we don't ruin it
            BoardGame nextBoard = (BoardGame)board.clone();
            nextBoard.makeMove( this, m );
            int[] s2 = opponent.alphaBetaMaxValue(nextBoard, ply-1, turn, alpha, beta);
            if (s2[0] < s[0]) {
                s[0] = s2[0];
                s[1] = m;            
            }
            if (s[0] <= alpha) {
                break;
            }    
            beta = (int)Math.min(beta, s[0]);
        }
        return s;
    }

    public int chooseMove(BoardGame board) {
        // Returns the next move that this player wants to make
        int move = -1;
        move = alphaBetaMove( board, ply);
        return move;
    }
} 

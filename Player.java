import java.util.*;

/**
 * Translated from the original python written by Christine Alvorado
 */

public class Player {

	public static Scanner sc = new Scanner(System.in);

	// Constants
	public static int HUMAN = 0;
    public static int RANDOM = 1;
    public static int MINIMAX = 2;
    public static int ABPRUNE = 3;
    public static int CUSTOM = 4;

	// Data Members
	private int num;
	private int opp;
	private int type;
	private int ply;
	private int nodes;

	// Constructor    
	public Player(int playerNum, int oppNum, int playerType, int ply) {
        this.num = playerNum;
        this.opp = oppNum;
        this.type = playerType;
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
    
   	public int minimaxMove(BoardGame board, int ply ) {
        // Choose the best minimax move.  Returns (move, val) 
        int move = -1;
        int score = Integer.MIN_VALUE;
        Player turn = this;
        for (Integer m : board.legalMoves( this )) {
            if (ply == 0) {
                return m;
            }
            if (board.gameOver()) {
                return -1;  // Can't make a move, the game is over
			}
            BoardGame nb = (BoardGame)board.clone();
            nb.makeMove(this, m);
            Player opp = new Player(this.opp, this.num, this.type, this.ply);
            int s[] = opp.minValue(nb, ply-1, turn);
            if (s[0] > score) {
                move = s[1];
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
            Player opponent = new Player(this.opp, this.num, this.type, this.ply);

            // Copy the board so that we don't ruin it
            BoardGame nextBoard = (BoardGame)board.clone();

            nextBoard.makeMove( this, m );
            int[] s2 = opponent.minValue(nextBoard, ply-1, turn);
            if (s2[0] > s[0]) {
                s[0] = s2[0];
                s[1] = s2[1];            
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
            Player opponent = new Player(this.opp, this.num, this.type, this.ply);
            // Copy the board so that we don't ruin it
            BoardGame nextBoard = (BoardGame)board.clone();
            nextBoard.makeMove( this, m );
            int[] s2 = opponent.maxValue(nextBoard, ply-1, turn);
            if (s2[0] < s[0]) {
                s[0] = s2[0];
                s[1] = s2[1];            
            }
        }
        return s;
    }


    // The default player defines a very simple score function
    // You will write the score function in the MancalaPlayer below
    // to improve on this function.
    public int score(BoardGame board) {
        // Returns the score for this player given the state of the board """
        if (board.hasWon( this.num )) {
            return 100;
        } else if (board.hasWon( this.opp )) {
            return 0;
        } else {
            return 50;
        }
	}


/*
    // You should not modify anything before this point.
    // The code you will add to this file appears below this line.

    // You will write this function (and any helpers you need)
    // You should write the function here in its simplest form:
    //   1. Use ply to determine when to stop (when ply == 0)
    //   2. Search the moves in the order they are returned from the board's
    //       legalMoves function.
    // However, for your custom player, you may copy this function
    // and modify it so that it uses a different termination condition
    // and/or a different move search order.
    def alphaBetaMove( this, board, ply ):
        """ Choose a move with alpha beta pruning """
        move = -1
        score = -INFINITY
        alpha = -INFINITY
        beta = INFINITY
        turn = this
        this.nodes = 0
        bmoves = board.legalMoves( this )
        nmoves = []
        gmoves = []
        for m in bmoves:
            if 7 - m == board.getPlayersCups(this.num)[m-1]:
                gmoves.append(m)
                //System.out.println("Killer move!!")
            else:
                nmoves.append(m)
        for m in gmoves:
            if ply == 0:
                return (this.score(board), m)
            if board.gameOver():
                return (-1, -1)  // Can't make a move, the game is over
            nb = deepcopy(board)
            nb.makeMove(this, m)
            opp = Player(this.num, this.type, this.ply)
            s, oppMove, nodes = opp.maxABValue(nb, ply-1, turn, alpha, beta)
            this.nodes += nodes + 1
            if s > score:
                move = m
                score = s
        for m in nmoves:
            if ply == 0:
                return (this.score(board), m)
            if board.gameOver():
                return (-1, -1)  // Can't make a move, the game is over
            nb = deepcopy(board)
            nb.makeMove(this, m)
            opp = Player(this.opp, this.type, this.ply)
            s, oppMove, nodes = opp.minABValue(nb, ply-1, turn, alpha, beta)
            this.nodes += nodes + 1
            if s > score:
                move = m
                score = s
        return score, move
                
    def maxABValue( this, board, ply, turn, alpha, beta):
        """ Find the minimax value for the next move for this player
            at a given board configuation
            Returns (score, oppMove)"""
        if board.gameOver():
            return (turn.score( board ), -1, 1)
        score = -INFINITY
        move = -1
        bmoves = board.legalMoves( this )
        nmoves = []
        gmoves = []
        for m in bmoves:
            if 7 - m == board.getPlayersCups(this.num)[m-1]:
                gmoves.append(m)
                //System.out.println("Killer move!!")
            else:
                nmoves.append(m)
        for m in gmoves:
            if ply == 0:
                return (turn.score( board ), m, 1)
            // make a new player to play the other side
            opponent = Player(this.num, this.type, this.ply)
            // Copy the board so that we don't ruin it
            nextBoard = deepcopy(board)
            nextBoard.makeMove( this, m )
            s, oppMove, nodes = opponent.maxABValue(nextBoard, ply-1, turn, alpha, beta)
            this.nodes += nodes
            if s > score:
                move = m
                score = s
            if score >= beta:
                break
            alpha = max(alpha, score)
        for m in nmoves:
            if ply == 0:
                return (turn.score( board ), m, 1)
            // make a new player to play the other side
            opponent = Player(this.opp, this.type, this.ply)
            // Copy the board so that we don't ruin it
            nextBoard = deepcopy(board)
            nextBoard.makeMove( this, m )
            s, oppMove, nodes = opponent.minABValue(nextBoard, ply-1, turn, alpha, beta)
            this.nodes += nodes
            if s > score:
                move = m
                score = s
            if score >= beta:
                break
            alpha = max(alpha, score)
        return (score, move, nodes)
    
    def minABValue( this, board, ply, turn, alpha, beta ):
        """ Find the minimax value for the next move for this player
            at a given board configuation"""
        if board.gameOver():
            return (turn.score( board ), -1, 1)
        score = INFINITY
        move = -1
        bmoves = board.legalMoves( this )
        nmoves = []
        gmoves = []
        for m in bmoves:
            if 7 - m == board.getPlayersCups(this.num)[m-1]:
                gmoves.append(m)
                //System.out.println("Killer move!!")
            else:
                nmoves.append(m)
        for m in gmoves:
            if ply == 0:
                return (turn.score( board ), m, 1)
            // make a new player to play the other side
            opponent = Player(this.num, this.type, this.ply)
            // Copy the board so that we don't ruin it
            nextBoard = deepcopy(board)
            nextBoard.makeMove( this, m )
            s, oppMove, nodes = opponent.minABValue(nextBoard, ply-1, turn, alpha, beta)
            this.nodes += nodes
            if s < score:
                score = s
                move = m
            if score <= alpha:
                break
            beta = min(beta, score)
        for m in nmoves:
            if ply == 0:
                return (turn.score( board ), m, 1)
            // make a new player to play the other side
            opponent = Player(this.opp, this.type, this.ply)
            // Copy the board so that we don't ruin it
            nextBoard = deepcopy(board)
            nextBoard.makeMove( this, m )
            s, oppMove, nodes = opponent.maxABValue(nextBoard, ply-1, turn, alpha, beta)
            this.nodes += nodes
            if s < score:
                score = s
                move = m
            if score <= alpha:
                break
            beta = min(beta, score)
        return (score, move, this.nodes)

*/

    public int chooseMove(BoardGame board) {
        // Returns the next move that this player wants to make
        int move = -1;
        if (type == HUMAN) {
        	System.out.println("Please enter your move:");
            move = sc.nextInt();
            while (!board.legalMove(this, move)) {
                System.out.println("" + move + "is not valid");
	        	System.out.println("Please enter your move:");
                move = sc.nextInt();
            }
        } else if (type == RANDOM) {
        	ArrayList<Integer> t = board.legalMoves(this);
            move = t.get((int)(Math.random() * t.size()));
            System.out.println("chose move" + move);
        } else if (type == MINIMAX) {
            move = minimaxMove( board, ply );
            System.out.println("chose move" + move);
//        } else if (this.type == this.ABPRUNE) {
//            move = this.alphaBetaMove( board, this.ply)
//            System.out.println("chose move" + move);
        } else if (type == CUSTOM) {
            System.out.println("Custom player not yet implemented");
        } else {
            System.out.println("Unknown player type");
		}
		return move;
	}
} 

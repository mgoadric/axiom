import java.util.*;

//1-23-12: Basic set up. What does my player need? What does my tree need to keep track of?

public class MonteCarloPlayer extends Player {
		
		//number of times to run simulations till move choosen
    protected int simnum;
    //Tree of moves
    protected MonteCarloTree moveTree;

    // Constructor    
    public MonteCarloPlayer(int playerNum, int oppNum, int simnum, MonteCarloTree moveTree) {
        super(playerNum, oppNum);
        this.simnum = simnum;
        this.moveTree = moveTree;
    }
    
    public int chooseMove(BoardGame board) {
        // Returns the next move that this player wants to make
        int move = -1;
       // move = monteCarloMove( board, ply);
        return move;
    }
} 

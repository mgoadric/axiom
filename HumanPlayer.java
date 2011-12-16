import java.util.*;

/**
 * Translated from the original python written by Christine Alvorado
 */

public class HumanPlayer extends Player {

    public static Scanner sc = new Scanner(System.in);

    // Constructor    
    public HumanPlayer(int playerNum, int oppNum) {
        super(playerNum, oppNum, 0);
    }

    public int chooseMove(BoardGame board) {
        // Returns the next move that this player wants to make
        int move = -1;
        System.out.println("Please enter your move:");
        move = sc.nextInt();
        while (!board.legalMove(this, move)) {
            System.out.println("" + move + "is not valid");
            System.out.println("Please enter your move:");
            move = sc.nextInt();
        }
        return move;
    }
} 

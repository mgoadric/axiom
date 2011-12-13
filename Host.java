import java.util.*;

/**
 * Hosts a game between two players
 */
public class Host {
    
    public static int SLEEP = 500;
    public static boolean VERBOSE = true;
    
    public static void graphicsBoard(String boardstate) {
        try {
            Thread.sleep(SLEEP);
            new ShowBoard(new Scanner(boardstate));
        } catch (InterruptedException ie) {
        } 
    }
    
    public static int hostGame(BoardGame game, Player player1, Player player2, boolean graphics) {
        Player currPlayer = player1; 
        Player waitPlayer = player2;
        int avemoves = 0;
        int count = 0;
        if (graphics) {
            ShowBoard.setupUniverse();
        }
        while (!(game.gameOver())) {
            count++;
            if (VERBOSE) {
                System.out.println("Current Board: Turn " + count);
                System.out.println(game);
            }

            if (graphics) {
                graphicsBoard(game.toString());
                if (count == 1) {
                    try {
                        Thread.sleep(4000);
                        new ShowBoard(new Scanner(game.toString()));
                    } catch (InterruptedException ie) {
                    } 
                }
            }
            int move = currPlayer.chooseMove(game);
            avemoves += game.legalMoves(currPlayer).size();
            while (!(game.legalMove(currPlayer, move))) {
                System.out.println("" + move + " is not legal");
                move = currPlayer.chooseMove(game);
            }
            game.makeMove(currPlayer, move);
            Player temp = currPlayer;
            currPlayer = waitPlayer;
            waitPlayer = temp;
            if (count > 1000) {
                break;
            }
        }

        // Someone just won the game! Tell us please!
        if (VERBOSE) {
            System.out.println("Current Board: Final");
            System.out.println(game);
        }
        if (graphics) {
            graphicsBoard(game.toString());
        }
        if (game.hasWon(currPlayer.getNum())) {
            System.out.println("Player " + currPlayer + " wins! avemoves = " + (avemoves / count));
            return currPlayer.getNum();
        } else if (game.hasWon(waitPlayer.getNum())) {
            System.out.println("Player " + waitPlayer + " wins! avemoves = " + (avemoves / count));
            return waitPlayer.getNum();
        } else {
            System.out.println("Tie Game avemoves = " + (avemoves / count));
            return 2;
        }
    }
}
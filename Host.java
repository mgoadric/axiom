import java.util.*;

/**
 * Hosts a game between two players
 */
public class Host {
    
    public static int hostGame(BoardGame game, Player player1, Player player2, boolean graphics) {
        Player currPlayer = player1; 
        Player waitPlayer = player2;
        int count = 0;
        if (graphics) {
            ShowBoard.setupUniverse();
        }
        while (!(game.gameOver())) {
            count++;
            System.out.println("Current Board: Turn " + count);
            System.out.println(game);

            if (graphics) {
                try {
                    Thread.sleep(1000);
                    new ShowBoard(new Scanner(game.toString()));
                } catch (InterruptedException ie) {
                }
            }
            int move = currPlayer.chooseMove(game);
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
        System.out.println("Current Board: Final");
        System.out.println(game);
        if (graphics) {
            try {
                Thread.sleep(1000);
                new ShowBoard(new Scanner(game.toString()));
            } catch (InterruptedException ie) {
            }
        }
        if (game.hasWon(currPlayer.getNum())) {
            System.out.println("Player " + currPlayer + " wins!");
            return currPlayer.getNum();
        } else if (game.hasWon(waitPlayer.getNum())) {
            System.out.println("Player " + waitPlayer + " wins!");
            return waitPlayer.getNum();
        } else {
            System.out.println("Tie Game");
            return 2;
        }
    }
}
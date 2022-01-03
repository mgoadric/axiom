import java.util.*;
import java.io.*;

/**
 * Hosts a game between two players
 */
public class Host {
    
    public static boolean VERBOSE = true;
    
    public static int hostGame(BoardGame game, Player player1, Player player2, boolean graphics, String dirName) {
        int winner = 0;
        try {
            Player currPlayer = player1; 
            Player waitPlayer = player2;
            boolean success = (new File("sessions/"+dirName)).mkdir();
            PrintWriter out = new PrintWriter(new FileWriter("sessions/" + dirName + "/" + System.currentTimeMillis() + ".txt"));
            BufferedWriter outZero = new BufferedWriter(new FileWriter("sessions/" + dirName + "/scores.csv", true));
            out.println("" + player1 + " vs " + player2 + "\n");
            int avemoves = 0;
            int count = 0;

            while (!(game.gameOver())) {
                count++;
                if (VERBOSE) {
                    System.out.println("Current Board: Turn " + count);
                    System.out.println(game);
                    out.print("" + count + "| ");
                }
    

                if (VERBOSE) {
                    System.out.println(game.showMoves(currPlayer));
                }
                int move = currPlayer.chooseMove(game);
                avemoves += game.legalMoves(currPlayer).size();
                while (!(game.legalMove(currPlayer, move))) {
                    System.out.println("" + move + " is not legal");
                    move = currPlayer.chooseMove(game);
                }
                if (VERBOSE) {
                    System.out.println("" + currPlayer.num + ": Making move " + move + " " + game.showMove(move));
                    out.println("" + currPlayer.num + ": Making move " + move + " " + game.showMove(move));
                    out.flush();
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
                out.println("\nFinal Board");
                System.out.println(game);
                out.println(game);
            }

            if (game.hasWon(currPlayer.num)) {
                winner = currPlayer.num;
            } else if (game.hasWon(waitPlayer.num)) {
                winner = waitPlayer.num;
            } else {
                System.out.println("Tie Game avemoves = " + (avemoves / count));
                winner = 2;
            }
            System.out.println("Player " + winner + " wins! avemoves = " + (avemoves / count));
            out.println("Player " + winner + " wins! avemoves = " + (avemoves / count));
            if(winner == 0){
              outZero.write(""+0+","+1);
            }
            else{
              outZero.write(""+1+","+0);
            }
            outZero.write("\n");
            outZero.flush();
            outZero.close();
            out.close();
        } catch (IOException ioe) {
            System.out.println("Something went wrong with the file output");
            ioe.printStackTrace();
        }
        return winner;

    }
}
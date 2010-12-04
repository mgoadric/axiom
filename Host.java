public class Host {
    
    public static void hostGame(BoardGame game, Player player1, Player player2) {
        Player currPlayer = player1; 
        Player waitPlayer = player2;
        while (!(game.gameOver())) {
            System.out.println(game);
            int move = currPlayer.chooseMove(game);
            while (!(game.legalMove(currPlayer, move))) {
                System.out.println("" + move + " is not legal");
                move = currPlayer.chooseMove(game);
            }
            game.makeMove(currPlayer, move);
            Player temp = currPlayer;
            currPlayer = waitPlayer;
            waitPlayer = temp;
        }

        // Someone just won the game!
        System.out.println(game);
        if (game.hasWon(currPlayer.getNum())) {
            System.out.println("Player" + currPlayer + " wins!");
        } else if (game.hasWon(waitPlayer.getNum())) {
            System.out.println("Player" + waitPlayer + " wins!");
        } else {
            System.out.println("Tie Game");
        }
    }
}
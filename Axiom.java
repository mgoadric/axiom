/*
 *      Axiom.java
 *      
 */


public class Axiom implements BoardGame{

	// data members
	
	// constructor
	
	// methods
	public void hostGame(Player player1, Player player2) {
        Player currPlayer = player1; 
        Player waitPlayer = player2;
        while (!(gameOver())) {
            boolean again = true;
            while (again) {
                System.out.println(this);
                int move = currPlayer.chooseMove(this);
                while (!(legalMove(currPlayer, move))) {
                    System.out.println("" + move + " is not legal");
                    move = currPlayer.chooseMove(this);
                }
                again = this.makeMove(currPlayer, move);
            Player temp = currPlayer;
            currPlayer = waitPlayer;
            waitPlayer = temp;
		}
        System.out.println(this);
        if (hasWon(currPlayer.getNum())) {
            System.out.println("Player" + currPlayer + " wins!");
        } else if (hasWon(waitPlayer.getNum())) {
            System.out.println("Player" + waitPlayer + " wins!");
        } else {
            System.out.println("Tie Game");
		}
	}
/*
    public static void main (String args[]) {
/*    
    // Initial Game State
    Cube b1 = new Cube([-1, 0, 2], [1, 0, 0, 0, 3, 0]);
    Cube b2 = new Cube([0, 0, 2], [0, 0, 0, 0, 0, 1]);
    Cube b3 = new Cube([1, 0, 2], [1, 0, 0, 0, 3, 0]);
    Cube b4 = new Cube([-1, 0, 2], [0, 1, 0, 0, 3, 0])
    Cube b5 = new Cube([-1, 0, 2], [0, 1, 0, 0, 3, 0])
    Cube b6 = new Cube([-1, 0, 2], [0, 1, 0, 0, 3, 0])
    
    Cube w1 = new Cube([-1, 0, 2], [0, 1, 0, 0, 3, 0])
    Cube w2 = new Cube([-1, 0, 2], [0, 1, 0, 0, 3, 0])
    Cube w3 = new Cube([-1, 0, 2], [0, 1, 0, 0, 3, 0])
    Cube w4 = new Cube([-1, 0, 2], [0, 1, 0, 0, 3, 0])
    Cube w5 = new Cube([-1, 0, 2], [0, 1, 0, 0, 3, 0])
    Cube w6 = new Cube([-1, 0, 2], [0, 1, 0, 0, 3, 0])
*/  
    }
}

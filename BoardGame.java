import java.util.*;

public interface BoardGame {

	public boolean gameOver();

	public boolean hasWon(int player);
	
	public boolean makeMove(Player p, int m);
	
	public ArrayList<Integer> legalMoves(Player p);
	
	public boolean legalMove(Player p, int m);

	public BoardGame clone();
	
	public void hostGame(Player p1, Player p2);

}
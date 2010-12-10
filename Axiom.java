import java.util.*;

/*
 *      Axiom.java
 *      
 */


public class Axiom implements BoardGame{

    // data members
    private HashMap<String, Cube> board;
    private ArrayList<String> moves;
    private Player[] players = new Player[2];
    private int turn = Cube.BLACK;
    
    // constructor
    public Axiom() {
        // Initial Game-state
        board = new HashMap<String, Cube>();
        Cube b1 = new Cube(-1,0,2, Cube.XUP, Cube.NONE, Cube.BLACK);
        b1.addSceptre(Cube.ZUP, Cube.BLACK);
        b1.setBoard(board); // link board to cube
        board.put(b1.getName(), b1); // link cube to board
        Cube b2 = new Cube(0,0,2, Cube.ZDOWN, Cube.NONE, Cube.BLACK);
        b2.setBoard(board);
        board.put(b2.getName(), b2);
        Cube b3 = new Cube(1,0,2, Cube.XUP, Cube.NONE, Cube.BLACK);
        b3.addSceptre(Cube.ZUP, Cube.BLACK);
        b3.setBoard(board);
        board.put(b3.getName(), b3);
        Cube b4 = new Cube(-1,0,1, Cube.ZUP, Cube.XDOWN, Cube.BLACK);
        b4.setBoard(board);
        board.put(b4.getName(), b4);
        Cube b5 = new Cube(0,0,1, Cube.YDOWN, Cube.XDOWN, Cube.BLACK);
        b5.setBoard(board);
        board.put(b5.getName(), b5);
        Cube b6 = new Cube(1,0,1, Cube.ZUP, Cube.XUP, Cube.BLACK);
        b6.setBoard(board);
        board.put(b6.getName(), b6);
        
        Cube w1 = new Cube(1,1,2, Cube.XDOWN, Cube.NONE, Cube.WHITE);
        w1.addSceptre(Cube.ZUP, Cube.WHITE);
        w1.setBoard(board); // link board to cube
        board.put(w1.getName(), w1); // link cube to board
        Cube w2 = new Cube(0,1,2, Cube.ZDOWN, Cube.NONE, Cube.WHITE);
        w2.setBoard(board);
        board.put(w2.getName(), w2);
        Cube w3 = new Cube(-1,1,2, Cube.XDOWN, Cube.NONE, Cube.WHITE);
        w3.addSceptre(Cube.ZUP, Cube.WHITE);
        w3.setBoard(board);
        board.put(w3.getName(), w3);
        Cube w4 = new Cube(1,1,1, Cube.ZUP, Cube.XUP, Cube.WHITE);
        w4.setBoard(board);
        board.put(w4.getName(), w4);
        Cube w5 = new Cube(0,1,1, Cube.YUP, Cube.XUP, Cube.WHITE);
        w5.setBoard(board);
        board.put(w5.getName(), w5);
        Cube w6 = new Cube(-1,1,1, Cube.ZUP, Cube.XDOWN, Cube.WHITE);
        w6.setBoard(board);
        board.put(w6.getName(), w6);
    }
    
    public Axiom(HashMap<String, Cube> board) {
        this.board = board;
    }
    
    public void firstPlayer(Player p1) {
    	players[0] = p1;
    }
    
    public void secondPlayer(Player p2) {
    	players[1] = p2;
    }
    
    public void swapPlayers() {
		turn = 1 - turn;
    }
    
    public void setTurn(int turn) {
    	this.turn = turn;
    }
    
    // interface methods 
    // Detect if the color has won in the current board
    public boolean hasWon(int color) {
    	// if it is not your turn and there are different color sceptres on one cube
    	// or if there are two of the same on the same cube and it is your turn
    	for (String k : board.keySet()) {
    		Cube c = board.get(k);
    		if (c.twoSceptreDifferentColor() && color != players[turn].getNum()) {
    			return true;
    		}
    		if (c.twoSceptreSameColor() && color != c.getFace(c.firstSceptre())) {
    			return true;
    		}
    	}
		// or, it is not your turn and the current player has no moves
		if (color != players[turn].getNum()) {
			movesCheck(players[turn]);
			if (moves.size() == 0) {
				return true;
			}
		}
        return false;
    }

    // Detect if the game is over in the current board
    public boolean gameOver() {
    	
    	// when two sceptres on one cube
    	for (String k : board.keySet()) {
    		Cube c = board.get(k);
    		if (c.twoSceptreDifferentColor() || c.twoSceptreSameColor()) {
    			return true;
    		}
    	}
    
    	// or the current player has no moves.
    	movesCheck(players[turn]);
    	if (moves.size() == 0) {
    		return true;
    	}
    
        return false;
    }

	// JMARTIN2 TO DO
	// Generate a list of strings representing the possible moves
	// for the Player p. num of Player is color.
	public void generateMoves(Player p) {

		moves = new ArrayList<String>();
		// piece together string
		// evaluate each cube
		int count = 0;
		for (String k : board.keySet()) {

			Cube c = board.get(k);
			
			// can it be moved?
			if (c.isFree() && c.getColor() == p.getNum()) {
			
				// find all locations where this cube can be placed
				for (String r : board.keySet()) {
					if (!r.equals(k)) {
					
						// find free faces not under the table.
						Cube c2 = board.get(r);
						for (int i : c2.freeFaces()) {
				
							// for each possible rotation to be in this space
							System.out.println(c + " " + c2 + " on face " + i);							
							count++;
						}
						
						// find places with domes that fit into this cube
						
					}
				}
			}
			
			// does it have a sceptre?
			if (c.isOccupied() && c.getFace(c.firstSceptre()) == p.getNum()) {
			
				// find all places where this sceptre can be placed
				int face = c.firstSceptre();
				
				// diagonal in same plane
				if (face == Cube.ZUP || face == Cube.ZDOWN) {
					
					int zmod = 1;
					String zc = "+";
					if (face == Cube.ZDOWN) {
						zmod = -1;
						zc = "-";
					}
					
					// while cubes in direction, keep making moves
					int[] xmod = {1, 1, -1, -1};
					int[] ymod = {1, -1, 1, -1};
					for (int i = 0; i < xmod.length; i++) {
						Cube cur = c;
						boolean nei = true;
						while (nei) {
							String neighbor = "" + (cur.getX() + xmod[i]) + "," + 
												   (cur.getY() + ymod[i]) + "," +
													cur.getZ();
							Cube who = board.get(neighbor);
							if (who != null && who.getFace(face) == Cube.EMPTY) {
								String neighbor2 = "" + (cur.getX() + xmod[i]) + "," + 
														(cur.getY() + ymod[i]) + "," +
														(cur.getZ() + zmod);
								if (board.get(neighbor2) == null) {
									moves.add("S(" + c.getName() + ") S(" + who.getName() + ")z" + zc);
									cur = who;
									count++;
								} else {
									nei = false;
								}
							} else {
								nei = false;
							}
						}
					}
				}
				if (face == Cube.XUP || face == Cube.XDOWN) {
					
					int xmod = 1;
					String xc = "+";
					if (face == Cube.XDOWN) {
						xmod = -1;
						xc = "-";
					}
					
					// while cubes in direction, keep making moves
					// XUP, YUP
					int[] zmod = {1, 1, -1, -1};
					int[] ymod = {1, -1, 1, -1};
					for (int i = 0; i < ymod.length; i++) {
						Cube cur = c;
						boolean nei = true;
						while (nei) {
							String neighbor = "" + (cur.getX()) + "," + 
												   (cur.getY() + ymod[i]) + "," +
												   (cur.getZ() + zmod[i]);
							Cube who = board.get(neighbor);
							if (who != null && who.getFace(face) == Cube.EMPTY) {
								String neighbor2 = "" + (cur.getX() + xmod) + "," + 
														(cur.getY() + ymod[i]) + "," +
														(cur.getZ() + zmod[i]);
								if (board.get(neighbor2) == null) {
									moves.add("S(" + c.getName() + ") S(" + who.getName() + ")x" + xc);
									cur = who;
									count++;
								} else {
									nei = false;
								}
							} else {
								nei = false;
							}
						}
					}
				}
				if (face == Cube.YUP || face == Cube.YDOWN) {
					
					int ymod = 1;
					String yc = "+";
					if (face == Cube.YDOWN) {
						ymod = -1;
						yc = "-";
					}
					
					// while cubes in direction, keep making moves
					// XUP, YUP
					int[] zmod = {1, 1, -1, -1};
					int[] xmod = {1, -1, 1, -1};
					for (int i = 0; i < xmod.length; i++) {
						Cube cur = c;
						boolean nei = true;
						while (nei) {
							nei = false;
							String neighbor = "" + (cur.getX() + xmod[i]) + "," + 
												   (cur.getY()) + "," +
												   (cur.getZ() + zmod[i]);
							Cube who = board.get(neighbor);
							if (who != null && who.getFace(face) == Cube.EMPTY) {
								String neighbor2 = "" + (cur.getX() + xmod[i]) + "," + 
														(cur.getY() + ymod) + "," +
														(cur.getZ() + zmod[i]);
								if (board.get(neighbor2) == null) {
									moves.add("S(" + c.getName() + ") S(" + who.getName() + ")y" + yc);
									cur = who;
									count++;
									nei = true;
								} 
							}
						}
					}
				}
				
				// orthogonal wrapping up, same or down
				Cube cur = c;
				int dir = face;
				boolean nei = true;
				while (nei) {						
					nei = false;
					Cube a = null;
					Cube b = null;
					if (dir == Cube.ZUP) {
						a = cur.getNeighbor(Cube.XUP);
						if (a != null) {
							b = a.getNeighbor(Cube.ZUP);
						}
						// check three placements
						if (a == null) {
							if (cur.isEmpty(Cube.XUP)) {
								moves.add("S(" + cur.getName() + ") S(" + cur.getName() + ")x+");
								nei = true;
							    count++;
							}
						} else if (b == null) {
							if (a.isEmpty(Cube.ZUP)) {
								moves.add("S(" + cur.getName() + ") S(" + a.getName() + ")z+");
								nei = true;
								cur = a;
							    count++;
							}						
						} else {
							if (b.isEmpty(Cube.XDOWN)) {
								moves.add("S(" + cur.getName() + ") S(" + b.getName() + ")x-");
								nei = true;
								cur = b;
							    count++;
							}						
						}
					}
				}
			}
		}
	}

    public boolean makeMove(Player p, int m) {
        // take string apart and make it happen
        // look up move in moves ArrayList
        
        String choice = moves.get(m);            
        int a = choice.indexOf(')');
        int b = choice.indexOf('(', a);            
        int e = choice.indexOf(',', b);
        int f = choice.indexOf(',', e);
        int g = choice.indexOf(')', f);
        int d = choice.indexOf(' ', f);
        String x = choice.substring(b + 1, e);
        String y = choice.substring(e +1, f);
        String z = choice.substring(f + 1, g);
        int h = choice.indexOf('(', d);
        int i = choice.indexOf(')', h);
        String dm1 = choice.substring(g + 1, d);
        String dm2 = choice.substring(d + 1, h - 1);
        String actualcube = choice.substring(2, choice.indexOf(')'));
        String s = choice.substring(a + 1, a + 3);
        
            
        // if Cube move
        if (choice.charAt(0) == 'C') {
            // remove from board
            int clr = board.get(actualcube).getColor();
            board.remove(actualcube);

            // add new cube to board

            int DM1 = -1;
            int DM2 = -1;
            if (dm1.equals("x+")) {
                DM1 = Cube.XUP;
            }
            else if (dm1.equals("x-")) {
                DM1 = Cube.XDOWN;
            }
            else if (dm1.equals("y+")) {
                DM1 = Cube.YUP;
            }
            else if (dm1.equals("y-")) {
                DM1 = Cube.YDOWN;
            }
            else if (dm1.equals("z+")) {
                DM1 = Cube.ZUP;
            }
            else if (dm1.equals("z-")) {
                DM1 = Cube.ZDOWN;
            }
        ////////////////////////////////////
            if (dm2.equals("x+")) {
                DM2 = Cube.XUP;
            }
            else if (dm2.equals("x-")) {
                DM2 = Cube.XDOWN;
            }
            else if (dm2.equals("y+")) {
                DM2 = Cube.YUP;
            }
            else if (dm2.equals("y-")) {
                DM2 = Cube.YDOWN;
            }
            else if (dm2.equals("z+")) {
                DM2 = Cube.ZUP;
            }
            else if (dm2.equals("z-")) {
                DM2 = Cube.ZDOWN;
            }
            Cube temp = new Cube(Integer.parseInt(x),
                                 Integer.parseInt(y), 
                                 Integer.parseInt(z), DM1, DM2, clr);
            temp.setBoard(board);
            board.put(temp.getName(), temp);
        }
        // if Sceptre move
        
        if (choice.charAt(0) == 'S') {
            
            int clr = board.get(choice.substring(2, choice.indexOf(')'))).getColor();
            board.remove(choice.substring(2, choice.indexOf(')')));
        
            // remove from current location
            if (dm2.equals("x+")) {
                int DM2 = Cube.XUP;
            }
            else if (dm2.equals("x-")) {
                int DM2 = Cube.XDOWN;
            }
            else if (dm2.equals("y+")) {
                int DM2 = Cube.YUP;
            }
            else if (dm2.equals("y-")) {
                int DM2 = Cube.YDOWN;
            }
            else if (dm2.equals("z+")) {
                int DM2 = Cube.ZUP;
            }
            else if (dm2.equals("z-")) {
                int DM2 = Cube.ZDOWN;
            }
            Cube was = board.get(actualcube);
            was.removeSceptre(was.firstSceptre());
    
            
            // add to new location
            
            // if sceptre currently encroaching and leaving that cube, 
            // make cube disappear
            
        }
        // else, print "Must move a sceptre(S) or a cube(C)"
        // ask them to make a move again
        swapPlayers();

        // never gets another move in this game, so always return false
        return false;
    }

    public ArrayList<Integer> legalMoves(Player p) {
		movesCheck(p);
		ArrayList<Integer> t = new ArrayList<Integer>();
		for (int i = 0; i < moves.size(); i++) {
			t.add(i);
		}
        return t;
    }
    
    public boolean legalMove(Player p, int m) {
		movesCheck(p);
        return m >= 0 && m < moves.size();
    }
    
    public String showMoves(Player p) {
		movesCheck(p);
    	String s = "";
    	int i = 0;
    	for (String tm : moves) {
    		s += i + ": " + tm + "\n";
    		i++;
    	}
    	return s;
    }

	public void movesCheck(Player p) {
    	if (moves == null) {
    		generateMoves(p);
    	}	
	}

    // Returns a copy of the current board, for searching
    public BoardGame clone() {
        HashMap<String, Cube> b = new HashMap<String, Cube>();
        for (String s : board.keySet()) {
            Cube c = board.get(s).clone();
            c.setBoard(b);
            b.put(s, c);
        }
        Axiom cl = new Axiom(b);
        cl.firstPlayer(players[0]);
        cl.secondPlayer(players[1]);
        cl.setTurn(turn);
        return cl;
    }

    public String toString() {
        String s = "Current Axiom Cubes\n";
        for (String k : board.keySet()) {
            Cube c = board.get(k);
            s += c + "\n";
            if (c.isOccupied()) {
                s += c.toSceptreString() + "\n";
            }
        }
        return s;
    }

    public static void main (String args[]) {
        Axiom g = new Axiom();
        Player p1 = new Player(Cube.BLACK, Cube.WHITE, Player.HUMAN, 2);
        Player p2 = new Player(Cube.WHITE, Cube.BLACK, Player.HUMAN, 0);
        g.firstPlayer(p1);
        g.secondPlayer(p2);
        Host.hostGame(g, p1, p2);
    }
}

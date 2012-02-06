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
        Cube b6 = new Cube(1,0,1, Cube.ZUP, Cube.XDOWN, Cube.BLACK);
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
        Cube w6 = new Cube(-1,1,1, Cube.ZUP, Cube.XUP, Cube.WHITE);
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
    
    public int getTurn(){
    	return this.turn;		
    }
    
    public void setMoves(ArrayList<String> moves) {
    	this.moves = moves;
    }
    
    public String showMove(int m) {
    	return moves.get(m);
    }
    
    // interface methods 
    // Detect if the color has won in the current board
    public boolean hasWon(int color) {
    	// if it is not your turn and there are different color sceptres on one cube
    	// or if there are two of the same on the same cube and it is your turn
    	for (String k : board.keySet()) {
    		Cube c = board.get(k);
    		if (c.twoSceptreDifferentColor() && color != players[turn].num) {
    			return true;
    		}
    		if (c.twoSceptreSameColor() && color != c.getFace(c.firstSceptre())) {
    			return true;
    		}
    	}
		// or, it is not your turn and the current player has no moves
		if (color != players[turn].num) {
			movesCheck(players[turn]);
			if (moves.isEmpty()) {
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
    	if (moves.isEmpty()) {
    		return true;
    	}
    
        return false;
    }

	// return the number of free cubes of the asking player
	public int freeCubes(int p) {
		int count = 0;
		for (String k : new HashSet<String>(board.keySet())) {
			Cube c = board.get(k);
			if (c.isFree() && c.color == p) {
				count++;
			}
		}
		return count;
	}

	// return the number of cubes belonging to the asking player
	public int numCubes(int p) {
		int count = 0;
		for (String k : new HashSet<String>(board.keySet())) {
			Cube c = board.get(k);
			if (c.color == p) {
				count++;
			}
		}
		return count;
	}

	// Generate a list of strings representing the possible moves
	// for the Player p. num of Player is color.
	public void generateMoves(Player p) {

		moves = new ArrayList<String>();
		// piece together string
		// evaluate each cube
		HashSet<String> sceptlocs = new HashSet<String>();
		HashSet<String> sceptcaplocs = new HashSet<String>();
		for (Cube c : board.values()) {
			
			// MHG 11/5/2011 add all sceptre locs to set
			if (c.firstSceptre() != Cube.NONE) {
				sceptlocs.add(c.getNeighborString(c.firstSceptre(), 1));
				sceptcaplocs.add(c.getNeighborString(c.firstSceptre(), 2));
			}
		}

		int count = 0;
		for (String k : new HashSet<String>(board.keySet())) {
			//System.out.println(k);
			Cube c = board.get(k);
			
			// CUBE MOVES
			if (c.isFree() && c.color == p.num) {
			
				// find all locations where this cube can be placed
				HashSet<String> spots = new HashSet<String>();
				for (String r : board.keySet()) {
					if (!r.equals(k)) {
					
						// find free faces not under the table.
						Cube c2 = board.get(r);
						for (String n : c2.freeFaces()) {
				
							spots.add(n);
							count++;
						}
					}
				}
				// MHG 11/5/2011 Need to remove faces where sceptres live...
				//System.out.println("Sceptre locs");
				for (String s : sceptlocs) {
					//System.out.println(s);
					spots.remove(s);
				}
				for (String s : sceptcaplocs) {
					spots.remove(s);
				}
				
				board.remove(k);
				//System.out.println("Free Faces");
				for (String s : spots) {
					//System.out.println(s);
					if (c.secondDome() == Cube.NONE) {
						for (int i = 0; i < 6; i++) {
							Cube t = new Cube(s, i, -1, c.color);
							t.setBoard(board);
							board.put(s, t);
							if (t.legal()) {
								moves.add("C(" + c.getName() + ") C(" + s + ")" + 
									Cube.fnames[i] + " ");
								//System.out.println("LEGAL");
							}
							board.remove(s);
						}
					} else {
						for (int i = 0; i < 5; i++) {
							for (int j = ((i + 1) % 2) + i + 1; j < 6; j++) {
								// MHG 11/5/2011 FIXED
								// cannot be opposite faces on dome, must be adjacent
								Cube t = new Cube(s, i, j, c.color);
								t.setBoard(board);
								board.put(s, t);
								if (t.legal()) {
									moves.add("C(" + c.getName() + ") C(" + s + ")" + 
										Cube.fnames[i] + " " + Cube.fnames[j]);
									//System.out.println("LEGAL");
								}
								board.remove(s);
							}
						}
					}
				}
				board.put(k, c);
			}
			
			// SCEPTRE MOVES 
			if (c.isOccupied() && c.getFace(c.firstSceptre()) == p.num) {
			
				// Record sceptre location and remove from list
				String whereami = c.getNeighborString(c.firstSceptre(), 1);
				sceptlocs.remove(whereami);
				
				// find all places where this sceptre can be placed
				int face = c.firstSceptre();
				
				// DIAGONAL in same plane 
				// TODO MHG 12/1/2011 need to add case where offdiag cubes block the path.
				// Only in the ZUP direction, otherwise it would have no support...
				int[] zeros = {0, 0, 0, 0};
				int[] ones = {1, 1, 1, 1};
				int[] negones = {-1, -1, -1, -1};
				int[] alttop = {1, 1, -1, -1};
				int[] altbottom = {1, -1, 1, -1};
				int[] xmod = null;
				int[] ymod = null;
				int[] zmod = null;
				int[] xmod2 = null;
				int[] ymod2 = null;
				int[] zmod2 = null;
				
				if (face == Cube.ZDOWN) {
					System.out.println("WHY IS THE SCEPTRE UPSIDE DOWN in ZDOWN???");
					System.exit(-1);
				}
					
				if (face == Cube.ZUP) {
					
					zmod = zeros;
					zmod2 = ones;
					
					xmod = xmod2 = alttop;
					ymod = ymod2 = altbottom;
					
				} else if (face == Cube.XUP || face == Cube.XDOWN) {
				
					xmod = zeros;
					xmod2 = ones;
					if (face == Cube.XDOWN) {
						xmod2 = negones;
					}
					
					zmod = zmod2 = alttop;
					ymod = ymod2 = altbottom;
					
				} else {
				
					ymod = zeros;
					ymod2 = ones;
					if (face == Cube.YDOWN) {
						ymod2 = negones;
					}
					
					xmod = xmod2 = alttop;
					zmod = zmod2 = altbottom;
					
				}
				
				// for each of the four directions
				for (int i = 0; i < 4; i++) {
					Cube cur = c;
					boolean nei = true;

					// while cubes in direction, keep making moves
					while (nei) {
						nei = false;
						Cube who = cur.getNeighbor(xmod[i], ymod[i], zmod[i]);
						if (who != null && who.getFace(face) == Cube.EMPTY) {
							Cube who2 = cur.getNeighbor(xmod2[i], ymod2[i], zmod2[i]);
							boolean barriers = false;
							if (face == Cube.ZUP && 
								cur.getNeighbor(xmod2[i] - xmod2[i], ymod2[i], zmod2[i]) != null &&
								cur.getNeighbor(xmod2[i], ymod2[i] - ymod2[i], zmod2[i]) != null) {
								barriers = true;
							}
							if (who2 == null && !barriers) {
								if (!sceptlocs.contains(who.getNeighborString(face, 1)) && 
								    !board.containsKey(who.getNeighborString(face, 2))) {
									moves.add(0, "S(" + c.getName() + ") S(" + who.getName() + ")" +
												 Cube.fnames[face] + " ");
								}
								cur = who;
								count++;
								nei = true;
							} 
						}
					}
				}				
				
				// ORTHOGONAL wrapping. There are six ways to move orthogonally
				// since there will never be overhangs, and we cannot go under the board
				// we can handle the X and Y directions differently than the Z direction,
				// where complete loops are possible
				int[][] map = {{Cube.XUP, Cube.XDOWN},
							   {Cube.XDOWN, Cube.XUP},
							   {Cube.YUP, Cube.YDOWN},
							   {Cube.YDOWN, Cube.YUP}};
				for (int i = 0; i < map.length; i++) {
					Cube cur = c;
					int dir = face;
					boolean nei = true; 
					while (nei) {						
						nei = false;
						//System.out.println(cur);
						if (dir == Cube.ZUP) {
							Cube a = null;
							Cube b = null;
							a = cur.getNeighbor(map[i][0]);
							if (a != null) {
								b = a.getNeighbor(Cube.ZUP);
							}
							// check three placements
							if (a == null) {
								if (cur.isEmpty(map[i][0])) { 
									if (!sceptlocs.contains(cur.getNeighborString(map[i][0], 1)) && 
								    	!board.containsKey(cur.getNeighborString(map[i][0], 2))) {
										moves.add(0, "S(" + c.getName() + ") S(" + cur.getName() + ")" + 
													 Cube.fnames[map[i][0]] + " ");
									}
									nei = true;
									dir = map[i][0];
									count++;
								}
							} else if (b == null) {
								if (a.isEmpty(Cube.ZUP)) { 
									if (!sceptlocs.contains(a.getNeighborString(Cube.ZUP, 1)) && 
								    	!board.containsKey(a.getNeighborString(Cube.ZUP, 2))) {
										moves.add(0, "S(" + c.getName() + ") S(" + a.getName() + ")" + 
													 Cube.fnames[Cube.ZUP] + " ");
									}
									nei = true;
									dir = Cube.ZUP;
									cur = a;
									count++;
								}						
							} else {
								if (b.isEmpty(map[i][1])) {
									if (!sceptlocs.contains(b.getNeighborString(map[i][1], 1)) && 
								    	!board.containsKey(b.getNeighborString(map[i][1], 2))) {
										moves.add(0, "S(" + c.getName() + ") S(" + b.getName() + ")" + 
													 Cube.fnames[map[i][1]] + " ");
									}
									nei = true;
									dir = map[i][1];
									cur = b;
									count++;
								}						
							}
						}
						if (dir == map[i][0]) {
							Cube a = null;
							Cube b = null;
							a = cur.getNeighbor(Cube.ZDOWN);
							if (a != null) {
								b = a.getNeighbor(map[i][0]);
							}
							// check three placements
							if (a == null) {
							} else if (b == null) {
								if (a.isEmpty(map[i][0])) {
									if (!sceptlocs.contains(a.getNeighborString(map[i][0], 1)) && 
								    	!board.containsKey(a.getNeighborString(map[i][0], 2))) {
										moves.add(0, "S(" + c.getName() + ") S(" + a.getName() + ")" + 
													 Cube.fnames[map[i][0]] + " ");
									}
									nei = true;
									dir = map[i][0];
									cur = a;
									count++;
								}						
							} else {
								if (b.isEmpty(Cube.ZUP)) { 
									if (!sceptlocs.contains(b.getNeighborString(Cube.ZUP, 1)) && 
								    	!board.containsKey(b.getNeighborString(Cube.ZUP, 2))) {
										moves.add(0, "S(" + c.getName() + ") S(" + b.getName() + ")" + 
													 Cube.fnames[Cube.ZUP] + " ");
									}
									nei = true;
									dir = Cube.ZUP;
									cur = b;
									count++;
								}						
							}
						}
						if (dir == map[i][1]) {
							Cube a = cur.getNeighbor(Cube.ZUP);
							// check two placements
							if (a == null) {
								if (cur.isEmpty(Cube.ZUP)) {
									if (!sceptlocs.contains(cur.getNeighborString(Cube.ZUP, 1)) && 
								    	!board.containsKey(cur.getNeighborString(Cube.ZUP, 2))) {
										moves.add(0, "S(" + c.getName() + ") S(" + cur.getName() + ")" + 
													 Cube.fnames[Cube.ZUP] + " ");
									}
									nei = true;
									dir = Cube.ZUP;
									count++;
								}
							} else {
								if (a.isEmpty(map[i][1])) { 
									if (!sceptlocs.contains(a.getNeighborString(map[i][1], 1)) && 
								    	!board.containsKey(a.getNeighborString(map[i][1], 2))) {
										moves.add(0, "S(" + c.getName() + ") S(" + a.getName() + ")" + 
													 Cube.fnames[map[i][1]] + " ");
									}
									nei = true;
									dir = map[i][1];
									cur = a;
									count++;
								}						
							}
						}
					}
				}
				
				// CLOCKWISE and COUNTERCLOCKWISE
				int[][] cmap = {{Cube.XUP, Cube.YUP, Cube.XDOWN, Cube.YDOWN},
								{Cube.XUP, Cube.YDOWN, Cube.XDOWN, Cube.YUP}};
				int[][] ax = {{0, -1, 0, 1},
							  {0, -1, 0, 1}};
				int[][] bx = {{1, -1, -1, 1}, 
						      {1, -1, -1, 1}};
				int[][] ay = {{1, 0, -1, 0},
							  {-1, 0, 1, 0}};
				int[][] by = {{1, 1, -1, -1},
							  {-1, -1, 1, 1}};
				
				for (int i = 0; i < cmap.length; i++) {
				
					int dir = -1;
					for (int j = 0; j < cmap[i].length; j++) {
						if (cmap[i][j] == face) {
							dir = j;
							break;
						}
					}

					Cube cur = c;
					boolean nei = true;  
					while (dir != -1 && nei) {
						nei = false;
						Cube a = null;
						Cube b = null;
						
						a = cur.getNeighbor(ax[i][dir], ay[i][dir], 0);
						b = cur.getNeighbor(bx[i][dir], by[i][dir], 0);
						
						int prev = (dir + 3) % 4;
						int next = (dir + 1) % 4;

						// check three placements
						if (b != null) {
							if (b.isEmpty(cmap[i][prev])) { // -1
								String m = "S(" + c.getName() + ") S(" + b.getName() + ")" + 
													 Cube.fnames[cmap[i][prev]] + " ";
								if (!sceptlocs.contains(b.getNeighborString(cmap[i][prev], 1)) && 
								    !board.containsKey(b.getNeighborString(cmap[i][prev], 2)) &&
								    !moves.contains(m)) {
									moves.add(0, m);
								}
								nei = true;
								dir = prev;
								cur = b;
								count++;
							}
						} else if (a != null) {
							if (a.isEmpty(cmap[i][dir])) { // 0
								String m = "S(" + c.getName() + ") S(" + a.getName() + ")" + 
													 Cube.fnames[cmap[i][dir]] + " ";
								if (!sceptlocs.contains(a.getNeighborString(cmap[i][dir], 1)) && 
								    !board.containsKey(a.getNeighborString(cmap[i][dir], 2)) &&
								    !moves.contains(m)) {
									moves.add(0, m);
								}
								nei = true;
								cur = a;
								count++;
							}
						} else {
							if (cur.isEmpty(cmap[i][next])) { // 1
								String m = "S(" + c.getName() + ") S(" + cur.getName() + ")" + 
													 Cube.fnames[cmap[i][next]] + " ";
								if (!sceptlocs.contains(cur.getNeighborString(cmap[i][next], 1)) && 
								    !board.containsKey(cur.getNeighborString(cmap[i][next], 2)) &&
								    !moves.contains(m)) {
									moves.add(0, m);
								}
								nei = true;
								dir = next;
								count++;
							}
						}						
					}
				}			
				
				sceptlocs.add(whereami);

			}			
		}
	}

    public boolean makeMove(Player p, int m) {
        // take string apart and make it happen
        // look up move in moves ArrayList
        
        String choice = moves.get(m);        
        //System.out.println("Making move" + choice);
        int a = choice.indexOf(')');
        int b = choice.indexOf('(', a);            
        int e = choice.indexOf(',', b);
        int f = choice.indexOf(',', e + 1);
        int g = choice.indexOf(')', f);
        int d = choice.indexOf(' ', f);
        String x = choice.substring(b + 1, e);
        String y = choice.substring(e + 1, f);
        String z = choice.substring(f + 1, g);
        String dm1 = choice.substring(g + 1, d);
        String dm2 = choice.substring(d + 1, choice.length());
        String actualcube = choice.substring(2, choice.indexOf(')'));
        String s = choice.substring(a + 1, a + 3);    
            
        // if Cube move
        if (choice.charAt(0) == 'C') {
            // remove from board
            int clr = board.get(actualcube).color;
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
            
            int clr = board.get(actualcube).color;
        	int DM1 = -1;
            // remove from current location
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
            
            Cube was = board.get(actualcube);
	        int sclr = was.getFace(was.firstSceptre());
            was.removeSceptre(was.firstSceptre());    
            
            // add to new location
            String whereto = "" + x + "," + y + "," + z;
            
           	Cube c2 = board.get(whereto);
           	//System.out.println(c2);
           	c2.addSceptre(DM1, sclr);
            
            // if sceptre currently encroaching and leaving that cube, 
            // make cube disappear
            // MHG 12/18/2011 Must be returning to the player's color, not just leaving free cube!
            if (sclr != clr && c2.color == sclr && was.isFree()) {
            	board.remove(actualcube);
            }            
        }

        swapPlayers();

		// remove all moves
		moves = null;

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
        cl.setMoves(moves);
        return cl;
    }

    public String toString() {
        String s = "";
        for (String k : board.keySet()) {
            Cube c = board.get(k);
            s += c + "\n";
            if (c.isOccupied()) {
                s += c.toSceptreString() + "\n";
            }
        }
        return s;
    }

	public static void main2(String[] args) {
	    int[] wins = new int[3];
    	for (int i = 0; i < 1000; i++) {
			Axiom g = new Axiom();
			Player p1 = new RandomPlayer(Cube.BLACK, Cube.WHITE);
			Player p2 = new RandomPlayer(Cube.WHITE, Cube.BLACK);
			g.firstPlayer(p1);
			g.secondPlayer(p2);
			int who = Host.hostGame(g, p1, p2, false);
			wins[who]++;
		}
		System.out.println("0: " + wins[0] + " 1: " + wins[1] + " tie: " + wins[2]);
	}

    public static void main(String args[]) {
		Axiom g = new Axiom();
		//Player p1 = new HumanPlayer(Cube.BLACK, Cube.WHITE);
		//Player p1 = new AlphaBetaPlayer(Cube.BLACK, Cube.WHITE, 4);
		Player p1 = new MonteCarloPlayer(Cube.BLACK, Cube.WHITE, 100);
		//Player p2 = new AlphaBetaPlayer(Cube.WHITE, Cube.BLACK, 2);
		Player p2 = new RandomPlayer(Cube.WHITE, Cube.BLACK);
		g.firstPlayer(p1);
		g.secondPlayer(p2);
		Host.hostGame(g, p1, p2, true);
    }
}

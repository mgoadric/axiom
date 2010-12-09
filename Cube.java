import java.util.*;

/*
 *      Cube.java
 *      making a test comment
 */


public class Cube {

    // Constants
    public static int X = 0;
    public static int Y = 1;
    public static int Z = 2;
    public static int BLACK = 0;
    public static int WHITE = 1;
    public static int EMPTY = 2;
    public static int DOME = 3;
    public static int XUP = 0;
    public static int XDOWN = 1;
    public static int YUP = 2;
    public static int YDOWN = 3;
    public static int ZUP = 4;
    public static int ZDOWN = 5;
    public static int NONE = -1;

    // Data members
    private int[] location;
    private int[] faces;
    private int color;
    private HashMap<String, Cube> board;
    
    // Constructor
    public Cube (int x, int y, int z, int d1, int d2, int color) {
        location = new int[3];
        location[X] = x;
        location[Y] = y;
        location[Z] = z;
        faces = new int[6];
        for (int i = 0; i < faces.length; i++) {
            if (i == d1 || i == d2) {
                faces[i] = DOME;
            } else {
                faces[i] = EMPTY;
            }
        }
        this.color = color;
    }
    
    public void setBoard(HashMap<String, Cube> b) {
        this.board = b;
    }

    public int getFace(int f) {
        if (f == NONE) {
            return NONE;
        }
        return faces[f];
    }
    
    public String getName() {
        return "" + getX() + "," + getY() + "," + getZ();
    }
    
    public int getX() {
        return location[X];
    }

    public int getY() {
        return location[Y];
    }

    public int getZ() {
        return location[Z];
    }

    public Cube clone() {
        Cube c = new Cube(location[X], location[Y], location[Z], firstDome(), secondDome(), color);
        if (addSceptre(firstSceptre(), getFace(firstSceptre()))) {
            addSceptre(secondSceptre(), getFace(secondSceptre()));
        }
        return c;
    }

    public int firstDome() {
        for (int i = 0; i < faces.length; i++) {
            if (faces[i] == DOME) {
                return i;
            }
        }
        // should NEVER happen
        System.err.println("UHOH!!!");
        return NONE;
    }

    public int secondDome() {
        for (int i = firstDome() + 1; i < faces.length; i++) {
            if (faces[i] == DOME) {
                return i;
            }
        }
        return NONE;
    }

    public int firstSceptre() {
        for (int i = 0; i < faces.length; i++) {
            if (faces[i] == BLACK || faces[i] == WHITE) {
                return i;
            }
        }
        return NONE;
    }
    
    public int secondSceptre() {
        int f = firstSceptre();
        if (f == NONE) {
            return NONE;
        }
        for (int i = f + 1; i < faces.length; i++) {
            if (faces[i] == BLACK || faces[i] == WHITE) {
                return i;
            }
        }
        return NONE;
    }

    public boolean addSceptre(int f, int color) {
        if (f != NONE && isEmpty(f)) {
            faces[f] = color;
            return true;
        }
        return false;
    }
    
    public int removeSceptre(int f) {
        if (faces[f] == WHITE || faces[f] == BLACK) {
            int temp = faces[f];
            faces[f] = EMPTY;
            // 
            return temp;
        }
        return -1;
    }
    
    public boolean isEncroached() {
        for (int i = 0; i < faces.length; i++) {
            if (faces[i] == WHITE && color == BLACK) {
                return true;           
            }
            if (faces[i] == BLACK && color == WHITE) {
                return true;
            }
        }
        return false;
    }
    
    public boolean isOccupied() {
        for (int i = 0; i < faces.length; i++) {
            if (faces[i] == WHITE || faces[i] == BLACK) {
                return true;
            }
        }
        return false;
    }

    public boolean isFree() {
        if (isOccupied()) {
            return false;
        }
        String above = "" + location[X] + ", " + 
                            location[Y] + ", " + 
                            (location[Z] + 1);
        if (board.get(above) == null) {
            return true;
        }
        return false;
    }

    public boolean twoSceptreSameColor() {
        int first = getFace(firstSceptre());
        int second = getFace(secondSceptre());
        if (first != NONE) {
            return first == second;
        }
        return false;
    }

    public boolean twoSceptreDifferentColor() {
        int first = getFace(firstSceptre());
        int second = getFace(secondSceptre());
        if (first != NONE && second != NONE) {
            return first != second;
        }
        return false;
    }
    
    public boolean isEmpty(int f) {
        return faces[f] == EMPTY;
    }
    
    // JMARTIN2 TODO
    // Rotate the cube in the direction d, being one of X, Y or Z
    public void rotate(int d) {
        if (d == Y) {
            int temp = faces[XUP];
            faces[XUP] = faces[ZUP];
            faces[ZUP] = faces[XDOWN];
            faces[XDOWN] = faces[ZDOWN];
            faces[ZDOWN] = temp;
        }
        else if (d == X) {
            int temp = faces[YUP];
            faces[YUP] = faces[ZUP];
            faces[ZUP] = faces[YDOWN];
            faces[YDOWN] = faces[ZDOWN];
            faces[ZDOWN] = temp;       
        }
        else if (d == Z) {
            int temp = faces[XUP];
            faces[XUP] = faces[YDOWN];
            faces[YDOWN] = faces[XDOWN];
            faces[XDOWN] = faces[YUP];
            faces[YUP] = temp;
            }
    }
    // JMARTIN2 TODO
    // Use the notation from the play by email paper
    public String toString() {
        String c = "C (" + getX() + "," + getY() + "," + getZ() + ")";
        int fd = firstDome();
        if (fd == XUP) {
            c += "x+";
        }
        else if (fd == XDOWN) {
            c += "x-";
        }
        else if (fd == YUP) {
            c += "y+";
        }
        else if (fd == YDOWN) {
            c += "y-";
        }
        else if (fd == ZUP) {
            c += "z+";
        }
        else if (fd == ZDOWN) {
            c += "z-";
        }
        if (secondDome() != NONE) {
            int sd = secondDome();
            if (sd == XUP) {
                c += " x+";
            }
            else if (sd == XDOWN) {
                c += " x-";
            }
            else if (sd == YUP) {
                c += " y+";
            }
            else if (sd == YDOWN) {
                c += " y-";
            }
            else if (sd == ZUP) {
               c += " z+";
            }
            else if (sd == ZDOWN) {
                c += " z-";
            }
        }
        if (color == WHITE) {
            c += " (white) ";
        } else {
            c += " (black) ";
        }

        return c;
    }
    
    // JMARTIN2 TODO
    // Use the notation from the play by email paper
    public String toSceptreString() {
        String t = "";
        String s = "S (" + getX() + "," + getY() + "," + getZ() + ")";
        if (firstSceptre() != NONE) {
            t += s;
            int fs = firstSceptre();
            if (fs == XUP) {
                t += "x+";
            }
            else if (fs == XDOWN) {
                t += "x-";
            }
            else if (fs == YUP) {
                t += "y+";
            }
            else if (fs == YDOWN) {
                t += "y-";
            }
            else if (fs == ZUP) {
                t += "z+";
            }
            else if (fs == ZDOWN) {
                t += "z-";
            }
            if (getFace(firstSceptre()) == WHITE) {
                t += " (white) ";
            } else {
                t += " (black) ";
            }
            if (secondSceptre() != NONE) {
                t += "\n" + s;
                int ss = secondSceptre();
                if (ss == XUP) {
                    t += " x+";
                }
                else if (ss == XDOWN) {
                    t += " x-";
                }
                else if (ss == YUP) {
                    t += " y+";
                }
                else if (ss == YDOWN) {
                    t += " y-";
                }
                else if (ss == ZUP) {
                    t += " z+";
                }
                else if (ss == ZDOWN) {
                    t += " z-";
                }
                if (getFace(secondSceptre()) == WHITE) {
                    t += " (white) ";
                } else {
                    t += " (black) ";
                }
            }
         }
        return t;
    }
    
    public static void main (String args[]) {
        HashMap<String, Cube> b = new HashMap<String, Cube>();
        Cube stuff = new Cube(-1, 0, 2, XUP, NONE, BLACK);
        stuff.addSceptre(ZUP, BLACK);
        stuff.setBoard(b);
        b.put(stuff.getName(), stuff);
        System.out.println(stuff);
        System.out.println(stuff.toSceptreString());
        System.out.println("rotating");
        stuff.rotate(X);
        System.out.println(stuff);
        System.out.println(stuff.toSceptreString());
     }
}

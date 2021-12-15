using System;
using System.Collections.Generic;
using System.Linq;

namespace Axiom
{
    public class Cube
    {
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
        public static string[] fnames = { "x+", "x-", "y+", "y-", "z+", "z-" };
        public static int[] opposite = { XDOWN, XUP, YDOWN, YUP, ZDOWN, ZUP };

        // Data members
        private int[] location;
        private int[] faces;
        public int color;
        private Dictionary<string, Cube> board;

        // Constructor
        public Cube(int x, int y, int z, int d1, int d2, int color)
        {
            location = new int[3];
            location[X] = x;
            location[Y] = y;
            location[Z] = z;
            faces = new int[6];
            for (int i = 0; i < faces.Length; i++)
            {
                if (i == d1 || i == d2)
                {
                    faces[i] = DOME;
                }
                else
                {
                    faces[i] = EMPTY;
                }
            }
            this.color = color;
        }

        public Cube(string loc, int d1, int d2, int color)
        {
            location = loc.Split(",").Select(s => Int32.Parse(s)).ToArray();

            faces = new int[6];
            for (int i = 0; i < faces.Length; i++)
            {
                if (i == d1 || i == d2)
                {
                    faces[i] = DOME;
                }
                else
                {
                    faces[i] = EMPTY;
                }
            }
            this.color = color;
        }

        public void setBoard(Dictionary<string, Cube> b)
        {
            this.board = b;
        }

        public int getFace(int f)
        {
            if (f == NONE)
            {
                return NONE;
            }
            return faces[f];
        }

        public string getName()
        {
            return "" + getX() + "," + getY() + "," + getZ();
        }

        public int getX()
        {
            return location[X];
        }

        public int getY()
        {
            return location[Y];
        }

        public int getZ()
        {
            return location[Z];
        }

        public Cube clone()
        {
            Cube c = new Cube(location[X], location[Y], location[Z], firstDome(), secondDome(), color);
            if (c.addSceptre(firstSceptre(), getFace(firstSceptre())))
            {
                c.addSceptre(secondSceptre(), getFace(secondSceptre()));
            }
            return c;
        }

        public int firstDome()
        {
            for (int i = 0; i < faces.Length; i++)
            {
                if (faces[i] == DOME)
                {
                    return i;
                }
            }
            // should NEVER happen
            Console.Error.WriteLine("UHOH!!! MISSING A DOME!!!");
            Console.Error.WriteLine(getName());
            Environment.Exit(-1);
            return NONE;
        }

        public int secondDome()
        {
            for (int i = firstDome() + 1; i < faces.Length; i++)
            {
                if (faces[i] == DOME)
                {
                    return i;
                }
            }
            return NONE;
        }

        public int firstSceptre()
        {
            for (int i = 0; i < faces.Length; i++)
            {
                if (faces[i] == BLACK || faces[i] == WHITE)
                {
                    return i;
                }
            }
            return NONE;
        }

        public int secondSceptre()
        {
            int f = firstSceptre();
            if (f == NONE)
            {
                return NONE;
            }
            for (int i = f + 1; i < faces.Length; i++)
            {
                if (faces[i] == BLACK || faces[i] == WHITE)
                {
                    return i;
                }
            }
            return NONE;
        }

        public bool addSceptre(int f, int color)
        {
            if (f != NONE && isEmpty(f))
            {
                faces[f] = color;
                return true;
            }
            return false;
        }

        public int removeSceptre(int f)
        {
            if (faces[f] == WHITE || faces[f] == BLACK)
            {
                int temp = faces[f];
                faces[f] = EMPTY;
                // 
                return temp;
            }
            return -1;
        }

        public bool isEncroached()
        {
            for (int i = 0; i < faces.Length; i++)
            {
                if (faces[i] == WHITE && color == BLACK)
                {
                    return true;
                }
                if (faces[i] == BLACK && color == WHITE)
                {
                    return true;
                }
            }
            return false;
        }

        public bool isOccupied()
        {
            for (int i = 0; i < faces.Length; i++)
            {
                if (faces[i] == WHITE || faces[i] == BLACK)
                {
                    return true;
                }
            }
            return false;
        }

        public bool isFree()
        {
            if (isOccupied())
            {
                return false;
            }
            Cube above = getNeighbor(ZUP);
            if (above == null)
            {
                return true;
            }
            return false;
        }

        public Cube getNeighbor(int f)
        {
            return board[getNeighborstring(f, 1)];
        }

        public string getNeighborstring(int f, int d)
        {
            if (f == ZUP)
            {
                return "" + getX() + "," + getY() + "," + (getZ() + d);
            }
            else if (f == ZDOWN)
            {
                return "" + getX() + "," + getY() + "," + (getZ() - d);
            }
            else if (f == YUP)
            {
                return "" + getX() + "," + (getY() + d) + "," + getZ();
            }
            else if (f == YDOWN)
            {
                return "" + getX() + "," + (getY() - d) + "," + getZ();
            }
            else if (f == XUP)
            {
                return "" + (getX() + d) + "," + getY() + "," + getZ();
            }
            else if (f == XDOWN)
            {
                return "" + (getX() - d) + "," + getY() + "," + getZ();
            }
            return null;
        }

        public Cube getNeighbor(int dx, int dy, int dz)
        {
            return board["" + (getX() + dx) + "," + (getY() + dy) + "," + (getZ() + dz)];
        }

        public List<string> freeFaces()
        {
            List<string> free = new List<string>();
            for (int i = 0; i < faces.Length; i++)
            {
                if (faces[i] == EMPTY || faces[i] == DOME)
                {
                    string neighbor = null;
                    Cube support = null;
                    if (i == XUP)
                    {
                        support = getNeighbor(1, 0, -1);
                        if (location[Z] == 1 || support != null)
                        {
                            neighbor = getNeighborstring(i, 1);
                        }
                    }
                    else if (i == YUP)
                    {
                        support = getNeighbor(0, 1, -1);
                        if (location[Z] == 1 || support != null)
                        {
                            neighbor = getNeighborstring(i, 1);
                        }
                    }
                    else if (i == ZUP)
                    {
                        neighbor = getNeighborstring(i, 1);
                    }
                    else if (i == XDOWN)
                    {
                        support = getNeighbor(-1, 0, -1);
                        if (location[Z] == 1 || support != null)
                        {
                            neighbor = getNeighborstring(i, 1);
                        }
                    }
                    else if (i == YDOWN)
                    {
                        support = getNeighbor(0, -1, -1);
                        if (location[Z] == 1 || support != null)
                        {
                            neighbor = getNeighborstring(i, 1);
                        }
                    }
                    if (neighbor != null && board[neighbor] == null)
                    {
                        free.Add(neighbor);
                    }
                }
            }
            return free;
        }

        public bool legal()
        {

            bool locked = false;

            // if you are not at Z level 1, you need support and locking in the z direction.
            if (location[Z] > 1)
            {
                string support = getNeighborstring(ZDOWN, 1);
                if (!board.ContainsKey(support))
                {
                    return false;
                }
                else
                {
                    if ((faces[ZDOWN] == DOME && board[support].getFace(ZUP) == EMPTY) ||
                        (faces[ZDOWN] == EMPTY && board[support].getFace(ZUP) == DOME))
                    {
                        locked = true;
                    }
                    else
                    {
                        return false;
                    }
                }
            }

            // Cannot have domes against the table.
            if (location[Z] == 1 && (firstDome() == ZDOWN || secondDome() == ZDOWN))
            {
                return false;
            }

            // do you have a locked dome and no conflicts DOME - DOME? *Denotes legal positioning*
            for (int i = 0; i < faces.Length; i++)
            {
                string neighbor = getNeighborstring(i, 1);
                if (board.ContainsKey(neighbor))
                {
                    if ((faces[i] == DOME && board[neighbor].getFace(opposite[i]) == EMPTY) ||
                        (faces[i] == EMPTY && board[neighbor].getFace(opposite[i]) == DOME))
                    {
                        locked = true;
                    }
                    if (faces[i] == DOME && board[neighbor].getFace(opposite[i]) == DOME)
                    {
                        return false;
                    }
                }
            }
            return locked;
        }

        public bool twoSceptreSameColor()
        {
            int first = getFace(firstSceptre());
            int second = getFace(secondSceptre());
            if (first != NONE)
            {
                return first == second;
            }
            return false;
        }

        public bool twoSceptreDifferentColor()
        {
            int first = getFace(firstSceptre());
            int second = getFace(secondSceptre());
            if (first != NONE && second != NONE)
            {
                return first != second;
            }
            return false;
        }

        public bool isEmpty(int f)
        {
            return faces[f] == EMPTY;
        }

        // Rotate the cube in the direction d, being one of X, Y or Z
        // MHG 11/5/2011 Ended up not being used..
        public void rotate(int d)
        {
            if (d == Y)
            {
                int temp = faces[XUP];
                faces[XUP] = faces[ZUP];
                faces[ZUP] = faces[XDOWN];
                faces[XDOWN] = faces[ZDOWN];
                faces[ZDOWN] = temp;
            }
            else if (d == X)
            {
                int temp = faces[YUP];
                faces[YUP] = faces[ZUP];
                faces[ZUP] = faces[YDOWN];
                faces[YDOWN] = faces[ZDOWN];
                faces[ZDOWN] = temp;
            }
            else if (d == Z)
            {
                int temp = faces[XUP];
                faces[XUP] = faces[YDOWN];
                faces[YDOWN] = faces[XDOWN];
                faces[XDOWN] = faces[YUP];
                faces[YUP] = temp;
            }
        }

        // Use the notation from the play by email paper
        override
        public string ToString()
        {
            string c = "C (" + getName() + ")";
            c += fnames[firstDome()];
            if (secondDome() != NONE)
            {
                c += " " + fnames[secondDome()];
            }
            if (color == WHITE)
            {
                c = " (white) " + c;
            }
            else
            {
                c = " (black) " + c;
            }

            return c;
        }

        // Use the notation from the play by email paper
        public string ToSceptreString()
        {
            string t = "";
            string y = "";
            string s = "S (" + getName() + ")";
            if (firstSceptre() != NONE)
            {
                t += s + fnames[firstSceptre()];
                if (getFace(firstSceptre()) == WHITE)
                {
                    t = " (white) " + t;
                }
                else
                {
                    t = " (black) " + t;
                }
                y += t;
                if (secondSceptre() != NONE)
                {
                    t = s + fnames[secondSceptre()];
                    if (getFace(secondSceptre()) == WHITE)
                    {
                        t = " (white) " + t;
                    }
                    else
                    {
                        t = " (black) " + t;
                    }
                    y += "\n" + t;
                }
            }
            return y;
        }
    }
}



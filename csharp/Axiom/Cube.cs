using System;
using System.Collections.Generic;
using System.Linq;
using ExtensionMethods;

namespace Axiom
{
    public class Cube
    {
        // Constants
        public const int X = 0;
        public const int Y = 1;
        public const int Z = 2;

        // Data members
        private int[] location;
        private Dictionary<Direction, Face> faces;
        private Color color;
        private Dictionary<string, Cube> board;
        private Direction firstDome;
        private Direction secondDome;
        private Direction firstSceptre;
        private Direction secondSceptre;

        // Constructor
        public Cube(Direction d1, Direction d2, Color color)
        {
            faces = new Dictionary<Direction, Face>();
            foreach (Direction d in MyExtensions.validDirections)
            {
                if (d == d1 || d == d2)
                {
                    faces[d] = Face.DOME;
                }
                else
                {
                    faces[d] = Face.EMPTY;
                }
            }

            this.color = color;

            firstDome = d1;
            secondDome = d2;
            firstSceptre = Direction.NONE;
            secondSceptre = Direction.NONE;
        }

        public Cube(int x, int y, int z, Direction d1, Direction d2, Color color)
            : this(d1, d2, color)
        {
            location = new int[3] {x, y, z};
        }

        public Cube(string loc, Direction d1, Direction d2, Color color)
            : this(d1, d2, color)
        {
            string[] pieces = loc.Split(",");
            location = new int[3] {Int32.Parse(pieces[0]),
                Int32.Parse(pieces[1]),
                Int32.Parse(pieces[2]),
                };
        }

        public void SetBoard(Dictionary<string, Cube> b)
        {
            this.board = b;
        }

        public Face GetFace(Direction d)
        {
            if (d == Direction.NONE)
            {
                return Face.NONE;
            }
            return faces[d];
        }

        public string GetName()
        {
            return "" + location[X] + "," + location[Y] + "," + location[Z];
        }

        public Color GetColor()
        {
            return color;
        }

        public Cube Clone()
        {
            Cube c = new Cube(location[X], location[Y], location[Z], FirstDome(), SecondDome(), color);
            if (c.AddSceptre(FirstSceptre(), GetFace(FirstSceptre())))
            {
                c.AddSceptre(SecondSceptre(), GetFace(SecondSceptre()));
            }
            return c;
        }

        public Direction FirstDome()
        {
            return firstDome;
        }

        public Direction SecondDome()
        {
            return secondDome;
        }

        public Direction FirstSceptre()
        {
            return firstSceptre;
        }

        public Direction SecondSceptre()
        {
            return secondSceptre;
        }

        public bool AddSceptre(Direction d, Face color)
        {
            if (d != Direction.NONE && IsEmpty(d))
            {
                faces[d] = color;
                if (FirstSceptre() == Direction.NONE)
                {
                    firstSceptre = d;
                } else
                {
                    secondSceptre = d;
                }
                return true;
            }
            return false;
        }

        public Face RemoveSceptre(Direction d)
        {
            if (faces[d] == Face.WHITE || faces[d] == Face.BLACK)
            {
                Face temp = faces[d];
                faces[d] = Face.EMPTY;
                if (SecondSceptre() != Direction.NONE)
                {
                    secondSceptre = Direction.NONE;
                } else
                {
                    firstSceptre = Direction.NONE;
                }
                return temp;
            }
            // TODO throw exception??
            return Face.NONE;
        }

        public bool IsEncroached()
        {
            if (FirstSceptre() != Direction.NONE)
            {
                if (faces[FirstSceptre()] == Face.WHITE && color == Color.BLACK)
                {
                    return true;
                }
                if (SecondSceptre() != Direction.NONE &&
                    faces[SecondSceptre()] == Face.WHITE && color == Color.BLACK)
                {
                    return true;
                }
            }
            
            return false;
        }

        public bool IsOccupied()
        {
            return FirstSceptre() != Direction.NONE;
        }

        public bool IsFree()
        {
            if (IsOccupied())
            {
                return false;
            }

            return !HasNeighbor(Direction.ZUP);
        }

        public bool HasNeighbor(Direction d)
        {
            return board.ContainsKey(GetNeighborString(d, 1));
        }

        public bool HasNeighbor(int dx, int dy, int dz)
        {
            return board.ContainsKey("" + (location[X] + dx) + "," + (location[Y] + dy) + "," + (location[Z] + dz));
        }

        public Cube GetNeighbor(Direction d)
        {
            return board[GetNeighborString(d, 1)];
        }

        public string GetNeighborString(Direction d, int delta)
        {
            switch (d)
            {
                case Direction.ZUP:
                    return "" + location[X] + "," + location[Y] + "," + (location[Z] + delta);
                case Direction.ZDOWN:
                    return "" + location[X] + "," + location[Y] + "," + (location[Z] - delta);
                case Direction.YUP:
                    return "" + location[X] + "," + (location[Y] + delta) + "," + location[Z];
                case Direction.YDOWN:
                    return "" + location[X] + "," + (location[Y] - delta) + "," + location[Z];
                case Direction.XUP:
                    return "" + (location[X] + delta) + "," + location[Y] + "," + location[Z];
                case Direction.XDOWN:
                    return "" + (location[X] - delta) + "," + location[Y] + "," + location[Z];
                default:
                    return null;
            }
        }

        public Cube GetNeighbor(int dx, int dy, int dz)
        {
            return board["" + (location[X] + dx) + "," + (location[Y] + dy) + "," + (location[Z] + dz)];
        }

        public List<Direction> FreeFaces()
        {
            List<Direction> free = new List<Direction>();
            foreach (Direction d in MyExtensions.validDirections)
            {
                if (faces[d] == Face.EMPTY || faces[d] == Face.DOME)
                {
                    bool possible = false;
                    if (d == Direction.XUP)
                    {
                        if (location[Z] == 1 || HasNeighbor(1, 0, -1))
                        {
                            possible = true;
                        }
                    }
                    else if (d == Direction.YUP)
                    {
                        if (location[Z] == 1 || HasNeighbor(0, 1, -1))
                        {
                            possible = true;
                        }
                    }
                    else if (d == Direction.ZUP)
                    {
                        possible = true;
                    }
                    else if (d == Direction.XDOWN)
                    {
                        if (location[Z] == 1 || HasNeighbor(-1, 0, -1))
                        {
                            possible = true;
                        }
                    }
                    else if (d == Direction.YDOWN)
                    {
                        if (location[Z] == 1 || HasNeighbor(0, -1, -1))
                        {
                            possible = true;
                        }
                    }

                    if (possible && !HasNeighbor(d))
                    {
                        free.Add(d);
                    }
                }
            }
            return free;
        }

        public bool IsLegal()
        {

            bool locked = false;

            // if you are not at Z level 1, you need support and locking in the z direction.
            if (location[Z] > 1)
            {
                if (!HasNeighbor(Direction.ZDOWN))
                {
                    return false;
                }
                else
                {
                    Cube support = GetNeighbor(Direction.ZDOWN);
                    if ((faces[Direction.ZDOWN] == Face.DOME && support.GetFace(Direction.ZUP) == Face.EMPTY) ||
                        (faces[Direction.ZDOWN] == Face.EMPTY && support.GetFace(Direction.ZUP) == Face.DOME))
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
            if (location[Z] == 1 && (FirstDome() == Direction.ZDOWN || SecondDome() == Direction.ZDOWN))
            {
                return false;
            }

            // do you have a locked dome and no conflicts DOME - DOME? *Denotes legal positioning*
            foreach (Direction d in MyExtensions.validDirections)
            {
                if (HasNeighbor(d))
                {
                    Cube neighbor = GetNeighbor(d);
                    if ((faces[d] == Face.DOME && neighbor.GetFace(d.Opposite()) == Face.EMPTY) ||
                        (faces[d] == Face.EMPTY && neighbor.GetFace(d.Opposite()) == Face.DOME))
                    {
                        locked = true;
                    }
                    if (faces[d] == Face.DOME && neighbor.GetFace(d.Opposite()) == Face.DOME)
                    {
                        return false;
                    }
                }
            }
            return locked;
        }

        public bool HasTwoSceptreSameColor()
        {
            Face first = GetFace(FirstSceptre());
            if (first != Face.NONE)
            {
                return first == GetFace(SecondSceptre());
            }
            return false;
        }

        public bool HasTwoSceptreDifferentColor()
        {
            Face first = GetFace(FirstSceptre());
            if (first != Face.NONE)
            {
                Face second = GetFace(SecondSceptre());
                if (second != Face.NONE)
                {
                    return first != second;
                }
            }
            return false;
        }

        public bool IsEmpty(Direction d)
        {
            return faces[d] == Face.EMPTY;
        }

        // Use the notation from the play by email paper
        override
        public string ToString()
        {
            string c = "C (" + GetName() + ") ";
            c += FirstDome().Name();
            if (SecondDome() != Direction.NONE)
            {
                c += " " + SecondDome().Name();
            }

            c = " (" + color.ToString().ToLower() + ") " + c;

            return c;
        }

        // Use the notation from the play by email paper
        public string ToSceptreString()
        {
            string y = "";
            string s = "S (" + GetName() + ") ";
            if (FirstSceptre() != Direction.NONE)
            {
                y += " (" + GetFace(FirstSceptre()).ToString().ToLower() + ") " + s + FirstSceptre().Name();
                if (SecondSceptre() != Direction.NONE)
                {
                    y += "\n" + " (" + GetFace(SecondSceptre()).ToString().ToLower() + ") " + s + SecondSceptre().Name();
                }
            }
            return y;
        }
    }
}
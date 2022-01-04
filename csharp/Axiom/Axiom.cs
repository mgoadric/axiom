using System;
using System.Collections.Generic;
using ExtensionMethods;

namespace Axiom
{
    public class Axiom : BoardGame
    {

        // data members
        private Dictionary<string, Cube> board;
        private List<string> moves;
        private Dictionary<Color, Player> players;
        private Color turn = Color.BLACK;

        // constructor
        public Axiom()
        {
            players = new Dictionary<Color, Player>();
            board = new Dictionary<string, Cube>();

            Cube b1 = new Cube(-1, 0, 2, Direction.XUP, Direction.NONE, Color.BLACK);
            Cube b2 = new Cube(0, 0, 2, Direction.ZDOWN, Direction.NONE, Color.BLACK);
            Cube b3 = new Cube(1, 0, 2, Direction.XUP, Direction.NONE, Color.BLACK);
            Cube b4 = new Cube(-1, 0, 1, Direction.ZUP, Direction.XDOWN, Color.BLACK);
            Cube b5 = new Cube(0, 0, 1, Direction.YDOWN, Direction.XDOWN, Color.BLACK);
            Cube b6 = new Cube(1, 0, 1, Direction.ZUP, Direction.XDOWN, Color.BLACK);

            b1.AddSceptre(Direction.ZUP, Face.BLACK);
            b3.AddSceptre(Direction.ZUP, Face.BLACK);

            b1.SetBoard(board); // link board to cube
            b2.SetBoard(board);
            b3.SetBoard(board);
            b4.SetBoard(board);
            b5.SetBoard(board);
            b6.SetBoard(board);

            board[b1.GetName()] = b1; // link cube to board
            board[b2.GetName()] = b2;
            board[b3.GetName()] = b3;
            board[b4.GetName()] = b4;
            board[b5.GetName()] = b5;
            board[b6.GetName()] = b6;

            Cube w1 = new Cube(1, 1, 2, Direction.XDOWN, Direction.NONE, Color.WHITE);
            Cube w2 = new Cube(0, 1, 2, Direction.ZDOWN, Direction.NONE, Color.WHITE);
            Cube w3 = new Cube(-1, 1, 2, Direction.XDOWN, Direction.NONE, Color.WHITE);
            Cube w4 = new Cube(1, 1, 1, Direction.ZUP, Direction.XUP, Color.WHITE);
            Cube w5 = new Cube(0, 1, 1, Direction.YUP, Direction.XUP, Color.WHITE);
            Cube w6 = new Cube(-1, 1, 1, Direction.ZUP, Direction.XUP, Color.WHITE);

            w1.AddSceptre(Direction.ZUP, Face.WHITE);
            w3.AddSceptre(Direction.ZUP, Face.WHITE);

            w1.SetBoard(board); // link board to cube
            w2.SetBoard(board);
            w3.SetBoard(board);
            w4.SetBoard(board);
            w5.SetBoard(board);
            w6.SetBoard(board);

            board[w1.GetName()] = w1; // link cube to board
            board[w2.GetName()] = w2;
            board[w3.GetName()] = w3;
            board[w4.GetName()] = w4;
            board[w5.GetName()] = w5;
            board[w6.GetName()] = w6;
        }

        public Axiom(Dictionary<string, Cube> board)
        {
            this.board = board;
            players = new Dictionary<Color, Player>();
        }

        public void FirstPlayer(Player p1)
        {
            players[p1.Color] = p1;
        }

        public void SecondPlayer(Player p2)
        {
            players[p2.Color] = p2;
        }

        public void SwapPlayers()
        {
            turn = players[turn].Opp;
        }

        public void SetTurn(Color turn)
        {
            this.turn = turn;
        }

        public Color GetTurn()
        {
            return this.turn;
        }

        public void SetMoves(List<string> moves)
        {
            this.moves = moves;
        }

        public String ShowMove(int m)
        {
            return moves[m];
        }

        // interface methods 
        // Detect if the color has won in the current board
        public bool HasWon(Color color)
        {
            // if it is not your turn and there are different color sceptres on one cube
            // or if there are two of the same on the same cube and it is your turn
            foreach (String k in board.Keys)
            {
                Cube c = board[k];
                if (c.HasTwoSceptreDifferentColor() && color != turn)
                {
                    return true;
                }
                if (c.HasTwoSceptreSameColor() &&
                    (color == Color.BLACK && Face.BLACK != c.GetFace(c.FirstSceptre()) ||
                     color == Color.WHITE && Face.WHITE != c.GetFace(c.FirstSceptre())))

                {
                    return true;
                }
            }
            // or, it is not your turn and the current player has no moves
            if (color != turn)
            {
                MovesCheck(players[turn]);
                if (moves.Count == 0)
                {
                    return true;
                }
            }
            return false;
        }

        // Detect if the game is over in the current board
        public bool GameOver()
        {

            // when two sceptres on one cube
            foreach (Cube c in board.Values)
            {
                if (c.HasTwoSceptreDifferentColor() || c.HasTwoSceptreSameColor())
                {
                    return true;
                }
            }

            // or the current player has no moves.
            MovesCheck(players[turn]);
            if (moves.Count == 0)
            {
                return true;
            }

            return false;
        }

        public int Heuristic(Color color)
        {
            return FreeCubes(color) + NumCubes(color) -
                (FreeCubes(players[color].Opp) + NumCubes(players[color].Opp));
        }

        // return the number of free cubes of the asking player
        private int FreeCubes(Color color)
        {
            int count = 0;
            foreach (Cube c in board.Values)
            {
                if (c.IsFree() && c.GetColor() == color)
                {
                    count++;
                }
            }
            return count;
        }

        // return the number of cubes belonging to the asking player
        private int NumCubes(Color color)
        {
            int count = 0;
            foreach (Cube c in board.Values)
            {
                if (c.GetColor() == color)
                {
                    count++;
                }
            }
            return count;
        }

        // Generate a list of strings representing the possible moves
        // for the Player p. num of Player is color.
        public void GenerateMoves(Player p)
        {

            moves = new List<string>();
            // piece together string
            // evaluate each cube
            HashSet<string> sceptlocs = new HashSet<string>();
            HashSet<string> sceptcaplocs = new HashSet<string>();
            HashSet<string> sceptdirs = new HashSet<string>();
            foreach (Cube c in board.Values)
            {

                // MHG 11/5/2011 add all sceptre locs to set
                if (c.FirstSceptre() != Direction.NONE)
                {
                    sceptlocs.Add(c.GetNeighborString(c.FirstSceptre(), 1));
                    sceptcaplocs.Add(c.GetNeighborString(c.FirstSceptre(), 2));
                    sceptdirs.Add(c.GetNeighborString(c.FirstSceptre(), 1) + "|" + c.FirstSceptre());
                }
            }

            int count = 0;
            foreach (string k in new HashSet<String>(board.Keys))
            {
                //Console.WriteLine(k);
                Cube c = board[k];

                // CUBE MOVES
                if (c.IsFree() && c.GetColor() == p.Color)
                {

                    // find all locations where this cube can be placed
                    HashSet<string> spots = new HashSet<string>();
                    foreach (string r in board.Keys)
                    {
                        if (r != k)
                        {

                            // find free faces not under the table.
                            Cube c2 = board[r];
                            foreach (Direction dir in c2.FreeFaces())
                            {

                                spots.Add(c2.GetNeighborString(dir, 1));
                                count++;
                            }
                        }
                    }
                    // MHG 11/5/2011 Need to remove faces where sceptres live...
                    //Console.WriteLine("Sceptre locs");
                    foreach (string s in sceptlocs)
                    {
                        //Console.WriteLine(s);
                        spots.Remove(s);
                    }
                    foreach (string s in sceptcaplocs)
                    {
                        spots.Remove(s);
                    }

                    board.Remove(k);
                    //Console.WriteLine("Free Faces");
                    foreach (string s in spots)
                    {
                        //Console.WriteLine(s);
                        if (c.SecondDome() == Direction.NONE)
                        {
                            foreach (Direction d in MyExtensions.validDirections)
                            {
                                Cube t = new Cube(s, d, Direction.NONE, c.GetColor());
                                t.SetBoard(board);
                                board[s] = t;
                                if (t.IsLegal())
                                {
                                    moves.Add("C(" + c.GetName() + ") C(" + s + ")" +
                                        d.Name() + " ");
                                    //Console.WriteLine("LEGAL");
                                }
                                board.Remove(s);
                            }
                        }
                        else
                        {
                            for (int i = 0; i < 5; i++)
                            {
                                for (int j = ((i + 1) % 2) + i + 1; j < 6; j++)
                                {
                                    // MHG 11/5/2011 FIXED
                                    // cannot be opposite faces on dome, must be adjacent
                                    Cube t = new Cube(s, MyExtensions.validDirections[i],
                                        MyExtensions.validDirections[j], c.GetColor());
                                    t.SetBoard(board);
                                    board[s] = t;
                                    if (t.IsLegal())
                                    {
                                        moves.Add("C(" + c.GetName() + ") C(" + s + ")" +
                                            MyExtensions.validDirections[i].Name() + " " +
                                            MyExtensions.validDirections[j].Name());
                                        //Console.WriteLine("LEGAL");
                                    }
                                    board.Remove(s);
                                }
                            }
                        }
                    }
                    board[k] = c;
                }

                // SCEPTRE MOVES 
                if (c.IsOccupied() && ((c.GetFace(c.FirstSceptre()) == Face.BLACK && p.Color == Color.BLACK) ||
                    (c.GetFace(c.FirstSceptre()) == Face.WHITE && p.Color == Color.WHITE)))
                {

                    // Record sceptre location and remove from list
                    String whereami = c.GetNeighborString(c.FirstSceptre(), 1);
                    sceptlocs.Remove(whereami);

                    // find all places where this sceptre can be placed
                    Direction face = c.FirstSceptre();

                    // DIAGONAL in same plane 
                    // TODO MHG 12/1/2011 need to add case where offdiag cubes block the path.
                    // Only in the ZUP direction, otherwise it would have no support...
                    int[] zeros = { 0, 0, 0, 0 };
                    int[] ones = { 1, 1, 1, 1 };
                    int[] negones = { -1, -1, -1, -1 };
                    int[] alttop = { 1, 1, -1, -1 };
                    int[] altbottom = { 1, -1, 1, -1 };
                    int[] xmod = null;
                    int[] ymod = null;
                    int[] zmod = null;
                    int[] xmod2 = null;
                    int[] ymod2 = null;
                    int[] zmod2 = null;

                    if (face == Direction.ZDOWN)
                    {
                        Console.WriteLine("WHY IS THE SCEPTRE UPSIDE DOWN in ZDOWN???");
                        Environment.Exit(-1);
                    }

                    if (face == Direction.ZUP)
                    {

                        zmod = zeros;
                        zmod2 = ones;

                        xmod = xmod2 = alttop;
                        ymod = ymod2 = altbottom;

                    }
                    else if (face == Direction.XUP || face == Direction.XDOWN)
                    {

                        xmod = zeros;
                        xmod2 = ones;
                        if (face == Direction.XDOWN)
                        {
                            xmod2 = negones;
                        }

                        zmod = zmod2 = alttop;
                        ymod = ymod2 = altbottom;

                    }
                    else
                    {

                        ymod = zeros;
                        ymod2 = ones;
                        if (face == Direction.YDOWN)
                        {
                            ymod2 = negones;
                        }

                        xmod = xmod2 = alttop;
                        zmod = zmod2 = altbottom;

                    }

                    // for each of the four directions
                    for (int i = 0; i < 4; i++)
                    {
                        Cube cur = c;
                        bool nei = true;

                        // while cubes in direction, keep making moves
                        while (nei)
                        {
                            nei = false;
                            if (cur.HasNeighbor(xmod[i], ymod[i], zmod[i]))
                            {
                                Cube who = cur.GetNeighbor(xmod[i], ymod[i], zmod[i]);
                                if (who.GetFace(face) == Face.EMPTY)
                                {
                                    bool barriers = false;
                                    if (face == Direction.ZUP &&
                                        cur.HasNeighbor(xmod2[i] - xmod2[i], ymod2[i], zmod2[i]) &&
                                        cur.HasNeighbor(xmod2[i], ymod2[i] - ymod2[i], zmod2[i]))
                                    {
                                        barriers = true;
                                    }
                                    if (!cur.HasNeighbor(xmod2[i], ymod2[i], zmod2[i]) && !barriers)
                                    {
                                        if (!sceptlocs.Contains(who.GetNeighborString(face, 1)) &&
                                            !board.ContainsKey(who.GetNeighborString(face, 2)) &&
                                            !sceptdirs.Contains(who.GetNeighborString(face, 2) + "|" + face.Opposite()))
                                        {
                                            moves.Insert(0, "S(" + c.GetName() + ") S(" + who.GetName() + ")" +
                                                         face.Name() + " ");
                                        }
                                        cur = who;
                                        count++;
                                        nei = true;
                                    }
                                }
                            }
                        }
                    }

                    // ORTHOGONAL wrapping. There are six ways to move orthogonally
                    // since there will never be overhangs, and we cannot go under the board
                    // we can handle the X and Y directions differently than the Z direction,
                    // where complete loops are possible
                    Direction[,] map = {{Direction.XUP, Direction.XDOWN},
                               {Direction.XDOWN, Direction.XUP},
                               {Direction.YUP, Direction.YDOWN},
                               {Direction.YDOWN, Direction.YUP}};
                    for (int i = 0; i < 4; i++)
                    {
                        Cube cur = c;
                        Direction dir = face;
                        bool nei = true;
                        while (nei)
                        {
                            nei = false;
                            //Console.WriteLine(cur);
                            if (dir == Direction.ZUP)
                            {
                                if (!cur.HasNeighbor(map[i, 0]))
                                {
                                    if (cur.IsEmpty(map[i, 0]))
                                    {
                                        if (!sceptlocs.Contains(cur.GetNeighborString(map[i, 0], 1)) &&
                                            !board.ContainsKey(cur.GetNeighborString(map[i, 0], 2)) &&
                                            !sceptdirs.Contains(cur.GetNeighborString(map[i, 0], 2) + "|" + map[i, 0].Opposite()))
                                        {
                                            moves.Insert(0, "S(" + c.GetName() + ") S(" + cur.GetName() + ")" +
                                                         map[i, 0].Name() + " ");
                                        }
                                        nei = true;
                                        dir = map[i, 0];
                                        count++;
                                    }
                                }
                                else {
                                    Cube a = cur.GetNeighbor(map[i, 0]);
                                    if (!a.HasNeighbor(Direction.ZUP))
                                    {
                                        if (a.IsEmpty(Direction.ZUP))
                                        {
                                            if (!sceptlocs.Contains(a.GetNeighborString(Direction.ZUP, 1)) &&
                                                !board.ContainsKey(a.GetNeighborString(Direction.ZUP, 2)) &&
                                                !sceptdirs.Contains(a.GetNeighborString(Direction.ZUP, 2) + "|" + Direction.ZUP.Opposite()))
                                            {
                                                moves.Insert(0, "S(" + c.GetName() + ") S(" + a.GetName() + ")" +
                                                             Direction.ZUP.Name() + " ");
                                            }
                                            nei = true;
                                            dir = Direction.ZUP;
                                            cur = a;
                                            count++;
                                        }

                                    } else
                                    {
                                        Cube b = a.GetNeighbor(Direction.ZUP);
                                        if (b.IsEmpty(map[i, 1]))
                                        {
                                            if (!sceptlocs.Contains(b.GetNeighborString(map[i, 1], 1)) &&
                                                !board.ContainsKey(b.GetNeighborString(map[i, 1], 2)) &&
                                                !sceptdirs.Contains(b.GetNeighborString(map[i, 1], 2) + "|" + map[i, 1].Opposite()))
                                            {
                                                moves.Insert(0, "S(" + c.GetName() + ") S(" + b.GetName() + ")" +
                                                             map[i, 1].Name() + " ");
                                            }
                                            nei = true;
                                            dir = map[i, 1];
                                            cur = b;
                                            count++;
                                        }
                                    }
                                }
                            }
                            if (dir == map[i,0])
                            {

                                // check two placements
                                if (cur.HasNeighbor(Direction.ZDOWN))
                                {
                                    Cube a = cur.GetNeighbor(Direction.ZDOWN);
                                    if (!a.HasNeighbor(map[i, 0]))
                                    {
                                        if (a.IsEmpty(map[i, 0]))
                                        {
                                            if (!sceptlocs.Contains(a.GetNeighborString(map[i, 0], 1)) &&
                                                !board.ContainsKey(a.GetNeighborString(map[i, 0], 2)) &&
                                                !sceptdirs.Contains(a.GetNeighborString(map[i, 0], 2) + "|" + map[i, 0].Opposite()))
                                            {
                                                moves.Insert(0, "S(" + c.GetName() + ") S(" + a.GetName() + ")" +
                                                             map[i, 0].Name() + " ");
                                            }
                                            nei = true;
                                            dir = map[i, 0];
                                            cur = a;
                                            count++;
                                        }
                                    }
                                    else
                                    {
                                        Cube b = a.GetNeighbor(map[i, 0]);
                                        if (b.IsEmpty(Direction.ZUP))
                                        {
                                            if (!sceptlocs.Contains(b.GetNeighborString(Direction.ZUP, 1)) &&
                                                !board.ContainsKey(b.GetNeighborString(Direction.ZUP, 2)) &&
                                                !sceptdirs.Contains(b.GetNeighborString(Direction.ZUP, 2) + "|" + Direction.ZUP.Opposite()))
                                            {
                                                moves.Insert(0, "S(" + c.GetName() + ") S(" + b.GetName() + ")" +
                                                             Direction.ZUP.Name() + " ");
                                            }
                                            nei = true;
                                            dir = Direction.ZUP;
                                            cur = b;
                                            count++;
                                        }
                                    }
                                }
                            }
                            if (dir == map[i,1])
                            {
                                // check two placements
                                if (!cur.HasNeighbor(Direction.ZUP))
                                {
                                    if (cur.IsEmpty(Direction.ZUP))
                                    {
                                        if (!sceptlocs.Contains(cur.GetNeighborString(Direction.ZUP, 1)) &&
                                            !board.ContainsKey(cur.GetNeighborString(Direction.ZUP, 2)) &&
                                            !sceptdirs.Contains(cur.GetNeighborString(Direction.ZUP, 2) + "|" + Direction.ZUP.Opposite()))
                                        {
                                            moves.Insert(0, "S(" + c.GetName() + ") S(" + cur.GetName() + ")" +
                                                         Direction.ZUP.Name() + " ");
                                        }
                                        nei = true;
                                        dir = Direction.ZUP;
                                        count++;
                                    }
                                }
                                else
                                {
                                    Cube a = cur.GetNeighbor(Direction.ZUP);
                                    if (a.IsEmpty(map[i,1]))
                                    {
                                        if (!sceptlocs.Contains(a.GetNeighborString(map[i,1], 1)) &&
                                            !board.ContainsKey(a.GetNeighborString(map[i,1], 2)) &&
                                            !sceptdirs.Contains(a.GetNeighborString(map[i,1], 2) + "|" + map[i,1].Opposite()))
                                        {
                                            moves.Insert(0, "S(" + c.GetName() + ") S(" + a.GetName() + ")" +
                                                         map[i,1].Name() + " ");
                                        }
                                        nei = true;
                                        dir = map[i,1];
                                        cur = a;
                                        count++;
                                    }
                                }
                            }
                        }
                    }

                    // CLOCKWISE and COUNTERCLOCKWISE
                    Direction[,] cmap = {{Direction.XUP, Direction.YUP, Direction.XDOWN, Direction.YDOWN},
                                {Direction.XUP, Direction.YDOWN, Direction.XDOWN, Direction.YUP}};
                    int[,] ax = {{0, -1, 0, 1},
                              {0, -1, 0, 1}};
                    int[,] bx = {{1, -1, -1, 1},
                              {1, -1, -1, 1}};
                    int[,] ay = {{1, 0, -1, 0},
                              {-1, 0, 1, 0}};
                    int[,] by = {{1, 1, -1, -1},
                              {-1, -1, 1, 1}};

                    for (int i = 0; i < 2; i++)
                    {

                        int dir = -1;
                        for (int j = 0; j < 4; j++)
                        {
                            if (cmap[i,j] == face)
                            {
                                dir = j;
                                break;
                            }
                        }

                        Cube cur = c;
                        bool nei = true;
                        while (dir != -1 && nei)
                        {
                            nei = false;

                            int prev = (dir + 3) % 4;
                            int next = (dir + 1) % 4;

                            // check three placements
                            if (cur.HasNeighbor(bx[i, dir], by[i, dir], 0))
                            {
                                Cube b = cur.GetNeighbor(bx[i, dir], by[i, dir], 0);
                                if (b.IsEmpty(cmap[i,prev]))
                                { // -1
                                    String m = "S(" + c.GetName() + ") S(" + b.GetName() + ")" +
                                                         cmap[i,prev].Name() + " ";
                                    if (!sceptlocs.Contains(b.GetNeighborString(cmap[i,prev], 1)) &&
                                        !board.ContainsKey(b.GetNeighborString(cmap[i,prev], 2)) &&
                                        !moves.Contains(m) &&
                                        !sceptdirs.Contains(b.GetNeighborString(cmap[i,prev], 2) + "|" + cmap[i,prev].Opposite()))
                                    {
                                        moves.Insert(0, m);
                                    }
                                    nei = true;
                                    dir = prev;
                                    cur = b;
                                    count++;
                                }
                            }
                            else if (cur.HasNeighbor(ax[i, dir], ay[i, dir], 0))
                            {
                                Cube a = cur.GetNeighbor(ax[i, dir], ay[i, dir], 0);
                                if (a.IsEmpty(cmap[i,dir]))
                                { // 0
                                    String m = "S(" + c.GetName() + ") S(" + a.GetName() + ")" +
                                                         cmap[i,dir].Name() + " ";
                                    if (!sceptlocs.Contains(a.GetNeighborString(cmap[i,dir], 1)) &&
                                        !board.ContainsKey(a.GetNeighborString(cmap[i,dir], 2)) &&
                                        !moves.Contains(m) &&
                                        !sceptdirs.Contains(a.GetNeighborString(cmap[i,dir], 2) + "|" + cmap[i,dir].Opposite()))
                                    {
                                        moves.Insert(0, m);
                                    }
                                    nei = true;
                                    cur = a;
                                    count++;
                                }
                            }
                            else
                            {
                                if (cur.IsEmpty(cmap[i,next]))
                                { // 1
                                    String m = "S(" + c.GetName() + ") S(" + cur.GetName() + ")" +
                                                         cmap[i,next].Name() + " ";
                                    if (!sceptlocs.Contains(cur.GetNeighborString(cmap[i,next], 1)) &&
                                        !board.ContainsKey(cur.GetNeighborString(cmap[i,next], 2)) &&
                                        !moves.Contains(m) &&
                                        !sceptdirs.Contains(cur.GetNeighborString(cmap[i,next], 2) + "|" + cmap[i,next].Opposite()))
                                    {
                                        moves.Insert(0, m);
                                    }
                                    nei = true;
                                    dir = next;
                                    count++;
                                }
                            }
                        }
                    }

                    sceptlocs.Add(whereami);

                }
            }
        }

        public bool MakeMove(Player p, int m)
        {
            // take string apart and make it happen
            // look up move in moves ArrayList

            // TODO This is ugly. Regular expression?

            String choice = moves[m];
            //Console.WriteLine("Making move" + choice);
            int a = choice.IndexOf(')');
            int b = choice.IndexOf('(', a);
            int e = choice.IndexOf(',', b);
            int f = choice.IndexOf(',', e + 1);
            int g = choice.IndexOf(')', f);
            int d = choice.IndexOf(' ', f);
            String x = choice.Substring(b + 1, e - (b + 1));
            String y = choice.Substring(e + 1, f - (e + 1));
            String z = choice.Substring(f + 1, g - (f + 1));
            String dm1 = choice.Substring(g + 1, 2);
            String dm2 = choice.Substring(d + 1);
            String actualcube = choice.Substring(2, a - 2);

            // if Cube move
            if (choice[0] == 'C')
            {
                // remove from board
                Color color = board[actualcube].GetColor();
                board.Remove(actualcube);

                // add new cube to board
                Cube temp = new Cube(Int32.Parse(x),
                                     Int32.Parse(y),
                                     Int32.Parse(z), dm1.DirectionStr(), dm2.DirectionStr(), color);
                temp.SetBoard(board);
                board[temp.GetName()] = temp;
            }

            // if Sceptre move
            if (choice[0] == 'S')
            {
                Color color = board[actualcube].GetColor();
                Direction DM1 = dm1.DirectionStr();

                Cube was = board[actualcube];
                Face sclr = was.GetFace(was.FirstSceptre());
                was.RemoveSceptre(was.FirstSceptre());

                // add to new location
                string whereto = "" + x + "," + y + "," + z;

                Cube c2 = board[whereto];
                //Console.WriteLine(c2);
                c2.AddSceptre(DM1, sclr);

                // if sceptre currently encroaching and leaving that cube, 
                // make cube disappear
                // MHG 12/18/2011 Must be returning to the player's color, not just leaving free cube!
                if (((sclr == Face.BLACK && color == Color.WHITE) ||
                     (sclr == Face.WHITE && color == Color.BLACK)) &&
                    ((sclr == Face.BLACK && c2.GetColor() == Color.BLACK) ||
                     (sclr == Face.WHITE && c2.GetColor() == Color.WHITE)) &&
                     was.IsFree())
                {
                    board.Remove(actualcube);
                    //Console.WriteLine("REMOVING CUBE!!!");
                }
            }

            SwapPlayers();

            // remove all moves
            moves = null;

            // never gets another move in this game, so always return false
            return false;
        }

        public List<int> LegalMoves(Player p)
        {
            MovesCheck(p);
            List<int> t = new List<int>();
            for (int i = 0; i < moves.Count; i++)
            {
                t.Add(i);
            }
            return t;
        }

        public bool LegalMove(Player p, int m)
        {
            MovesCheck(p);
            return m >= 0 && m < moves.Count;
        }

        public String ShowMoves(Player p)
        {
            MovesCheck(p);
            String s = "";
            int i = 0;
            foreach (string tm in moves)
            {
                s += i + ": " + tm + "\n";
                i++;
            }
            return s;
        }

        public void MovesCheck(Player p)
        {
            if (moves == null)
            {
                GenerateMoves(p);
            }
        }

        // Returns a copy of the current board, for searching
        public BoardGame Clone()
        {
            Dictionary<string, Cube> b = new Dictionary<string, Cube>();
            foreach (string s in board.Keys)
            {
                Cube c = board[s].Clone();
                c.SetBoard(b);
                b[s] = c;
            }
            Axiom cl = new Axiom(b);
            cl.FirstPlayer(players[Color.BLACK]);
            cl.SecondPlayer(players[Color.WHITE]);
            cl.SetTurn(turn);
            cl.SetMoves(moves);
            return cl;
        }

        override
        public string ToString()
        {
            string s = "";
            foreach (Cube c in board.Values)
            {
                s += c + "\n";
                if (c.IsOccupied())
                {
                    s += c.ToSceptreString() + "\n";
                }
            }
            return s;
        }

        public static void main2(String[] args)
        {
            int[] wins = new int[3];
            for (int i = 0; i < 1000; i++)
            {
                Axiom g = new Axiom();
                Player p1 = new RandomPlayer(Color.BLACK, Color.WHITE);
                Player p2 = new RandomPlayer(Color.WHITE, Color.BLACK);
                g.FirstPlayer(p1);
                g.SecondPlayer(p2);
                Color who = Host.HostGame(g, p1, p2, "random");
                wins[(int)who]++;
            }
            Console.WriteLine("0: " + wins[0] + " 1: " + wins[1] + " tie: " + wins[2]);
        }

        public static void main(String[] args)
        {
            Axiom g = new Axiom();
            int sims = 20000;
            double eve = 0.37;
            int ply = 2;
            String dirname = "Ply" + ply + "Eve" + eve + "Sims" + sims;
            Player p1 = new HumanPlayer(Color.BLACK, Color.WHITE);
            Player p2 = new AlphaBetaPlayer(Color.WHITE, Color.BLACK, ply);
            g.FirstPlayer(p1);
            g.SecondPlayer(p2);
            Host.HostGame(g, p1, p2, dirname);
        }
    }
}


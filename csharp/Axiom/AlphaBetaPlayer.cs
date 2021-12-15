using System;
namespace Axiom
{
    public class AlphaBetaPlayer : Player
    {

        protected int ply;

        // Constructor    
        AlphaBetaPlayer(int playerNum, int oppNum, int ply)
        : base(playerNum, oppNum)
        {
            this.ply = ply;
        }

        public int AlphaBetaMove(BoardGame board, int ply)
        {
            // Choose the best minimax move.  Returns (move, val) 
            int move = -1;
            int score = int.MinValue;
            int alpha = int.MinValue;
            int beta = int.MaxValue;

            AlphaBetaPlayer turn = this;
            foreach (int m in board.LegalMoves(this))
            {
                if (ply == 0)
                {
                    return m;
                }
                if (board.GameOver())
                {
                    return -1;  // Can't make a move, the game is over
                }
                BoardGame nb = (BoardGame)board.Clone();
                nb.MakeMove(this, m);
                AlphaBetaPlayer opp = new AlphaBetaPlayer(this.opp, this.num, this.ply);
                int[] s = opp.AlphaBetaMinValue(nb, ply - 1, turn, alpha, beta);
                if (s[1] == -1)
                {
                    s[0] *= 100;
                }
                if (Host.VERBOSE)
                {
                    Console.WriteLine("Move " + m + " score " + s[0] + " reply " + s[1]);
                }
                if (s[0] > score)
                {
                    move = m;
                    score = s[0];
                }
            }
            return move;
        }

        public int[] AlphaBetaMaxValue(BoardGame board, int ply, Player turn, int alpha, int beta)
        {
            // Find the minimax value for the next move for this player
            //    at a given board configuation
            //    Returns (score, oppMove)
            if (board.GameOver())
            {
                int[] t = { turn.Score(board), -1 };
                return t;
            }
            int[] s = { int.MinValue, -1 };
            foreach (int m in board.LegalMoves(this))
            {
                if (ply == 0)
                {
                    s[0] = turn.Score(board);
                    s[1] = m;
                    return s;
                }

                // make a new player to play the other side
                AlphaBetaPlayer opponent = new AlphaBetaPlayer(this.opp, this.num, this.ply);

                // Copy the board so that we don't ruin it
                BoardGame nextBoard = (BoardGame)board.Clone();

                nextBoard.MakeMove(this, m);
                int[] s2 = opponent.AlphaBetaMinValue(nextBoard, ply - 1, turn, alpha, beta);
                if (s2[0] > s[0])
                {
                    s[0] = s2[0];
                    s[1] = m;
                }
                if (s[0] >= beta)
                {
                    break;
                }
                alpha = (int)Math.Max(alpha, s[0]);
            }
            return s;
        }

        public int[] AlphaBetaMinValue(BoardGame board, int ply, Player turn, int alpha, int beta)
        {
            // Find the minimax value for the next move for this player
            //    at a given board configuation
            if (board.GameOver())
            {
                int[] t = { turn.Score(board), -1 };
                return t;
            }
            int[] s = { int.MaxValue, -1 };
            foreach (int m in board.LegalMoves(this))
            {
                if (ply == 0)
                {
                    s[0] = turn.Score(board);
                    s[1] = m;
                    return s;
                }
                // make a new player to play the other side
                AlphaBetaPlayer opponent = new AlphaBetaPlayer(this.opp, this.num, this.ply);
                // Copy the board so that we don't ruin it
                BoardGame nextBoard = (BoardGame)board.Clone();
                nextBoard.MakeMove(this, m);
                int[] s2 = opponent.AlphaBetaMaxValue(nextBoard, ply - 1, turn, alpha, beta);
                if (s2[0] < s[0])
                {
                    s[0] = s2[0];
                    s[1] = m;
                }
                if (s[0] <= alpha)
                {
                    break;
                }
                beta = (int)Math.Min(beta, s[0]);
            }
            return s;
        }

        override
        public int ChooseMove(BoardGame board)
        {
            // Returns the next move that this player wants to make
            return AlphaBetaMove(board, ply);
        }
    }
}


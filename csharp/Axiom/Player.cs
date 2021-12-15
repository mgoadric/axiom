using System;
namespace Axiom
{
    abstract public class Player
    {

        // Data Members
        public int num;
        protected int opp;
        protected int type;

        // Constructor    
        public Player(int playerNum, int oppNum)
        {
            this.num = playerNum;
            this.opp = oppNum;
        }

        // Methods
        override
        public string ToString()
        {
            return "" + this.num + ":" + this.GetType().Name;
        }

        public int Score(BoardGame board)
        {
            // Returns the score for this player given the state of the board
            if (board.HasWon(this.num))
            {
                return 100;
            }
            else if (board.HasWon(this.opp))
            {
                return -100;
            }
            else
            {
                return board.Heuristic();
                //return b.freeCubes(this.num) + b.numCubes(this.num) - (b.freeCubes(this.opp) + b.numCubes(this.opp));
            }
        }

        abstract public int ChooseMove(BoardGame board);
    }
}


using System;
namespace Axiom
{
    abstract public class Player
    {

        // Data Members
        protected Color color;
        protected Color opp;

        public Color Color { get { return color; } }
        public Color Opp { get { return opp; } }

        // Constructor    
        public Player(Color player, Color opp)
        {
            this.color = player;
            this.opp = opp;
        }

        // Methods
        override
        public string ToString()
        {
            return "" + this.color + ":" + this.GetType().Name;
        }

        public int Score(BoardGame board)
        {
            // Returns the score for this player given the state of the board
            if (board.HasWon(this.color))
            {
                return 100;
            }
            else if (board.HasWon(this.opp))
            {
                return -100;
            }
            else
            {
                return board.Heuristic(this.color);
            }
        }

        abstract public int ChooseMove(BoardGame board);
    }
}


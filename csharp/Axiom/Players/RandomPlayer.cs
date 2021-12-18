using System;
using System.Collections.Generic;

namespace Axiom
{
    public class RandomPlayer : Player
    {
        private Random random = new Random();

        // Constructor    
        public RandomPlayer(Color player, Color opp) : base(player, opp) { }

        override
        public int ChooseMove(BoardGame board)
        {
            // Returns the next move that this player wants to make
            List<int> t = board.LegalMoves(this);
            return t[random.Next(t.Count)];
        }
    } 
}


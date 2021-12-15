using System;
using System.Collections.Generic;

namespace Axiom
{
    public class RandomPlayer : Player
    {
        private Random random = new Random();

        // Constructor    
        RandomPlayer(int playerNum, int oppNum) : base(playerNum, oppNum) { }

        override
        public int ChooseMove(BoardGame board)
        {
            // Returns the next move that this player wants to make
            List<int> t = board.LegalMoves(this);
            return t[random.Next(t.Count)];
        }
    } 
}


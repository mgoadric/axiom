using System;
namespace Axiom
{
    public class HumanPlayer : Player
    {

        // Constructor
        public HumanPlayer(Color player, Color opp) : base(player, opp) { }

        override
        public int ChooseMove(BoardGame board)
        {
            // Returns the next move that this player wants to make
            int move = -1;
            Console.WriteLine("Please enter your move:");
            move = Convert.ToInt32(Console.ReadLine());
            while (!board.LegalMove(this, move))
            {
                Console.WriteLine("" + move + "is not valid");
                Console.WriteLine("Please enter your move:");
                move = Convert.ToInt32(Console.ReadLine());
            }
            return move;
        }
    }
}


using System;
using System.Collections.Generic;

namespace Axiom
{
    class Program
    {
        static void Main(string[] args)
        {


            int[] wins = new int[3];
            for (int i = 0; i < 10; i++)
            {
                Axiom g = new Axiom();
                Player p1 = new RandomPlayer(Color.BLACK, Color.WHITE);
                Player p2 = new AlphaBetaPlayer(Color.WHITE, Color.BLACK, 3);
                g.FirstPlayer(p1);
                g.SecondPlayer(p2);
                Color who = Host.HostGame(g, p1, p2, "random");
                wins[(int)who]++;
            }
            Console.WriteLine("0: " + wins[0] + " 1: " + wins[1] + " tie: " + wins[2]);
        }
    }
}


using System;
using System.Collections.Generic;

namespace Axiom
{
    public interface BoardGame
    {

        public bool GameOver();

        public bool HasWon(Color color);

        public bool MakeMove(Player p, int m);

        public List<int> LegalMoves(Player p);

        public bool LegalMove(Player p, int m);

        public string ShowMoves(Player p);

        public string ShowMove(int m);

        public BoardGame Clone();

        public Color GetTurn();

        public int Heuristic(Color color);

    }
}


using System;
using System.Collections.Generic;

namespace Axiom
{
    class Program
    {
        static void Main(string[] args)
        {
            Console.WriteLine("Hello World!");

            Dictionary<string, Cube> b = new Dictionary<string, Cube>();
            Cube stuff = new Cube(-1, 0, 2, Direction.YDOWN, Direction.XDOWN, Color.BLACK);
            stuff.AddSceptre(Direction.ZUP, Face.BLACK);
            stuff.SetBoard(b);
            b[stuff.GetName()] = stuff;
            Console.WriteLine(stuff);
            Console.WriteLine(stuff.ToSceptreString());
            Console.WriteLine("rotating");
            stuff.Rotate(Cube.X);
            Console.WriteLine(stuff);
            Console.WriteLine(stuff.ToSceptreString());

        }
    }
}


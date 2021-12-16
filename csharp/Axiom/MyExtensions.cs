﻿using System;
using Axiom;

namespace ExtensionMethods
{
    public static class MyExtensions
    {
        public static Direction Opposite(this Direction d)
        {
            switch (d)
            {
                case Direction.XUP:
                    return Direction.XDOWN;
                case Direction.XDOWN:
                    return Direction.XUP;
                case Direction.YUP:
                    return Direction.YDOWN;
                case Direction.YDOWN:
                    return Direction.YUP;
                case Direction.ZUP:
                    return Direction.ZDOWN;
                case Direction.ZDOWN:
                    return Direction.ZUP;
                default:
                    return Direction.NONE;
            }
        }

        public static string Name(this Direction d)
        {
            switch (d)
            {
                case Direction.XUP:
                    return "x+";
                case Direction.XDOWN:
                    return "x-";
                case Direction.YUP:
                    return "y+";
                case Direction.YDOWN:
                    return "y-";
                case Direction.ZUP:
                    return "z+";
                case Direction.ZDOWN:
                    return "z-";
                default:
                    return "none";
            }
        }
    }
}
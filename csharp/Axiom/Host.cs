using System;
using System.IO;

namespace Axiom
{
    public static class Host
    {

        public static bool VERBOSE = true;

        public static Color HostGame(BoardGame game, Player player1, Player player2, string dirName)
        {
            Color winner = Color.TIE;

            Player currPlayer = player1;
            Player waitPlayer = player2;

            var sw = OpenStream(@"textfile.txt");
            if (sw is null)
            {
                return Color.TIE;
            }

            sw.WriteLine("" + player1 + " vs " + player2 + "\n");
            int avemoves = 0;
            int count = 0;

            while (!(game.GameOver()))
            {
                count++;
                if (VERBOSE)
                {
                    Console.WriteLine("Current Board: Turn " + count);
                    Console.WriteLine(game);
                    sw.Write("" + count + "| ");
                }

                if (VERBOSE)
                {
                    Console.WriteLine(game.ShowMoves(currPlayer));
                }
                int move = currPlayer.ChooseMove(game);
                avemoves += game.LegalMoves(currPlayer).Count;
                while (!(game.LegalMove(currPlayer, move)))
                {
                    Console.WriteLine("" + move + " is not legal");
                    move = currPlayer.ChooseMove(game);
                }
                if (VERBOSE)
                {
                    Console.WriteLine("" + currPlayer.Color + ": Making move " + move + " " + game.ShowMove(move));
                    sw.WriteLine("" + currPlayer.Color + ": Making move " + move + " " + game.ShowMove(move));
                    sw.Flush();
                }
                game.MakeMove(currPlayer, move);
                Player temp = currPlayer;
                currPlayer = waitPlayer;
                waitPlayer = temp;
                if (count > 1000)
                {
                    break;
                }
            }

            // Someone just won the game! Tell us please!
            if (VERBOSE)
            {
                Console.WriteLine("Current Board: Final");
                sw.WriteLine("\nFinal Board");
                Console.WriteLine(game);
                sw.WriteLine(game);
            }

            if (game.HasWon(currPlayer.Color))
            {
                winner = currPlayer.Color;
            }
            else if (game.HasWon(waitPlayer.Color))
            {
                winner = waitPlayer.Color;
            }
            else
            {
                Console.WriteLine("Tie Game avemoves = " + (avemoves / count));
            }
            Console.WriteLine("Player " + winner + " wins! avemoves = " + (avemoves / count));
            sw.WriteLine("Player " + winner + " wins! avemoves = " + (avemoves / count));


            sw.Close();

            return winner;

        }

        static StreamWriter OpenStream(string path)
        {
            if (path is null)
            {
                Console.WriteLine("You did not supply a file path.");
                return null;
            }

            try
            {
                var fs = new FileStream(path, FileMode.OpenOrCreate);
                return new StreamWriter(fs);
            }
            catch (FileNotFoundException)
            {
                Console.WriteLine("The file or directory cannot be found.");
            }
            catch (DirectoryNotFoundException)
            {
                Console.WriteLine("The file or directory cannot be found.");
            }
            catch (DriveNotFoundException)
            {
                Console.WriteLine("The drive specified in 'path' is invalid.");
            }
            catch (PathTooLongException)
            {
                Console.WriteLine("'path' exceeds the maxium supported path length.");
            }
            catch (UnauthorizedAccessException)
            {
                Console.WriteLine("You do not have permission to create this file.");
            }
            catch (IOException e) when ((e.HResult & 0x0000FFFF) == 32)
            {
                Console.WriteLine("There is a sharing violation.");
            }
            catch (IOException e) when ((e.HResult & 0x0000FFFF) == 80)
            {
                Console.WriteLine("The file already exists.");
            }
            catch (IOException e)
            {
                Console.WriteLine($"An exception occurred:\nError code: " +
                                  $"{e.HResult & 0x0000FFFF}\nMessage: {e.Message}");
            }
            return null;
        }
    }
}


# Axiom

An implementation of the 3D board game [Axiom](https://www.axiomgame.com/) in Java and C#. 

We use the original [play by email notation](https://boardgamegeek.com/filepage/1340/axiomemailplay2pdf) to 
record the board state and moves. Currently all in the terminal, hoping to provide visuals through Unity soon.

## Players

### Human

Select your move when prompted from the list provided.

### Random

A random move from the available options is selected uniformly.

### Alpha-Beta 

Moves are ordered to have Sceptre moves evaluated first, followed by Cube moves. Currently 4-ply search is feasible.

### Monte Carlo Tree Search
